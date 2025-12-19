package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.SalesProductRepository;
import kr.hhplus.be.server.application.redis.SalesProductRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SalesProductRepositoryImpl implements SalesProductRepository {

    private final SalesProductRedisRepository salesProductRedisRepository;

    @Override
    public void addSalesQuantity(Map<String, Integer> salseData) {
        salesProductRedisRepository.updateDailySalesQuantity(salseData);
    }
}
