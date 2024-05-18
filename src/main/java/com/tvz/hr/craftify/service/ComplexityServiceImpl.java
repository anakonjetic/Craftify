package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Complexity;
import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.ComplexityRepository;
import com.tvz.hr.craftify.service.dto.ComplexityDTO;
import com.tvz.hr.craftify.service.dto.ComplexityGetDTO;
import com.tvz.hr.craftify.service.dto.ComplexityPostPutDTO;
import com.tvz.hr.craftify.service.dto.ProjectDTO;
import com.tvz.hr.craftify.utilities.MapFromDTOHelper;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tvz.hr.craftify.utilities.MapFromDTOHelper.mapComplexityGetDTOToComplexity;
import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToComplexityDTO;
import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToComplexityGetDTO;

@Service
@AllArgsConstructor
public class ComplexityServiceImpl implements ComplexityService {
    ComplexityRepository complexityRepository;
    @Override
    public List<ComplexityDTO> getAllComplexities() {
        List<Complexity> complexities = complexityRepository.findAll();
        return complexities.stream()
                .map(MapToDTOHelper::mapToComplexityDTO)
                .collect(Collectors.toList());
    };
    @Override
    public Optional<ComplexityGetDTO> getComplexityById(Long id) {
        Optional<Complexity> optionalComplexity = complexityRepository.findById(id);
        return optionalComplexity.map(MapToDTOHelper::mapToComplexityGetDTO);
    };
    @Override
    public ComplexityGetDTO createComplexity(ComplexityPostPutDTO complexity) {
        Complexity newComplexity = new Complexity(complexity.getId(), complexity.getName());
        return mapToComplexityGetDTO(complexityRepository.save(newComplexity));
    };
    @Override
    public ComplexityGetDTO updateComplexity(ComplexityPostPutDTO complexity, Long id) {
        Optional<Complexity> optionalComplexity = complexityRepository.findById(id);
        if (optionalComplexity.isEmpty()) {
            return null;
        }
        Complexity existingComplexity = optionalComplexity.get();
        existingComplexity.setName(complexity.getName());

        return mapToComplexityGetDTO(complexityRepository.save(existingComplexity));
    };
    @Override
    public void deleteComplexity(Long id) { complexityRepository.deleteById(id); };
}
