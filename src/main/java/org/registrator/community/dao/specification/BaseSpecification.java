package org.registrator.community.dao.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.registrator.community.dto.search.SearchColumn;
import org.registrator.community.enumeration.RoleType;
import org.registrator.community.enumeration.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;


public class BaseSpecification<T> implements Specification<T> {

  private final String searchField;

  private final String searchValue;

  private final String searchType;
  
  private static final Logger logger = LoggerFactory.getLogger(BaseSpecification.class);

  public BaseSpecification(String searchField, String searchValue, String searchType) {
    this.searchField = searchField;
    this.searchValue = searchValue;
    this.searchType = searchType;
  }

  private SearchColumn criteria;

  public SearchColumn getCriteria() {
    return criteria;
  }

  public void setCriteria(SearchColumn criteria) {
    this.criteria = criteria;
  }

  @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (searchType.equalsIgnoreCase("greater")) {
          return greaterThanOrEqualToBuilder(root, builder);
        } 
        else if (searchType.equalsIgnoreCase("less")) {
          return lessThanOrEqualToBuilder(root, builder);
        } 
        else if (searchType.equalsIgnoreCase("like")) {
          return likeBuilder(root, builder);
        }
        else if (searchType.equalsIgnoreCase("statusType")){
          return statusTypeBuilder(root, builder);
        }
        else if (searchType.equalsIgnoreCase("roleType")){
            return roleTypeBuilder(root, builder);
          }
        
//        else if (criteria.getSearch().getCompareSign().equalsIgnoreCase("equal")){
//        	return equalBuilder(root, builder);
//        }
        else if (searchType.equalsIgnoreCase("equal")){
            return equalBuilder(root, builder);
        }
        return null;
    }

  // public Path<String> getPathFromRoot(Root<T> root){
  // if(criteria.getData().contains("_")){
  // String[]pathParameters = criteria.getData().split("_");
  // return root.join(pathParameters[0]).get(pathParameters[1]);
  // }
  // return root.get(criteria.getData());
  // }
  public Path<String> getPathFromRoot(Root<T> root) {
    if (searchField.contains("_")) {
      String[] pathParameters = searchField.split("_");
      return root.join(pathParameters[0]).get(pathParameters[1]);
    }
    return root.get(searchField);
  }

  // private Predicate equalBuilder(Root<T> root, CriteriaBuilder builder) {
  // return builder.equal(getPathFromRoot(root), criteria.getSearch().getValue());
  // }
  private Predicate equalBuilder(Root<T> root, CriteriaBuilder builder) {
    return builder.equal(getPathFromRoot(root), searchValue);
  }


  // private Predicate likeBuilder(Root<T> root, CriteriaBuilder builder) {
  // return builder.like(
  // root.<String>get(criteria.getName()), "%" + searchValue + "%");
  // }
  private Predicate likeBuilder(Root<T> root, CriteriaBuilder builder) {
    return builder.like(getPathFromRoot(root), "%" + searchValue + "%");
  }

  // private Predicate lessThanOrEqualToBuilder(Root<T> root,
  // CriteriaBuilder builder) {
  // return builder.lessThanOrEqualTo(
  // root.<String> get(criteria.getData()), searchValue);
  // }
  private Predicate lessThanOrEqualToBuilder(Root<T> root, CriteriaBuilder builder) {
    return builder.lessThanOrEqualTo(getPathFromRoot(root), searchValue);
  }
  
  private Predicate statusTypeBuilder(Root<T> root, CriteriaBuilder builder) {
      UserStatus status = UserStatus.ACTIVE;
      try{
          status = UserStatus.valueOf(searchValue);
      }catch(IllegalArgumentException e){
          logger.warn("Bad user status argument: "+searchValue);
      }
    return builder.equal(getPathFromRoot(root), status);
  }
  
  private Predicate roleTypeBuilder(Root<T> root, CriteriaBuilder builder) {
      RoleType roleType = RoleType.USER;
      try{
          roleType = RoleType.valueOf(searchValue);
      }catch(IllegalArgumentException e){
          logger.warn("Bad user role argument: "+searchValue);
      }
    return builder.equal(getPathFromRoot(root), roleType);
  }

  // private Predicate greaterThanOrEqualToBuilder(Root<T> root,
  // CriteriaBuilder builder) {
  // return builder.greaterThanOrEqualTo(
  // root.<String> get(criteria.getData()), searchValue);
  // }
  private Predicate greaterThanOrEqualToBuilder(Root<T> root, CriteriaBuilder builder) {
    return builder.greaterThanOrEqualTo(getPathFromRoot(root), searchValue);
  }
}
