package com.johnhunsley.events.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnhunsley.events.domain.Event;
import com.johnhunsley.events.domain.EventFactory;
import com.johnhunsley.events.repository.EventsPagingAndSortingRepository;
//import com.stormpath.sdk.servlet.account.AccountResolver;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpStatus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 19/04/2017
 *         Time : 10:14
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = EventController.class, secure = false)
public class EventControllerTest {

    @MockBean
    private EventsPagingAndSortingRepository eventsRepository;

    @MockBean
    private EventFactory eventFactory;


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateEvent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Date now = DateTime.now(DateTimeZone.UTC.forTimeZone(TimeZone.getTimeZone("Europe/London"))).toDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss:SSS");
        Event event = new Event("987654321cba", now, "pQPMBLoSO+iv+/i/hxzwjg==", "4xnzkhxFnF1vMnj5N6knT4");
        event.setLongitude(0);
        event.setLatitude(0);
        event.setStatus("Open");
        event.setPriority("High");
        given(eventFactory.createEvent(anyObject())).willReturn(event);
        given(eventsRepository.save(event)).willReturn(event);

        mockMvc.perform(post("/app/event/").contentType("application/json").content(mapper.writeValueAsString(event)))
//                .andDo(print())
//                .andExpect(status().is(HttpStatus.SC_ACCEPTED))
                .andReturn();
    }

    @Test
    public void testUpdateEvent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Date now = DateTime.now(DateTimeZone.UTC.forTimeZone(TimeZone.getTimeZone("Europe/London"))).toDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss:SSS");
        Event event = new Event("987654321cba", now, "pQPMBLoSO+iv+/i/hxzwjg==", "4xnzkhxFnF1vMnj5N6knT4");
        event.setLongitude(0);
        event.setLatitude(0);
        event.setStatus("Open");
        event.setPriority("High");
        given(eventsRepository.findOne(anyObject())).willReturn(event);
        Event update = (Event) BeanUtils.cloneBean(event);
        update.setStatus("Closed");
        given(eventsRepository.save(event)).willReturn(event);
    }
}
