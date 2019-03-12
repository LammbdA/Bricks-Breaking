package sk.tuke.gamestudio.server.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQuery( name = "Comment.getComments",
        query = "SELECT c FROM Comment c WHERE c.game=:game ORDER BY c.commentedon DESC")
public class Comment implements Serializable{
    private String player;
    private String game;
    private String comment;

//    @Temporal(TemporalType.DATE)
    private Date commentedon;

    @Id
    @GeneratedValue
    private Integer ident;

    public Comment(){
    }

    public Comment(String game, String player, String comment, Date commentedon) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedon = commentedon;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentedon() {
        return commentedon;
    }

    public void setCommentedon(Date commentedon) {
        this.commentedon = commentedon;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "ident=" + ident +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", comment=" + comment +
                ", commentedon=" + commentedon +
                '}';
    }
}
