package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {

        private final ProjectService projectService;

        @GetMapping("/all")
        public List<ProjectDTO> getProjects() {
            return projectService.getAllProjects();
        }

        @GetMapping("/{id}")
        public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
            Optional<ProjectDTO> projectOptional = projectService.getProjectById(id);
            return projectOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }

        @GetMapping("/{id}/likes")
        public ResponseEntity<List<UserDTO>> getUsersWhoLikedProject(@PathVariable long id) {
            return projectService.getUsersWhoLikedProject(id)
                    .map(users -> ResponseEntity.ok().body(users))
                    .orElseGet(() -> ResponseEntity.noContent().build());
        }

        @GetMapping("/preference/{id}")
        public ResponseEntity<List<ProjectGetDTO>> getProjectsByUserPreferences(@PathVariable long id) {
            return projectService.getProjectsByUserPreference(id)
                    .map(users -> ResponseEntity.ok().body(users))
                    .orElseGet(() -> ResponseEntity.noContent().build());
        }

        @PostMapping
        public ResponseEntity<ProjectPostDTO> createProject(@RequestBody ProjectPostDTO project) {
            projectService.createProject(project);
            return new ResponseEntity<>(project, HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ProjectPutDTO> updateProject(@PathVariable Long id, @RequestBody ProjectPutDTO project) {
            try {
                projectService.updateProject(project, id);
                return new ResponseEntity<>(project, HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        }

        @PostMapping("/filter")
        public ResponseEntity<List<ProjectGetDTO>> filterProjects(@RequestBody FilterProjectDTO filterProjectDTO) {
            return projectService.getFilteredProjects(filterProjectDTO)
                    .map(projects -> ResponseEntity.ok().body(projects))
                    .orElseGet(() -> ResponseEntity.noContent().build());
        }
    }

