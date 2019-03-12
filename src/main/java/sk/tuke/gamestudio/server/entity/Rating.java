package sk.tuke.gamestudio.server.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = "Rating.getRating",
                query = "SELECT r FROM Rating r WHERE r.game=:game AND r.player=:player ORDER BY r.ratedon DESC"),
        @NamedQuery(name = "Rating.getAverageRating",
                query = "SELECT r FROM Rating r WHERE r.game=:game ORDER BY r.ratedon DESC"),
        @NamedQuery(name = "Rating.deleteRating",
                query = "DELETE FROM Rating r WHERE r.player=:player AND r.game=:game")
})
public class Rating implements Serializable {
    private String player;
    private String game;
    private int rating;

//    @Temporal(TemporalType.DATE)
    private Date ratedon;

    @Id
    @GeneratedValue
    private Integer ident;

    public Rating() {
    }

    public Rating(String game, String player, int rating, Date ratedon) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedon = ratedon;
    }

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedon() {
        return ratedon;
    }

    public void setRatedon(Date ratedon) {
        this.ratedon = ratedon;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ident=" + ident +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", rating=" + rating +
                ", ratedon=" + ratedon +
                '}';
    }
}
