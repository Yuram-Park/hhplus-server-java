package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.UserRepository;
import kr.hhplus.be.server.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Override
    public User findPointById(String userId) {
        return null;
    }

    @Override
    public User updatePointById(String userId, int finalPoint) {
        return null;
    }

    @Override
    public User usePointById(String userId, int amount) {
        return null;
    }
}
