package com.airplane.service.controllers;

import com.airplane.service.dao.UserDAO;
import com.airplane.service.dto.*;
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

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/Admin")
public class AdminController {

    @Autowired
    UserDAO userDAO;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/GetAllUserList")
    public ResponseEntity<PaginationResponseDTO<List<User>>> getAllUserList(@RequestBody PaginationDTO r){
        PaginationResponseDTO<List<User>> responseDTO = new PaginationResponseDTO<>();
        Page<User> userPage = userDAO.findAll(PageRequest.of(r.getPageNumber()-1, r.getNumberOfRecordPerPage()));
        responseDTO.setData(userPage.stream().filter(user -> !user.getRole().equalsIgnoreCase("admin")).toList());
        responseDTO.setTotalPage(userPage.getTotalPages());
        responseDTO.setCurrentPage(r.getPageNumber());
        responseDTO.setTotalRecords(userPage.getTotalElements());
        responseDTO.setMessage(responseDTO.getData().isEmpty() ? "No records" : "Records found");
        responseDTO.setIsSuccess(true);
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/ManupulateCustomerAccount")
    public ResponseEntity<BasicResponseDTO<?>> manupulateCustomerAccount(@RequestBody UserManipulateRequestDTO r){
        BasicResponseDTO<?> responseDTO = new BasicResponseDTO<>(true, "Process completed", null);
        Optional<User> _user = userDAO.findById(r.getUserID());
        if(_user.isPresent()){
            User user = _user.get();
            if(r.getBlock())
                user.setIsActive(false);
            if(r.getUnblock())
                user.setIsActive(true);
            userDAO.save(user);
            return new  ResponseEntity(responseDTO, HttpStatus.OK);
        }
        responseDTO.setIsSuccess(false);
        responseDTO.setMessage("User not found");
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }
}
