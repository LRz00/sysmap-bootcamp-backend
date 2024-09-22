/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.DTOs;

import java.math.BigDecimal;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import se.michaelthelin.spotify.enums.AlbumType;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.ExternalUrl;
import se.michaelthelin.spotify.model_objects.specification.Image;

/**
 *
 * @author lara
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpotifyAlbumDTO {

    private AlbumType albumType;
    
    private ArtistSimplified[] artists;
    
    private ExternalUrl externalUrls;
    
    private String id;
    
    private Image[] images;
    
    private String name;
    
    private String releaseDate;
    
    private ModelObjectType type;
    
    private BigDecimal value;
}
