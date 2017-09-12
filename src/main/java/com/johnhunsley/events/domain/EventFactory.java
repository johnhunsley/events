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
    public final static String FIRSTNAME_KEY = "firstName";
    public final static String LASTNAME_KEY = "lastName";
    public final static String PHONE_KEY = "phoneNumber";

    @Value("${auth0.custom.claim.prefix}")
    private String customClaimPrefix;

    private String organisationClaim;
    private String firstNameClaim;
    private String lastNameClaim;
    private String phoneClaim;

    @PostConstruct
    public void init() {
        organisationClaim = customClaimPrefix+ORGANISATION_KEY;
        firstNameClaim = customClaimPrefix+FIRSTNAME_KEY;
        lastNameClaim = customClaimPrefix+LASTNAME_KEY;
        phoneClaim = customClaimPrefix+PHONE_KEY;
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
     * @return {@link Event}
     * @throws EventException
     */
    public Event createEventFromTemplate(AuthenticationJsonWebToken token, Event template) throws EventException {
        Event event = createEvent(token);
        event.setStatus(template.getStatus());
        event.setPriority(template.getPriority());
        event.setLatitude(template.getLatitude());
        event.setLongitude(template.getLongitude());
        DecodedJWT decoded = (DecodedJWT)token.getDetails();
        event.setFirstName(decoded.getClaim(firstNameClaim).asString());
        event.setLastName(decoded.getClaim(lastNameClaim).asString());
        event.setPhoneNumber(decoded.getClaim(phoneClaim).asString());
        return event;
    }

    /**
     * <p>
     *      Derives the Auth0 userId from the HREF of the given {@link AuthenticationJsonWebToken} instance
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
     *  Derives the organisationId from the HREF of the given {@link AuthenticationJsonWebToken}
     * </p>
     * @param token
     * @return {@link String} claim
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
     * @return {@link String} md5 hash of the given user and date
     */
    private String md5Hash(final String user, final Date created) {
        final String str = user+":"+created.toString();
        Hash hash = new Hash(Hash.MD5_TYPE);
        return new String(Base64.getEncoder().encode(hash.hash(str)));
    }
}
