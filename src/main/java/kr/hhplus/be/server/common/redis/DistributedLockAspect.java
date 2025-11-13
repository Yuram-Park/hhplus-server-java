package kr.hhplus.be.server.common.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j(topic = "distributedLockAspect")
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    @Pointcut("@annotation(distributedLock")
    public void lockPointcut(DistributedLock distributedLock) {}

    public Object aroundLockPointcut(
            ProceedingJoinPoint joinPoint,
            DistributedLock distributedLock
    ) throws Throwable {
        RLock lock = redissonClient.getLock(distributedLock.keyName());
        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if(isLocked) {
                return joinPoint.proceed();
            } else {
                throw new IllegalStateException("Could not obtain lock - key : " + distributedLock.keyName());
            }
        } finally {
            if(isLocked) {
                lock.unlock();
            }
        }
    }

}
