package org.registrator.community.service;

import org.registrator.community.dto.ResourceDTO;
import org.registrator.community.entity.Resource;
import org.registrator.community.entity.ResourceType;

import java.util.List;


public interface ResourceService {
	
	ResourceDTO addNewResource(ResourceDTO resourceDTO);
	
	ResourceDTO findByIdentifier(String identifier);

    List<Resource> findByType (ResourceType type);

    long count();
    
    List<String> getDescriptionBySearchTag(String searchTag);
    
}

