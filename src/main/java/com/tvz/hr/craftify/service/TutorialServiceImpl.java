package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.MediaRepository;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToTutorialDTO;

@Service
@AllArgsConstructor
public class TutorialServiceImpl implements TutorialService {

    private TutorialRepository tutorialRepository;
    private CategoryService categoryService;
    private UsersService usersService;
    private ComplexityService complexityService;
    private MediaRepository mediaRepository;
    private final LoggedUserContentService loggedUserContentService;
    private final GuestUserContentService guestUserContentService;
    private final UserAuthorizationService userAuthorizationService;


    @Override
    public List<TutorialDTO> getAllTutorials() {
        Users user = userAuthorizationService.getLoggedInUser();
        ContentService contentService;
        if (user != null)
            contentService = loggedUserContentService;
        else
            contentService = guestUserContentService;
        return contentService.getAllTutorials();
    }

    @Override
    public Optional<TutorialDTO> getTutorialById(Long id) {
        Optional<Tutorial> optionalTutorial = tutorialRepository.findById(id);
        return optionalTutorial.map(MapToDTOHelper::mapToTutorialDTO);
    }

    @Override
    public TutorialDTO createTutorial(TutorialPostDTO postTutorial) {
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
        List<Long> mediaListId = postTutorial.getMediaList().stream().distinct().toList();
        List<Media> mediaList = mediaListId.stream()
                .map(mediaId -> mediaRepository.findById(mediaId).orElseThrow(() -> new RuntimeException("Media not found with ID: " + mediaId)))
                .collect(Collectors.toList());

        newTutorial.setMediaList(mediaList);

        return mapToTutorialDTO(tutorialRepository.save(newTutorial));
    }

    @Override
    public TutorialDTO updateTutorial(TutorialPutDTO tutorialPutDTO, Long id) {
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
            return mapToTutorialDTO(tutorialRepository.save(tutorialToUpdate));
        }else {
            throw new RuntimeException("Tutorial with ID: " + id + "not found");
        }
    }

    @Override
    public void deleteTutorialById(Long id) {
        tutorialRepository.deleteById(id);

    }
}
