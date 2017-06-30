package com.johnhunsley.events.api;

import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import com.johnhunsley.events.domain.Event;
import com.johnhunsley.events.domain.EventException;
import com.johnhunsley.events.domain.EventFactory;
import com.johnhunsley.events.domain.EventId;
import com.johnhunsley.events.repository.EventsPagingAndSortingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 18/04/2017
 *         Time : 12:11
 */
@RestController
@RequestMapping("app/event/")
public class EventController {

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private EventsPagingAndSortingRepository eventsRepository;

    /**
     *
     * @param template
     * @return
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
//    @PreAuthorize("hasPermission('customer', 'CUSTOMER')")
    public ResponseEntity createEvent(@RequestBody Event template) {
        AuthenticationJsonWebToken authentication = (AuthenticationJsonWebToken) SecurityContextHolder.getContext().getAuthentication();

        try {
            eventsRepository.save(
                    eventFactory.createEventFromTemplate(authentication, template));
            return new ResponseEntity(HttpStatus.ACCEPTED);

        } catch (EventException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @CrossOrigin
    @RequestMapping(value = "{hash}", method = RequestMethod.GET, produces = "application/json")
//    @PreAuthorize("hasPermission('serviceProvider', 'SERVICE_PROVIDER')")
    public ResponseEntity<Event> getEventById(@PathVariable("hash") final String hash) {
        AuthenticationJsonWebToken authentication = (AuthenticationJsonWebToken) SecurityContextHolder.getContext().getAuthentication();

        try {
            Collection<Event> events = eventsRepository.findByHashAndOrganisation(hash, eventFactory.resolveOrgId(authentication));

            if(events.isEmpty() || events.size() > 1) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Event event = events.iterator().next();

            if(event !=  null) return new ResponseEntity<>(event, HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (EventException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <p>
     *     Updates the status of the resolved {@link Event} with the status of the given object
     *     //todo maybe move this to a service instance and update all the event fields from the given template object
     * </p>
     * @param hash
     * @param template
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "{hash}", method = RequestMethod.PUT, consumes = "application/json")
    @PreAuthorize("hasPermission('serviceProvider', 'SERVICE_PROVIDER')")
    public ResponseEntity updateEvent(@PathVariable final String hash, @RequestBody final Event template) {
        AuthenticationJsonWebToken authentication = (AuthenticationJsonWebToken) SecurityContextHolder.getContext().getAuthentication();

        try {
            Collection<Event> events = eventsRepository.findByHashAndOrganisation(hash, eventFactory.resolveOrgId(authentication));

            if(events.isEmpty() || events.size() > 1) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Event event = events.iterator().next();
            event.setStatus(template.getStatus());
            eventsRepository.save(event);
            return new ResponseEntity(HttpStatus.ACCEPTED);

        } catch (EventException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
