package kr.hhplus.be.server.application.jpa;

import kr.hhplus.be.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, String> {

    Optional<User> findById(String userId);

    User save(User user);
}
