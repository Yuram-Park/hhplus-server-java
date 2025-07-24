package kr.hhplus.be.server.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/point")
@Tag(name = "포인트", description = "포인트 관련 API")
public class PointController {

    /**
     * 포인트 잔액 조회
     * @return
     */
    @Operation(summary = "포인트 잔액 조회")
    @GetMapping("/userPoint")
    public ResponseEntity<User> getUserPoint() {
        // TODO 사용자 토큰으로 userId 추출
        return ResponseEntity.ok(new User("1", 10000));
    }

    /**
     * 포인트 충전
     * @param amount
     * @return
     */
    @Operation(summary = "포인트 충전")
    @PostMapping("/charge")
    public ResponseEntity<User> chargeUserPoint(@RequestParam int amount) {
        // TODO 사용자 토큰으로 userId 추출
        return ResponseEntity.ok(new User("1", 10000 + amount));
    }
}
