package com.johnhunsley.events.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @deprecated Not using compound ID
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 */
@DynamoDBDocument
public class EventId implements Serializable {
    private static final long serialVersionUID = 100L;

    @DynamoDBHashKey(attributeName = "hash")
    private String hash;

    @DynamoDBRangeKey(attributeName = "user")
    private String user;

    public EventId() {}

    public EventId(final String hash, final String user) {
        this.hash = hash;
        this.user = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventId eventId = (EventId) o;

        if (!getHash().equals(eventId.getHash())) return false;
        return getUser().equals(eventId.getUser());

    }

    @Override
    public int hashCode() {
        int result = getHash().hashCode();
        result = 31 * result + getUser().hashCode();
        return result;
    }
}
