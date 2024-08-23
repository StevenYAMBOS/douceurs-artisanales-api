package com.stevenyambos.douceurs_artisanales.service;

import com.stevenyambos.douceurs_artisanales.model.BakeryModel;
import com.stevenyambos.douceurs_artisanales.repository.BakeryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BakeryService {

    @Autowired
    private BakeryRepository bakeryRepository;

    // Créer une enseigne
/*    public Bakery createBakery(Bakery bakery) {

    }*/

    // Récupérer toutes les enseignes
    public List<BakeryModel> findAllBakeries() {
        return bakeryRepository.findAll();
    }

    public BakeryModel getBakeryById(UUID id) {
        return bakeryRepository.findById(id).get();
    }
}
