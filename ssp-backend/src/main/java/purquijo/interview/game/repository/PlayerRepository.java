package purquijo.games.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purquijo.games.game.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {}
