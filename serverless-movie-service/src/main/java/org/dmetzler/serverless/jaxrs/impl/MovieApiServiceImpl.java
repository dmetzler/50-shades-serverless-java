package org.dmetzler.serverless.jaxrs.impl;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.dmetzler.serverless.jaxrs.MovieApiService;
import org.dmetzler.serverless.model.Movie;
import org.dmetzler.serverless.service.MovieDao;
import org.dmetzler.serverless.service.MovieNotFoundException;

@Singleton
public class MovieApiServiceImpl implements MovieApiService {

    @Inject
    protected MovieDao dao;

    @Override
    public Response addMovie(Movie body, SecurityContext securityContext)  {
        Movie movie = dao.saveMovie(body);
        return Response.ok().entity(movie).build();
    }

    @Override
    public Response deleteMovieById(String id, SecurityContext securityContext) {
        try {
            return Response.ok().entity(dao.deleteMovieById(id)).build();
        } catch (MovieNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @Override
    public Response getMovieById(String id, SecurityContext securityContext) {
        try {
            return Response.ok().entity(dao.getMovieById(id)).build();
        } catch (MovieNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @Override
    public Response searchMovie(String searchString, SecurityContext securityContext) {
        return Response.ok().entity(dao.searchMovie(searchString)).build();
    }

    @Override
    public Response updateMovieById(String id, Movie body, SecurityContext securityContext) {
        try {
            dao.getMovieById(id);
            return Response.ok().entity(dao.saveMovie(body)).build();
        } catch (MovieNotFoundException e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }
}
