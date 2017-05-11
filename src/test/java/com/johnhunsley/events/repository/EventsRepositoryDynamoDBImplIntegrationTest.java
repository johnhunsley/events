package com.johnhunsley.events.repository;

import com.johnhunsley.events.domain.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 10/05/2017
 *         Time : 12:17
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class EventsRepositoryDynamoDBImplIntegrationTest {

    @Autowired
    private EventsRepository eventsRepository;

    @Test
    public void testFindByOrganisationAndPriorityOrderByCreatedDateDesc() {
        try {
            Page<Event> eventPage =
                    eventsRepository.findByOrganisationAndPriorityOrderByCreatedDateDesc(
                                                            "4xnzkhxFnF1vMnj5N6knT7", "High", 2, 2);
            assertFalse(eventPage.getPagedItems().isEmpty());
        } catch (RepositoryException e) {
            e.printStackTrace();
            fail();
        }
    }

}
