package com.tvz.hr.craftify.service;
import com.tvz.hr.craftify.model.*;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.TutorialRepository;
import com.tvz.hr.craftify.service.dto.CategoryDTO;
import com.tvz.hr.craftify.repository.CategoryRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.dto.CategoryGetDTO;
import com.tvz.hr.craftify.service.dto.CategoryPostPutDTO;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToCategoryDTO;
import static com.tvz.hr.craftify.utilities.MapToDTOHelper.mapToCategoryGetDTO;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private UsersRepository usersRepository;
    private ProjectRepository projectRepository;
    private TutorialRepository tutorialRepository;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(MapToDTOHelper::mapToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> getCategoryById(long id) { return categoryRepository.findById(id); };
    @Override
    public Optional<CategoryGetDTO> getCategory(long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.map(MapToDTOHelper::mapToCategoryGetDTO);
    }

    @Override
    public CategoryGetDTO createCategory(CategoryPostPutDTO category) {
        List<Project> projects = new ArrayList<>();
        List<Long> projectIds = category.getProjectIdList();
        if (!projectIds.isEmpty()) {
            projects = projectIds.stream()
                    .map(projectId -> projectRepository.findById(projectId))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        List<Tutorial> tutorials = new ArrayList<>();
        List<Long> tutorialIds = category.getTutorialIdList();
        if (!tutorialIds.isEmpty()) {
            tutorials = tutorialIds.stream()
                    .map(tutorialId -> tutorialRepository.findById(tutorialId))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }

        Category newCategory = new Category(category.getName(), projects, tutorials);

        return mapToCategoryGetDTO(categoryRepository.save(newCategory));
    }

    @Override
    public CategoryGetDTO updateCategory(CategoryPostPutDTO category, long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category categoryToUpdate = optionalCategory.get();

            List<Project> projects = new ArrayList<>();
            List<Long> projectIds = category.getProjectIdList();
            if (!projectIds.isEmpty()) {
                projects = projectIds.stream()
                        .map(projectId -> projectRepository.findById(projectId))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            }

            List<Tutorial> tutorials = new ArrayList<>();
            List<Long> tutorialIds = category.getTutorialIdList();
            if (!tutorialIds.isEmpty()) {
                tutorials = tutorialIds.stream()
                        .map(tutorialId -> tutorialRepository.findById(tutorialId))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            }

            categoryToUpdate.setName(category.getName());
            categoryToUpdate.setProjectList(projects);
            categoryToUpdate.setTutorialList(tutorials);

            return mapToCategoryGetDTO(categoryRepository.save(categoryToUpdate));
        }
        else{
            throw new RuntimeException("Category with ID: " + id + " not found");
        }
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO addUserPreference(Long categoryId, Long userID) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Users user = usersRepository.findById(userID).orElse(null);
        if (category != null && user != null) {
            category.getUserPreferences().add(user);
            category = categoryRepository.save(category);
            categoryRepository.flush();
        }
        return mapToCategoryDTO(category);
    }

    @Override
    public CategoryDTO removeUserPreference(Long categoryId, Long userID) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        Users user = usersRepository.findById(userID).orElse(null);

        if (category != null && user != null) {
            category.getUserPreferences().remove(user);
            category = categoryRepository.save(category);
            categoryRepository.flush();
        }
        return mapToCategoryDTO(category);
    }

}
