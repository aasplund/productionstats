package se.callistaenterprise.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;

@Provider
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {
    private ObjectMapper mapper = new ObjectMapper();

    public JacksonConfigurator() {
    }
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
