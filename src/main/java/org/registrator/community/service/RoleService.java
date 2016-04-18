package org.registrator.community.service;

import java.util.List;

import org.registrator.community.entity.Role;
import org.registrator.community.enumeration.RoleType;

public interface RoleService {
	
	public List<Role> getAllRole();

	/**
	 * Returns roles which current user can assign to users with specified RoleType
	 * @param roleType users role for which changing roles is applied
     */
	List<Role> getAvailableRolesToAssign(RoleType roleType);
}
