package org.dmetzler.serverless.service;

import org.dmetzler.serverless.jaxrs.impl.MemoryMovieDao;

public class MemoryMovieDaoTest extends AbstractMovieDaoTest {

    @Override
    protected MovieDao getMovieDao() {
        return new MemoryMovieDao();
    }

}
