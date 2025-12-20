package kr.hhplus.be.server.common.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheNames {
    public static final String COUPON_TYPE = "COUPON::TYPE";
    public static final long COUPON_TYPE_EXPIRATION_MIN = 2L;
    public static final String PRODUCT_SALES = "PRODUCT::SALES";
    public static final long PRODUCT_SALES_EXPIRATION_MIN = 60L;

    public static List<CacheName> getAll() {
        return List.of(
                new CacheName(COUPON_TYPE, COUPON_TYPE_EXPIRATION_MIN, TimeUnit.MINUTES)
        );
    }

    public record CacheName(
            String name,
            long expirationTime,
            TimeUnit timeUnit
    ) {}
}
