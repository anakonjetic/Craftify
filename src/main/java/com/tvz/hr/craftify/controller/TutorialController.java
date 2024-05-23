package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.Tutorial;
import com.tvz.hr.craftify.service.TutorialService;
import com.tvz.hr.craftify.service.dto.TutorialDTO;
import com.tvz.hr.craftify.service.dto.TutorialPostDTO;
import com.tvz.hr.craftify.service.dto.TutorialPutDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tutorial")
@AllArgsConstructor

public class TutorialController {
    private final TutorialService tutorialService;

    @GetMapping("/all")
    public List<TutorialDTO> getAllTutorials() {
        return tutorialService.getAllTutorials();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TutorialDTO> getTutorialById(@PathVariable Long id) {
        Optional<TutorialDTO> tutorialOptional = tutorialService.getTutorialById(id);
        return tutorialOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TutorialPostDTO> createTutorial(@RequestBody TutorialPostDTO tutorial) {
        tutorialService.createTutorial(tutorial);
        return new ResponseEntity<>(tutorial, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TutorialPutDTO> updateTutorial(@RequestBody Long id, @RequestBody TutorialPutDTO tutorial) {
        try{
            tutorialService.updateTutorial(tutorial, id);
            return new ResponseEntity<>(tutorial, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTutorial(@PathVariable Long id) {
        tutorialService.deleteTutorialById(id);
        return ResponseEntity.noContent().build();
    }
}
