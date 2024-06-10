package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Complexity;
import com.tvz.hr.craftify.repository.ComplexityRepository;
import com.tvz.hr.craftify.service.dto.ComplexityDTO;
import com.tvz.hr.craftify.service.dto.ComplexityGetDTO;
import com.tvz.hr.craftify.service.dto.ComplexityPostPutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ComplexityServiceImplTest {

    @Mock
    private ComplexityRepository complexityRepository;

    @InjectMocks
    private ComplexityServiceImpl complexityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllComplexities() {
        List<Complexity> complexities = new ArrayList<>();
        when(complexityRepository.findAll()).thenReturn(complexities);

        List<ComplexityDTO> result = complexityService.getAllComplexities();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(complexityRepository, times(1)).findAll();
    }

    @Test
    void getComplexityById() {
        long id = 1L;
        Complexity complexity = new Complexity();
        complexity.setId(id);
        when(complexityRepository.findById(id)).thenReturn(Optional.of(complexity));

        Optional<ComplexityGetDTO> result = complexityService.getComplexityById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(complexityRepository, times(1)).findById(id);
    }

    @Test
    void createComplexity() {
        ComplexityPostPutDTO complexityDTO = new ComplexityPostPutDTO();
        when(complexityRepository.save(any())).thenReturn(new Complexity());

        ComplexityGetDTO result = complexityService.createComplexity(complexityDTO);

        assertNotNull(result);
        verify(complexityRepository, times(1)).save(any());
    }

    @Test
    void updateComplexity() {
        long id = 1L;
        ComplexityPostPutDTO complexityDTO = new ComplexityPostPutDTO();
        complexityDTO.setName("New Name");
        Complexity existingComplexity = new Complexity();
        existingComplexity.setId(id);
        when(complexityRepository.findById(id)).thenReturn(Optional.of(existingComplexity));
        when(complexityRepository.save(any())).thenReturn(existingComplexity);

        ComplexityGetDTO result = complexityService.updateComplexity(complexityDTO, id);

        assertNotNull(result);
        assertEquals(complexityDTO.getName(), result.getName());
        verify(complexityRepository, times(1)).findById(id);
        verify(complexityRepository, times(1)).save(any());
    }

    @Test
    void deleteComplexity() {
        long id = 1L;

        complexityService.deleteComplexity(id);

        verify(complexityRepository, times(1)).deleteById(id);
    }
}