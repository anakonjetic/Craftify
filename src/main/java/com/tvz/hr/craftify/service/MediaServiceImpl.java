package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Media;
import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Tutorial;
import com.tvz.hr.craftify.repository.MediaRepository;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tvz.hr.craftify.utilities.MapToDTOHelper.*;

@Service
@AllArgsConstructor
public class MediaServiceImpl implements MediaService {
    MediaRepository mediaRepository;
    ProjectRepository projectRepository;
    TutorialRepository tutorialRepository;
    public List<MediaDTO> getAllMedia() {
        return mediaRepository.findAll().stream()
                .map(MapToDTOHelper::mapToMediaDTO)
                .collect(Collectors.toList());
    };

    public Optional<MediaGetDTO> getMedia(Long id) {
        Optional<Media> optionalMedia = mediaRepository.findById(id);
        return optionalMedia.map(this::mapToMediaGetDTO);
    };
    public MediaGetDTO addMedia(MediaPutPostDTO media) {
        Optional<Project> project = projectRepository.findById(media.getProjectId());
        Optional<Tutorial> tutorial = media.getTutorialId() != null ? tutorialRepository.findById(media.getTutorialId()) : Optional.empty();

        Media newMedia = new Media();
        newMedia.setMedia(saveMediaToFileSystem(media.getMedia()));
        newMedia.setMediaOrder(media.getMediaOrder());
        project.ifPresent(newMedia::setProject);
        tutorial.ifPresent(newMedia::setTutorial);

        return mapToMediaGetDTO(mediaRepository.save(newMedia));
    };
    public MediaGetDTO updateMedia(MediaPutPostDTO media, Long id) {
        Optional<Project> project = projectRepository.findById(media.getProjectId());
        Optional<Tutorial> tutorial = tutorialRepository.findById(media.getTutorialId());

        Media existingMedia = mediaRepository.getById(id);
        existingMedia.setMedia(saveMediaToFileSystem(media.getMedia()));
        existingMedia.setMediaOrder(media.getMediaOrder());
        project.ifPresent(existingMedia::setProject);
        tutorial.ifPresent(existingMedia::setTutorial);

        return mapToMediaGetDTO(mediaRepository.save(existingMedia));
    };
    public void deleteMedia(Long id) { mediaRepository.deleteById(id); };

    @Override
    public List<Media> getMediaByIds(List<Long> ids) {
        return mediaRepository.findAllById(ids); // Fetches media by list of IDs
    }

    public MediaGetDTO mapToMediaGetDTO(Media media){
        Long projectId = media.getProject() != null ? media.getProject().getId() : null;
        ProjectGetDTO projectDTO = projectId != null ? mapToProjectGetDTO(media.getProject()) : null;
        Long tutorialId = media.getTutorial() != null ? media.getTutorial().getId() : null;
        TutorialDTO tutorialDTO = tutorialId != null ? mapToTutorialDTO(media.getTutorial()) : null;

        return new MediaGetDTO(
                media.getId(),
                media.getMedia(),
                media.getMediaOrder(),
                projectDTO,
                tutorialDTO
        );
    }

    //function for saving media (images or videos) to file storage and retrieving path/URL
    private String saveMediaToFileSystem(byte[] b){
        String url = "http...";
        return url;
    }
}
