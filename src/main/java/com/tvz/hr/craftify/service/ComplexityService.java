package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Complexity;
import com.tvz.hr.craftify.repository.ComplexityRepository;
import com.tvz.hr.craftify.service.dto.ComplexityDTO;
import com.tvz.hr.craftify.service.dto.ComplexityGetDTO;
import com.tvz.hr.craftify.service.dto.ComplexityPostPutDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface ComplexityService {
    List<ComplexityDTO> getAllComplexities();
    Optional<ComplexityGetDTO> getComplexityById(Long id);
    ComplexityGetDTO createComplexity(ComplexityPostPutDTO complexity);
    ComplexityGetDTO updateComplexity(ComplexityPostPutDTO complexity, Long id);
    void deleteComplexity(Long id);
}
