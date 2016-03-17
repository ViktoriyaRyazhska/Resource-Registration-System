package org.registrator.community.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.registrator.community.dto.json.SearchResultJson;
import org.registrator.community.dto.ParameterSearchResultDTO;
import org.registrator.community.dto.ResourceDTO;
import org.registrator.community.dto.UserDTO;
import org.registrator.community.dto.json.PolygonJson;
import org.registrator.community.dto.json.ResourceSearchJson;
import org.registrator.community.entity.*;
import org.registrator.community.service.DiscreteParameterService;
import org.registrator.community.service.LinearParameterService;
import org.registrator.community.service.ResourceDeleteService;
import org.registrator.community.service.ResourceService;
import org.registrator.community.service.ResourceTypeService;
import org.registrator.community.service.UserService;
import org.registrator.community.validator.ResourceDTOValidator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/registrator/resource")
public class ResourceController {

    @Autowired
    private Logger logger;

    @Autowired
    private ResourceDTOValidator validator;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceDeleteService resourceDeleteService;

    @Autowired
    private ResourceTypeService resourceTypeService;

    @Autowired
    private DiscreteParameterService discreteParameterService;

    @Autowired
    private LinearParameterService linearParameterService;

    @Autowired
    private UserService userService;

    /**
     * Method for loading form for input the parameter of resource (with
     * existing resource types)
     * 
     * @param model
     * @return addResource.jsp
     */
    @PreAuthorize("hasRole('ROLE_REGISTRATOR')")
    @RequestMapping(value = "/addresource", method = RequestMethod.GET)
    public String addResourceForm(Model model) {

        /* load list of resource types on UI form */
        List<ResourceType> listOfResourceType = resourceTypeService.findAll();
        logger.info(listOfResourceType.size() + " resource types was found");
        model.addAttribute("listOfResourceType", listOfResourceType);
        ResourceDTO newresource = new ResourceDTO();

        /*
         * fill default registration number of resource depending on
         * authenticated user
         */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        newresource.setIdentifier(resourceService.getRegistrationNumber(auth.getName()));
        model.addAttribute("newresource", newresource);
        return "addResource";
    }

    /**
     * Method save the resource with all parameters from UI in database
     * 
     * @param resourceDTO
     * @param result
     * @param model
     * @param ownerLogin
     * @return showResource.jsp (addResource.jsp page if resource not valid)
     */
    @PreAuthorize("hasRole('ROLE_REGISTRATOR')")
    @RequestMapping(value = "/addresource", method = RequestMethod.POST)
    public String addResource(@Valid @ModelAttribute("newresource") ResourceDTO resourceDTO, BindingResult result,
            Model model, String ownerLogin) {

        logger.info("The ownerLogin is " + ownerLogin);

        /* check if given resourceDTO is valid */
        validator.validate(resourceDTO, result);
        if (result.hasErrors()) {
            logger.info("The resoursrDTO is not valid");
            model.addAttribute("newresource", resourceDTO);
            model.addAttribute("ownerLogin", ownerLogin);
            return "addResource";
        }
        /* get the logged registrar by login */
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User registrator = userService.getUserByLogin(auth.getName());
        logger.info("The logged register is" + registrator.getLastName() + " " + registrator.getFirstName());

        /* save resourceDTO on service layer and inquiry */
        resourceDTO = resourceService.addNewResource(resourceDTO, ownerLogin, registrator);
        logger.info("Resource was successfully saved");
        model.addAttribute("resource", resourceDTO);
        return "showResource";

    }

    /**
     * Show the information about resource by identifier
     * 
     * @param identifier
     * @param model
     * @return
     */
    @RequestMapping(value = "/get/{identifier}", method = RequestMethod.GET)
    public String getResourceByIdentifier(@PathVariable("identifier") String identifier, Model model) {

        ResourceDTO resourceDTO = resourceService.findByIdentifier(identifier);
        model.addAttribute("resource", resourceDTO);
        return "showResource";
    }

    /**
     * Load the list of all resource parameters of selected resource type
     * 
     * @param typeName
     * @param model
     * @return resourceValues.jsp
     */
    @RequestMapping(value = "/getParameters", method = RequestMethod.POST)
    public String add(@RequestParam("resourceTypeName") String typeName, Model model) {

        /* find the resource type in database by type name */
        ResourceType resType = resourceTypeService.findByName(typeName);

        /* find the resource parameters on resType */
        List<DiscreteParameter> discreteParameters = discreteParameterService.findAllByResourceType(resType);
        List<LinearParameter> linearParameters = linearParameterService.findAllByResourceType(resType);

        model.addAttribute("discreteParameters", discreteParameters);
        model.addAttribute("linearParameters", linearParameters);
        return "resourceValues";
    }

    /**
     * Depending on chosen resource type store all parameters and send them to
     * view at the page Resource search by parameters
     * 
     * @param i
     *            - Resource type, received from view
     * @param model
     * @return
     */
    @RequestMapping(value = "/getResourcesByTypeId", method = RequestMethod.POST)
    public String showAllResourcesByTypeId(@RequestParam("resourceTypeId") Integer i, Model model) {
        ResourceType type = resourceTypeService.findById(i);

        List<DiscreteParameter> discreteParameters = discreteParameterService.findAllByResourceType(type);
        List<LinearParameter> linearParameters = linearParameterService.findAllByResourceType(type);

        model.addAttribute("discreteParameters", discreteParameters);
        model.addAttribute("linearParameters", linearParameters);
        return "parameters";
    }

    /**
     * Depending on received parameters create List of resourceDTO and send them
     * to view
     * 
     * @param json - search parameters in JSON format
     * @param model
     * @return
     */

    @ResponseBody
    @PreAuthorize("hasRole('ROLE_REGISTRATOR') or hasRole('ROLE_USER')")
    @RequestMapping(value = "/resourceSearch", method = RequestMethod.POST)
    public String resourceSearch(@RequestBody ResourceSearchJson json, Model model) {
        SearchResultJson result = new SearchResultJson();
        ParameterSearchResultDTO searchResult = resourceService.getAllByParameters(json);
        long countResults = searchResult.getCount();
        List<Resource> resources = searchResult.getResources();
        List<PolygonJson> polygons = new ArrayList<>();

        int countPolygons = 0;
        for (Resource resource : resources) {
            polygons.addAll(resourceService.createPolygonJSON(resource, countPolygons));
            countPolygons = polygons.size();
        }

        result.setPolygons(polygons);
        result.setCountPolygons(countResults);

        Gson gson = new Gson();
        return gson.toJson(result);
    }

    @ResponseBody
    @RequestMapping(value = "/decs", method = RequestMethod.GET)
    public Map<String, Set<String>> getDescriptionProposition(@RequestParam("descTag") String descTag) {
        Map<String, Set<String>> suggestions = new HashMap<String, Set<String>>();
        // suggestions.put("query", new TreeSet<String>().add("unit"));
        suggestions.put("suggestions", resourceService.getDescriptionBySearchTag(descTag));
        return suggestions;
    }

    /**
     * Create the Set of resources identifiers depending on received parameters
     * and generate JSON response
     * 
     * @param minLat
     *            - minimum latitude
     * @param maxLat
     *            - maximum latitude
     * @param minLng
     *            - minimum longitude
     * @param maxLng
     *            - maximum longitude
     * @param resType
     *            - resource type id
     * @param model
     * @return JSON with information about polygons which can be located between
     *         received coordinates
     */

    @ResponseBody
    @RequestMapping(value = "/getResourcesByAreaLimits", method = RequestMethod.POST)
    public String showAllResourcesByAreaLimits(@RequestParam("minLat") Double minLat,
            @RequestParam("maxLat") Double maxLat, @RequestParam("minLng") Double minLng,
            @RequestParam("maxLng") Double maxLng, @RequestParam("resType") String resType,
            @RequestParam("page") Integer page, Model model) {
        SearchResultJson result = new SearchResultJson();
        Integer countResults = resourceService.countAllByAreaLimits(minLat, maxLat, minLng, maxLng);

        int countPolygons = 0;
        Set<Resource> resources = resourceService.getAllByAreaLimits(minLat, maxLat, minLng, maxLng, resType, page);
        List<PolygonJson> polygons = new ArrayList<>();

        for (Resource resource : resources) {
            polygons.addAll(resourceService.createPolygonJSON(resource, countPolygons));
            countPolygons = polygons.size();
        }

        result.setPolygons(polygons);
        result.setCountPolygons(countResults);

        Gson gson = new Gson();
        return gson.toJson(result);
    }

    /**
     * Search on map by point Create set of resource identifiers depending on
     * received point coordinates
     * 
     * @param lat
     *            - point latitude
     * @param lng
     *            - point longitude
     * @param model
     * @return JSON with information about polygons where received point can be
     *         located
     */
    @ResponseBody
    @RequestMapping(value = "/getResourcesByPoint", method = RequestMethod.POST)
    public String showAllResourcesByAreaLimits(@RequestParam("lat") Double lat, @RequestParam("lng") Double lng,
                                               @RequestParam("page") Integer page, Model model) {
        SearchResultJson result = new SearchResultJson();
        Integer countResults = resourceService.countAllByPoint(lat, lng);

        Set<Resource> resources = resourceService.getAllByPoint(lat, lng, page);
        List<PolygonJson> polygons = new ArrayList<>();

        int countPolygons = 0;
        for (Resource resource: resources) {
            polygons.addAll(resourceService.createPolygonJSON(resource, countPolygons));
            countPolygons = polygons.size();
        }

        result.setPolygons(polygons);
        result.setCountPolygons(countResults);

        Gson gson = new Gson();
        return gson.toJson(result);
    }

    /**
     * View for the Search on map page
     * 
     * @param model
     * @return
     */
    @PreAuthorize("hasRole('ROLE_REGISTRATOR') or hasRole('ROLE_USER')")
    @RequestMapping(value = "/searchOnMap", method = RequestMethod.GET)
    public String searchOnMap(Model model) {
        List<ResourceType> resourceTypes = resourceTypeService.findAll();
        model.addAttribute("resourceTypes", resourceTypes);
        return "searchOnMap";
    }

    /**
     * Find the list of owners with similar surname
     * 
     * @param ownerDesc
     *            corresponds to first letters of surname
     * @return userList list of users
     */
    @ResponseBody
    @RequestMapping(value = "/owners", method = RequestMethod.POST)
    public List<UserDTO> getOwnersSuggestions(@RequestParam("ownerDesc") String ownerDesc) {
        List<UserDTO> userList = userService.getUserBySearchTag(ownerDesc);
        return userList;
    }

    /**
     * Find the selected owner by login
     * 
     * @param ownerLogin
     * @return owner
     */
    @ResponseBody
    @RequestMapping(value = "/getOwnerInfo", method = RequestMethod.GET)
    public UserDTO getOwnerInfo(@RequestParam("ownerLogin") String ownerLogin) {
        return userService.getUserDto(ownerLogin);
    }

    @ResponseBody
    @RequestMapping(value = "/showAllResources", method = RequestMethod.POST)
    public String showAllResources(@RequestParam("resType") Integer resType) {
        ResourceType resourceType = resourceTypeService.findById(resType);
        List<Resource> resources = resourceService.findByType(resourceType);
        List<PolygonJson> polygons = new ArrayList<>();

        int countPolygons = 0;
        for (Resource resource : resources) {
            polygons.addAll(resourceService.createPolygonJSON(resource, countPolygons));
            countPolygons = polygons.size();
        }

        Gson gson = new Gson();
        return gson.toJson(polygons);
    }

    /**
     * Method delete resource with given identifier.
     * 
     * @param resourceIdentifier - identifier of the resource.
     * @return searchOnMap.jsp
     */
    @PreAuthorize("hasRole('ROLE_REGISTRATOR')")
    @RequestMapping(value = "/delete/{resourceIdentifier}")
    public String deleteResource(@PathVariable String resourceIdentifier) {
        logger.info("begin deleteResource, param resourceIdentifier = " + resourceIdentifier);
        resourceDeleteService.deleteResource(resourceIdentifier);
        logger.info("end deleteResource");
        return "redirect:/registrator/resource/searchOnMap";
    }

}