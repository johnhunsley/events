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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *     AWS DynamoDB Java SDK implementation of the {@link EventsRepository}
 * </p>
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 09/05/2017
 *         Time : 15:33
 */
@Repository("eventsRepository")
public class EventsRepositoryDynamoDBImpl implements EventsRepository {
    @Autowired
    DynamoDBConfig dynamoDBConfig;

    private DateFormat df;

    @PostConstruct
    public void initDateFormat() {
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    @Override
    public Event findByHashAndOrganisation(String hash, String organisation) {
        return null;
    }

    @Override
    public Page<Event> findByOrganisationAndPriorityOrderByCreatedDateDesc(final String organisation,
                                                                           final String priority,
                                                                           final int pageSize,
                                                                           final int pageNumber) throws RepositoryException {
        AmazonDynamoDB client = dynamoDBConfig.amazonDynamoDB();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(dynamoDBConfig.getTable());
        Index index = table.getIndex(dynamoDBConfig.getIndex());
        QuerySpec spec = new QuerySpec()
                .withKeyConditionExpression("organisation = :organisation")
                .withFilterExpression("priority = :priority")
                .withValueMap(new ValueMap().withString(":organisation", organisation)
                                            .withString(":priority", priority)).withScanIndexForward(true)
                .withMaxPageSize(pageSize);
        ItemCollection<QueryOutcome> items = index.query(spec);
        List<Event> pagedEvents = new ArrayList<>();
        int pageCount = 0;

        for(com.amazonaws.services.dynamodbv2.document.Page<Item, QueryOutcome> page : items.pages()) {

            if(pageNumber == ++pageCount) {
                Iterator<Item> itemsItr = page.iterator();

                while(itemsItr.hasNext()) {
                    Item item = itemsItr.next();
                    Event event = new Event();

                    for(String key : item.asMap().keySet()) {

                        try {

                            if(PropertyUtils.isWriteable(event, key)) {

                                if(PropertyUtils.getPropertyType(event, key).equals(String.class))
                                    PropertyUtils.setProperty(event, key, item.getString(key));

                                else if(PropertyUtils.getPropertyType(event, key).equals(Date.class)) {

                                    try {
                                        PropertyUtils.setProperty(event, key, df.parse(item.getString(key)));

                                    } catch (ParseException e) {
                                        throw new RepositoryException(e);
                                    }
                                }

                                else if(PropertyUtils.getPropertyType(event, key).equals(double.class))
                                    PropertyUtils.setProperty(event, key, item.getDouble(key));

                                else if(PropertyUtils.getPropertyType(event, key).equals(Integer.class))
                                    PropertyUtils.setProperty(event, key, item.getInt(key));

                                else throw new RepositoryException(key+" field of "+event.getClass()+ " unsupported type");

                            }

                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            throw new RepositoryException(e);
                        }
                    }

                    pagedEvents.add(event);
                }


            }
        }

        Collections.reverse(pagedEvents);
        return new Page<>(pagedEvents, items.getAccumulatedItemCount(), pageCount);
    }

    @Override
    public void save(Event event) {

    }
}
