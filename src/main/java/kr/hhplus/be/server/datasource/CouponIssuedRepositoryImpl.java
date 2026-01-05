package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.CouponIssuedRepository;
import kr.hhplus.be.server.dto.CouponRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * 쿠폰 타입 별 대기열에서 당첨자 뽑기
     * @param size
     * @return
     */
    @Override
    public List<CouponRequestDto> findWaitingList(int size) {
        Set<String> allKeys = new HashSet<>();
        ScanOptions scanOptions = ScanOptions.scanOptions() // 스캔 옵션 설정
                .match(COUPON_WAITING_KEY_PREFIX + "*") // key에 해당하는 모든
                .count(1000) // 1000개씩
                .type(DataType.ZSET) // sortedset 구조로 스캔할 것이다.
                .build();

        try(Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            cursor.forEachRemaining(allKeys::add); // scan이 끝날때까지 반복하여 전부 가져온다.
        }

        List<CouponRequestDto> result = new ArrayList<>();

        for(String key : allKeys) {
            // couponId별 이라면, 한장 당 하나.
            // 여기서는 couponType별 선착순 10명씩.
            List<CouponRequestDto> requests = redisTemplate.opsForZSet().popMin(key, size)
                    .stream()
                    .map(item -> new CouponRequestDto(
                        item.getValue(), key.split(":")[1], item.getScore().longValue()
                    ))
                    .toList();

            result.addAll(requests); // 쿠폰종류 별로 size명씩 add
        }
        return result;
    }

}
