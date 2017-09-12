package com.johnhunsley.events.api;

import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import com.johnhunsley.events.domain.Event;
import com.johnhunsley.events.domain.EventException;
import com.johnhunsley.events.domain.EventFactory;
import com.johnhunsley.events.repository.EventsPagingAndSortingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *     REST Controller for searching and paging {@link Event} instances
 * </p>
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
    private EventsPagingAndSortingRepository eventsRepository;


    /**
     * <p>
     *     Get the {@link Event} instances for the authenticated user's org
     * </p>
     * @param page
     * @param size
     * @return {@link ResponseEntity<Page<Event>>}
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Page<Event>> getEvents(@RequestParam("page") final int page,
                                                 @RequestParam("size") final int size) {
        AuthenticationJsonWebToken authentication = (AuthenticationJsonWebToken)SecurityContextHolder.getContext().getAuthentication();

        try {
            PageRequest pageRequest = new PageRequest(page, size);
            return new ResponseEntity<>(
                    eventsRepository.findByOrganisationOrderByCreatedDateDesc(
                            eventFactory.resolveOrgId(authentication),  pageRequest), HttpStatus.OK);

        } catch (EventException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
