package com.somo.pms.services;

import com.somo.pms.dto.*;
import org.apache.coyote.BadRequestException;

public interface UserService {
    public UserResponse createUser(UserRequest userRequest) throws BadRequestException;
    public TokenResponse createToken(LoginRequest userRequest) throws BadRequestException;
    public UserResponse updateUser(UpdateUserRequest userRequest, String id) throws BadRequestException;
    public UserResponse updatePassword(String password,String email) throws BadRequestException;
    public UserResponse getUser(String id) throws BadRequestException;
    public UserResponse getUserByEmail(String email) throws BadRequestException;
}
