package org.dmetzler.serverless.service.impl;

import java.io.IOException;

import org.dmetzler.serverless.jaxrs.RFC3339DateFormat;
import org.dmetzler.serverless.model.Movie;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.appengine.api.datastore.Entity;

public class MovieEntityMapper {

    static ObjectMapper MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                                                   .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                                   .registerModule(new JavaTimeModule())
                                                   .setDateFormat(new RFC3339DateFormat());

    static final String ID = "Id";

    static final String VALUE = "Value";

    public Entity toEntity(Movie movie) throws JsonProcessingException {
        Entity entity = new Entity("Movie");
        entity.setIndexedProperty(ID, movie.getId().toString());
        entity.setProperty(VALUE, MAPPER.writeValueAsString(movie));
        return entity;
    }

    public Movie fromEntity(Entity entity) throws JsonParseException, JsonMappingException, IOException {
        return MAPPER.readValue((String) entity.getProperty(VALUE), Movie.class);
    }

}
