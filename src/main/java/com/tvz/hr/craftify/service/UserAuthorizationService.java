package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.service.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserAuthorizationService {
    void checkAuthorization(Long userId);
    Users getLoggedInUser();
}
