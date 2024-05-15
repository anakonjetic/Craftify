package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.Complexity;
import com.tvz.hr.craftify.service.ComplexityService;
import com.tvz.hr.craftify.service.dto.ComplexityDTO;
import com.tvz.hr.craftify.service.dto.ComplexityGetDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/complexity")
@AllArgsConstructor
public class ComplexityController {
    ComplexityService complexityService;

    @GetMapping("/all")
    public List<ComplexityDTO> getAllComplexities() { return complexityService.getAllComplexities(); }

    @GetMapping("/{id}")
    public ResponseEntity<ComplexityGetDTO> getComplexity(@PathVariable Long id) {
        Optional<ComplexityGetDTO> complexityOptional = complexityService.getComplexityById(id);
        if (complexityOptional.isPresent()) {
            return ResponseEntity.ok(complexityOptional.get());
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping
    public ResponseEntity<Complexity> createComplexity(@RequestBody Complexity complexity){
        return new ResponseEntity<>(
                complexityService.createComplexity(complexity),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Complexity> updateComplexity(@RequestBody Complexity complexity, @PathVariable Long id){
        try {
            Complexity updatedComplexity = complexityService.updateComplexity(complexity, id);
            return new ResponseEntity<>(updatedComplexity, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplexity(@PathVariable Long id){
        complexityService.deleteComplexity(id);
        return ResponseEntity.noContent().build();
    }

}
