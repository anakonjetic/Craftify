package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Complexity;
import com.tvz.hr.craftify.model.Media;
import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Tutorial;
import com.tvz.hr.craftify.repository.MediaRepository;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapFromDTOHelper;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tvz.hr.craftify.utilities.MapFromDTOHelper.mapProjectDTOToProject;
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
        return optionalMedia.map(MapToDTOHelper::mapToMediaGetDTO);
    };
    public MediaGetDTO addMedia(MediaPutPostDTO media) {
        Optional<Project> project = media.getProjectId() != null ? projectRepository.findById(media.getProjectId()) : Optional.empty();
        Optional<Tutorial> tutorial = media.getTutorialId() != null ? tutorialRepository.findById(media.getTutorialId()) : Optional.empty();

        Media newMedia = new Media();
        newMedia.setMedia((media.getMedia()));
        newMedia.setMediaOrder(media.getMediaOrder());
        project.ifPresent(newMedia::setProject);
        tutorial.ifPresent(newMedia::setTutorial);

        return mapToMediaGetDTO(mediaRepository.save(newMedia));
    };
    public MediaGetDTO updateMedia(MediaPutPostDTO media, Long id) {
        Optional<Media> optionalMedia = mediaRepository.findById(id);
        if (optionalMedia.isEmpty()) {
            return null;
        }

        Optional<Project> project = media.getProjectId() != null ? projectRepository.findById(media.getProjectId()) : Optional.empty();
        Optional<Tutorial> tutorial = media.getTutorialId() != null ? tutorialRepository.findById(media.getTutorialId()) : Optional.empty();

        Media existingMedia = optionalMedia.get();
        existingMedia.setMedia((media.getMedia()));
        existingMedia.setMediaOrder(media.getMediaOrder());
        project.ifPresent(existingMedia::setProject);
        tutorial.ifPresent(existingMedia::setTutorial);

        return mapToMediaGetDTO(mediaRepository.save(existingMedia));
    };
    public void deleteMedia(Long id) { mediaRepository.deleteById(id); };

    @Override
    public List<MediaGetDTO> getMediaByIds(List<Long> ids) {
        return mediaRepository.findAllById(ids).stream().map(MapToDTOHelper::mapToMediaGetDTO).collect(Collectors.toList());
    }

    /*public MediaGetDTO mapToMediaGetDTO(Media media){
        ProjectGetDTO projectDTO = media.getProject() != null ? mapToProjectGetDTO(media.getProject()) : null;
        TutorialDTO tutorialDTO = media.getTutorial() != null ? mapToTutorialDTO(media.getTutorial()) : null;

        return new MediaGetDTO(
                media.getId(),
                media.getMedia(),
                media.getMediaOrder(),
                projectDTO,
                tutorialDTO
        );
    }*/
}
