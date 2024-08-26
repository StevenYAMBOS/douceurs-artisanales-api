package com.stevenyambos.douceurs_artisanales.repository;

import com.stevenyambos.douceurs_artisanales.model.BakeryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BakeryRepository extends MongoRepository<BakeryModel, String> {
}
