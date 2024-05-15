package com.tvz.hr.craftify.request;

import com.tvz.hr.craftify.service.dto.CategoryDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersRequest {
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean isAdmin;
    private List<CategoryDTO> userPreferences;

    public UsersRequest(Long id, String username, String email, String password, boolean isAdmin){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
