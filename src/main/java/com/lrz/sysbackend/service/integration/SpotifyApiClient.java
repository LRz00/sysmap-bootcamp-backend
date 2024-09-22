/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.service.integration;

import com.lrz.sysbackend.DTOs.SpotifyAlbumDTO;
import com.neovisionaries.i18n.CountryCode;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author lara
 */
@Service
@Slf4j
public class SpotifyApiClient {

    private final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("9e2fa158c770496dab41990084ef170c")
            .setClientSecret("d9f0c2187e5844cbbe5c7679f681d2d3").build();

    public List<SpotifyAlbumDTO> getAlbums(String search) {
        try {
            spotifyApi.setAccessToken(getAcessToken());
            var albums = spotifyApi.searchAlbums(search).market(CountryCode.BR)
                    .limit(10).build().execute().getItems();
            List<SpotifyAlbumDTO> albumDto = new java.util.ArrayList<>(List.of());

            for (var album : albums) {
                albumDto.add(SpotifyAlbumDTO.builder()
                        .albumType(album.getAlbumType())
                        .artists(album.getArtists())
                        .externalUrls(album.getExternalUrls())
                        .id(album.getId())
                        .images(album.getImages())
                        .name(album.getName())
                        .releaseDate(album.getReleaseDate())
                        .type(album.getType())
                        .value(BigDecimal.valueOf(
                                Math.random() * ((100.00 - 17.00) + 1) + 17.00).setScale(2, RoundingMode.UP))
                        .build());
            }
            log.info("Returning Spotify albums");
            return albumDto;

        } catch (IOException | ParseException | SpotifyWebApiException e) {
            log.error("Error searching for Spotify albums");
            throw new RuntimeException(e);
        }
    }

    public String getAcessToken() throws IOException, SpotifyWebApiException, ParseException {
        ClientCredentialsRequest clientCredentialrequest = this.spotifyApi.clientCredentials().build();
        return clientCredentialrequest.execute().getAccessToken();
    }
}
