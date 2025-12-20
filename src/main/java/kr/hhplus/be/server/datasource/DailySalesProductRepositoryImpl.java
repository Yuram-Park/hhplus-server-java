package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.DailySalesProductRepository;
import kr.hhplus.be.server.application.redis.DailySalesProductRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DailySalesProductRepositoryImpl implements DailySalesProductRepository {

    private final DailySalesProductRedisRepository dailySalesProductRedisRepository;

    @Override
    public void addSalesQuantity(Map<String, Integer> salseData) {
        dailySalesProductRedisRepository.updateDailySalesQuantity(salseData);
    }

    @Override
    public Map<String, Integer> findYesterdayRankingProduct() {
        return dailySalesProductRedisRepository.getYesterdayRankingProductIds();
    }
}
