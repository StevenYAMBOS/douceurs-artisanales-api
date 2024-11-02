package com.stevenyambos.douceurs_artisanales.controller;

import com.stevenyambos.douceurs_artisanales.dto.AddToFavoritesRequest;
import com.stevenyambos.douceurs_artisanales.model.BakeryModel;
import com.stevenyambos.douceurs_artisanales.model.UserModel;
import com.stevenyambos.douceurs_artisanales.service.BakeryService;
import com.stevenyambos.douceurs_artisanales.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/bakery")
public class BakeryController {

    private final BakeryService bakeryService;
    private final UserService userService;

    public BakeryController(BakeryService bakeryService, UserService userService) {
        this.bakeryService = bakeryService;
        this.userService = userService;
    }

    // Récupérer toutes les boulangeries
    @GetMapping("/bakeries")
    public ResponseEntity<List<BakeryModel>> getAllBakeries() {
        List<BakeryModel> bakeries = bakeryService.getAllBakeries();
        return new ResponseEntity<>(bakeries, HttpStatus.OK);
    }

    // Récupérer les boulangeries par code postal
    @GetMapping("/zip-code")
    public ResponseEntity<List<BakeryModel>> getBakeriesByZipCode(@RequestParam Integer zipCode) {
        List<BakeryModel> bakeries = bakeryService.getBakeriesByZipCode(zipCode);
        return new ResponseEntity<>(bakeries, HttpStatus.OK);
    }

    // Nombre de boumangeries par code postal
    @GetMapping("/zip-code-count")
    public ResponseEntity<Long> getBakeriesCountByZipCode(@RequestParam Integer zipCode) {
        // Utilise la méthode du service pour obtenir le nombre de boulangeries
        Long bakeryCount = bakeryService.getBakeryCountByZipCode(zipCode);
        return new ResponseEntity<>(bakeryCount, HttpStatus.OK);
    }

    // Récupérer les boulangeries publiées
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/published")
    public ResponseEntity<List<BakeryModel>> findByPublished() {
        List<BakeryModel> publishedBakeries = bakeryService.findByPublished();
        return new ResponseEntity<>(publishedBakeries, HttpStatus.OK);
    }

    // Récupérer une boulangerie
    @GetMapping("/{id}")
    public ResponseEntity<?> getBakeryById(@PathVariable String id) {
        try {
            BakeryModel bakery = bakeryService.getBakeryById(id);
            return ResponseEntity.ok(bakery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Créer une boulangerie
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")  // Autoriser les OWNERS et ADMINs
    @PostMapping("/bakery")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBakery(@RequestBody BakeryModel bakery) {
        try {
            // Récupérer l'utilisateur authentifié
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserModel user = userService.getUserByEmail(userDetails.getUsername());

            List<UserModel> Owners = new ArrayList<>();
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Calendar calendar = Calendar.getInstance();
            String weekOfYear = calendar.get(Calendar.YEAR) + "-W" + calendar.get(Calendar.WEEK_OF_YEAR);
            String monthOfYear = new SimpleDateFormat("yyyy-MM").format(new Date());

            Owners.add(user);
            bakery.setOwners(Owners);
            bakery.setRating(null);
            bakery.setComments(new String[0]);
            bakery.setImages(new String[0]);
            bakery.setPublished(false);
            bakery.setTotalViewsCount(0);
            bakery.getDailyViews().put(today, 0);
            bakery.getWeeklyViews().put(weekOfYear, 0);
            bakery.getMonthlyViews().put(monthOfYear, 0);
            bakery.setCreatedAt(new Date());
            bakery.setUpdatedAt(new Date());
            BakeryModel createdBakery = bakeryService.createBakery(bakery);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBakery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Modifier une boulangerie
    @PutMapping("/{id}")
    public ResponseEntity<BakeryModel> updateBakery(@PathVariable("id") String id, @RequestBody BakeryModel bakery) {
        BakeryModel updatedBakery = bakeryService.updateBakery(id, bakery);
        return new ResponseEntity<>(updatedBakery, HttpStatus.OK);
    }

    // Supprimer une boulangerie
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBakery(@PathVariable("id") String id) {
        bakeryService.deleteBakery(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Supprimer toutes les boulangeries
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/bakeries")
    public ResponseEntity<HttpStatus> deleteAllBakeries() {
        bakeryService.deleteAllBakeries();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
