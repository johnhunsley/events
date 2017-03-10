package com.johnhunsley.events.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnhunsley.events.domain.Event;
import com.johnhunsley.events.domain.EventFactory;
import com.johnhunsley.events.repository.EventsPagingAndSortingRepository;
import org.apache.http.HttpStatus;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 07/03/2017
 *         Time : 14:15
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = EventsController.class, secure = false)
public class EventsControllerTest {

    @MockBean
    private EventsPagingAndSortingRepository eventsRepository;

    @MockBean
    private EventFactory eventFactory;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetOpenEventsByOrg() throws Exception {
        Date now = DateTime.now().toDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss:SSS");
        final String expected = "{\"content\":[{\"user\":\"987654321cba\",\"created\":\""+df.format(now)+"\",\"longitude\":0.0,\"latitude\":0.0,\"priority\":\"High\",\"status\":\"Open\",\"hash\":\"pQPMBLoSO+iv+/i/hxzwjg==\",\"org\":\"4xnzkhxFnF1vMnj5N6knT4\"}],\"totalPages\":1,\"totalElements\":1,\"last\":true,\"size\":20,\"number\":0,\"sort\":null,\"numberOfElements\":1,\"first\":true}";
        Event event = new Event("987654321cba", now, "pQPMBLoSO+iv+/i/hxzwjg==", "4xnzkhxFnF1vMnj5N6knT4");
        event.setLongitude(0);
        event.setLatitude(0);
        event.setStatus("Open");
        event.setPriority("High");
        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        Page<Event> page = new PageImpl<>(eventList, new PageRequest(0,20), 1);
        given(eventFactory.resolveOrgId(anyObject())).willReturn("");
        given(eventsRepository.findByOrgAndStatus(anyString(), anyString(), anyObject())).willReturn(page);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0");
        params.add("size", "20");

        mockMvc.perform(get("/events/").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected))
                .andReturn();
    }

    @Test
    public void testCreateEvent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Date now = DateTime.now().toDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss:SSS");
        Event event = new Event("987654321cba", now, "pQPMBLoSO+iv+/i/hxzwjg==", "4xnzkhxFnF1vMnj5N6knT4");
        event.setLongitude(0);
        event.setLatitude(0);
        event.setStatus("Open");
        event.setPriority("High");
        given(eventFactory.createEvent(anyObject())).willReturn(event);
        given(eventsRepository.save(event)).willReturn(event);

        mockMvc.perform(post("/events/").contentType("application/json").content(mapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.SC_ACCEPTED))
                .andReturn();
    }

}
