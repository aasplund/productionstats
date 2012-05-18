package se.callistaenterprise.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
@XmlRootElement
public class TimeSpan {

   @XmlElement private int fromDay;
   @XmlElement  private int toDay;
   @XmlElement  private String fromTime;
   @XmlElement  private String toTime;
    
    @SuppressWarnings("unused")
    private TimeSpan() {}
    
    public TimeSpan(int fromDay, int toDay, String fromTime, String toTime) {
        this.fromDay = fromDay;
        this.toDay = toDay;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public int getFromDay() {
        return fromDay;
    }

    public int getToDay() {
        return toDay;
    }

    public String getFromTime() {
        return fromTime;
    }
    
    public String getToTime() {
        return toTime;
    }
}
