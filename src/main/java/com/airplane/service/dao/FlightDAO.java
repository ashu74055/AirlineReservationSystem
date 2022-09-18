package com.airplane.service.dao;

import com.airplane.service.models.FlightsDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDAO extends JpaRepository<FlightsDetail, Long> {
    Page<FlightsDetail> findAllByCompanyIgnoreCase(String company, Pageable pageable);
    Page<FlightsDetail> findAllByToLevelIgnoreCaseAndDestinationIgnoreCase(String to,String dest, Pageable pageable);

    Page<FlightsDetail> findAllByToLevelIgnoreCaseAndDestinationIgnoreCaseOrderByPrice(String to,String dest, Pageable pageable);
    Page<FlightsDetail> findAllByToLevelIgnoreCaseAndDestinationIgnoreCaseOrderByFlightName(String to,String dest, Pageable pageable);
    Page<FlightsDetail> findAllByToLevelIgnoreCaseAndDestinationIgnoreCaseOrderByFlightTime(String to,String dest, Pageable pageable);
}
