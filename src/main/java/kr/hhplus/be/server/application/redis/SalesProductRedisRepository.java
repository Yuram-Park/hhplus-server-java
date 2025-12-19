package kr.hhplus.be.server.application.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SalesProductRedisRepository {

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


}
