package com.tvz.hr.craftify.service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersGetDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean isAdmin;
    private List<CategoryDTO> userPreferences;
}
