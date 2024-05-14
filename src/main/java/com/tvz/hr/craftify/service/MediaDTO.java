package com.tvz.hr.craftify.service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO {
    private Long id;
    private String media;
    private Integer mediaOrder;
}
