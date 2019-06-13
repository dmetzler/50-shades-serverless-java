package org.dmetzler.serverless.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.dmetzler.serverless.jaxrs.RFC3339DateFormat;
import org.dmetzler.serverless.model.Movie;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public abstract class AbstractMovieDaoTest {

    protected MovieDao dao;

    protected abstract MovieDao getMovieDao();

    @Before
    public void doBefore() {
        dao = getMovieDao();
    }

    @Test
    public void can_save_and_get_a_movie() throws Exception {
        UUID id = UUID.randomUUID();
        Movie movie = new Movie().id(id).title("The Big Lebowski").releaseDate(LocalDate.now());

        dao.saveMovie(movie);

        movie = dao.getMovieById(id.toString());
        assertThat(movie).isNotNull();
        assertThat(movie.getTitle()).isEqualTo("The Big Lebowski");

    }

    @Test
    public void searching_without_pattern_returns_all_movies() throws Exception {
        UUID id = UUID.randomUUID();
        Movie movie = new Movie().id(id).title("The Big Lebowski").releaseDate(LocalDate.now());
        dao.saveMovie(movie);

        Collection<Movie> result = dao.searchMovie(null);
        assertThat(result).isNotEmpty();
    }

    @Test
    public void searching_with_a_pattern_returns_filtered_result() throws Exception {

        loadFixturesMovie();

        Collection<Movie> result = dao.searchMovie(null);
        assertThat(result).hasSize(3);

        result = dao.searchMovie("Lebovski");
        assertThat(result).hasSize(1);

    }

    @Test
    public void can_delete_movie() throws Exception {
        loadFixturesMovie();

        List<Movie> result = dao.searchMovie("Lebovski");
        assertThat(result).hasSize(1);

        Movie movie = result.get(0);

        dao.deleteMovieById(movie.getId().toString());

        assertThat(dao.searchMovie("Lebovski")).isEmpty();
    }

    @Test(expected = MovieNotFoundException.class)
    public void can_not_get_non_existent_movie() throws Exception {
        loadFixturesMovie();

        dao.getMovieById("nonexistentid");
    }

    protected void loadFixturesMovie() throws IOException, JsonParseException, JsonMappingException {
        InputStream in = AbstractMovieDaoTest.class.getClassLoader().getResourceAsStream("test-fixtures.json");

        ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                                .registerModule(new JavaTimeModule())
                                                .setDateFormat(new RFC3339DateFormat());
        List<Movie> movies = mapper.readValue(in, new TypeReference<List<Movie>>() {
        });

        movies.forEach(m -> dao.saveMovie(m));
    }
}
