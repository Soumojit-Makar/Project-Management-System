package com.somo.pms.services;

import com.somo.pms.dto.request.LoginRequest;
import com.somo.pms.dto.request.UpdateUserRequest;
import com.somo.pms.dto.request.UserRequest;
import com.somo.pms.dto.response.TokenResponse;
import com.somo.pms.dto.response.UserResponse;
import com.somo.pms.models.User;
import org.apache.coyote.BadRequestException;

public interface UserService {
    public UserResponse createUser(UserRequest userRequest) throws BadRequestException;
    public TokenResponse createToken(LoginRequest userRequest) throws BadRequestException;
    public UserResponse updateUser(UpdateUserRequest userRequest, String id) throws BadRequestException;
    public UserResponse updatePassword(String password,String email) throws BadRequestException;
    public UserResponse getUser(String id) throws BadRequestException;
    public UserResponse getUserByEmail(String email) throws BadRequestException;
    public User findUserByJWT(String jwt) throws BadRequestException;
    public UserResponse updateUserProjectSize(String userId,int number) throws BadRequestException;
    public String findUserIdByJWT(String jwt) throws BadRequestException;
}
