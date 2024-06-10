package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Media;
import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Tutorial;
import com.tvz.hr.craftify.repository.MediaRepository;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.service.dto.MediaDTO;
import com.tvz.hr.craftify.service.dto.MediaGetDTO;
import com.tvz.hr.craftify.service.dto.MediaPutPostDTO;
import com.tvz.hr.craftify.utilities.MapFromDTOHelper;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
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

class MediaServiceImplTest {

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private MediaServiceImpl mediaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMedia_Success() {
        List<Media> mediaList = new ArrayList<>();
        mediaList.add(new Media());
        mediaList.add(new Media());

        when(mediaRepository.findAll()).thenReturn(mediaList);

        List<MediaDTO> result = mediaService.getAllMedia();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getMedia_Success() {
        Media media = new Media();
        media.setId(1L);

        when(mediaRepository.findById(1L)).thenReturn(Optional.of(media));

        Optional<MediaGetDTO> resultOptional = mediaService.getMedia(1L);

        assertTrue(resultOptional.isPresent());
        assertEquals(1L, resultOptional.get().getId());
    }

    @Test
    void addMedia_Success() {
        MediaPutPostDTO mediaDTO = new MediaPutPostDTO();
        mediaDTO.setMedia("Media content");

        when(projectRepository.findById(any())).thenReturn(Optional.empty());
        when(tutorialRepository.findById(any())).thenReturn(Optional.empty());
        when(mediaRepository.save(any())).thenReturn(new Media());

        MediaGetDTO result = mediaService.addMedia(mediaDTO);

        assertNotNull(result);
    }

    @Test
    void updateMedia_Success() {
        MediaPutPostDTO mediaDTO = new MediaPutPostDTO();
        mediaDTO.setMedia("Updated media content");

        when(mediaRepository.findById(1L)).thenReturn(Optional.of(new Media()));
        when(projectRepository.findById(any())).thenReturn(Optional.empty());
        when(tutorialRepository.findById(any())).thenReturn(Optional.empty());
        when(mediaRepository.save(any())).thenReturn(new Media());

        MediaGetDTO result = mediaService.updateMedia(mediaDTO, 1L);

        assertNotNull(result);
    }

    @Test
    void deleteMedia_Success() {
        doNothing().when(mediaRepository).deleteById(1L);

        assertDoesNotThrow(() -> mediaService.deleteMedia(1L));
    }

    @Test
    void getMediaByIds_Success() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<Media> mediaList = new ArrayList<>();
        mediaList.add(new Media());
        mediaList.add(new Media());

        when(mediaRepository.findAllById(ids)).thenReturn(mediaList);

        Optional<List<MediaGetDTO>> resultOptional = mediaService.getMediaByIds(ids);

        assertTrue(resultOptional.isPresent());
        assertEquals(2, resultOptional.get().size());
    }
}
