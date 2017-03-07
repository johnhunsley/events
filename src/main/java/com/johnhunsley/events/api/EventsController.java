package com.johnhunsley.events.api;

import com.johnhunsley.events.domain.Event;
import com.johnhunsley.events.domain.EventException;
import com.johnhunsley.events.domain.EventFactory;
import com.johnhunsley.events.repository.EventsPagingAndSortingRepository;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.servlet.account.AccountResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 07/03/2017
 *         Time : 12:15
 */
@CrossOrigin
@RestController
@RequestMapping("events/")
public class EventsController {

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private EventsPagingAndSortingRepository eventsRepository;

    /**
     *
     * @param template
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity createEvent(@RequestBody Event template, HttpServletRequest request) {
        Account principle = AccountResolver.INSTANCE.getAccount(request);

        try {
            Event event = eventFactory.createEvent(principle);
            event.setStatus(template.getStatus());
            event.setPriority(template.getPriority());
            event.setLatitude(template.getLatitude());
            event.setLongitude(template.getLongitude());
            eventsRepository.save(event);
            return new ResponseEntity(HttpStatus.OK);

        } catch (EventException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     *
     * @param page
     * @param size
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Page<Event>> getOpenEventsByOrg(@RequestParam("page") final int page,
                                                          @RequestParam("size") final int size,
                                                          HttpServletRequest request) {
        Account principle = AccountResolver.INSTANCE.getAccount(request);

        try {
            return new ResponseEntity<>(
                    eventsRepository.findByOrgAndStatus(
                            eventFactory.resolveOrgId(principle), "Open", new PageRequest(page,size)), HttpStatus.OK);

        } catch (EventException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
