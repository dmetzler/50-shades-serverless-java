package org.dmetzler.serverless.movie;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dmetzler.serverless.jaxrs.CustomJsonProvider;
import org.dmetzler.serverless.model.Movie;
import org.dmetzler.serverless.movie.service.impl.DynamoDBDaoIT;
import org.dmetzler.serverless.movie.service.impl.DynamoDBMovieDao;
import org.dmetzler.serverless.service.AbstractMovieDaoTest;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

import cloud.localstack.DockerTestUtils;

public class AwsMovieServiceIT {

    private static ObjectMapper MAPPER = CustomJsonProvider.MAPPER;


    protected MovieServiceHandlerHarness handler;

    protected Context lambdaContext;

    @Before
    public void doBefore() {
        handler = new MovieServiceHandlerHarness();
        lambdaContext = new MockLambdaContext();
        DynamoDBDaoIT.createTable(DockerTestUtils.getClientDynamoDb(), DynamoDBMovieDao.getMoviesTableName());

    }

    @Test
    public void can_create_and_retrieve_a_movie() throws IOException {

        Movie movie = AbstractMovieDaoTest.getTestMovies().get(0);
        AwsProxyResponse response = post("movie/", movie);

        given(response)//
                       .status(Response.Status.OK) //
                       .headers(HttpHeaders.CONTENT_TYPE, a -> a.startsWith(MediaType.APPLICATION_JSON)) //
                       .body("title", a -> a.isEqualTo("The Big Lebovski"));//

        response = get("movie/" + movie.getId().toString());
        given(response)//
                       .status(Response.Status.OK) //
                       .headers(HttpHeaders.CONTENT_TYPE, a -> a.startsWith(MediaType.APPLICATION_JSON)) //
                       .body("title", a -> a.isEqualTo("The Big Lebovski"));//

    }

    private void handle(InputStream is, ByteArrayOutputStream os) throws IOException {
        handler.handleRequest(is, os, lambdaContext);
    }

    private AwsProxyResponse readResponse(ByteArrayOutputStream responseStream) throws IOException {
        return LambdaContainerHandler.getObjectMapper().readValue(responseStream.toByteArray(), AwsProxyResponse.class);
    }

    public ResponseAssert given(AwsProxyResponse response) {
        return new ResponseAssert(response);
    }

    private AwsProxyResponse request(String method, String url) throws IOException {
        return request(method, url, null);
    }

    private AwsProxyResponse request(String method, String url, Object body) throws IOException {
        AwsProxyRequestBuilder builder = new AwsProxyRequestBuilder(url, method.toString()).header(HttpHeaders.ACCEPT,
                MediaType.APPLICATION_JSON).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        if (body != null) {
            builder.body(MAPPER.writeValueAsString(body));
        }
        InputStream requestStream = builder.buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        return readResponse(responseStream);

    }

    private AwsProxyResponse post(String url) throws IOException {
        return post(url, null);
    }

    private AwsProxyResponse post(String url, Object body) throws IOException {
        return request(HttpMethod.POST, url, body);
    }

    private AwsProxyResponse get(String url) throws IOException {
        return request(HttpMethod.GET, url);
    }

    private AwsProxyResponse delete(String url) throws IOException {
        return request(HttpMethod.DELETE, url);
    }

}
