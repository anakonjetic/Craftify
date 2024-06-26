package com.tvz.hr.craftify.service.dto;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String username;
    private Boolean admin;
}
