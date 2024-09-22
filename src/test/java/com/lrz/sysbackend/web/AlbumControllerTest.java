/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.web;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.lrz.sysbackend.DTOs.AlbumDTO;
import com.lrz.sysbackend.models.Album;
import com.lrz.sysbackend.service.AlbumService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.List;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 *
 * @author lara
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumService albumService;

    @Test
    public void testBuyAlbum_Success() throws Exception {
        AlbumDTO albumDTO = new AlbumDTO(UUID.randomUUID(), "Album Title", "spotify123", "Artist Name", "image_url", "spotify_url", new BigDecimal("15.99"));

        Mockito.when(albumService.sale(any(Album.class))).thenReturn(albumDTO);

        String jsonRequest = "{\"name\":\"Album Title\", \"idSpotify\":\"spotify123\", \"artistName\":\"Artist Name\", \"imageUrl\":\"image_url\", \"spotifyUrl\":\"spotify_url\", \"value\":15.99}";

        mockMvc.perform(post("/albums/sale")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + albumDTO.getId().toString() + "\","
                        + "\"name\":\"Album Title\","
                        + "\"idSpotify\":\"spotify123\","
                        + "\"artistName\":\"Artist Name\","
                        + "\"imageUrl\":\"image_url\","
                        + "\"spotifyUrl\":\"spotify_url\","
                        + "\"value\":15.99}"));
    }

    @Test
    public void testGetMyCollection_Success() throws Exception {
        UUID userId = UUID.randomUUID();

        AlbumDTO album1 = new AlbumDTO(UUID.randomUUID(), "Album 1", "spotify123_1", "Artist 1", "image_url_1", "spotify_url_1", new BigDecimal("10.00"));
        AlbumDTO album2 = new AlbumDTO(UUID.randomUUID(), "Album 2", "spotify123_2", "Artist 2", "image_url_2", "spotify_url_2", new BigDecimal("12.50"));

        List<AlbumDTO> albumCollection = List.of(album1, album2);

        Mockito.when(albumService.getMyCollection(any(UUID.class))).thenReturn(albumCollection);

        mockMvc.perform(get("/albums/my-collection/" + userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("["
                        + "{\"id\":\"" + album1.getId().toString() + "\","
                        + "\"name\":\"Album 1\","
                        + "\"idSpotify\":\"spotify123_1\","
                        + "\"artistName\":\"Artist 1\","
                        + "\"imageUrl\":\"image_url_1\","
                        + "\"spotifyUrl\":\"spotify_url_1\","
                        + "\"value\":10.00},"
                        + "{\"id\":\"" + album2.getId().toString() + "\","
                        + "\"name\":\"Album 2\","
                        + "\"idSpotify\":\"spotify123_2\","
                        + "\"artistName\":\"Artist 2\","
                        + "\"imageUrl\":\"image_url_2\","
                        + "\"spotifyUrl\":\"spotify_url_2\","
                        + "\"value\":12.50}"
                        + "]"));
    }

    @Test
    public void testDeleteAlbum_Success() throws Exception {
        UUID albumId = UUID.randomUUID();

        Mockito.doNothing().when(albumService).deleteAlbum(any(UUID.class));

        mockMvc.perform(delete("/albums/remove/" + albumId))
                .andExpect(status().isOk())
                .andExpect(content().string("Album removed"));
    }


}
