package com.avinashkonduri.eCommerce.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import com.avinashkonduri.eCommerce.entity.Product;
import com.avinashkonduri.eCommerce.entity.ProductCategory;



@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer{

	private EntityManager entityManager;
	
	@Autowired
	public MyDataRestConfig(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}
	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};
		
		// disable Http methods for Product: PUT, POST and DELETE
		config.getExposureConfiguration() // ExposureConfiguration
				.forDomainType(Product.class) // ExposureConfigurer 
				.withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
				.withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
		
		// disable Http methods for ProductCategory: PUT, POST and DELETE
				config.getExposureConfiguration() // ExposureConfiguration
						.forDomainType(ProductCategory.class) // ExposureConfigurer 
						.withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
						.withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
		
		// call an internal helper method
				exposeIds(config);
	}
	private void exposeIds(RepositoryRestConfiguration config) {
		// expose entity ids
		//
		
		// -get a list of all entity classes from entity manager
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		
		// - create as array of entity type
		List<Class> entityClasses = new ArrayList();
		
		// - get the entity types for the entities
		for (EntityType tempEntityType : entities ) {
			entityClasses.add(tempEntityType.getJavaType());
		}
		
		// - expose the entity ids for the array of entity/ domain types
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		config.exposeIdsFor(domainTypes);
	}
}
