package org.registrator.community.service;

import java.util.List;

import org.registrator.community.dto.CommunityDTO;
import org.registrator.community.entity.TerritorialCommunity;
import org.registrator.community.entity.User;
import org.registrator.community.enumeration.CommunityStatus;
import org.registrator.community.enumeration.RoleType;

public interface CommunityService {

    List<TerritorialCommunity> findAll();

    List<TerritorialCommunity> findAllByAsc();

    TerritorialCommunity findByName(String name);

    TerritorialCommunity addCommunity(TerritorialCommunity territorialCommunity);

    boolean deleteCommunity(TerritorialCommunity territorialCommunity);

    TerritorialCommunity findById(Integer id);

    List<TerritorialCommunity> getCommunityBySearchTag(String searchTag);

    boolean updateCommunity(CommunityDTO communityDTO);

	void setCommunityStatus(TerritorialCommunity community, CommunityStatus status);

    /**
     *
     * This method provide list of communities which can be assigned to other user according to user roletype
     *
     * E. g. Admin can assign user to all communities, Commissioner can assign only his community
     *
     * @param user User which want to change community for another user
     */
    List<TerritorialCommunity> getCommunitiesToAssignByUser(User user);
}
