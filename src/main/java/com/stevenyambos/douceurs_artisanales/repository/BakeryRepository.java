package com.stevenyambos.douceurs_artisanales.repository;

import com.stevenyambos.douceurs_artisanales.model.BakeryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BakeryRepository extends MongoRepository<BakeryModel, String> {

    // Récupérer les boulangeries par arrondissement
    List<BakeryModel> findByZipCode(Integer zipCode);

    // Compte les boulangeries par zipCode
    Long countByZipCode(Integer zipCode);

}
