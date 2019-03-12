package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Score;

import java.util.List;

public interface ScoreService {
    public void addScore(Score score) throws ScoreException;

    public List getBestScores(String gameName) throws ScoreException;
}
