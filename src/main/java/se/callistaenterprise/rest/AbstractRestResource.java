package se.callistaenterprise.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import se.callistaenterprise.rest.writers.Bean2CSVWriter;

public abstract class AbstractRestResource {
    private List<String> properties;
    private Bean2CSVWriter writer;
    private static final int DEFAULT_COUNT = 1000;
    private static final int MAX_COUNT = 5000;
    private static final int DEFAULT_OFFSET = 0;
    
    @Context private UriInfo context;

    protected Query prepareQuery() {
        Query q = new Query();
        MultivaluedMap<String, String> queryParameters = context.getQueryParameters();
        int count = DEFAULT_COUNT;
        int offset = DEFAULT_OFFSET;
        
        if (queryParameters.containsKey("fields")) {
            String[] fields = queryParameters.getFirst("fields").split(",");
            for (String field : fields) {
                q.fields().include(field);
            }
            writer.setProperties(Arrays.asList(fields));
        } else {
            writer.setProperties(properties);
        }
        if (context.getQueryParameters().containsKey("count")) {
            count = Integer.parseInt(context.getQueryParameters().getFirst("count"));
            if(count > MAX_COUNT) {
                throw new WebApplicationException(Status.BAD_REQUEST);
            }
        }
        if (context.getQueryParameters().containsKey("offset")) {
            offset = Integer.parseInt(context.getQueryParameters().getFirst("offset"));
        }
        q.skip(offset).limit(count);
        
        Set<String> usedQueryKeys = queryParameters.keySet();
        
        //Get the intersection between valid properties and the query params.
        usedQueryKeys.retainAll(properties);
        for (String param : usedQueryKeys) {
            q.addCriteria(Criteria.where(param).all(queryParameters.get(param)));
        }
        return q;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }
    
    @Autowired
    public void setWriter(Bean2CSVWriter writer) {
        this.writer = writer;
    }
}