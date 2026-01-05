package kr.hhplus.be.server.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.service.UserService;
import kr.hhplus.be.server.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/point")
@Tag(name = "포인트", description = "포인트 관련 API")
@RequiredArgsConstructor
public class PointController {

    private final UserService userService;

    /**
     * 포인트 잔액 조회
     * @return
     */
    @Operation(summary = "포인트 잔액 조회")
    @GetMapping("/userPoint")
    public ResponseEntity<User> getUserPoint(@RequestParam String userId) {
        User user = userService.getUser(userId).orElseThrow();
        return ResponseEntity.ok(user);
    }

    /**
     * 포인트 충전
     * @param amount
     * @return
     */
    @Operation(summary = "포인트 충전")
    @PostMapping("/charge")
    public ResponseEntity<User> chargeUserPoint(@RequestParam String userId, @RequestParam int amount) {
        User user = userService.chargePoint(userId, amount);
        return ResponseEntity.ok(user);
    }
}
