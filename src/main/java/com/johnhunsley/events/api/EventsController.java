package com.johnhunsley.events.api;

import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import com.johnhunsley.events.domain.Event;
import com.johnhunsley.events.domain.EventException;
import com.johnhunsley.events.domain.EventFactory;
import com.johnhunsley.events.repository.EventsPagingAndSortingRepository;
//import com.johnhunsley.events.repository.Page;
//import com.stormpath.sdk.account.Account;
//import com.stormpath.sdk.servlet.account.AccountResolver;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

//    @Autowired
//    private AccountResolver accountResolver;

    @Autowired
    private EventsPagingAndSortingRepository eventsRepository;


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
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
//    @PreAuthorize("hasPermission('serviceProvider', 'SERVICE_PROVIDER')")
    public ResponseEntity<Page<Event>> getEventsByOrg(@RequestParam("page") final int page,
                                                     @RequestParam("size") final int size,
                                                     HttpServletRequest request) {
        AuthenticationJsonWebToken authentication = (AuthenticationJsonWebToken)SecurityContextHolder.getContext().getAuthentication();

        try {
            PageRequest pageRequest = new PageRequest(page, size);
            ResponseEntity r =  new ResponseEntity<>(
                    eventsRepository.findByOrganisationOrderByCreatedDateDesc(
                            eventFactory.resolveOrgId(authentication),  pageRequest), HttpStatus.OK);
            return r;

        } catch (EventException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }



}
