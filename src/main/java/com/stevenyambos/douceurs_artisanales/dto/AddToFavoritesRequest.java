package com.stevenyambos.douceurs_artisanales.dto;

import lombok.Data;

@Data
public class AddToFavoritesRequest {
    private String bakeryId;
    private String userEmail;
}
