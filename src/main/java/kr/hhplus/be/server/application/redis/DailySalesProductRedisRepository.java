package kr.hhplus.be.server.application.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class DailySalesProductRedisRepository {

    private static final String KEY_PREFIX = "PRODUCT_SALES:DAILY:";

    private final StringRedisTemplate redisTemplate;

    public void updateDailySalesQuantity(Map<String, Integer> salesData) {
        // Key build
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String key = KEY_PREFIX + formatter.format(LocalDate.now());
        var serializer = redisTemplate.getStringSerializer();
        byte[] rawKey = redisTemplate.getStringSerializer().serialize(key);

        long ttl = Duration.ofDays(3).toSeconds();

        redisTemplate.executePipelined((RedisCallback<Object>) connection ->  {
            salesData.forEach((productId, quantity) -> {
                connection.zSetCommands().zIncrBy(
                        rawKey,
                        quantity.doubleValue(),
                        serializer.serialize(productId)
                );
            });

            // EXPIRE 명령 추가
            connection.keyCommands().expire(rawKey, ttl);
            return null;
        });
    }

    /**
     * Redis에 저장된 전날의 TOP 3 랭킹 상품 리스트 조회
     * @return productId-score map
     */
    public Map<String, Integer> getYesterdayRankingProductIds() {

        // Key build (yesterday)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String key = KEY_PREFIX + formatter.format(LocalDate.now().minusDays(1));

        Set<ZSetOperations.TypedTuple<String>> rawResult = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 2); // TOP 3개 가져오기
        if(rawResult == null || rawResult.isEmpty()) {
            return Map.of();
        }

        Map<String, Integer> result = new LinkedHashMap<>();

        for(ZSetOperations.TypedTuple<String> item : rawResult) {
            if(item.getValue() == null || item.getScore() == null) continue;

            String productId = item.getValue();
            int score = (int) Math.round(item.getScore());

            result.put(productId, score);
        }

        return result;
    }

}
