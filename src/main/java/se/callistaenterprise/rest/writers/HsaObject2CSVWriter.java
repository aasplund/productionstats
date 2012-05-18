package se.callistaenterprise.rest.writers;

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

import se.callistaenterprise.domain.CareContact;

@Provider
@Produces("application/csv")
public class HsaObject2CSVWriter implements MessageBodyWriter<CareContact> {

    private Bean2CSVWriter writer;

    public void setCSVWriter(Bean2CSVWriter writer) {
        this.writer = writer;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return CareContact.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(CareContact t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }


    @Override
    public void writeTo(CareContact hsaObject, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException, WebApplicationException {
        
        PrintStream ps = new PrintStream(out, false, "UTF-8");
        writer.writeHeader(ps);
        writer.writeData(ps, hsaObject);
        ps.close();
    }
}
