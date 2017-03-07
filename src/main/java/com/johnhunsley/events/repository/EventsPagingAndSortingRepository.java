package com.johnhunsley.events.repository;

import com.johnhunsley.events.domain.Event;
import com.johnhunsley.events.domain.EventId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBPagingAndSortingRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 *         Time : 14:49
 */
@EnableScan
@EnableScanCount
public interface EventsPagingAndSortingRepository extends DynamoDBPagingAndSortingRepository<Event, EventId> {

    Page<Event> findByOrg(@Param("org") String organisation, Pageable pageable);

    Collection<Event> findByOrg(@Param("org") String organisation);

    Page<Event> findByOrgAndStatus(@Param("org") String organisation, @Param("status") String status, Pageable pageable);

    Collection<Event> findByOrgAndStatus(@Param("org") String organisation, @Param("status") String status);

}
