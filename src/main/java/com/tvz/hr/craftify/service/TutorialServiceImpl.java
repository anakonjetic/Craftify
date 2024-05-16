package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Category;
import com.tvz.hr.craftify.model.Complexity;
import com.tvz.hr.craftify.model.Tutorial;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TutorialServiceImpl implements TutorialService {

    private TutorialRepository tutorialRepository;
    private CategoryService categoryService;
    private UsersService usersService;
    private ComplexityService complexityService;


    @Override
    public List<TutorialDTO> getAllTutorials() {
        List<Tutorial> tutorials = tutorialRepository.findAll();
        return tutorials.stream()
                .map(MapToDTOHelper::mapToTutorialDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TutorialDTO> getTutorialById(Long id) {
        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(id);
        return optionalTutorial.map(MapToDTOHelper::mapToTutorialDTO);
    }

    @Override
    public Tutorial createTutorial(TutorialPostDTO postTutorial) {
        Tutorial newTutorial = new Tutorial();
        newTutorial.setTitle(postTutorial.getTitle());
        newTutorial.setContent(postTutorial.getContent());

        Optional<UsersGetDTO> userRequest = usersService.getUser(postTutorial.getUserId());
        if (userRequest.isPresent()) {
            Users user = Users.mapToUserFromUserRequest(userRequest.get());
            newTutorial.setUser(user);
        }else {
            throw new RuntimeException("User not found");
        }
        Category category = categoryService.getCategoryById(postTutorial.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        newTutorial.setCategory(category);

        Optional<ComplexityGetDTO> complexityDTO = complexityService.getComplexityById(postTutorial.getComplexityId());
        if (complexityDTO.isPresent()) {
            Complexity complexity = new Complexity(complexityDTO.get().getId(), complexityDTO.get().getName());
            newTutorial.setComplexity(complexity);
        }else{
            throw new RuntimeException("Complexity with ID: " + postTutorial.getComplexityId()+ "not found");
        }

        return tutorialRepository.save(newTutorial);
    }

    @Override
    public Tutorial updateTutorial(TutorialPutDTO tutorialPutDTO, Long id) {
        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(id);
        if (optionalTutorial.isPresent()) {
            Tutorial tutorialToUpdate = optionalTutorial.get();
            tutorialToUpdate.setTitle(tutorialPutDTO.getTitle());
            tutorialToUpdate.setContent(tutorialPutDTO.getContent());
            Category category = categoryService.getCategoryById(tutorialPutDTO.getCategoryId()).orElseThrow(() -> new RuntimeException("category not found"));
            tutorialToUpdate.setCategory(category);
            Optional<ComplexityGetDTO> complexityDTO = complexityService.getComplexityById(tutorialPutDTO.getComplexityId());
            if (complexityDTO.isPresent()) {
                Complexity complexity = new Complexity(complexityDTO.get().getId(), complexityDTO.get().getName());
                tutorialToUpdate.setComplexity(complexity);
            }else{
                throw new RuntimeException("Complexity with ID: " + tutorialPutDTO.getComplexityId()+ "not found");
            }
            return tutorialRepository.save(tutorialToUpdate);
        }else {
            throw new RuntimeException("Tutorial with ID: " + id + "not found");
        }
    }

    @Override
    public void deleteTutorialById(Long id) {
        tutorialRepository.deleteById(id);

    }
}
