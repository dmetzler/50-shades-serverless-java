package org.dmetzler.serverless.jaxrs.factories;

import org.dmetzler.serverless.jaxrs.MovieApiService;
import org.glassfish.jersey.internal.inject.InjectionManager;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaJerseyServerCodegen", date = "2019-06-04T22:14:04.761-07:00[America/Los_Angeles]")
public class MovieApiServiceFactory {
    private static InjectionManager im;

    public static MovieApiService getMovieApi() {
        return im.getInstance(MovieApiService.class);
    }

    public static void setInjector(InjectionManager im) {
        MovieApiServiceFactory.im = im;

    }
}
