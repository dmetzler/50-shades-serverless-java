package org.dmetzler.serverless;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import org.dmetzler.serverless.jaxrs.CustomJacksonFeature;
import org.dmetzler.serverless.jaxrs.MovieApiService;
import org.dmetzler.serverless.jaxrs.impl.MemoryMovieDao;
import org.dmetzler.serverless.jaxrs.impl.MovieApiServiceImpl;
import org.dmetzler.serverless.service.MovieDao;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/")
public class MovieServiceApplication extends ResourceConfig {

    public static class MovieServiceBinder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(MovieApiServiceImpl.class).to(MovieApiService.class).in(Singleton.class);
            bind(MemoryMovieDao.class).to(MovieDao.class).in(Singleton.class);
        }

    }

    public MovieServiceApplication() {
        register(new MovieServiceBinder());
        register(CustomJacksonFeature.class);
        packages("org.dmetzler.serverless.jaxrs");
    }
}
