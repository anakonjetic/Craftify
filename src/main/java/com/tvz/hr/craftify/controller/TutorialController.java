package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.Tutorial;
import com.tvz.hr.craftify.service.TutorialService;
import com.tvz.hr.craftify.service.dto.TutorialDTO;
import com.tvz.hr.craftify.service.dto.TutorialPostDTO;
import com.tvz.hr.craftify.service.dto.TutorialPutDTO;
import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tutorial")
@AllArgsConstructor
//@CrossOrigin(origins = {"http://test-craftify.vercel.app", "http://localhost:4200"})
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
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TutorialDTO> createTutorial(@RequestBody TutorialPostDTO tutorial) {
        return new ResponseEntity<>(
                tutorialService.createTutorial(tutorial),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<TutorialDTO> updateTutorial(@RequestBody Long id, @RequestBody TutorialPutDTO tutorial) {
        try{
            TutorialDTO updatedTutorial = tutorialService.updateTutorial(tutorial, id);
            return new ResponseEntity<>(updatedTutorial, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTutorial(@PathVariable Long id) {
        tutorialService.deleteTutorialById(id);
        return ResponseEntity.noContent().build();
    }

    public UserDetails getLoggedInUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }
}
