package com.johnhunsley.events.repository;

import com.johnhunsley.events.domain.Event;
import com.johnhunsley.events.domain.EventId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBPagingAndSortingRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 */
public interface EventsPagingAndSortingRepository extends DynamoDBPagingAndSortingRepository<Event, EventId> {

    @EnableScan
    @EnableScanCount
    Page<Event> findByOrganisation(@Param("organisation") String organisation, Pageable pageable);

    @EnableScan
    @EnableScanCount
    Collection<Event> findByHashAndOrganisation(@Param("hash") String hash, @Param("organisation") String organisation);

    Collection<Event> findByOrganisation(@Param("organisation") String organisation);

    @EnableScan
    @EnableScanCount
    Page<Event> findByOrganisationAndStatus(@Param("organisation") String organisation, @Param("status") String status, Pageable pageable);

    Collection<Event> findByOrganisationAndStatus(@Param("organisation") String organisation, @Param("status") String status);

    @EnableScan
    @EnableScanCount
    Page<Event> findByOrganisationAndStatusAndPriority(@Param("organisation") String organisation, @Param("status") String status, @Param("priority") String priority, Pageable pageable);


    Page<Event> findByOrganisationAndPriorityOrderByCreatedDateDesc(@Param("organisation") String organisation, @Param("priority") String priority, Pageable pageable);


    Page<Event> findByOrganisationOrderByCreatedDateDesc(@Param("organisation") String organisation, Pageable pageable);

    @EnableScan
    @EnableScanCount
    Page<Event> findByStatusAndPriority(@Param("status") String status, @Param("priority") String priority, Pageable pageable);



}
