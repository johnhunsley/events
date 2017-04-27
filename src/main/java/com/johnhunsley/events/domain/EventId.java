package com.johnhunsley.events.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

import java.io.Serializable;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 */
@DynamoDBDocument
public class EventId implements Serializable {
    private static final long serialVersionUID = 100L;

    @DynamoDBHashKey(attributeName = "hash")
    private String hash;

    @DynamoDBRangeKey(attributeName = "org")
//    @DynamoDBIndexHashKey(globalSecondaryIndexName = "org-created-index")
    private String organisation;

    public EventId() {}

    public EventId(final String hash, final String organisation) {
        this.hash = hash;
        this.organisation = organisation;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventId eventId = (EventId) o;

        if (!getHash().equals(eventId.getHash())) return false;
        return getOrganisation().equals(eventId.getOrganisation());

    }

    @Override
    public int hashCode() {
        int result = getHash().hashCode();
        result = 31 * result + getOrganisation().hashCode();
        return result;
    }
}
