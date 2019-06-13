package org.dmetzler.serverless.jaxrs.impl;

import org.dmetzler.serverless.service.MemoryMovieDaoImpl;

public class InMemoryMovieApiServiceImpl extends MovieApiServiceImpl {

    public InMemoryMovieApiServiceImpl() {
        dao = new MemoryMovieDaoImpl();
    }

}
