package org.dmetzler.serverless.jaxrs.factories;

import org.dmetzler.serverless.jaxrs.MovieApiService;
import org.dmetzler.serverless.jaxrs.impl.MovieApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaJerseyServerCodegen", date = "2019-06-04T22:14:04.761-07:00[America/Los_Angeles]")public class MovieApiServiceFactory {
    private final static MovieApiService service = new MovieApiServiceImpl();

    public static MovieApiService getMovieApi() {
        return service;
    }
}
