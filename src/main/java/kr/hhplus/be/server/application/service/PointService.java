package kr.hhplus.be.server.application.service;

import kr.hhplus.be.server.common.DistributedLock;
import kr.hhplus.be.server.datasource.UserRepositoryImpl;
import kr.hhplus.be.server.domain.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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
        return userRepository.findPointById(userId).orElseThrow(() -> new NoSuchElementException(userId + " : 해당하는 아이디가 존재하지 않습니다."));
    }

    /**
     * 포인트 충전
     * @param userId
     * @param amount
     * @return
     */
    public User chargePoint(String userId, int amount) {
        User user = userRepository.findPointById(userId).orElseThrow(() -> new NoSuchElementException(userId + " : 해당하는 아이디가 존재하지 않습니다."));
        user.chargePoint(amount);
        return userRepository.updatePointById(user);
    }

    /**
     * 포인트 사용
     * @param userId
     * @param amount
     * @return
     */
//    @DistributedLock(key = "'point:' + #userId")
    public User usePoint(String userId, int amount) {
        User user = userRepository.findPointById(userId).orElseThrow(() -> new NoSuchElementException(userId + " : 해당하는 아이디가 존재하지 않습니다."));
        user.usePoint(amount);
        return userRepository.updatePointById(user);
    }
}
