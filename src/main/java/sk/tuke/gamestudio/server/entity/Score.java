package sk.tuke.gamestudio.server.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@NamedQuery( name = "Score.getBestScores",
        query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points DESC")
public class Score implements Comparable<Score>, Serializable {

    @Id
    @GeneratedValue
    private Integer ident;

    private String game;
    private String player;
    private int points;

//    @Temporal(TemporalType.DATE)
    private Date playedon;

    public Score(){
    }

    public Score(String game, String player, int points, Date playedon) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedon = playedon;
    }

    public Integer getIdent() {
        return ident;
    }

    public void setIdent(Integer ident) {
        this.ident = ident;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getPlayedon() {
        return playedon;
    }

    public void setPlayedon(Date playedon) {
        this.playedon = playedon;
    }

    @Override
    public String toString() {
        return "Score{" +
                "ident=" + ident +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", points=" + points +
                ", playedon=" + playedon +
                '}';
    }

    @Override
    public int compareTo(Score o) {
        if(o == null) return -1;
        return this.getPoints() - o.getPoints();
    }
}
