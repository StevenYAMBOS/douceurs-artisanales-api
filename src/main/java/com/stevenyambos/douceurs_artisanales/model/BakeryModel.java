package com.stevenyambos.douceurs_artisanales.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@Document(collection = "bakery")
public class BakeryModel {
    @Id
    private UUID id;
    private String name;
    private String address;
    private String cover;
    private String logo;
    private String description;
    private Integer zipCode;
    private Integer rating;
//    private ArrayList comments;
//    private ArrayList images;
    private Boolean images;
    private Date createdAt;
    private Date updatedAt;
}
