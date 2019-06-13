package org.dmetzler.serverless.jaxrs.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.dmetzler.serverless.jaxrs.MovieApiService;
import org.dmetzler.serverless.jaxrs.NotFoundException;
import org.dmetzler.serverless.model.Movie;
import org.dmetzler.serverless.service.MovieDao;
import org.dmetzler.serverless.service.MovieNotFoundException;

public class MovieApiServiceImpl extends MovieApiService {

    protected MovieDao dao;

    @Override
    public Response addMovie(Movie body, SecurityContext securityContext) throws NotFoundException {
        Movie movie = dao.saveMovie(body);
        return Response.ok().entity(movie).build();
    }

    @Override
    public Response deleteMovieById(String id, SecurityContext securityContext) throws NotFoundException {
        try {
            return Response.ok().entity(dao.deleteMovieById(id)).build();
        } catch (MovieNotFoundException e) {
            throw new NotFoundException(404, e.getMessage());
        }
    }

    @Override
    public Response getMovieById(String id, SecurityContext securityContext) throws NotFoundException {
        try {
            return Response.ok().entity(dao.getMovieById(id)).build();
        } catch (MovieNotFoundException e) {
            throw new NotFoundException(404, e.getMessage());
        }
    }

    @Override
    public Response searchMovie(String searchString, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(dao.searchMovie(searchString)).build();
    }

    @Override
    public Response updateMovieById(String id, Movie body, SecurityContext securityContext) throws NotFoundException {
        try {
            dao.getMovieById(id);
            return Response.ok().entity(dao.saveMovie(body)).build();
        } catch (MovieNotFoundException e) {
            throw new NotFoundException(404, e.getMessage());
        }
    }
}
