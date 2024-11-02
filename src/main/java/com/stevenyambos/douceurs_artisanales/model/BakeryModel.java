package com.stevenyambos.douceurs_artisanales.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Document(collection = "bakery")
public class BakeryModel {

    @Id
    private String id;

    @NotBlank(message = "Le nom de la boulangerie est obligatoire.")
    private String name;

    @NotBlank(message = "L'adresse de la boulangerie est obligatoire.")
    private String address;

    @NotBlank(message = "La cover de la boulangerie est obligatoire.")
    private String cover;

    @NotBlank(message = "Le logo de l'entreprise est obligatoire.")
    private String logo;

    @NotBlank(message = "La description de la boulangerie est obligatoire.")
    private String description;

    private String instagram;

    @NotBlank(message = "L'arrondissement est obligatoire.")
    private Integer zipCode;

    // Nombre de vues/visites total
    private Integer totalViewsCount = 0;
    private Map<String,Integer> dailyViews = new HashMap<>();
    private Map<String,Integer> weeklyViews = new HashMap<>();
    private Map<String,Integer> monthlyViews = new HashMap<>();

    private List<UserModel> Owners;
//    private Integer uniqueViewsCount;
//    private List<UserModel> Likes;
    private Integer rating;
    private Float averageRating;
    private String[] Comments;
    private String[] images;
    private Boolean multipleLocations;
    private Boolean published;
    private Date createdAt;
    private Date updatedAt;

    public void incrementDailyViews(String date) {
        dailyViews.put(date, dailyViews.getOrDefault(date, 0) + 1);
    }

    public void incrementWeeklyViews(String week) {
        weeklyViews.put(week, weeklyViews.getOrDefault(week, 0) + 1);
    }

    public void incrementMonthlyViews(String month) {
        monthlyViews.put(month, monthlyViews.getOrDefault(month, 0) + 1);
    }
}
