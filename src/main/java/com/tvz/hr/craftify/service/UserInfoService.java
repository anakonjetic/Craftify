package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.service.dto.UsersGetDTO;

import java.util.List;

public interface UserInfoService {
    UsersGetDTO setUserPreference(List<Long> categories, Long userId);
    UsersGetDTO changeUserInfoVisibility(boolean isPrivate, Long id);
    UsersGetDTO changeUserPassword(String newPassword, Long id);
}
