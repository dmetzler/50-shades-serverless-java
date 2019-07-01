package org.dmetzler.serverless.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.dmetzler.serverless.model.Movie;
import org.dmetzler.serverless.service.MovieDao;
import org.dmetzler.serverless.service.MovieNotFoundException;
import org.glassfish.jersey.internal.guava.Preconditions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class DataStoreMovieDao implements MovieDao {

    static final String ENTITY_NAME = "Movie";

    private DatastoreService datastore;

    public DataStoreMovieDao() {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public List<Movie> searchMovie(String search) {
        final Query q = new Query(ENTITY_NAME);

        PreparedQuery pq = datastore.prepare(q);
        List<Entity> entities = pq.asList(FetchOptions.Builder.withDefaults());

        List<Movie> movies = entities.stream().map(e -> {
            try {
                return new MovieEntityMapper().fromEntity(e);
            } catch (IOException e1) {
                return null;
            }
        }).collect(Collectors.toList());

        if (StringUtils.isBlank(search)) {
            return movies;
        } else {
            return movies.stream().filter(m -> m.getTitle().contains(search)).collect(Collectors.toList());
        }

    }

    @Override
    public Movie getMovieById(String movieId) throws MovieNotFoundException {
        try {
            return new MovieEntityMapper().fromEntity(getMovieEntityById(movieId));
        } catch (IOException e) {
            return null;
        }

    }

    private Entity getMovieEntityById(String movieId) throws MovieNotFoundException {
        final Query q = new Query(ENTITY_NAME).setFilter(
                new FilterPredicate(MovieEntityMapper.ID, FilterOperator.EQUAL, movieId));

        PreparedQuery pq = datastore.prepare(q);
        List<Entity> movies = pq.asList(FetchOptions.Builder.withDefaults());
        if (movies.isEmpty()) {
            throw new MovieNotFoundException(movieId);
        }

        return movies.get(0);

    }

    @Override
    public Movie saveMovie(Movie movie) {

        try {
            Preconditions.checkArgument(movie.getId() != null);
            Entity item = new MovieEntityMapper().toEntity(movie);
            datastore.put(item);

            return movie;
        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to encode conversion as Json", e);
        }

    }

    @Override
    public Movie deleteMovieById(String movieId) throws MovieNotFoundException {
        Entity entity = getMovieEntityById(movieId);
        datastore.delete(entity.getKey());
        try {
            return new MovieEntityMapper().fromEntity(entity);
        } catch (IOException e) {
            return null;
        }
    }

}
