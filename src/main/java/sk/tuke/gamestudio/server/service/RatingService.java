package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Rating;

public interface RatingService {
    void setRating(Rating rating) throws RatingException;
    int getAverageRating(String game) throws RatingException;
    int getRating(String game, String name) throws RatingException;
}
