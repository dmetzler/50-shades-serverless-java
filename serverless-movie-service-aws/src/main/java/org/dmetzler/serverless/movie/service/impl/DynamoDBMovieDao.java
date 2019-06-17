package org.dmetzler.serverless.movie.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.dmetzler.serverless.model.Movie;
import org.dmetzler.serverless.service.MovieDao;
import org.dmetzler.serverless.service.MovieNotFoundException;
import org.glassfish.jersey.internal.guava.Preconditions;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.fasterxml.jackson.core.JsonProcessingException;

public class DynamoDBMovieDao implements MovieDao {

    private static final String MOVIES_TABLE_NAME_PROPERTY_NAME = "MOVIES_TABLE_NAME";

    private static final String DEFAULT_MOVIES_TABLE_NAME = "movies";

    private DynamoDB dynamo;

    private Table table;

    private static final MovieItemMapper MAPPER = new MovieItemMapper();

    private AmazonDynamoDB client;

    @Inject
    public DynamoDBMovieDao(AmazonDynamoDB client) {
        this.client = client;
        this.dynamo = new DynamoDB(client);
        table = dynamo.getTable(getMoviesTableName());
    }

    @Override
    public List<Movie> searchMovie(String search) {
        List<Movie> movies = new ArrayList<>();


        Map<String, AttributeValue> lastKeyEvaluated = null;
        do {
            ScanRequest scanRequest = new ScanRequest()
                .withTableName(getMoviesTableName())
                .withLimit(100)
                .withExclusiveStartKey(lastKeyEvaluated);

            ScanResult result = client.scan(scanRequest);
            for (Item item : ItemUtils.toItemList(result.getItems())){
                try {
                    movies.add(MAPPER.fromItem(item));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lastKeyEvaluated = result.getLastEvaluatedKey();
        } while (lastKeyEvaluated != null);

        if (StringUtils.isBlank(search)) {
            return movies;
        } else {
            return movies.stream().filter(m -> m.getTitle().contains(search)).collect(Collectors.toList());
        }
    }

    @Override
    public Movie getMovieById(String movieId) throws MovieNotFoundException {
        Item item = table.getItem("Id", movieId);
        if (item == null) {
            throw new MovieNotFoundException(movieId);
        }

        try {
            return MAPPER.fromItem(item);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to decode movie from storage (movieId: %s)", movieId), e);
        }
    }

    @Override
    public Movie saveMovie(Movie movie) {
        try {
            Preconditions.checkArgument(movie.getId() != null);

            Item item = new MovieItemMapper().toItem(movie);
            table.putItem(item);

            return movie;
        } catch (JsonProcessingException e) {

            throw new RuntimeException("Unable to encode conversion as Json", e);
        }

    }

    @Override
    public Movie deleteMovieById(String movieId) throws MovieNotFoundException {
        Movie movie = getMovieById(movieId);
        table.deleteItem("Id", movieId);
        return movie;
    }

    public static String getMoviesTableName() {
        if (StringUtils.isNotBlank(System.getenv(MOVIES_TABLE_NAME_PROPERTY_NAME))) {
            return System.getenv(MOVIES_TABLE_NAME_PROPERTY_NAME);
        } else {
            return DEFAULT_MOVIES_TABLE_NAME;
        }
    }

}
