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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 07/03/2017
 */
@RestController
@RequestMapping("app/events/")
public class EventsController {

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private AccountResolver accountResolver;

    @Autowired
    private EventsPagingAndSortingRepository eventsRepository;

    /**
     *
     * @param template
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @PreAuthorize("hasPermission('customer', 'CUSTOMER')")
    public ResponseEntity createEvent(@RequestBody Event template, HttpServletRequest request) {
        Account principle = accountResolver.getAccount(request);

        try {
            eventsRepository.save(
                    eventFactory.createEventFromTemplate(principle, template));
            return new ResponseEntity(HttpStatus.ACCEPTED);

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
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasPermission('serviceProvider', 'SERVICE_PROVIDER')")
    public ResponseEntity<Page<Event>> getOpenEventsByOrg(@RequestParam("page") final int page,
                                                          @RequestParam("size") final int size,
                                                          HttpServletRequest request) {
        Account principle = accountResolver.getAccount(request);

        try {
            return new ResponseEntity<>(
                    eventsRepository.findByOrgAndStatus(
                            eventFactory.resolveOrgId(principle), "Open", new PageRequest(page,size)), HttpStatus.OK);

        } catch (EventException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * <p>
     *     Get the {@link Event} instances for the authenitcated user's org and given priority
     * </p>
     * @param page
     * @param size
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "{priority}", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasPermission('serviceProvider', 'SERVICE_PROVIDER')")
    public ResponseEntity<Page<Event>> getOpenEventsByOrgAndPriority(@PathVariable("priority") final String priority,
                                                          @RequestParam("page") final int page,
                                                          @RequestParam("size") final int size,
                                                          HttpServletRequest request) {
        Account principle = accountResolver.getAccount(request);

        try {
            return new ResponseEntity<>(
                    eventsRepository.findByOrgAndPriority(
                            eventFactory.resolveOrgId(principle), priority, new PageRequest(page, size)), HttpStatus.OK);

        } catch (EventException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }



}
