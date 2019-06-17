package org.dmetzler.serverless.movie;

import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import org.dmetzler.serverless.jaxrs.MovieApiService;
import org.dmetzler.serverless.jaxrs.impl.MovieApiServiceImpl;
import org.dmetzler.serverless.movie.service.impl.DynamoDBMovieDao;
import org.dmetzler.serverless.service.MovieDao;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class AwsServiceMovieHandler extends AbstractMovieServiceHandler {

    public static class MovieServiceBinder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(MovieApiServiceImpl.class).to(MovieApiService.class);
            bind(DynamoDBMovieDao.class).to(MovieDao.class).in(Singleton.class);
            bind(AmazonDynamoDBClientBuilder.standard().build()).to(AmazonDynamoDB.class).in(Singleton.class);
        }

    }

    @Override
    protected List<AbstractBinder> binders() {
        return Arrays.asList(new MovieServiceBinder());
    }

}
