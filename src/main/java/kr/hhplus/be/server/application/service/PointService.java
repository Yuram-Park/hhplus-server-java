package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.datasource.UserRepositoryImpl;
import kr.hhplus.be.server.domain.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserRepositoryImpl userRepository;

    /**
     * 포인트 조회
     * @param userId
     * @return
     */
    public User getUserPoint(String userId) {
        return userRepository.findPointById(userId);
    }

    /**
     * 포인트 충전
     * @param userId
     * @param amount
     * @return
     */
    public User chargePoint(String userId, int amount) {
        User user = userRepository.findPointById(userId);
        user.chargePoint(amount);
        return userRepository.updatePointById(user.getUserId(), user.getUserPoint());
    }

    /**
     * 포인트 사용
     * @param userId
     * @param amount
     * @return
     */
    public User usePoint(String userId, int amount) {
        User user = userRepository.findPointById(userId);
        user.usePoint(amount);
        return userRepository.updatePointById(user.getUserId(), user.getUserPoint());
    }
}
