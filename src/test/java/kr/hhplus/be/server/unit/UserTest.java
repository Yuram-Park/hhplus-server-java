package kr.hhplus.be.server.unit;


import kr.hhplus.be.server.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("User 도메인 테스트")
class UserTest {

    @Nested
    @DisplayName("포인트 충전 시")
    class ChargePoint {

        @Test
        @DisplayName("정상적으로 포인트가 충전된다.")
        void 포인트_충전_성공() {
            // given
            int chargePoint = 1000;
            User user = new User("ID01", 0);

            // when
            user.chargePoint(chargePoint);

            // then
            assertThat(user.getUserPoint()).isEqualTo(1000);
        }

        @Test
        @DisplayName("0원 이하 금액을 충전할 수 없다.")
        void 영원_이하_충전_불가() {
            // given
            int chargePoint = 0;
            User user = new User("ID01", 1000);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> user.chargePoint(chargePoint));
        }

        @Test
        @DisplayName("100,000원 까지만 충전이 가능하다.")
        void 최대_충전_금액() {
            // given
            int chargePoint = 55_000;
            User user = new User("ID01", 50_000);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> user.chargePoint(chargePoint));
        }
    }

    @Nested
    @DisplayName("포인트 사용 시")
    class UsePoint {

        @Test
        @DisplayName("정상적으로 포인트가 차감된다.")
        void 포인트_사용_성공() {
            // given
            int usePoint = 1000;
            User user = new User("ID01", 2000);

            // when
            user.usePoint(usePoint);

            // then
            assertThat(user.getUserPoint()).isEqualTo(1000);
        }

        @Test
        @DisplayName("0원 이하 금액을 사용할 수 없다.")
        void 영원_이하_차감_불가() {
            // given
            int usePoint = 0;
            User user = new User("ID01", 1000);

            // when, then
            assertThrows(IllegalArgumentException.class,() -> user.usePoint(usePoint));
        }

        @Test
        @DisplayName("잔액 초과 금액을 사용할 수 없다.")
        void 잔액_초과_차감_불가() {
            // given
            int usePoint = 2000;
            User user = new User("ID01", 1000);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> user.usePoint(usePoint));
        }
    }
}
