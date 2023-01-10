package com.airplane.service.controllers;

import com.airplane.service.dao.FlightDAO;
import com.airplane.service.dao.TicketDetailDAO;
import com.airplane.service.dao.UserDAO;
import com.airplane.service.dao.UserPersonalDetailsDAO;
import com.airplane.service.dto.*;
import com.airplane.service.models.FlightsDetail;
import com.airplane.service.models.TicketDetail;
import com.airplane.service.models.User;
import com.airplane.service.models.UserPersonalDetail;
import com.airplane.service.services.FileUploadStorage;
import com.airplane.service.services.FilesStorageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static com.cloudinary.utils.ObjectUtils.asMap;
import static java.util.Collections.emptyMap;

@RestController
@CrossOrigin
@RequestMapping("/api/Customer")
public class CustomerController {
    @Autowired
    UserDAO userDAO;

    @Autowired
    FlightDAO flightDAO;

    @Autowired
    UserPersonalDetailsDAO userPersonalDetailsDAO;

    @Autowired
    TicketDetailDAO ticketDetailDAO;

    @Autowired
    FilesStorageService filesStorageService;

    private ModelMapper modelMapper = new ModelMapper();

    @Value("${files.upload.url}")
    private String fileUrl;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/SearchAndFilterFlights")
    public ResponseEntity<PaginationResponseDTO<List<FlightsDetail>>> searchAndFilterFlights(@RequestBody CustomerSearchDTO r){
        PaginationResponseDTO<List<FlightsDetail>> responseDTO = new PaginationResponseDTO<>();
        //PageRequest pageRequest = PageRequest.of(r.getPageNumber() -1, r.getNumberOfRecordPerPage());
        List<FlightsDetail> flightsDetailPage = flightDAO.findAll();
//        if(r.isSearch() && r.isFilter()){
//            if(r.getFilterOn().isPrice())
//                flightsDetailPage = flightDAO.findAllByToLevelIgnoreCaseAndDestinationIgnoreCaseOrderByPrice(r.getTo(), r.getDestination(), pageRequest);
//            if(r.getFilterOn().isTime())
//                flightsDetailPage = flightDAO.findAllByToLevelIgnoreCaseAndDestinationIgnoreCaseOrderByFlightTime(r.getTo(), r.getDestination(), pageRequest);
//            if(r.getFilterOn().isName())
//                flightsDetailPage = flightDAO.findAllByToLevelIgnoreCaseAndDestinationIgnoreCaseOrderByFlightName(r.getTo(), r.getDestination(), pageRequest);
//        } else if(r.isFilter()){
//            flightsDetailPage = flightDAO.findAll(pageRequest);
//        } else if(r.isSearch()){
//            flightsDetailPage = flightDAO.findAllByToLevelIgnoreCaseAndDestinationIgnoreCase(r.getTo(), r.getDestination(), pageRequest);
//        }
        responseDTO.setMessage(flightsDetailPage.stream().count()==0 ? "No records" : "Records found");
        responseDTO.setData(flightsDetailPage);
        responseDTO.setTotalRecords(flightsDetailPage.stream().count());
        responseDTO.setCurrentPage(r.getPageNumber());
        responseDTO.setIsSuccess(true);
        responseDTO.setTotalPage((int)Math.ceil(flightsDetailPage.stream().count()/r.getNumberOfRecordPerPage()));
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/UpdateCustomerInfomation")
    public ResponseEntity<BasicResponseDTO<?>> updateCustomerInfomation(@RequestBody UpdateCustomerDTO r){
        BasicResponseDTO<?> responseDTO = new BasicResponseDTO<>(true, "Process completed", null);
        Optional<UserPersonalDetail> _user = userPersonalDetailsDAO.findByUserID(r.getUserID());
        if(_user.isPresent()){
            UserPersonalDetail p = _user.get();
            p.setUserID(r.getUserID());
            p.setFirstName(r.getFirstName());
            p.setLastName(r.getLastName());
            p.setAddress(r.getAddress());
            p.setCity(r.getCity());
            p.setState(r.getState());
            p.setZipCode(r.getZipCode());
            p.setHomePhone(r.getHomePhone());
            p.setPersonalNumber(r.getPersonalNumber());
            p.setEmail(r.getEmail());
            p.setGender(r.getGender());
            p.setDob(r.getDob());
            p.setOccupation(r.getOccupation());
            userPersonalDetailsDAO.save(p);
            return new  ResponseEntity(responseDTO, HttpStatus.OK);
        }
        responseDTO.setIsSuccess(false);
        responseDTO.setMessage("User not found");
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/GetCustomerInfomation")
    public ResponseEntity<BasicResponseDTO<UserPersonalDetail>> getCustomerInfomation(@RequestParam("UserID") Long userID){
        BasicResponseDTO<UserPersonalDetail> responseDTO = new BasicResponseDTO<>(true, "User found", null);
        Optional<UserPersonalDetail> _user = userPersonalDetailsDAO.findByUserID(userID);
        if(_user.isPresent()){
            responseDTO.setData(_user.get());
        }
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/PaymentGetway")
    public ResponseEntity<BasicResponseDTO<TicketDetail>> paymentGetway(@RequestBody PaymentGatwayReqDTO r){
        BasicResponseDTO<TicketDetail> responseDTO = new BasicResponseDTO<>(true, "Payment Updated", null);
        TicketDetail ticketDetail = modelMapper.map(r, TicketDetail.class);
        ticketDetail.setInsetionDate(new Date());
        ticketDetailDAO.save(ticketDetail);
        responseDTO.setData(ticketDetail);
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/UpdateTicket")
    public ResponseEntity<BasicResponseDTO<?>> updateTicket(@RequestBody UpdateTicketReqDTO r){
        BasicResponseDTO<UserPersonalDetail> responseDTO = new BasicResponseDTO<>(true, "Cancel Ticket Successfully", null);
        Optional<TicketDetail> ticketDetail = ticketDetailDAO.findById(r.getTicketID());
        if(ticketDetail.isEmpty()){
            responseDTO.setMessage("Ticket not found");
            return new  ResponseEntity(responseDTO, HttpStatus.OK);
        }
        TicketDetail ticketDetail1 = ticketDetail.get();
        ticketDetail1.setStatus(r.getStatus());
        ticketDetailDAO.save(ticketDetail1);
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/GetUserTickets")
    public ResponseEntity<PaginationResponseDTO<List<GetUserTicketsResponseDTO>>> getUserTickets(@RequestBody GetUserTicketReqDTO r){
        PaginationResponseDTO<List<GetUserTicketsResponseDTO>> responseDTO = new PaginationResponseDTO<>();
        List<GetUserTicketsResponseDTO> response = new ArrayList<>();
        Page<TicketDetail> ticketDetailPage = null;
        if(r.getUserID()==-1){
            ticketDetailPage = ticketDetailDAO.findAll(
                    PageRequest.of(r.getPageNumber() - 1, r.getNumberOfRecordPerPage()));
        }else {
            ticketDetailPage = ticketDetailDAO.findAllByUserID(r.getUserID(),
                    PageRequest.of(r.getPageNumber() - 1, r.getNumberOfRecordPerPage()));
        }
        ticketDetailPage.forEach(X->{
            GetUserTicketsResponseDTO data = new GetUserTicketsResponseDTO();
            data.setTicketID(X.getTicketID());
            data.setUserID(X.getUserID());
            data.setFlightID(X.getFlightID());
            FlightsDetail flightsDetail = flightDAO.getReferenceById(X.getFlightID());
            data.setFlightName(flightsDetail.getFlightName());
            data.setCompany(flightsDetail.getCompany());
            data.setTo(flightsDetail.getToLevel());
            //Destination, FlightDate, Time, PaymentType,CardNo, UPIID, Price, Status, SeatClass
            data.setDestination(flightsDetail.getDestination());
            data.setFlightDate(X.getFlightDate());
            data.setTime(flightsDetail.getFlightTime());
            data.setPaymentType(X.getPaymentType());
            data.setCardNo(X.getCartNo());
            data.setUPIID(X.getUpiid());
            data.setPrice(X.getPrice());
            data.setStatus(X.getStatus());
            data.setSeatClass(X.getSeatClass());
            response.add(data);
        });
        responseDTO.setTotalPage(ticketDetailPage.getTotalPages());
        responseDTO.setData(response);
        responseDTO.setTotalRecords(ticketDetailPage.getTotalElements());
        responseDTO.setCurrentPage(r.getPageNumber());
        responseDTO.setIsSuccess(true);
        responseDTO.setMessage(responseDTO.getData().isEmpty() ? "No records": "Records found");
        return new  ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/UpdateImage", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BasicResponseDTO<?>> updateImage(@ModelAttribute UpdateImageReqDTO r) throws IOException {
        BasicResponseDTO<UserPersonalDetail> responseDTO = new BasicResponseDTO<>(true, "Image Upload Successfully", null);
        try {
            UserPersonalDetail _user = userPersonalDetailsDAO.getReferenceById(r.getUserID());
            if (_user != null) {
                // Cloudinary Image Upload
                // Account Configuration
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "dzavgoc9w",
                        "api_key", "842688657531372",
                        "api_secret", "-djtDm1NRXVtjZ3L-HGaLfYnNBw",
                        "secure", true));
                // Upload Image
                Map uploadResult = cloudinary.uploader().upload(r.getFile().getBytes(), ObjectUtils.emptyMap());
                //_user.setImageUrl(FileUploadStorage.uploadFile(r.getFile()));
                // Update Image Url
                _user.setImageUrl(uploadResult.get("url").toString());
                userPersonalDetailsDAO.save(_user);
                responseDTO.setIsSuccess(true);
                responseDTO.setMessage("File upload issue");
                return new ResponseEntity(responseDTO, HttpStatus.OK);
            } else {
                responseDTO.setMessage("user not found");
                responseDTO.setIsSuccess(false);
                return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            responseDTO.setIsSuccess(false);
            responseDTO.setMessage("Exception Message : "+ex.getMessage());
            return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
        }
//        if(_user.isPresent()){
//            //Optional<String> fileName = filesStorageService.save(r.getFile());
//            String fileName = filesStorageService.uploadFile(r.getFile());
//            if(fileName.isEmpty()){
//
//                responseDTO.setIsSuccess(false);
//                responseDTO.setMessage("File upload issue");
//                return new  ResponseEntity(responseDTO, HttpStatus.OK);
//            }
//            UserPersonalDetail userPersonalDetail = _user.get();
//            userPersonalDetail.setImageUrl(fileUrl+fileName.get());
//            userPersonalDetail.setPublicID(UUID.randomUUID().toString());
//            userPersonalDetailsDAO.save(userPersonalDetail);
//            return new  ResponseEntity(responseDTO, HttpStatus.OK);
//        }

    }
}
