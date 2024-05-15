package com.tvz.hr.craftify.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SubscriptionDTO {
    private Long userId;
    //can be user or project id
    private Long followingId;
}
