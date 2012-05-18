package se.callistaenterprise.domain;

import javax.xml.bind.annotation.XmlEnum;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
@XmlEnum
public enum Sex {
    K, M;
}
