package org.registrator.community.validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.registrator.community.dao.UserRepository;
import org.registrator.community.dto.json.CommunityParamJson;
import org.registrator.community.dto.json.RoleTypeJson;
import org.registrator.community.entity.TerritorialCommunity;
import org.registrator.community.entity.User;
import org.registrator.community.enumeration.RoleType;
import org.registrator.community.enumeration.UIMessages;
import org.registrator.community.service.CommunityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MassUserOpsValidator implements Validator {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(MassUserOpsValidator.class);

    private List<User> userList = new ArrayList<User>();
    private List<Class<?>> supportedClasses = new ArrayList<Class<?>>();

    @Override
    public boolean supports(Class<?> clazz) {
        supportedClasses.add(CommunityParamJson.class);
        supportedClasses.add(RoleTypeJson.class);
        return supportedClasses.contains(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof CommunityParamJson) {
            CommunityParamJson taskInfo = (CommunityParamJson) target;
            validateCommunJson(taskInfo, errors);
        } else if (target instanceof RoleTypeJson) {
            RoleTypeJson taskInfo = (RoleTypeJson) target;
            validateRoleTypeJson(taskInfo, errors);
        }
    }

    private void validateRoleTypeJson(RoleTypeJson task, Errors errors){
        if (task.getLogin() == null || task.getRole() == null) {
            logger.warn("Empty RoleTypeJson batch file");
            errors.reject(UIMessages.WRONG_INPUT.getMessage());
            return;
        }

        if (!checkIfUsersExist(task.getLogin())) {
            errors.reject(UIMessages.WRONG_INPUT.getMessage());
            return;
        }

        if (!checkIfIsSelf()) {
            errors.reject(UIMessages.CANT_CHANGE_SELF.getMessage());
            return;
        }

        if (!checkIfIsAdmin()) {
            errors.reject(UIMessages.IS_ADMIN.getMessage());
            return;
        }

        if (!checkIfIsFromSameCommunity()) {
            errors.reject(UIMessages.DIFFIRENT_TCS.getMessage());
            return;
        }

        if (!checkIfRoleExists(task.getRole())) {
            errors.reject(UIMessages.WRONG_INPUT.getMessage());
            return;
        }
    }
    
    private void validateCommunJson(CommunityParamJson task, Errors errors){
        if (task.getLogin() == null
                || task.getCommunityId() == null) {
            logger.warn("Empty RoleTypeJson batch file");
            errors.reject(UIMessages.WRONG_INPUT.getMessage());
            return;
        }

        if (!checkIfUsersExist(task.getLogin())) {
            errors.reject(UIMessages.WRONG_INPUT.getMessage());
            return;
        }

        if (!checkIfIsSelf()) {
            errors.reject(UIMessages.CANT_CHANGE_SELF.getMessage());
            return;
        }

        if (!checkIfIsAdmin()) {
            errors.reject(UIMessages.IS_ADMIN.getMessage());
            return;
        }

        if (!checkIfCommunityExists(task.getCommunityId())) {
            errors.reject(UIMessages.WRONG_INPUT.getMessage());
            return;
        }
    }
    
    private boolean checkIfUsersExist(String userLogins) {
        List<String> givenUsers = new ArrayList<String>();
        Collections.addAll(givenUsers, userLogins.split(","));

        userList = userRepository.findUsersByLoginList(givenUsers);
        logger.warn("Bad login list");
        boolean testSuccessfull = !userList.isEmpty();
        if (!testSuccessfull) {
            logger.warn("Bad login list");
        }
        return testSuccessfull;
    }

    private boolean checkIfIsAdmin() {
        for (User user : userList) {
            if (user.getRole().getType() == RoleType.ADMIN) {
                logger.warn("Cant change admins");
                return false;
            }
        }
        return true;

    }

    private boolean checkIfIsSelf() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String autherizedUser = auth.getName();
        logger.info("Performed by: " + autherizedUser);

        for (User user : userList) {
            if (user.getLogin().equals(autherizedUser)) {
                logger.warn("Tried to change self's role");
                return false;
            }
        }
        return true;
    }

    private boolean checkIfIsFromSameCommunity() {
        int territorialCommunityId = -1;
        for (User user : userList) {
            int tmp = user.getTerritorialCommunity().getTerritorialCommunityId();
            if (territorialCommunityId == -1) {
                territorialCommunityId = tmp;
            } else {
                if (territorialCommunityId != tmp) {
                    logger.warn("Cant select users with diffirent communities");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkIfRoleExists(String roleName) {
        try {
            RoleType.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            logger.warn("Role \"" + roleName + "\" doesnt exist");
            return false;
        }
        return true;
    }

    private boolean checkIfCommunityExists(String communityId) {
        Integer parsedCommunityId = new Integer(-1);
        try {
            parsedCommunityId = Integer.parseInt(communityId);
        } catch (Exception e) {
            return false;
        }
        TerritorialCommunity community = communityService.findById(parsedCommunityId);
        boolean testSuccessfull = (community != null);
        if (!testSuccessfull) {
            logger.warn("Community with id# " + communityId + " doesnt exist");
        }
        return (community != null);
    }
}

