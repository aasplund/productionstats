package se.callistaenterprise.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.callistaenterprise.domain.CareContact;
import se.callistaenterprise.domain.CareContact.CareContactBuilder;
import se.callistaenterprise.domain.Sex;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HsaObjectRepositoryIntegrationTest {

    @Autowired
    private MongoTemplate template;

    @Before
    public void setUp() {
        for (int i = 1; i <= 10; i++) {
            CareContact careContact = new CareContactBuilder()
                .setCompany("Företag " + i)
                .setAge(23)
                .setSex(Sex.K)
                .build();
            template.save(careContact);
        }
    }
    
    @After
    public void tearDown() {
        template.dropCollection(CareContact.class);
    }

    @Test
    public void shouldFindCareUnitByHsaId() {
        CareContact acctualCareUnit = template.findOne(new Query(Criteria.where("company").is("Företag 1")), CareContact.class);
        assertThat(acctualCareUnit, is(notNullValue()));
        assertThat(acctualCareUnit.getSex(), is(Sex.K));
        assertThat(acctualCareUnit.getAge(), is(23));
        assertThat(acctualCareUnit.getCompany(), is("Företag 1"));
    }

}
