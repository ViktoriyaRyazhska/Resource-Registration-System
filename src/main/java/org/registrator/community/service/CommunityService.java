package org.registrator.community.service;

import java.util.List;

import org.registrator.community.dto.CommunityDTO;
import org.registrator.community.entity.TerritorialCommunity;
import org.registrator.community.enumeration.CommunityStatus;

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
}
