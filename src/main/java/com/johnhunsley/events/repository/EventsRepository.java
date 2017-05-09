package com.johnhunsley.events.repository;

import com.johnhunsley.events.domain.Event;

/**
 * <p>
 *     None JPA Repository
 * </p>
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 09/05/2017
 *         Time : 13:05
 */
public interface EventsRepository {

    Event findByHashAndOrganisation(String hash, String organisation);

    Page<Event> findByOrganisationAndPriorityOrderByCreatedDateDesc(String organisation, String priority, int pageSize, int pageNumber) throws RepositoryException;

    void save(Event event);

}
