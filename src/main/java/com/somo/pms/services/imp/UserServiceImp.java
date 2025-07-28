package com.somo.pms.services.imp;
import com.somo.pms.auth.JwtProvider;
import com.somo.pms.dto.*;
import com.somo.pms.models.User;
import com.somo.pms.repositories.UserRepository;
import com.somo.pms.services.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
@Service

public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomeUserDetailsImpl customeUserDetailsImpl;

    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, CustomeUserDetailsImpl customeUserDetailsImpl) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customeUserDetailsImpl = customeUserDetailsImpl;
    }
    public UserResponse createUser(UserRequest userRequest) throws BadRequestException {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new BadRequestException("User is already exist");
        }
//        System.out.println(userRequest.toString());
        User user = userRequestMapToUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProjectSize(0);
//        System.out.println(user.toString());
        return userMapToUserResponse(userRepository.save(user));
    }
    public TokenResponse createToken(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new BadCredentialsException("Email not found"));
        Authentication authentication = this.authenticate(request.getEmail(),request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token =jwtProvider.generateToken(authentication);

        return TokenResponse.builder()
                .token(token)
                .message("Successfully logged in")
                .expires(Date.from(Instant.now().plusSeconds(60*60*24)))
                .user(userMapToUserResponse(user))
                .header("Bearer ")
                .build();
    }
    public UserResponse updateUser(UpdateUserRequest userRequest, String id) throws BadRequestException {
        User user = userRepository.findById(id).orElseThrow(()->new BadRequestException("User not found"));
        if ( userRequest.getEmail() ==null || !userRequest.getEmail().equals(user.getEmail())) {
            user.setEmail(userRequest.getEmail());

        }
        if (userRequest.getFullName() ==null || !userRequest.getFullName().equals(user.getFullName())) {
            user.setFullName(user.getFullName());
        }
        user.setProjectSize(userRequest.getProjectSize());
        return userMapToUserResponse(userRepository.save(user));
    }
    public UserResponse getUser(String id) throws BadRequestException {
        User user = userRepository.findById(id).orElseThrow(()->new BadRequestException("User not found"));
        return userMapToUserResponse(user);
    }
    public UserResponse getUserByEmail(String email) throws BadRequestException {
        User user = userRepository.findByEmail(email).orElseThrow(()->new BadRequestException("User not found"));
        return userMapToUserResponse(user);
    }
    public UserResponse updatePassword( String password,String email ) throws BadRequestException {
        User user = userRepository.findByEmail(email).orElseThrow(()->new BadRequestException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        return userMapToUserResponse(userRepository.save(user));
    }


    private  Authentication authenticate(@NotBlank(message = "Email id required") @Email(message = "Invalid Email Address") String email, @NotBlank(message = "Password id required") String password) {
        UserDetails userDetails=customeUserDetailsImpl.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid Email Address");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())) {
           throw new BadCredentialsException("Invalid Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private static User userRequestMapToUser(UserRequest userRequest) {
        return User.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }
    private static UserResponse userMapToUserResponse(User user) {
        return  UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .projectSize(user.getProjectSize())
                .build();
    }


}
