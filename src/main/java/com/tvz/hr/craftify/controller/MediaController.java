package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.service.MediaService;
import com.tvz.hr.craftify.service.dto.MediaDTO;
import com.tvz.hr.craftify.service.dto.MediaGetDTO;
import com.tvz.hr.craftify.service.dto.MediaPutPostDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/media")
@AllArgsConstructor
@CrossOrigin(origins = {"http://test-craftify.vercel.app", "http://localhost:4200"})
public class MediaController {
    public MediaService mediaService;

    @GetMapping("/all")
    public List<MediaDTO> getAllMedia(){ return mediaService.getAllMedia(); }

    @GetMapping("/{id}")
    public ResponseEntity<MediaGetDTO> getMedia(@PathVariable Long id){
        Optional<MediaGetDTO> mediaOptional = mediaService.getMedia(id);
        return mediaOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<MediaGetDTO> addMedia(@RequestBody MediaPutPostDTO media){
        return new ResponseEntity<>(
                mediaService.addMedia(media),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<MediaGetDTO> updateMedia(@RequestBody MediaPutPostDTO media, @PathVariable Long id){
        try{
            MediaGetDTO updatedMedia = mediaService.updateMedia(media, id);
            return new ResponseEntity<>(updatedMedia,HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id){
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }
}
