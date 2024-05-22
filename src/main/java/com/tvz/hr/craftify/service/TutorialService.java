package com.tvz.hr.craftify.service;
import com.tvz.hr.craftify.model.Tutorial;
import com.tvz.hr.craftify.service.dto.TutorialDTO;
import com.tvz.hr.craftify.service.dto.TutorialPostDTO;
import com.tvz.hr.craftify.service.dto.TutorialPutDTO;

import java.util.List;
import java.util.Optional;

public interface TutorialService {
    List<TutorialDTO> getAllTutorials();
    Optional<TutorialDTO> getTutorialById(Long id);
    TutorialDTO createTutorial(TutorialPostDTO tutorialPostDTO);
    TutorialDTO updateTutorial(TutorialPutDTO tutorialPutDTO, Long id);
    void deleteTutorialById(Long id);
}
