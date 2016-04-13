package org.registrator.community.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.registrator.community.dao.RoleRepository;
import org.registrator.community.entity.Role;
import org.registrator.community.enumeration.RoleType;
import org.registrator.community.service.RoleService;
import org.registrator.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserService userService;
	
	@Transactional
	@Override
	public List<Role> getAllRole() {
		return roleRepository.findAll();
	}

	/**
	 * Returns roles which current user can assign to users with specified RoleType
	 * @param currentRole users role for which changing roles is applied
	 */
	@Override
	public List<Role> getAvailableRolesToAssign(RoleType currentRole) {
        /*
        so we have redundant Role entity, it would be much simpler with @Enumerable RoleType on User
        instead of separate entity Role
        couldn't fix that right now
        */

		Role userRole = userService.getLoggedUser().getRole();
		List<RoleType> availableRoleType = Collections.emptyList();
		if (userRole.getType() == RoleType.ADMIN) {
			// admin can change all roles
			availableRoleType = Arrays.asList(RoleType.values());
		} else if (userRole.getType() == RoleType.COMMISSIONER) {
			// commissioner can't change admin role
			if (currentRole == RoleType.ADMIN) {
				availableRoleType = Collections.singletonList(RoleType.ADMIN);
			} else {
                availableRoleType = Arrays.stream(RoleType.values())
                        .filter(roleType -> roleType != RoleType.ADMIN)
                        .collect(Collectors.toList());
            }
		}

        List<Role> result = new ArrayList<>();
        for (Role role : getAllRole()) {
            if (availableRoleType.contains(role.getType())) {
                result.add(role);
            }
        }

        return result;
	}
}
