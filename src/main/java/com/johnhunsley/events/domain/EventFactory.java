package com.johnhunsley.events.domain;


import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

/**
 * <p>
 *     Factory for creating {@link Event} instances in which the USerId, OrgId and resulting Hash are derived
 *     from a given Auth0
 * </p>
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 */
@Component
public class EventFactory {
    public final static String ORGANISATION_KEY = "organisation";

    @Value("${auth0.custom.claim.prefix}")
    private String customClaimPrefix;

    private String organisationClaim;

    @PostConstruct
    public void init() {
       organisationClaim = customClaimPrefix+ORGANISATION_KEY;
    }

    /**
     * <p>
     *    Create an {@link Event} from the given credentials with a created date of now
     * </p>
     * @param token
     * @return {@link Event}
     */
    public Event createEvent(AuthenticationJsonWebToken token) throws EventException {
        return createEvent(token, DateTime.now().toDate());
    }

    /**
     * <p>
     *     Create an {@link Event} from the given credentials and given date. UserId, OrgId
     *     and Hash are derived from the given Account.
     * </p>
     * @param token
     * @param date
     * @return {@link Event}
     */
    public Event createEvent(AuthenticationJsonWebToken token, Date date) throws EventException {
        final String userId = resolveUserId(token);
        final String orgId = resolveOrgId(token);
        return new Event(userId, date, md5Hash(userId, date), orgId);
    }

    /**
     * <p>
     *     Creates a new {@link Event} instance using the given {@link Object} credentials and the
     *     given Event instance properties. Only the credentials from the given account are used to
     *     formulate the Hash, userId and OrgId of the resulting Event instance
     * </p>
     * @param token
     * @param template
     * @return
     * @throws EventException
     */
    public Event createEventFromTemplate(AuthenticationJsonWebToken token, Event template) throws EventException {
        Event event = createEvent(token);
        event.setStatus(template.getStatus());
        event.setPriority(template.getPriority());
        event.setLatitude(template.getLatitude());
        event.setLongitude(template.getLongitude());
//        event.setFirstName(user.getGivenName());
//        event.setLastName(user.getFamilyName());
//        event.setPhoneNumber(user.getExtraInfo().get(PHONENUMBER_KEY).toString());
        return event;
    }

    /**
     * <p>
     *      Derives the Stormpath userId from the HREF of the given {@link Object} instance
     * </p>
     * @param token
     * @return userID
     * @throws EventException
     */
    public String resolveUserId(AuthenticationJsonWebToken token) throws EventException {
        return token.getPrincipal().toString();
    }

    /**
     * <p>
     *  Derives the organisationId from the HREF of the given {@link Object}
     * </p>
     * @param token
     * @return
     * @throws EventException
     */
    public String resolveOrgId(AuthenticationJsonWebToken token) throws EventException {
        DecodedJWT decoded = (DecodedJWT)token.getDetails();
        final Claim claim = decoded.getClaim(organisationClaim);

        if(claim == null) throw new EventException("No custom org claim for user "+token.getPrincipal().toString());

        String claimStr =  claim.asString();
        return claimStr;
    }

    /**
     * <p>
     *     Creates and returns an MD5 Hash of the given user and the given date
     *     concatenated with a colon character
     * </p>
     * @param user
     * @param created
     * @return
     */
    private String md5Hash(final String user, final Date created) {
        final String str = user+":"+created.toString();
        Hash hash = new Hash(Hash.MD5_TYPE);
        return new String(Base64.getEncoder().encode(hash.hash(str)));
    }
}
