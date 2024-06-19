package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Tutorial;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.service.dto.*;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import com.tvz.hr.craftify.utilities.exceptions.ApplicationException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToUsersGetDTO;

@Service
@AllArgsConstructor
public class LoggedUserContentService implements ContentService{
    private UserAuthorizationService userAuthorizationService;
    private ProjectRepository projectRepository;
    private TutorialRepository tutorialRepository;

    @Override
    public List<ProjectDTO> getAllProjects(){
        try {
            Users user = userAuthorizationService.getLoggedInUser();
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            UsersGetDTO loggedInUser  = mapToUsersGetDTO(user);
            List<CategoryDTO> userPreferences = loggedInUser.getUserPreferences();

            List<Project> projects = new ArrayList<>();
            for (CategoryDTO category : userPreferences) {
                List<Project> projectsByCategory = projectRepository.getAllProjectsByCategoryIdAndOrderByUserLikes(category.getId());
                projects.addAll(projectsByCategory);
            }

            List<Long> preferredCategoryIds = userPreferences.stream()
                    .map(CategoryDTO::getId)
                    .collect(Collectors.toList());

            List<Project> otherProjects = projectRepository.getAllProjectsAndOrderByUserLikes()
                    .stream()
                    .filter(project -> !preferredCategoryIds.contains(project.getCategory().getId()))
                    .collect(Collectors.toList());

            projects.addAll(otherProjects);

            return projects.stream().map(MapToDTOHelper::mapToProjectDTO).collect(Collectors.toList());

        } catch (DataAccessException ex) {
            throw new ApplicationException("Database error occurred while getting projects by preference", ex);
        } catch (Exception ex) {
            throw new ApplicationException("An unexpected error occurred while getting projects by preference", ex);
        }
    }

    @Override
    public List<TutorialDTO> getAllTutorials() {
        try {
            Users user = userAuthorizationService.getLoggedInUser();
            if (user == null) throw new RuntimeException("User not found");
            UsersGetDTO loggedInUser = mapToUsersGetDTO(user);
            List<CategoryDTO> userPreferences = loggedInUser.getUserPreferences();

            List<Tutorial> tutorials = new ArrayList<>();
            for (CategoryDTO category : userPreferences) {
                List<Tutorial> tutorialsByCategory = tutorialRepository.findByCategory_Id(category.getId());
                tutorials.addAll(tutorialsByCategory);
            }

            List<Long> preferredCategoryIds = userPreferences.stream()
                    .map(CategoryDTO::getId)
                    .collect(Collectors.toList());

            List<Tutorial> otherTutorials = tutorialRepository.findAll()
                    .stream()
                    .filter(tutorial -> !preferredCategoryIds.contains(tutorial.getCategory().getId()))
                    .collect(Collectors.toList());

            tutorials.addAll(otherTutorials);
            return tutorials.stream().map(MapToDTOHelper::mapToTutorialDTO).collect(Collectors.toList());

        } catch (DataAccessException ex) {
            throw new ApplicationException("Database error occurred while getting tutorials by preference", ex);
        } catch (Exception ex) {
            throw new ApplicationException("An unexpected error occurred while getting tutorials by preference", ex);
        }
    }
}
