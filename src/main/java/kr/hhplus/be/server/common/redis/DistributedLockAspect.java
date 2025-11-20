package kr.hhplus.be.server.common.redis;

import kr.hhplus.be.server.common.utils.CustomSpringELParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Order(1)
@Slf4j(topic = "distributedLockAspect")
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;

    @Pointcut("@annotation(distributedLock)")
    public void lockPointcut(DistributedLock distributedLock) {}

    @Around(value = "lockPointcut(distributedLock)")
    public Object aroundLockPointcut(
            ProceedingJoinPoint joinPoint,
            DistributedLock distributedLock
    ) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
                signature.getParameterNames(),
                joinPoint.getArgs(),
                distributedLock.keyName()
        );
        log.info("lock on [method:{}] [key:{}]", method, key);

        RLock lock = redissonClient.getLock(key);
        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

            if(!isLocked) {
                throw new IllegalStateException("Could not obtain lock - key : " + distributedLock.keyName());
            }

            long start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            return result;

        } finally {
            try {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                    log.info("lock released [key:{}]", key);
                } else {
                    log.warn("lock not held by current thread when unlocking. [key:{}]", key);
                }
            } catch (IllegalMonitorStateException e) {
                log.error("attempt to unlock but not owner. [key:{}]", key, e);
            }
        }
    }

}
