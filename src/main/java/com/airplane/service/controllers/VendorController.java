package com.airplane.service.controllers;

import com.airplane.service.dao.FlightDAO;
import com.airplane.service.dto.*;
import com.airplane.service.models.Feedback;
import com.airplane.service.models.FlightsDetail;
import com.airplane.service.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/Vendor")
public class VendorController {
    @Autowired
    FlightDAO flightDAO;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/InsertFlightDetails")
    public ResponseEntity<BasicResponseDTO<?>> insertFlightDetails(@RequestBody InsertFlightDetailsRequestDTO r){
        BasicResponseDTO<?> responseDTO = new BasicResponseDTO<>(true, "Flight Successfully added", null);
        FlightsDetail flight = new FlightsDetail();
        flight.setFlightName(r.getFlightName());
        flight.setToLevel(r.getTo());
        flight.setDestination(r.getDestination());
        flight.setFlightTime(r.getTime());
        flight.setPrice(r.getPrice());
        flight.setCompany(r.getCompany());
        flight.setDescription(r.getDescription());
        flight.setInsetionDate(new Date());
        flightDAO.save(flight);
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/GetFlightDetails")
    public ResponseEntity<PaginationResponseDTO<List<FlightsDetail>>>  getAllFlights(@RequestBody GetFlightDetailsRequestDTO requestDTO){
        PaginationResponseDTO<List<FlightsDetail>> responseDTO = new PaginationResponseDTO<>();
        Page<FlightsDetail> flightsDetailPage;
        if(requestDTO.getType().equalsIgnoreCase("all")){
            flightsDetailPage = flightDAO.findAll(PageRequest.of(requestDTO.getPageNumber() -1, requestDTO.getNumberOfRecordPerPage()));
        } else {
            flightsDetailPage = flightDAO.findAllByCompanyIgnoreCase(requestDTO.getType() ,PageRequest.of(requestDTO.getPageNumber() -1, requestDTO.getNumberOfRecordPerPage()));
        }

        responseDTO.setCurrentPage(requestDTO.getPageNumber());
        responseDTO.setTotalRecords(flightsDetailPage.getTotalElements());
        responseDTO.setTotalPage(flightsDetailPage.getTotalPages());
        responseDTO.setData(flightsDetailPage.toList());
        responseDTO.setMessage(responseDTO.getData().isEmpty() ? "No records" : "Data found");
        responseDTO.setIsSuccess(true);
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/UpdateFlightDetails")
    public ResponseEntity<BasicResponseDTO<?>> updateFlightDetails(@RequestBody UpdateFlightDetailsRequestDTO r){
        BasicResponseDTO<?> responseDTO = new BasicResponseDTO<>(true, "Flight successfully updated", null);
        Optional<FlightsDetail> _flight = flightDAO.findById(r.getFlightID());
        if(_flight.isPresent()){
            FlightsDetail flight = _flight.get();
            flight.setFlightName(r.getFlightName());
            flight.setToLevel(r.getTo());
            flight.setDestination(r.getDestination());
            flight.setFlightTime(r.getTime());
            flight.setPrice(r.getPrice());
            flight.setCompany(r.getCompany());
            flight.setDescription(r.getDescription());
            flightDAO.save(flight);
            return new  ResponseEntity(responseDTO, HttpStatus.OK);
        }
        responseDTO.setIsSuccess(false);
        responseDTO.setMessage("Flight not found.");
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/DeleteFlightDetails")
    public ResponseEntity<BasicResponseDTO<?>> deleteFlightDetails(@RequestBody DeleteFlightDetailsDTO r){
        BasicResponseDTO<?> responseDTO = new BasicResponseDTO<>(true, "Flight successfully deleted", null);
        Optional<FlightsDetail> _flight = flightDAO.findById(r.getFlightID());
        if(_flight.isPresent()){
            flightDAO.delete(_flight.get());
            return new  ResponseEntity(responseDTO, HttpStatus.OK);
        }
        responseDTO.setIsSuccess(false);
        responseDTO.setMessage("Flight not found.");
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }
}
