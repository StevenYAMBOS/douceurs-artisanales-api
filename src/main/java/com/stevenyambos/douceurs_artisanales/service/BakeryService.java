package com.stevenyambos.douceurs_artisanales.service;

import com.stevenyambos.douceurs_artisanales.model.BakeryModel;
import com.stevenyambos.douceurs_artisanales.repository.BakeryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BakeryService {

    private final BakeryRepository bakeryRepository;

    public BakeryService(BakeryRepository bakeryRepository) {
        this.bakeryRepository = bakeryRepository;
    }

    // Créer une enseigne
    public BakeryModel createBakery(BakeryModel bakery) {
        bakery.setId(UUID.randomUUID().toString());
        return bakeryRepository.save(bakery);
    }

    // Récupérer toutes les enseignes
    public List<BakeryModel> getAllBakeries() {
        return bakeryRepository.findAll();
    }

    public BakeryModel getBakeryById(String id) {
        return bakeryRepository.findById(id).get();
    }

    public List<BakeryModel> getBakeriesByZipCode(Integer zipCode) {
        return bakeryRepository.findByZipCode(zipCode);
    }

    public Long getBakeryCountByZipCode(Integer zipCode) {
        // Appelle le repository pour compter les boulangeries avec le zipCode
        return bakeryRepository.countByZipCode(zipCode);
    }


    // Mettre à jour une enseigne
    public BakeryModel updateBakery(String id, BakeryModel bakery) {
        if (bakeryRepository.existsById(id)) {
            bakery.setId(id);
            bakery.setUpdatedAt(new Date());
            return bakeryRepository.save(bakery);
        } else {
            throw new IllegalArgumentException("Aucune boulangerie trouvée.");
        }
    }

    // Supprimer une enseigne par ID
    public void deleteBakery(String id) {
        bakeryRepository.deleteById(id);
    }

    // Supprimer toutes les enseignes
    public void deleteAllBakeries() {
        bakeryRepository.deleteAll();
    }

    // Trouver les enseignes publiées
    public List<BakeryModel> findByPublished() {
        return bakeryRepository.findAll().stream()
                .filter(BakeryModel::getPublished)
                .collect(Collectors.toList());
    }

}
