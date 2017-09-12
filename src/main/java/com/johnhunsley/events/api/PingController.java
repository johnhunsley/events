package com.johnhunsley.events.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     Ping controller which simply returns an HTTP 200 for a GET request.
 * </p>
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 29/08/2017
 *         Time : 12:50
 */
@RestController
@RequestMapping("app/ping/")
public class PingController {

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public void ping() {
    }
}
