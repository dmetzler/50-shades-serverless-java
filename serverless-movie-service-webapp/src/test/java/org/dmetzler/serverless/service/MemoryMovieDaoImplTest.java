package org.dmetzler.serverless.service;

public class MemoryMovieDaoImplTest extends AbstractMovieDaoTest {

    @Override
    protected MovieDao getMovieDao() {
        return new MemoryMovieDaoImpl();
    }

}
