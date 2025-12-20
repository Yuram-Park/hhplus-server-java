package kr.hhplus.be.server.application.batch;

import kr.hhplus.be.server.application.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyProductSalesSchedular {

    private final ProductService productService;

    @Scheduled(cron = "5 0 0 * * *", zone = "Asia/Seoul")
    @SchedulerLock(
            name = "LOCK:DAILY_PRODUCT_SALES",
            lockAtLeastFor = "PT1M",
            lockAtMostFor = "PT10M"
    )
    public void refreshDailyTopProduct() {
        productService.refreshDailyTopProducts();
    }
}
