package com.johnhunsley.events.domain;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.organization.Organization;
import com.stormpath.sdk.organization.OrganizationList;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

/**
 * <p>
 *     Factory for creating {@link Event} instances in which the USerId, OrgId and resulting Hash are derived
 *     from a given Stormpath {@link Account}
 * </p>
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 */
@Component
public class EventFactory {

    @Value("${stormpath.base.url}")
    private String STORMPATH_API_BASE_URL;

    @Value("${stormpath.version.path}")
    private String STORMPATH_API_VERSION;

    @Value("${stormpath.accounts.path}")
    private String STORMPATH_API_ACCOUNTS_PATH;

    @Value("${stormpath.org.path}")
    private String STORMPATH_API_ORG_PATH;

    private String stormpathAccountsUrl;

    private String stormpathOrgsUrl;

    @PostConstruct
    public void init() {
        stormpathAccountsUrl = STORMPATH_API_BASE_URL+STORMPATH_API_VERSION+STORMPATH_API_ACCOUNTS_PATH;
        stormpathOrgsUrl = STORMPATH_API_BASE_URL+STORMPATH_API_VERSION+STORMPATH_API_ORG_PATH;
    }

    /**
     * <p>
     *    Create an {@link Event} from the given credentials with a created date of now
     * </p>
     * @param account
     * @return {@link Event}
     */
    public Event createEvent(Account account) throws EventException {
        return createEvent(account, DateTime.now().toDate());
    }

    /**
     * <p>
     *     Create an {@link Event} from the given credentials and given date. UserId, OrgId
     *     and Hash are derived from the given Account.
     * </p>
     * @param account
     * @param date
     * @return {@link Event}
     */
    public Event createEvent(Account account, Date date) throws EventException {
        final String userId = resolveUserId(account);
        final String orgId = resolveOrgId(account);
        return new Event(userId, date, md5Hash(userId, date), orgId);
    }

    /**
     * <p>
     *     Creates a new {@link Event} instance using the given {@link Account} credentials and the
     *     given Event instance properties. Only the credentials from the given account are used to
     *     formulate the Hash, userId and OrgId of the resulting Event instance
     * </p>
     * @param account
     * @param template
     * @return
     * @throws EventException
     */
    public Event createEventFromTemplate(Account account, Event template) throws EventException {
        Event event = createEvent(account);
        event.setStatus(template.getStatus());
        event.setPriority(template.getPriority());
        event.setLatitude(template.getLatitude());
        event.setLongitude(template.getLongitude());
        event.setFirstName(account.getGivenName());
        event.setLastName(account.getSurname());
        event.setPhoneNumber(account.getCustomData().get("PhoneNumber").toString());
        return event;
    }

    /**
     * <p>
     *      Derives the Stormpath userId from the HREF of the given {@link Account} instance
     * </p>
     * @param account
     * @return userID
     * @throws EventException
     */
    public String resolveUserId(Account account) throws EventException {
        final String userHref = account.getHref();
        final String userId = userHref.substring(stormpathAccountsUrl.length());

        if(userId.length() < 1) throw new EventException("Invalid userId within account HREF - "+userHref);

        return userId;
    }

    /**
     * <p>
     *  Derives the organisationId from the HREF of the given {@link Account}
     * </p>
     * @param account
     * @return
     * @throws EventException
     */
    public String resolveOrgId(Account account) throws EventException {
        OrganizationList list =  account.getDirectory().getOrganizations();

        if(list.getSize() != 1) throw new EventException(
                "Account "+account.getHref()+" is not associated to a single Organisation. Org list size = "+list.getSize());

        Organization org = list.iterator().next();
        final String orgId = org.getHref().substring(stormpathOrgsUrl.length());

        if(orgId.length() < 1) throw new EventException("Invalid orgId within org HREF - "+org.getHref());

        return orgId;
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
