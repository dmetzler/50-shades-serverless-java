package org.dmetzler.serverless.jaxrs;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.base.JsonMappingExceptionMapper;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.base.JsonParseExceptionMapper;

public class CustomJacksonFeature implements Feature {


    @Override
    public boolean configure(final FeatureContext context) {

        // Register Jackson.
        // add the default Jackson exception mappers
        context.register(JsonParseExceptionMapper.class);
        context.register(JsonMappingExceptionMapper.class);

        context.register(CustomJsonProvider.class, MessageBodyReader.class, MessageBodyWriter.class);

        return true;
    }
}
