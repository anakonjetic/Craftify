package com.tvz.hr.craftify.service;

import com.tvz.hr.craftify.model.Project;
import com.tvz.hr.craftify.model.Users;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.request.UsersRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{
    private UsersRepository usersRepository;
    private ProjectRepository projectRepository;
    public List<UserDTO> getUserFollowers(Long userId){
        List<Users> users = usersRepository.findById(userId).get().getFollowers();
        return users.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUserFollowings(Long userId){
        List<Users> users = usersRepository.findById(userId).get().getFollowedUsers();
        return users.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectDTO> getUserProjectFollowings(Long userId){
        List<Project> projects = usersRepository.findById(userId).get().getFollowingProjects();
        return projects.stream()
                .map(this::mapToProjectDTO)
                .collect(Collectors.toList());
    };

    public List<UserDTO> getProjectFollowers(Long projectId) {
        List<Users> users = projectRepository.findById(projectId).get().getProjectFollowers();
        return users.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    };

    public void followUser(SubscriptionDTO sub){
        Users user = usersRepository.findById(sub.getUserId()).get();
        Users followedUser = usersRepository.findById(sub.getFollowingId()).get();
        List<Users> followingUsers = usersRepository.findById(sub.getUserId()).get().getFollowedUsers();
        if (!followingUsers.contains(followedUser)) {
            followingUsers.add(followedUser);
            user.setFollowedUsers(followingUsers);
            usersRepository.save(user);
        }
    }

    public void unfollowUser(SubscriptionDTO sub){
        Users user = usersRepository.findById(sub.getUserId()).get();
        Users followedUser = usersRepository.findById(sub.getFollowingId()).get();
        List<Users> followingUsers = usersRepository.findById(sub.getUserId()).get().getFollowedUsers();
        if (followingUsers.contains(followedUser)) {
            followingUsers.remove(followedUser);
            user.setFollowedUsers(followingUsers);
            usersRepository.save(user);
        }
    }

    public void followProject(SubscriptionDTO sub){
        Users user = usersRepository.findById(sub.getUserId()).get();
        Project followedProject = projectRepository.findById(sub.getFollowingId()).get();
        List<Project> followingProjects = usersRepository.findById(sub.getUserId()).get().getFollowingProjects();

        if(!followingProjects.contains(followedProject)){
            followingProjects.add(followedProject);
            user.setFollowingProjects(followingProjects);
            usersRepository.save(user);
        }
    }

    public void unfollowProject(SubscriptionDTO sub){
        Users user = usersRepository.findById(sub.getUserId()).get();
        Project followedProject = projectRepository.findById(sub.getFollowingId()).get();
        List<Project> followingProjects = usersRepository.findById(sub.getUserId()).get().getFollowingProjects();

        if(followingProjects.contains(followedProject)){
            followingProjects.remove(followedProject);
            user.setFollowingProjects(followingProjects);
            usersRepository.save(user);
        }
    }


    private UserDTO mapToUserDTO(Users user){
        return new UserDTO(
                user.getId(),
                user.getUsername()
        );
    }
    private ProjectDTO mapToProjectDTO(Project project){
        return new ProjectDTO(
                project.getId(),
                project.getUser().getUsername(),
                project.getTitle(),
                project.getDescription(),
                project.getContent(),
                project.getCategory().getName(),
                project.getComplexity().getName(),
                project.getMediaList()
        );
    }
}
