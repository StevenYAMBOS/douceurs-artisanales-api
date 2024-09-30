package com.stevenyambos.douceurs_artisanales.controller;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @GetMapping("/get-all")
    public ResponseEntity<List<BakeryModel>> getAllBakeries() {
        List<BakeryModel> bakeries = bakeryService.getAllBakeries();
        return new ResponseEntity<>(bakeries, HttpStatus.OK);
    }

    @GetMapping("/get-bakeries-by-zip-code")
    public ResponseEntity<List<BakeryModel>> getBakeriesByZipCode(@RequestParam Integer zipCode) {
        List<BakeryModel> bakeries = bakeryService.getBakeriesByZipCode(zipCode);
        return new ResponseEntity<>(bakeries, HttpStatus.OK);
    }

    @GetMapping("/get-bakeries-count-by-zip-code")
    public ResponseEntity<Long> getBakeriesCountByZipCode(@RequestParam Integer zipCode) {
        // Utilise la méthode du service pour obtenir le nombre de boulangeries
        Long bakeryCount = bakeryService.getBakeryCountByZipCode(zipCode);
        return new ResponseEntity<>(bakeryCount, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BakeryModel> getBakeryById(@PathVariable String id) {
        BakeryModel bakery = bakeryService.getBakeryById(id);
        return new ResponseEntity<>(bakery, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")  // Autoriser les OWNERS et ADMINs
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BakeryModel> createBakery(@RequestBody BakeryModel bakery) {
        // Récupérer l'utilisateur authentifié
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel user = userService.getUserByEmail(userDetails.getUsername());

        List<UserModel> owner = new ArrayList<>();
        owner.add(user);
        bakery.setOwner(owner);
        bakery.setRating(null);
        bakery.setComments(new String[0]);
        bakery.setImages(new String[0]);
        bakery.setPublished(false);
        bakery.setCreatedAt(new Date());
        bakery.setUpdatedAt(new Date());
        BakeryModel createdBakery = bakeryService.createBakery(bakery);
        return new ResponseEntity<>(createdBakery, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BakeryModel> updateBakery(@PathVariable("id") String id, @RequestBody BakeryModel bakery) {
        BakeryModel updatedBakery = bakeryService.updateBakery(id, bakery);
        return new ResponseEntity<>(updatedBakery, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBakery(@PathVariable("id") String id) {
        bakeryService.deleteBakery(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-all")
    public ResponseEntity<HttpStatus> deleteAllBakeries() {
        bakeryService.deleteAllBakeries();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/published")
    public ResponseEntity<List<BakeryModel>> findByPublished() {
        List<BakeryModel> publishedBakeries = bakeryService.findByPublished();
        return new ResponseEntity<>(publishedBakeries, HttpStatus.OK);
    }


}
