package kr.hhplus.be.server.application.batch;

import kr.hhplus.be.server.presentation.controller.CouponController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueSchedular {

    private final CouponController couponController; // TODO controller가 맞나?

    @Scheduled(fixedRate = 2000) //2초마다 실행
    @SchedulerLock(name = "releaseFromWaitingQueue", lockAtLeastFor = "PT1S") // 분산서버 스케줄락. "최소 1초 유지"
    public void releaseFromQueue() {
        try {
            couponController.issueCoupon();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
