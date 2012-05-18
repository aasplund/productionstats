package se.callistaenterprise.rest.writers;

import static se.callistaenterprise.Utils.NEW_LINE;
import static se.callistaenterprise.Utils.QUOT;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import se.callistaenterprise.domain.Count;

@Provider
@Produces("application/csv")
public class Count2CSVWriter implements MessageBodyWriter<Count> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Count.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Count t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Count t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException,
            WebApplicationException {
        PrintStream ps = new PrintStream(out, false, "UTF-8");
        
        ps.append(QUOT).append("Antal").append(QUOT).append(NEW_LINE);
        ps.append(QUOT).append(t.getCount().toString()).append(QUOT);
        
    }
}
