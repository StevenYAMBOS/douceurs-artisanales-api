package com.stevenyambos.douceurs_artisanales.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

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
    private Integer rating;
    private String[] comments;
    private String[] images;
    private Boolean multipleLocations;
    private Boolean published;
    private List<UserModel> owner;
    private Date createdAt;
    private Date updatedAt;

}
