package kr.hhplus.be.server.datasource;

import kr.hhplus.be.server.application.interfaces.UserRepository;
import kr.hhplus.be.server.application.jpa.UserJpaRepository;
import kr.hhplus.be.server.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User updateUserById(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findUserById(String userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public User updatePointById(User user) {
        return userJpaRepository.save(user);
    }

}
