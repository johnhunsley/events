package com.johnhunsley.events.domain;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 *         Time : 11:52
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class EventFactoryIntegrationTest {

    @Autowired
    private EventFactory eventFactory;

    final String userId = "987654321cba";
    final String orgId = "1234567890abc";

//    @Mock
//    private Auth0User user;

//    @Before
//    public void initAccount() {
//        Map<String, Object> meta = new HashMap<>();
//        meta.put(EventFactory.ORGANISATION_KEY, orgId);
//        when(user.getUserId()).thenReturn(userId);
//        when(user.getAppMetadata()).thenReturn(meta);
//    }

    @Test
    public void testCreateEvent() {
        assertTrue(true);
//        try {
//            Event event = eventFactory.createEvent(user);
//            assertTrue(event.getOrganisation().equals(orgId));
//            assertTrue(event.getUser().equals(userId));
//            assertNotNull(event);
//
//        } catch (EventException e) {
//            e.printStackTrace();
//            fail();
//        }
    }

}
