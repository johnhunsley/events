package com.johnhunsley.events.repository;

import com.johnhunsley.events.domain.Event;
import com.johnhunsley.events.domain.EventException;
import com.johnhunsley.events.domain.EventFactory;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountList;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.organization.Organization;
import com.stormpath.sdk.organization.OrganizationList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 *         Time : 14:54
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("integration")
public class EventsRepositoryIntegrationTest {

    @Autowired
    private EventsPagingAndSortingRepository eventsRepository;

    @Autowired
    private EventFactory eventFactory;

    @Mock
    private Account account;

    final String accountId = "987654321cba";
    final String orgId = "1234567890abc";

    @Before
    public void initAccount() {
        Organization org = Mockito.mock(Organization.class);
        when(org.getHref()).thenReturn("https://api.stormpath.com/v1/organizations/"+orgId);
        Collection col = new ArrayList<>();
        col.add(org);

        Directory dir = Mockito.mock(Directory.class);
        OrganizationList organizations = Mockito.mock(OrganizationList.class);
        when(organizations.getSize()).thenReturn(1);
        when(organizations.iterator()).thenReturn(col.iterator());
        when(dir.getOrganizations()).thenReturn(organizations);
        when(account.getHref()).thenReturn("https://api.stormpath.com/v1/accounts/"+accountId);
        when(account.getDirectory()).thenReturn(dir);
    }

    @Test
    public void testWriteEvent() {
        try {
            Event event = eventFactory.createEvent(account);
            event.setPriority("High");
            eventsRepository.save(event);

        } catch (EventException e) {
            e.printStackTrace();
            fail();
        }
    }

//    @Test
//    public void testPageEvents() {
//        eventsRepository.findAll()
//    }

}
