package com.johnhunsley.events.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 */
@DynamoDBTable(tableName = "events")
public class Event implements Serializable {
    private static final long serialVersionUID = 100L;

    @Id
    @DynamoDBIgnore
    @JsonIgnore
    private EventId eventId;

    @DynamoDBAttribute(attributeName = "user")
    private String user;

    @DynamoDBAttribute(attributeName = "dateCreated")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:mm:ss:SSS", timezone="GMT")
    private Date created;

    @DynamoDBAttribute(attributeName = "longitude")
    private double longitude;

    @DynamoDBAttribute(attributeName = "latitude")
    private double latitude;

    @DynamoDBAttribute(attributeName = "priority")
    private String priority;

    @DynamoDBAttribute(attributeName = "status")
    private String status;

    public Event() {}

    /**
     *
     * @param user
     * @param created
     * @param hash
     * @param org
     */
    public Event(final String user,
                 final Date created,
                 final String hash,
                 final String org) {
        this.eventId = new EventId(hash, org);
        this.user = user;
        this.created = created;
    }

    @DynamoDBHashKey(attributeName = "hash")
    public String getHash() {
        return eventId != null ? eventId.getHash() : null;
    }

    public void setHash(final String hash) {
        if(eventId == null) eventId = new EventId();
        eventId.setHash(hash);
    }

    @DynamoDBRangeKey(attributeName = "org")
    public String getOrg() {
        return eventId != null ? eventId.getOrganisation() : null;
    }

    public void setOrg(final String organisation) {
        if(eventId == null) eventId = new EventId();
        eventId.setOrganisation(organisation);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return !(eventId != null ? !eventId.equals(event.eventId) : event.eventId != null);

    }

    @Override
    public int hashCode() {
        return eventId != null ? eventId.hashCode() : 0;
    }
}
