package purquijo.games.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import purquijo.games.user.AppUser;

import java.io.Serializable;

@Entity
public class Player implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Game game;

    private int score;

    @ManyToOne
    @JsonIgnore
    private AppUser appUser;

    public Player(AppUser appUser) {
        this.appUser = appUser;
    }

    public Player() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getUsername() {
        if (this.appUser == null) {
            return "BOT";
        }
        return this.appUser.getUsername();
    }

    public ChoiceType getAvatar() {
        if (this.appUser == null) {
            return null;
        }
        return this.appUser.getAvatar();
    }
}
