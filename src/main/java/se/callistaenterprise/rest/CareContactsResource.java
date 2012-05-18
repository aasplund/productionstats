package se.callistaenterprise.rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;

import se.callistaenterprise.domain.CareContact;
import se.callistaenterprise.domain.Count;

import com.sun.jersey.multipart.FormDataParam;

@Controller
@Path("/careContacts")
public class CareContactsResource extends AbstractRestResource {

    private MongoTemplate mongoTemplate;

    @Autowired
    public CareContactsResource(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GET
    @Path("/{year}/{month}")
    @Produces({ "application/json;charset=UTF-8;qs=3.5",
            "application/vnd-sll.productionstats.carecontacts.v1+json;charset=UTF-8;qs=3.2",
            "application/xml;charset=UTF-8;qs=2.5",
            "application/vnd-sll.productionstats.carecontacts.v1+xml;charset=UTF-8;qs=2.2",
            "application/csv;charset=UTF-8;qs=1.5",
            "application/vnd-sll.productionstats.carecontacts.v1+csv;charset=UTF-8;qs=1.2" })
    public Collection<CareContact> findAll(@PathParam("year") String year, @PathParam("month") String month) {
        Criteria c = Criteria.where("year").is(year).and("month").is(month);
        Query q = prepareQuery().addCriteria(c);
        List<CareContact> foundHsaObjects = mongoTemplate.find(q, CareContact.class);
        return foundHsaObjects;
    }

    @GET
    @Path("/{year}/{month}/sum")
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/json;charset=UTF-8;qs=3.5", "application/xml;charset=UTF-8;qs=2.5" })
    public Count sum(@PathParam("year") String year, @PathParam("month") String month) {
        Criteria c = Criteria.where("year").is(year).and("month").is(month);
        Query q = prepareQuery().addCriteria(c).limit(0);
        
        @SuppressWarnings("rawtypes")
        MapReduceResults<Map> results = mongoTemplate.mapReduce(q, "careContact",
                
                "function() {" +
                  "emit('numberOfContacts', {count: this.numberOfContacts});" +
                "}",
                
                "function(key, values) {" +
                "var result = {count: 0};" +
                "values.forEach(function(value) {" +
                "   result.count += value.count;" +
                "});" +
                  "return result;" + 
                "}",
                
                Map.class);

        Count count = null;
        for (@SuppressWarnings("rawtypes") Map map : results) {
            @SuppressWarnings("rawtypes")
            Double numberOfContacts = (Double)((Map)map.get("value")).get("count");
            count = new Count(numberOfContacts.longValue());
        }
        
        return count;
    }

    @GET
    @Path("/{year}/{month}/count")
    @Consumes({ "application/json", "application/xml" })
    @Produces({ "application/json;charset=UTF-8;qs=3.5", "application/xml;charset=UTF-8;qs=2.5" })
    public Count count(@PathParam("year") String year, @PathParam("month") String month) {
        Criteria c = Criteria.where("year").is(year).and("month").is(month);
        Query q = prepareQuery().addCriteria(c);
        Count count = new Count(mongoTemplate.count(q, CareContact.class));
        return count;
    }

    @GET
    @Path("/{year}/{month}/count")
    @Consumes("application/csv")
    @Produces("application/csv;charset=UTF-8;qs=1.5")
    public Response countCsv(@PathParam("year") String year, @PathParam("month") String month) {
        Criteria c = Criteria.where("year").is(year).and("month").is(month);
        Query q = prepareQuery().addCriteria(c);
        Count count = new Count(mongoTemplate.count(q, CareContact.class));
        return Response.ok(count).build();
    }

    @DELETE
    public Response deleteAllIndicators() {
        mongoTemplate.dropCollection(CareContact.class);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response loadIndicators(@FormDataParam("file") InputStream is) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
            String careContactLine;
            while ((careContactLine = br.readLine()) != null) {
                CareContact careContact = CareContact.parse(careContactLine);
                mongoTemplate.save(careContact);
            }
            return Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(e);
        }
    }
}
