package org.dmetzler.serverless.service;

import java.util.List;

import org.dmetzler.serverless.model.Movie;

public interface MovieDao {
    /**
     * Search movies based on the title.
     * @param search
     * @return a list of {@link Movie}
     */
    List<Movie> searchMovie(String search);

    /**
     * Gets a movie by its Id
     * @param movieId
     * @return a {@link Movie} if found or throws
     * @throws MovieNotFoundException if movie does not exists.
     */
    Movie getMovieById(String movieId) throws MovieNotFoundException;

    /**
     * Save the given movie in the backend. The Movie object MUST have an id.
     * @param movie
     * @return The Movie
     */
    Movie saveMovie(Movie movie);

    /**
     * Deletes a movie based on its id.
     * @param movieId
     * @return the movie that was deleted.
     * @throws MovieNotFoundException if movie does not exists.
     */
    Movie deleteMovieById(String movieId) throws MovieNotFoundException;;

}
