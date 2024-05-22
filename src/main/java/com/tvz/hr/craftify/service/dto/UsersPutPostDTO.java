package com.tvz.hr.craftify.service.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersPutPostDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private boolean isAdmin;
    private boolean isPrivate;
    private List<Long> userPreferences;
}
