package com.johnhunsley.events.repository;

import com.johnhunsley.events.domain.Event;
import com.johnhunsley.events.domain.EventException;
import com.johnhunsley.events.domain.EventFactory;
//import com.stormpath.sdk.account.Account;
//import com.stormpath.sdk.directory.Directory;
//import com.stormpath.sdk.organization.Organization;
//import com.stormpath.sdk.organization.OrganizationList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
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
public class EventsRepositoryIntegrationTest {

    @Autowired
    private EventsPagingAndSortingRepository eventsRepository;

    @Autowired
    private EventFactory eventFactory;

//    @Mock
//    private Auth0User user;

    final String userId = "1kIn78A7CBQMbKabi6ipDm";
    final String orgId = "company1";

//    @Before
//    public void initAccount() {
//        Map<String, Object> meta = new HashMap<>();
//        meta.put(EventFactory.ORGANISATION_KEY, orgId);
//        when(user.getUserId()).thenReturn(userId);
//        when(user.getAppMetadata()).thenReturn(meta);
//    }

    @Test
    public void testWriteEvent() {
        assertTrue(true);
//        try {
//            Event event = eventFactory.createEvent(user);
//            event.setPriority("High");
//            event.setStatus("Open");
//            eventsRepository.save(event);
//
//        } catch (EventException e) {
//            e.printStackTrace();
//            fail();
//        }
    }

    @Test
    public void testPageAllEvents() {
        Page<Event> page = eventsRepository.findAll(new PageRequest(0, 20));
        assertFalse(page.getContent().isEmpty());
    }

    @Test
    public void testPageEventsByOrg() {
        Page<Event> page = eventsRepository.findByOrganisationOrderByCreatedDateDesc(orgId, new PageRequest(0, 20));
        assertFalse(page.getContent().isEmpty());
    }

    @Test
    public void testFindEventsbyStatus() {
        Page<Event> page = eventsRepository.findByOrganisationAndStatus(orgId, "Open", new PageRequest(0,20));
        assertFalse(page.getContent().isEmpty());
    }

    @Test
    public void testFindByHashAndOrganisation() {
        Collection<Event> results = eventsRepository.findByHashAndOrganisation("aDeYoa6xIibhJc1C2cU7Qw==", "4xnzkhxFnF1vMnj5N6knT7");
        assertFalse(results.isEmpty());
    }
}
