package com.tvz.hr.craftify.controller;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.service.ProjectDTO;
import com.tvz.hr.craftify.service.ProjectPostDTO;
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

        @PostMapping
        public ResponseEntity<Project> createProject(@RequestBody ProjectPostDTO project) {
            return new ResponseEntity<>(projectService.createProject(project), HttpStatus.CREATED);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
            try {
                Project updatedProject = projectService.updateProject(project, id);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        }
    }

