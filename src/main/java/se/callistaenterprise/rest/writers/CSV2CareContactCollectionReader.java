package se.callistaenterprise.rest.writers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import se.callistaenterprise.domain.CareContact;

//@Provider
//@Produces("application/csv")
public class CSV2CareContactCollectionReader implements MessageBodyReader<Collection<CareContact>> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<CareContact> readFrom(Class<Collection<CareContact>> type, Type genericType,
            Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
            InputStream entityStream) throws IOException, WebApplicationException {
        // TODO Auto-generated method stub
        return null;
    }

//    @Override
//    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
//        return true;
//    }
//
//    @Override
//    public Collection<CareContact> readFrom(Class<Collection<CareContact>> type, Type genericType,
//            Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
//            InputStream entityStream) throws IOException, WebApplicationException {
//        return CareContact.parse(entityStream);
//    }

}
