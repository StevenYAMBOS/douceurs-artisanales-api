package com.stevenyambos.douceurs_artisanales.controller;

import com.stevenyambos.douceurs_artisanales.model.BakeryModel;
import com.stevenyambos.douceurs_artisanales.service.BakeryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/bakery")
public class BakeryController {

    private final BakeryService bakeryService;

    public BakeryController(BakeryService bakeryService) {
        this.bakeryService = bakeryService;
    }


    @GetMapping("/get-all")
    public ResponseEntity<List<BakeryModel>> getAllBakeries() {
        List<BakeryModel> bakeries = bakeryService.getAllBakeries();
        return new ResponseEntity<>(bakeries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BakeryModel> getBakeryById(@PathVariable String id) {
        BakeryModel bakery = bakeryService.getBakeryById(id);
        return new ResponseEntity<>(bakery, HttpStatus.OK);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BakeryModel> createBakery(@RequestBody BakeryModel bakery) {
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteBakery(@PathVariable("id") String id) {
        bakeryService.deleteBakery(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<HttpStatus> deleteAllBakeries() {
        bakeryService.deleteAllBakeries();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/published")
    public ResponseEntity<List<BakeryModel>> findByPublished() {
        List<BakeryModel> publishedBakeries = bakeryService.findByPublished();
        return new ResponseEntity<>(publishedBakeries, HttpStatus.OK);
    }


}
