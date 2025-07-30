package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.User;

import java.util.Optional;

public interface UserRepository {

    /**
     * 포인트를 조회한다.
     * @param userId
     * @return
     */
    Optional<User> findById(String userId);

    /**
     * user 정보를 변경한다.
     * @param user
     * @return
     */
    User save(User user);

}
