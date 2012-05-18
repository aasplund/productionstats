package se.callistaenterprise.domain;

import static org.apache.commons.lang3.StringUtils.remove;
import static se.callistaenterprise.Utils.tryParseDate;
import static se.callistaenterprise.Utils.tryParseInt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@XmlRootElement
@JsonSerialize(include = Inclusion.NON_EMPTY)
public final class CareContact {
    private static final String HSA_ID_PATTERN = "^SE\\d{10}-.{4}$";
    private static final int HSA_ID = 1;
    private static final int COMPANY = 2;
    private static final int SEX = 3;
    private static final int AGE = 4;
    private static final int VISIT = 6;
    private static final int TYPE_OF_VISIT = 8;
    private static final int CAREGIVER_TYPE = 9;
    private static final int WAS_LISTED = 10;
    private static final int NUMBER_OF_CONTACTS = 11;
    

    
    
    @XmlElement @Indexed private String hsaId;
    @XmlElement private String hsaIdUrlEncoded;
    @XmlElement private String company;
    @XmlElement private Sex sex;
    @XmlElement private Integer age;
    @XmlElement private Date visit;
    @XmlElement @Indexed private String year;
    @XmlElement @Indexed private String month;
    @XmlElement private String description;
    @XmlElement private String typeOfVisit;
    @XmlElement private String caregiverType;
    @XmlElement private String wasListed;
    @XmlElement private Integer numberOfContacts;

    
    
    public static class CareContactBuilder implements EntityBuilder<CareContact> {

        private CareContact hsaObject = new CareContact();

        public CareContactBuilder() {
        }

        public CareContactBuilder(CareContact other) {
            hsaObject.hsaId = other.hsaId;
        }

        public CareContactBuilder setCompany(String company) {
            hsaObject.company = company;
            return this;
        }

        public CareContactBuilder setHsaId(String hsaId) {
            hsaObject.hsaId = hsaId;
            try {
                hsaObject.hsaIdUrlEncoded = URLEncoder.encode(hsaId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                hsaObject.hsaIdUrlEncoded = hsaId;
            }
            return this;
        }

        public CareContactBuilder setSex(Sex sex) {
            hsaObject.sex = sex;
            return this;
        }

        public CareContactBuilder setAge(Integer age) {
            hsaObject.age = age;
            return this;
        }

        public CareContactBuilder setVisit(Date visit) {
            Date d = null;
            if (visit != null) {
                d = new Date(visit.getTime());
            }
            hsaObject.visit = d;
            return this;
        }

        public CareContactBuilder setYear(String year) {
            hsaObject.year = year;
            return this;
        }

        public CareContactBuilder setMonth(String month) {
            hsaObject.month = month;
            return this;
        }

        public CareContactBuilder setCaregiverType(String caregiverType) {
            hsaObject.caregiverType = caregiverType;
            return this;
        }

        public CareContactBuilder setTypeOfVisit(String typeOfVisit) {
            hsaObject.typeOfVisit = typeOfVisit;
            return this;
        }
        
        public CareContactBuilder setWasListed(String wasListed) {
            hsaObject.wasListed = wasListed;
            return this;
        }

        public CareContactBuilder setNumberOfContacts(Integer numberOfContacts) {
            hsaObject.numberOfContacts = numberOfContacts;
            return this;
        }

        @Override
        public CareContact build() {
            return hsaObject;
        }
    }

    private CareContact() {
    }

    public String getHsaId() {
        return hsaId;
    }

    public String getHsaIdUrlEncoded() {
        return hsaIdUrlEncoded;
    }

    public String getCompany() {
        return company;
    }

    public Sex getSex() {
        return sex;
    }

    public Integer getAge() {
        return age;
    }

    public Date getVisit() {
        return visit;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDescription() {
        return description;
    }

    public String getTypeOfVisit() {
        return typeOfVisit;
    }

    public String getCaregiverType() {
        return caregiverType;
    }
    
    public String getWasListed() {
        return wasListed;
    }
    
    public Integer getNumberOfContacts() {
        return numberOfContacts;
    }

    public static CareContact parse(String careContactString) {
        Scanner elementScanner = new Scanner(careContactString).useDelimiter(";");
        CareContactBuilder sb = new CareContactBuilder();
        int position = 0;
        
        while(elementScanner.hasNext()) {
            String elementValue = remove(elementScanner.next(), '\"');
            switch (++position) {
            case HSA_ID:
                sb.setHsaId(elementValue.matches(HSA_ID_PATTERN)?elementValue:"");
                break;
            case COMPANY:
                sb.setCompany(elementValue);
                break;
            case SEX:
                sb.setSex(Sex.valueOf(elementValue));
                break;
            case AGE:
                sb.setAge(tryParseInt(elementValue));
                break;
            case VISIT:
                Date visitDate = tryParseDate(elementValue, "yyyyMMdd");
                sb.setVisit(visitDate)
                .setYear(new SimpleDateFormat("yyyy").format(visitDate))
                .setMonth(new SimpleDateFormat("MM").format(visitDate));
                break;
            case TYPE_OF_VISIT:
                sb.setTypeOfVisit(elementValue);
                break;
            case CAREGIVER_TYPE:
                sb.setCaregiverType(elementValue);
                break;
            case WAS_LISTED:
                sb.setWasListed(elementValue);
                break;
            case NUMBER_OF_CONTACTS:
                sb.setNumberOfContacts(tryParseInt(elementValue));
                break;
            }
        }

    return sb.build();
}
}
