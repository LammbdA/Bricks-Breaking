package sk.tuke.gamestudio.server.service;

import javax.transaction.Transactional;

import sk.tuke.gamestudio.server.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        entityManager.createNamedQuery("Rating.deleteRating").setParameter("player", rating.getPlayer())
                .setParameter("game", rating.getGame()).executeUpdate();
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        List<Rating> list = entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getResultList();
        int rowCount = 0;
        int allRatings = 0;
        for (Rating rating : list) {
            allRatings = allRatings + rating.getRating();
            rowCount++;
        }
        if (rowCount != 0)
            return allRatings / rowCount;
        return 0;
    }

    @Override
    public int getRating(String game, String name) throws RatingException {
        return ((Rating) entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game).setParameter("player", name).getSingleResult()).getRating();
    }
}
