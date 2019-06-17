package org.dmetzler.serverless.movie.service.impl;

import java.io.IOException;

import org.dmetzler.serverless.jaxrs.RFC3339DateFormat;
import org.dmetzler.serverless.model.Movie;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MovieItemMapper {

    static ObjectMapper MAPPER = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule())
            .setDateFormat(new RFC3339DateFormat());

    static final String ID = "Id";

    static final String VALUE = "Value";

    public Item toItem(Movie movie) throws JsonProcessingException{
        return new Item().withPrimaryKey(ID, movie.getId().toString()).withJSON(VALUE, MAPPER.writeValueAsString(movie));
    }

    public Movie fromItem(Item item) throws IOException {

        return MAPPER.readValue(item.getJSON(VALUE), Movie.class);

    }

}
