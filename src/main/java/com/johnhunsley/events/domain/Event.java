package com.johnhunsley.events.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@DynamoDBTable(tableName = "events2")
public class Event implements Serializable {
    private static final long serialVersionUID = 100L;

    @DynamoDBHashKey(attributeName = "hash")
    private String hash;

    @DynamoDBIndexHashKey(attributeName = "organisation", globalSecondaryIndexName = "organisation-createdDate-index")
    private String organisation;

    @DynamoDBIndexRangeKey(attributeName = "createdDate", globalSecondaryIndexName = "organisation-createdDate-index")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:mm:ss:SSS", timezone="Europe/London")
    private Date createdDate;

    @DynamoDBAttribute(attributeName = "user")
    private String user;

    @DynamoDBAttribute(attributeName = "longitude")
    private double longitude;

    @DynamoDBAttribute(attributeName = "latitude")
    private double latitude;

    @DynamoDBAttribute(attributeName = "priority")
    private String priority;

    @DynamoDBAttribute(attributeName = "status")
    private String status;

    @DynamoDBAttribute(attributeName = "firstName")
    private String firstName;

    @DynamoDBAttribute(attributeName = "lastName")
    private String lastName;

    @DynamoDBAttribute(attributeName = "phoneNumber")
    private String phoneNumber;

    public Event() {}

    /**
     *
     * @param user
     * @param createdDate
     * @param hash
     * @param organisation
     */
    public Event(final String user,
                 final Date createdDate,
                 final String hash,
                 final String organisation) {
//        this.eventId = new EventId(hash, user);
        this.hash = hash;
        this.user = user;
        this.createdDate = createdDate;
        this.organisation = organisation;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return getHash().equals(event.getHash());

    }

    @Override
    public int hashCode() {
        return getHash().hashCode();
    }
}
