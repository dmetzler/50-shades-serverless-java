package org.dmetzler.serverless.application;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import org.dmetzler.serverless.jaxrs.MovieApiService;
import org.dmetzler.serverless.jaxrs.impl.MovieApiServiceImpl;
import org.dmetzler.serverless.service.MovieDao;
import org.dmetzler.serverless.service.impl.DataStoreMovieDao;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class MovieServiceApplication extends ResourceConfig {

    public static class MovieServiceBinder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(MovieApiServiceImpl.class).to(MovieApiService.class);
            bind(DataStoreMovieDao.class).to(MovieDao.class).in(Singleton.class);
        }
    }

    public MovieServiceApplication() {
        register(new MovieServiceBinder());
        packages("org.dmetzler.serverless.jaxrs");
    }
}
