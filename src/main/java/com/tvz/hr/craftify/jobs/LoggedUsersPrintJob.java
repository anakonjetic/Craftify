package com.tvz.hr.craftify.jobs;

import com.tvz.hr.craftify.model.RefreshToken;
import com.tvz.hr.craftify.repository.ProjectRepository;
import com.tvz.hr.craftify.repository.RefreshTokenRepository;
import com.tvz.hr.craftify.repository.UsersRepository;
import com.tvz.hr.craftify.service.JwtService;
import com.tvz.hr.craftify.service.RefreshTokenService;
import com.tvz.hr.craftify.service.dto.UserDTO;
import com.tvz.hr.craftify.utilities.MapToDTOHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class LoggedUsersPrintJob extends QuartzJobBean {
    private Logger log = LoggerFactory.getLogger(LoggedUsersPrintJob.class);
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;

    public LoggedUsersPrintJob(RefreshTokenRepository refreshTokenRepository,RefreshTokenService refreshTokenService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        final List<RefreshToken> tokens = refreshTokenRepository.findAll();
        List<UserDTO> todaysLoggedUsers = new ArrayList<>();
        if(!tokens.isEmpty()){
            todaysLoggedUsers = tokens.stream()
                    .map(token -> {
                        try {
                            return refreshTokenService.verifyExpiration(token);
                        } catch (RuntimeException ex) {
                            log.info("Refresh token for user {} has expired.", token.getUser().getUsername());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(RefreshToken::getUser)
                    .map(MapToDTOHelper::mapToUserDTO)
                    .distinct()
                    .toList();
        }
        if(!todaysLoggedUsers.isEmpty()){
            log.info("These are the users who are currently logged in.");
            log.info("---------------------------------------");
            todaysLoggedUsers.forEach(user -> log.info("Id=" + user.getId() + ", user=" + user.getName() + ", username=" + user.getUsername()));
            log.info("---------------------------------------");
        } else {
            log.info("There are no users currently logged in.");
        }
    }
}
