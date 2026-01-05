package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.CouponIssuedRepository;
import kr.hhplus.be.server.dto.CouponRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponIssuedRepositoryImpl implements CouponIssuedRepository {
    public static final String COUPON_WAITING_KEY_PREFIX = "coupon_waiting:";
    private final StringRedisTemplate redisTemplate;

    @Override
    public void save(CouponRequestDto requestDto) {
        // couponType으로 Redis Sorted Set 확인하여 userId 없으면 추가
        redisTemplate.opsForZSet()
                .addIfAbsent(
                        COUPON_WAITING_KEY_PREFIX + requestDto.getCouponType(),
                        requestDto.getUserId(),
                        requestDto.getRequestTime()
                );


    }

    @Override
    public boolean isAlreadyIssued(CouponRequestDto requestDto) {

        Double score = redisTemplate.opsForZSet().score(COUPON_WAITING_KEY_PREFIX + requestDto.getCouponType(), requestDto.getUserId());

        return score != null;
    }
}
