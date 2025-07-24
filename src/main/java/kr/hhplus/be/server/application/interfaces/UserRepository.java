package kr.hhplus.be.server.application.interfaces;

import kr.hhplus.be.server.domain.User;

public interface UserRepository {

    /**
     * 포인트를 조회한다.
     * @param userId
     * @return
     */
    public User findPointById(String userId);

    public User updatePointById(String userId, int finalPoint);

    public User usePointById(String userId, int amount);
}
