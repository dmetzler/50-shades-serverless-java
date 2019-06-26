package org.dmetzler.serverless.jaxrs.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dmetzler.serverless.model.Movie;
import org.dmetzler.serverless.service.MovieDao;
import org.dmetzler.serverless.service.MovieNotFoundException;

public class MemoryMovieDao implements MovieDao {

    Map<String, Movie> movies = new ConcurrentHashMap<>();

    @Override
    public List<Movie> searchMovie(String search) {
        if (StringUtils.isBlank(search)) {
            return new ArrayList<>(movies.values());
        } else {
            return movies.values().stream().filter(m -> m.getTitle().contains(search)).collect(Collectors.toList());
        }
    }

    @Override
    public Movie getMovieById(String movieId) throws MovieNotFoundException {
        if (movies.containsKey(movieId)) {
            return movies.get(movieId);
        } else {
            throw new MovieNotFoundException(movieId);
        }
    }

    @Override
    public Movie saveMovie(Movie movie) {
        movies.put(movie.getId().toString(), movie);
        return movie;
    }

    @Override
    public Movie deleteMovieById(String movieId) throws MovieNotFoundException {
        Movie movie = movies.get(movieId);
        movies.remove(movieId);
        return movie;
    }

}