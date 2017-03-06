package com.johnhunsley.events.domain;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.organization.Organization;
import com.stormpath.sdk.organization.OrganizationList;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 */
public class EventFactory {

    @Value("${stormpath.base.url}")
    private String STORMPATH_API_BASE_URL;

    @Value("${stormpath.version.path}")
    private String STORMPATH_API_VERSION;

    @Value("${stormpath.accounts.path}")
    private String STORMPATH_API_ACCOUNTS_PATH;

    @Value("${stormpath.org.path}")
    private String STORMPATH_API_ORG_PATH;


    private final String stormpathAccountsUrl;

    private final String stormpathOrgsUrl;

    private EventFactory() {
        stormpathAccountsUrl = STORMPATH_API_BASE_URL+STORMPATH_API_VERSION+STORMPATH_API_ACCOUNTS_PATH;
        stormpathOrgsUrl = STORMPATH_API_BASE_URL+STORMPATH_API_VERSION+STORMPATH_API_ORG_PATH;
    }

    public static EventFactory getInstance() {
        return new EventFactory();
    }

    /**
     * <p>
     *
     * </p>
     * @param account
     * @return {@link Event}
     */
    public Event createEvent(Account account) throws EventException {
        return createEvent(account, new Date(System.currentTimeMillis()));
    }

    /**
     * <p>
     *
     * </p>
     * @param account
     * @param date
     * @return
     */
    public Event createEvent(Account account, Date date) throws EventException {
        final String userId = resolveUserId(account);
        final String orgId = resolveOrgId(account);
        return new Event(userId, date, md5Hash(userId, date), orgId);
    }

    /**
     * <p>
     *
     * </p>
     * @param account
     * @return
     * @throws EventException
     */
    private String resolveUserId(Account account) throws EventException {
        final String userHref = account.getHref();
        final String userId = userHref.substring(stormpathAccountsUrl.length()-1);

        if(userId.length() < 1) throw new EventException("Invalid userId within account HREF - "+userHref);

        return userId;
    }

    /**
     * <p>
     *
     * </p>
     * @param account
     * @return
     * @throws EventException
     */
    private String resolveOrgId(Account account) throws EventException {
        OrganizationList list =  account.getDirectory().getOrganizations();

        if(list.getSize() != 1) throw new EventException(
                "Account "+account.getHref()+" is not associated to a single Organisation. Org list size = "+list.getSize());

        Organization org = list.iterator().next();
        final String orgId = org.getHref().substring(stormpathOrgsUrl.length()-1);

        if(orgId.length() < 1) throw new EventException("Invalid orgId within org HREF - "+org.getHref());

        return orgId;
    }

    /**
     *
     * @param user
     * @param created
     * @return
     */
    private String md5Hash(final String user, final Date created) {
        final String str = user+":"+created.toString();
        Hash hash = new Hash(Hash.MD5_TYPE);
        return new String(hash.hash(str));
    }
}
