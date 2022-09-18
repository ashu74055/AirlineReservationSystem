package com.airplane.service.controllers;

import com.airplane.service.dao.FeedbackDAO;
import com.airplane.service.dao.UserDAO;
import com.airplane.service.dto.*;
import com.airplane.service.models.Feedback;
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
@RequestMapping("api/Feedback")
public class FeedbackController {
    @Autowired
    FeedbackDAO feedbackDAO;

    @Autowired
    UserDAO userDAO;


    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/InsertFeedback")
    public ResponseEntity<BasicResponseDTO<?>> insertFeedback(@RequestBody InsertFeedbackRequestDTO requestDTO){
        BasicResponseDTO<?> responseDTO = new BasicResponseDTO<>(true, "Send Feedback Successfully", null);
        Feedback feedback = new Feedback();
        feedback.setFeedbackSummary(requestDTO.getFeedback());
        feedback.setUserID(requestDTO.getUserID());
        feedback.setInsertionDate(new Date());
        feedbackDAO.save(feedback);
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/DeleteFeedback")
    public ResponseEntity<BasicResponseDTO<?>> deleteFeedback(@RequestBody DeleteFeedbackRequestDTO requestDTO){
        BasicResponseDTO<?> responseDTO = new BasicResponseDTO<>(true, "Feedback Successfully deleted", null);
        feedbackDAO.deleteById(requestDTO.getFeedbackID());
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/GetFeedback")
    public ResponseEntity<PaginationResponseDTO<List<GetFeedbackDTO>>>  getAllFeedback(@RequestBody PaginationDTO requestDTO){
        PaginationResponseDTO<List<GetFeedbackDTO>> responseDTO = new PaginationResponseDTO<>();
        Page<Feedback> feedbackPage = feedbackDAO.findAll(PageRequest.of(requestDTO.getPageNumber() -1, requestDTO.getNumberOfRecordPerPage()));
        responseDTO.setCurrentPage(requestDTO.getPageNumber());
        responseDTO.setTotalRecords(feedbackPage.getTotalElements());
        responseDTO.setTotalPage(feedbackPage.getTotalPages());
        List<GetFeedbackDTO> feedbackDTOList = feedbackPage.stream().map(feedback -> {
           GetFeedbackDTO feedbackDTO =  new GetFeedbackDTO(
                   feedback.getFeedbackID(),
                   feedback.getInsertionDate(),
                   feedback.getFeedbackSummary(),
                   null
                   );
            Optional<User> user = userDAO.findById(feedback.getUserID());
            if(user.isPresent()){
                feedbackDTO.setUserName(user.get().getFirstName() + " "+ user.get().getLastName());
            }
            return feedbackDTO;
        }).toList();
        if(!feedbackDTOList.isEmpty()){
            responseDTO.setData(feedbackDTOList);
        }
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }
}
