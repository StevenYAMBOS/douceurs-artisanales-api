package com.stevenyambos.douceurs_artisanales.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@Document(collection = "bakery")
public class BakeryModel {
    @Id
    private String id;
    private String name;
    private String address;
    private String cover;
    private String logo;
    private String description;
    private Integer zipCode;
    private Integer rating;
    private String[] comments;
    private String[] images;
    private Boolean multipleLocations;
    private Boolean published;
    private Date createdAt;
    private Date updatedAt;
}
