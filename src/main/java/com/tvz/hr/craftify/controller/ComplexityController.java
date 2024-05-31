package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.Complexity;
import com.tvz.hr.craftify.service.ComplexityService;
import com.tvz.hr.craftify.service.dto.ComplexityDTO;
import com.tvz.hr.craftify.service.dto.ComplexityGetDTO;
import com.tvz.hr.craftify.service.dto.ComplexityPostPutDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/complexity")
@AllArgsConstructor
@CrossOrigin(origins = {"http://test-craftify.vercel.app", "http://localhost:4200"})
public class ComplexityController {
    ComplexityService complexityService;

    @GetMapping("/all")
    public List<ComplexityDTO> getAllComplexities() { return complexityService.getAllComplexities(); }

    @GetMapping("/{id}")
    public ResponseEntity<ComplexityGetDTO> getComplexity(@PathVariable Long id) {
        Optional<ComplexityGetDTO> complexityOptional = complexityService.getComplexityById(id);
        return complexityOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ComplexityGetDTO> createComplexity(@RequestBody ComplexityPostPutDTO complexity){
        return new ResponseEntity<>(
                complexityService.createComplexity(complexity),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ComplexityGetDTO> updateComplexity(@RequestBody ComplexityPostPutDTO complexity, @PathVariable Long id){
        try {
            ComplexityGetDTO updatedComplexity = complexityService.updateComplexity(complexity, id);
            return new ResponseEntity<>(updatedComplexity, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteComplexity(@PathVariable Long id){
        complexityService.deleteComplexity(id);
        return ResponseEntity.noContent().build();
    }

}
