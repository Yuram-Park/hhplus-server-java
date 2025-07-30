package kr.hhplus.be.server.service;

import kr.hhplus.be.server.application.service.PointService;
import kr.hhplus.be.server.datasource.UserRepositoryImpl;
import kr.hhplus.be.server.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PointService 테스트")
public class PointServiceTest {

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private PointService pointService;


    @Nested
    @DisplayName("포인트 조회 시")
    class UserPoint {

        @Test
        @DisplayName("id로 조회 시 현재 잔액을 응답한다.")
        void 포인트_조회_성공() {
            // given
            String userId = "ID01";
            String userPassword = "1111";
            int remainingPoints = 1000;

            User user = new User(userId, userPassword, null, null, remainingPoints, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

            // when
            User userResult = pointService.getUserPoint(user.getUserId());

            // then
            assertThat(userResult.getUserPoint()).isEqualTo(user.getUserPoint());
        }
    }


    @Nested
    @DisplayName("포인트 충전 시")
    class ChargePoint {

        @Test
        @DisplayName("특정 금액을 충전하면, 금액이 충전된다.")
        void 충전_성공() {
            // given
            String userId = "ID01";
            String userPassword = "1111";
            int startPoint = 0;
            int chargePoint = 1000;
            User user = new User(userId, userPassword, null, null, startPoint, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

            when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
            when(userRepository.save(any())).thenReturn(new User(userId, userPassword, null, null, startPoint + chargePoint, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

            // when
            user = pointService.chargePoint(user.getUserId(), chargePoint);

            // then
            assertThat(user.getUserPoint()).isEqualTo(startPoint + chargePoint);
        }

        @Test
        @DisplayName("0원 이하 금액을 충전할 수 없다.")
        void 영원_충전_실패() {
            // given
            String userId = "ID01";
            String userPassword = "1111";
            int startPoint = 0;
            int chargePoint = 0;
            User user = new User(userId, userPassword, null, null, startPoint, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

            when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> pointService.chargePoint(user.getUserId(), chargePoint));
        }

        @Test
        @DisplayName("100,000원까지만 충전이 가능하다.")
        void 최대_충전_금액() {
            // given
            String userId = "ID01";
            String userPassword = "1111";
            int startPoint = 0;
            int chargePoint = 100_001;
            User user = new User(userId, userPassword, null, null, startPoint, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

            when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> pointService.chargePoint(user.getUserId(), chargePoint));
        }
    }

    @Nested
    @DisplayName("포인트 사용 시")
    class GetUserPoint {

        @Test
        @DisplayName("포인트 잔액이 차감된다.")
        void 잔액_차감_성공() {
            // given
            String userId = "ID01";
            String userPassword = "1111";
            int startPoint = 5000;
            int usePoint = 1000;

            User user = new User(userId, userPassword, null, null, startPoint, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

            when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
            when(userRepository.save(any())).thenReturn(new User(userId, userPassword, null, null, startPoint, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

            // when
            pointService.usePoint(user.getUserId(), usePoint);

            // then
            assertThat(user.getUserPoint()).isEqualTo(startPoint - usePoint);
        }

        @Test
        @DisplayName("사용 금액은 0원 이하일 수 없다.")
        void 영원_이하_차감_실패() {
            // given
            String userId = "ID01";
            String userPassword = "1111";
            int startPoint = 5000;
            int usePoint = 0;

            User user = new User(userId, userPassword, null, null, startPoint, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

            when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
            // when, then
            assertThrows(IllegalArgumentException.class, () -> pointService.usePoint(user.getUserId(), usePoint));
        }

        @Test
        @DisplayName("잔액 초과 금액을 사용할 수 없다.")
        void 잔액_초과_차감_실패() {
            // given
            String userId = "ID01";
            String userPassword = "1111";
            int startPoint = 1000;
            int usePoint = 1001;

            User user = new User(userId, userPassword, null, null, startPoint, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> pointService.usePoint(user.getUserId(), usePoint));
        }
    }
}
