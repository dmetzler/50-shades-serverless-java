package org.dmetzler.serverless.jaxrs;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

import org.dmetzler.serverless.jaxrs.factories.MovieApiServiceFactory;
import org.dmetzler.serverless.jaxrs.impl.MovieApiServiceImpl;
import org.dmetzler.serverless.service.MemoryMovieDaoImpl;
import org.dmetzler.serverless.service.MovieDao;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/test")
public class MovieServiceApplication extends ResourceConfig {

    @Inject
    InjectionManager im;

    public static class MovieServiceBinder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(MovieApiServiceImpl.class).to(MovieApiService.class);
            bind(MemoryMovieDaoImpl.class).to(MovieDao.class).in(Singleton.class);

        }

    }

    public MovieServiceApplication() {
        register(new MovieServiceBinder());
        MovieApiServiceFactory.setInjector(im);
        packages("ord.dmetzler.serverless.jaxrs");
    }
}
