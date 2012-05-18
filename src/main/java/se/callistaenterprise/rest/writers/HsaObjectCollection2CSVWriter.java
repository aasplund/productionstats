package se.callistaenterprise.rest.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import se.callistaenterprise.domain.CareContact;

@Provider
@Produces("application/csv")
public class HsaObjectCollection2CSVWriter implements MessageBodyWriter<Collection<CareContact>> {

    private Bean2CSVWriter writer;

    public void setCSVWriter(Bean2CSVWriter writer) {
        this.writer = writer;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // Ensure that we're handling only Collection<HsaObject> objects.
        boolean isWritable;
        if (Collection.class.isAssignableFrom(type) && genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] actualTypeArgs = (parameterizedType.getActualTypeArguments());
            isWritable = (actualTypeArgs.length == 1 && actualTypeArgs[0].equals(CareContact.class));
        } else {
            isWritable = false;
        }
        return isWritable;
    }

    @Override
    public long getSize(Collection<CareContact> t, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Collection<CareContact> hsaObjects, Class<?> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException,
            WebApplicationException {

        PrintStream ps = new PrintStream(out, true, "UTF-8");
        writer.writeHeader(ps);
        for (CareContact hsaObject : hsaObjects) {
            writer.writeData(ps, hsaObject);
        }
        ps.close();
    }
}
