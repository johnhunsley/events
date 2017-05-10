package com.johnhunsley.events.domain;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.impl.organization.DefaultOrganization;
import com.stormpath.sdk.organization.Organization;
import com.stormpath.sdk.organization.OrganizationList;
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

    final String accountId = "987654321cba";
    final String orgId = "1234567890abc";

    @Mock
    private Account account;

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
    public void testCreateEvent() {
        try {
            Event event = eventFactory.createEvent(account);
            assertTrue(event.getOrganisation().equals(orgId));
            assertTrue(event.getUser().equals(accountId));
            assertNotNull(event);

        } catch (EventException e) {
            e.printStackTrace();
            fail();
        }
    }

}
