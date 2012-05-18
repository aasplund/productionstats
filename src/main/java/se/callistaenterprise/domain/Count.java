package se.callistaenterprise.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Count {
    private Long count;
    
    @SuppressWarnings("unused")
    private Count(){/* Needed by JAXB */}
    
    public Count(Long count) {
        this.count = count;
    }
    
    @XmlAttribute
    public Long getCount() {
        return count;
    }
}
