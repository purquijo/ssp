package purquijo.games.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import purquijo.games.game.entity.GameRound;

@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, Long> {
}
