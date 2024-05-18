package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.service.dto.UsersGetDTO;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private ProjectRepository projectRepository;
    private CategoryService categoryService;
    private UsersService usersService;
    private ComplexityService complexityService;
    private MediaService mediaService;

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(MapToDTOHelper::mapToProjectDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProjectDTO> getProjectById(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        return optionalProject.map(MapToDTOHelper::mapToProjectDTO);
    }

    @Override
    public Optional<List<UserDTO>> getUsersWhoLikedProject(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        List<UserDTO> usersWhoLiked = new ArrayList<>();

        optionalProject.ifPresent(project -> usersWhoLiked.addAll(project.getUserLikes().stream()
                .map(MapToDTOHelper::mapToUserDTO)
                .toList()));

        return Optional.of(usersWhoLiked);
    }


    @Override
    public Project createProject(ProjectPostDTO postProject) {
        Project newProject = new Project();
        newProject.setTitle(postProject.getTitle());
        newProject.setDescription(postProject.getDescription());
        newProject.setContent(postProject.getContent());
        //user setup
        Optional<UsersGetDTO> userRequest = usersService.getUser(postProject.getUserId());
        if (userRequest.isPresent()) {
            Users user = Users.mapToUserFromUserRequest(userRequest.get());
            newProject.setUser(user);
        } else{
            throw new RuntimeException("User with ID: " + postProject.getUserId() + " not found");
        }
        //category setup
        Category category = categoryService.getCategoryById(postProject.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("   Invalid category ID"));
        newProject.setCategory(category);
        //user likes setup
        List<UsersGetDTO> userRequestLikes = usersService.getAllUsers().stream().filter(u -> postProject.getUserLikesIdList().contains(u.getId())).toList();
        List<Users> userLikes = userRequestLikes.stream().map(Users::mapToUserFromUserRequest).toList();
        newProject.setUserLikes(userLikes);
        //favorite projects setup
        List<UsersGetDTO> userRequestFavoriteProjects = usersService.getAllUsers().stream().filter(u -> postProject.getFavoriteProjectUserIdList().contains(u.getId())).toList();
        List<Users> userFavoriteProjects = userRequestFavoriteProjects.stream().map(Users::mapToUserFromUserRequest).toList();
        newProject.setFavoriteProjects(userFavoriteProjects);
        //project followers setup
        List<UsersGetDTO> userRequestFollowers = usersService.getAllUsers().stream().filter(u -> postProject.getProjectFollowersIdList().contains(u.getId())).toList();
        List<Users> userFollowers = userRequestFollowers.stream().map(Users::mapToUserFromUserRequest).toList();
        newProject.setProjectFollowers(userFollowers);
        //complexity setup
        Optional<ComplexityGetDTO> complexityDTO = complexityService.getComplexityById(postProject.getComplexityId());
        if (complexityDTO.isPresent()) {
            Complexity complexity = new Complexity(complexityDTO.get().getId(), complexityDTO.get().getName());
            newProject.setComplexity(complexity);
        } else{
            throw new RuntimeException("Complexity with ID: " + postProject.getComplexityId() + " not found");
        }

        Project savedProject = projectRepository.save(newProject);



        //media setup
        List<MediaPutPostDTO> mediaPost = postProject.getMediaList();
        mediaPost.forEach(media -> {
            media.setProjectId(savedProject.getId());
            mediaService.addMedia(media);
        });
        //TODO Fetch and set comment nedostaje

        return newProject;
    }

    @Override
    public Project updateProject(ProjectPutDTO projectPutDTO, Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project projectToUpdate = optionalProject.get();
            projectToUpdate.setTitle(projectPutDTO.getTitle());
            projectToUpdate.setDescription(projectPutDTO.getDescription());
            projectToUpdate.setContent(projectPutDTO.getContent());
            Category category = categoryService.getCategoryById(projectPutDTO.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            projectToUpdate.setCategory(category);
            Optional<ComplexityGetDTO> complexityDTO = complexityService.getComplexityById(projectPutDTO.getComplexityId());
            if (complexityDTO.isPresent()) {
                Complexity complexity = new Complexity(complexityDTO.get().getId(), complexityDTO.get().getName());
                projectToUpdate.setComplexity(complexity);
            } else{
                throw new RuntimeException("Complexity with ID: " + projectPutDTO.getComplexityId() + " not found");
            }
            List<Long> mediaToRemove = projectToUpdate.getMediaList().stream().map(Media::getId).toList();
            mediaToRemove.forEach(
                    m -> {
                        mediaService.deleteMedia(m);
                    }
            );
            List<MediaPutPostDTO> mediaPost = projectPutDTO.getMediaList();
            mediaPost.forEach(media -> {
                media.setProjectId(projectToUpdate.getId());
                mediaService.addMedia(media);
            });
            return projectRepository.save(projectToUpdate);
        } else {
            throw new RuntimeException("Project with ID: " + id + " not found");
        }
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public Optional<List<ProjectGetDTO>> getFilteredProjects(FilterProjectDTO filterProjectDTO) {
        try {
            List<Project> projects = projectRepository.findByFilters(
                    filterProjectDTO.getNameOrUser(),
                    filterProjectDTO.getCategoryId(),
                    filterProjectDTO.getComplexityId());

            List<ProjectGetDTO> projectGetDTOS = projects.stream()
                    .map(MapToDTOHelper::mapToProjectGetDTO)
                    .toList();

            return projects.isEmpty() ? Optional.empty() : Optional.of(projectGetDTOS);
        } catch (DataAccessException ex) {
            throw new ApplicationException("Database error occurred while filtering projects", ex);
        } catch (Exception ex) {
            throw new ApplicationException("An unexpected error occurred while filtering projects", ex);
        }
    }


}
