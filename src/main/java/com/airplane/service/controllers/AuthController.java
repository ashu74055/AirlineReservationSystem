package com.airplane.service.controllers;

import com.airplane.service.dao.UserDAO;
import com.airplane.service.dao.UserPersonalDetailsDAO;
import com.airplane.service.dto.*;
import com.airplane.service.models.User;
import com.airplane.service.models.UserPersonalDetail;
import com.airplane.service.services.UserDetailsService;
import com.airplane.service.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/Authentication")
public class AuthController {
    @Autowired
    UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    UserPersonalDetailsDAO userPersonalDetailsDAO;

    @PostMapping("/SignUp")
    public ResponseEntity<BasicResponseDTO<RegisterResponseDTO>> registerUser(@RequestBody SignUpRequestDTO registerRequestDTO) {
        BasicResponseDTO<RegisterResponseDTO> basicResponseDTO = new BasicResponseDTO<>();
        basicResponseDTO.setData(null);
        basicResponseDTO.setIsSuccess(false);

        if(userDAO.existsByEmail(registerRequestDTO.getEmail()) ){
            basicResponseDTO.setMessage("User already exists");
            return new ResponseEntity<>(basicResponseDTO, HttpStatus.OK);
        }
        if(!registerRequestDTO.getRole().equalsIgnoreCase("customer") && !registerRequestDTO.getMasterPassword().equals("India@123") ){
            basicResponseDTO.setMessage("Wrong master password");
            return new ResponseEntity<>(basicResponseDTO, HttpStatus.OK);
        }
        User user = new User();
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setEmail(registerRequestDTO.getEmail().toLowerCase());
        user.setRole(registerRequestDTO.getRole().toLowerCase());
        user.setIsActive(true);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setDateTime(new Date());
        userDAO.save(user);
        UserPersonalDetail userPersonalDetail = new UserPersonalDetail();
        userPersonalDetail.setUserID(user.getUserID());
        userPersonalDetail.setFirstName(user.getFirstName());
        userPersonalDetail.setLastName(user.getLastName());
        userPersonalDetail.setEmail(user.getEmail());
        userPersonalDetail.setImageUrl("https://res.cloudinary.com/dzavgoc9w/image/upload/v1663592085/Images/Default_j3wvy8.png");
        userPersonalDetailsDAO.save(userPersonalDetail);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        basicResponseDTO.setData(new RegisterResponseDTO(jwtUtil.generateToken(userDetails), user.getEmail(), user.getFirstName()));
        basicResponseDTO.setIsSuccess(true);
        basicResponseDTO.setMessage("User Register Successfully");
        return new ResponseEntity<>(basicResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/SignIn")
    public ResponseEntity<SignInResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        SignInResponseDTO result = this.loginHelper(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()
        );
        return new ResponseEntity<>(result, result.getIsSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }
    public SignInResponseDTO loginHelper(String email, String password) {
        SignInResponseDTO basicResponseDTO = new SignInResponseDTO();
        Optional<User> _user = userDAO.findUserByEmail(email);
        if(_user.isEmpty()){
            basicResponseDTO.setIsSuccess(false);
            basicResponseDTO.setMessage("User not found");
            return basicResponseDTO;
        }
        User user = _user.get();
        if(!user.getIsActive()||user.getCount()>4){
            basicResponseDTO.setIsSuccess(false);
            basicResponseDTO.setMessage("User Account Is Blocked, Please Contact To Admin");
            return basicResponseDTO;
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password
                    )
            );
        } catch (BadCredentialsException e) {
            user.setCount(user.getCount()+1);
            userDAO.save(user);
            basicResponseDTO.setIsSuccess(false);
            basicResponseDTO.setMessage("Credentials not matched");
            return basicResponseDTO;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        SignInDTO signInDTO = new SignInDTO();
        basicResponseDTO.setToken(jwtUtil.generateToken(userDetails));
        signInDTO.setFullName(user.getFirstName() + " "+user.getLastName());
        signInDTO.setUserID(user.getUserID());
        signInDTO.setRole(user.getRole());
        signInDTO.setEmail(user.getEmail());
        basicResponseDTO.setData(signInDTO);
        basicResponseDTO.setIsSuccess(true);
        basicResponseDTO.setMessage("Login Successfully");
        return basicResponseDTO;
    }

}
