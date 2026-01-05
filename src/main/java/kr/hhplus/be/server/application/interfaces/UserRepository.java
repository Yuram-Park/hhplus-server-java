package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository {

    /**
     * 사용자 정보 등록 or 업데이트
     * @param user
     * @return
     */
    public User updateUserById(User user);

    /**
     * 포인트를 조회한다.
     * @param userId
     * @return
     */
    public Optional<User> findUserById(String userId);

    /**
     * 포인트를 변경한다.
     * @param user
     * @return
     */
    public User updatePointById(User user);

}
