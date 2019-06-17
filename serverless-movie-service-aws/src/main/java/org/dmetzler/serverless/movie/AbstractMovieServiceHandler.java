package org.dmetzler.serverless.movie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.dmetzler.serverless.jaxrs.CustomJacksonFeature;
import org.dmetzler.serverless.jaxrs.factories.MovieApiServiceFactory;
import org.dmetzler.serverless.movie.jersey.JerseyLambdaContainerHandler;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.ResourceConfig;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public abstract class AbstractMovieServiceHandler implements RequestStreamHandler {

    private final JerseyLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    protected InjectionManager im;

    // List of application bindings to configure injections, this
    // allows to have different injection in tests
    protected abstract List<AbstractBinder> binders();

    public AbstractMovieServiceHandler() {
        ResourceConfig jerseyApplication = new ResourceConfig()//
                                                               .packages("org.dmetzler.serverless.jaxrs")
                                                               .register(CustomJacksonFeature.class);

        binders().forEach(jerseyApplication::register);

        handler = JerseyLambdaContainerHandler.getAwsProxyHandler(jerseyApplication);
        im = handler.getInjectionManager();
        MovieApiServiceFactory.setInjector(im);
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
        outputStream.close();
    }
}