package com.waes.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.waes.data.entity.JsonTracking;

/**
 * Repository Interface JsonTrackingRepository extending the great features from
 * the CrudRepository providing sophisticated CRUD functionality for the entity
 * JsonTracking
 * 
 * @author Alejandro Aguirre
 * @version 1.0
 * @since 2019/02/10
 */
@Repository
public interface JsonTrackingRepository extends CrudRepository<JsonTracking, Long> {
	/**
	 * This method should return a JsonTracking Object if found by jsonId and side
	 * 
	 * @param jsonId
	 * @param side
	 * @return JsonTracking
	 */
	JsonTracking findByJsonIdAndSide(String jsonId, String side);
}
