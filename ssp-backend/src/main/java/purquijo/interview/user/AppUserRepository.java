package purquijo.games.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsUserByUsername(String username);
    Optional<AppUser> findByUsername(String username);
}
