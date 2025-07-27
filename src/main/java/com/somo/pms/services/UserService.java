package com.somo.pms.services;

import com.somo.pms.dto.LoginRequest;
import com.somo.pms.dto.TokenResponse;
import com.somo.pms.dto.UserRequest;
import com.somo.pms.dto.UserResponse;
import org.apache.coyote.BadRequestException;

public interface UserService {
    public UserResponse createUser(UserRequest userRequest) throws BadRequestException;
    public TokenResponse createToken(LoginRequest userRequest) throws BadRequestException;

}
