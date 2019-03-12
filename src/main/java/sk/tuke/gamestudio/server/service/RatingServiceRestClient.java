package sk.tuke.gamestudio.server.service;

import sk.tuke.gamestudio.server.entity.Rating;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

public class RatingServiceRestClient implements RatingService {
    private static final String URL = "http://localhost:8080/rest/rating";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            Client client = ClientBuilder.newClient();
            Response response = client.target(URL)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(rating, MediaType.APPLICATION_JSON), Response.class);
        } catch (Exception e) {
            throw new RatingException("Error saving rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try {
            Client client = ClientBuilder.newClient();
            return client.target(URL)
                    .path("/" + game)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<Integer>(){});
        } catch (Exception e) {
            throw new RatingException("Error loading rating", e);
        }
    }

    @Override
    public int getRating(String game, String name) throws RatingException {
        try {
            Client client = ClientBuilder.newClient();
            return client.target(URL)
                    .path("/" + game + "/" + name)
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<Integer>(){});
        } catch (Exception e) {
            throw new RatingException("Error loading rating", e);
        }
    }
}