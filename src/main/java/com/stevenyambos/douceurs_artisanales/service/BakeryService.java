package com.stevenyambos.douceurs_artisanales.service;

import com.stevenyambos.douceurs_artisanales.model.BakeryModel;
import com.stevenyambos.douceurs_artisanales.repository.BakeryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BakeryService {

    private final BakeryRepository bakeryRepository;
    public BakeryService(BakeryRepository bakeryRepository) {
        this.bakeryRepository = bakeryRepository;
    }

    // Créer une boulangerie
    public BakeryModel createBakery(BakeryModel bakery) {
        bakery.setId(UUID.randomUUID().toString());
        return bakeryRepository.save(bakery);
    }

    // Récupérer toutes les boulangeries
    public List<BakeryModel> getAllBakeries() {
        return bakeryRepository.findAll();
    }

    // Récupérer une boulangerie
    public BakeryModel getBakeryById(String id) {
        Optional<BakeryModel> bakery = bakeryRepository.findById(id);

        if (bakery.isPresent()) {
            BakeryModel bakeryModel = bakery.get();

            // Incrémente le total des vues/visites
            bakeryModel.setTotalViewsCount(bakeryModel.getTotalViewsCount() + 1);

            // Incrémente les vues de la journée
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            bakeryModel.incrementDailyViews(today);

            // Incrémenter les vues hebdomadaires
            Calendar calendar = Calendar.getInstance();
            String weekOfYear = calendar.get(Calendar.YEAR) + "-W" + calendar.get(Calendar.WEEK_OF_YEAR);
            bakeryModel.incrementWeeklyViews(weekOfYear);

            // Incrémenter les vues mensuelles
            String monthOfYear = new SimpleDateFormat("yyyy-MM").format(new Date());
            bakeryModel.incrementMonthlyViews(monthOfYear);

            return bakeryRepository.save(bakeryModel);
        } else {
            throw  new RuntimeException("La boulangerie n'existe pas.");
        }
    }

    // Récupérer les boulangeries par code postal
    public List<BakeryModel> getBakeriesByZipCode(Integer zipCode) {
        return bakeryRepository.findByZipCode(zipCode);
    }

    // Nombre de boulangeries par code postal
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

    // Réinitialiser les vues de la journée à minuit tous les jours
    @Scheduled(cron = "0 0 0 * * ?") // Exécution à minuit tous les jours
    public void resetDailyViews() {
        Iterable<BakeryModel> bakeries = bakeryRepository.findAll();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        for (BakeryModel bakery : bakeries) {
            bakery.getDailyViews().put(today, 0); // Réinitialise les vues de la journée
            bakeryRepository.save(bakery);
        }
    }

    // Réinitialiser les vues hebdomadaires chaque début de semaine
    @Scheduled(cron = "0 0 0 * * MON")
    public void resetWeeklyViews() {
        Iterable<BakeryModel> bakeries = bakeryRepository.findAll();
        Calendar calendar = Calendar.getInstance();
        String weekOfYear = calendar.get(Calendar.YEAR) + "-W" + calendar.get(Calendar.WEEK_OF_YEAR);

        for (BakeryModel bakery : bakeries) {
            bakery.getWeeklyViews().put(weekOfYear, 0);
            bakeryRepository.save(bakery);
        }
    }

    // Réinitialiser les vues mensuelles chaque début de mois
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetMonthlyViews() {
        Iterable<BakeryModel> bakeries = bakeryRepository.findAll();
        String monthOfYear = new SimpleDateFormat("yyyy-MM").format(new Date());

        for (BakeryModel bakery : bakeries) {
            bakery.getMonthlyViews().put(monthOfYear, 0);
            bakeryRepository.save(bakery);
        }
    }
}
