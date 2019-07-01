package org.dmetzler.serverless.service.impl;

import org.dmetzler.serverless.service.AbstractMovieDaoTest;
import org.dmetzler.serverless.service.MovieDao;
import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DataStoreMovieDaoTest extends AbstractMovieDaoTest {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void doBefore() {
        super.doBefore();
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Override
    protected MovieDao getMovieDao() {
        return new DataStoreMovieDao();
    }
}
