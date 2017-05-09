package com.johnhunsley.events.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.johnhunsley.events.DynamoDBConfig;
import com.johnhunsley.events.domain.Event;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

/**
 * <p>
 *     AWS DynamoDB Java SDK implementation of the {@link EventsRepository}
 * </p>
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 09/05/2017
 *         Time : 15:33
 */
public class EventsRepositoryDynamoDBImpl implements EventsRepository {
    @Autowired
    DynamoDBConfig dynamoDBConfig;

    @Override
    public Event findByHashAndOrganisation(String hash, String organisation) {
        return null;
    }

    @Override
    public Page<Event> findByOrganisationAndPriorityOrderByCreatedDateDesc(final String organisation,
                                                                           final String priority,
                                                                           final int pageSize,
                                                                           final int pageNumber) throws RepositoryException {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(dynamoDBConfig.getTable());
        Index index = table.getIndex(dynamoDBConfig.getIndex());
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("organisation = :organisation order by createdDate desc")
                .withFilterExpression("priority = :priority")
                .withValueMap(new ValueMap().withString(":organisation", organisation)
                                            .withString(":priority", priority))
                .withMaxPageSize(pageSize);
        ItemCollection<QueryOutcome> items = index.query(spec);
        int count = 0;

        for(com.amazonaws.services.dynamodbv2.document.Page<Item, QueryOutcome> page : items.pages()) {

            if(pageNumber == ++count) {

                Iterator<Item> itemsItr = page.iterator();

                while(itemsItr.hasNext()) {
                    Item item = itemsItr.next();
                    Event event = new Event();

                    for(String key : item.asMap().keySet()) {

                        try {

                            if(PropertyUtils.isWriteable(event, key)) {

                                if(PropertyUtils.getPropertyType(event, key).equals(String.class))
                                    PropertyUtils.setProperty(event, key, item.getString(key));

                                else if(PropertyUtils.getPropertyType(event, key).equals(Integer.class))
                                    PropertyUtils.setProperty(event, key, item.getInt(key));

                                else throw new RepositoryException(key+" field of "+event.getClass()+ " unsupported type");

                            }

                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            throw new RepositoryException(e);
                        }
                    }
                }

                break;
            }
        }

        return null;
    }

    @Override
    public void save(Event event) {

    }
}
