/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.web;

import com.lrz.sysbackend.DTOs.SpotifyAlbumDTO;
import com.lrz.sysbackend.DTOs.AlbumDTO;
import com.lrz.sysbackend.models.Album;
import com.lrz.sysbackend.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 *
 * @author lara
 */
@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
@Tag(name = "Album", description = "Endpoints for Managing albuns")
public class AlbumController {
    
    private final AlbumService albumService;
    
    @GetMapping("/all")
           @Operation(
            summary = "Returns all albuns from Spotify",
            description = "Returns all Spotify albuns that match a search String",
            tags = {"Album"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Albuns found.",
                        content = @Content(schema = @Schema(implementation = SpotifyAlbumDTO.class))
                ),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<List<SpotifyAlbumDTO>> getAllAbums(@RequestParam("search") String search){
        return ResponseEntity.ok(this.albumService.getAlbums(search));
    } 
    

        @PostMapping("/sale")
           @Operation(
            summary = "Allows an user to buy a album",
            description = "Receives the album details and saves it as belonging to the authenticated user",
            tags = {"Album"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Album bought.",
                        content = @Content(schema = @Schema(implementation = AlbumDTO.class))
                ),
                @ApiResponse(responseCode = "409", description = "User already owns this album", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<AlbumDTO> sale(@RequestBody Album album){
        return ResponseEntity.ok(this.albumService.sale(album));
    }
    
    @GetMapping("/my-collection/{userId}")
           @Operation(
            summary = "Returns a user album collection",
            description = "Receives an user UUID and returns all the albuns in their collection",
            tags = {"Album"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Collection found.",
                        content = @Content(schema = @Schema(implementation = AlbumDTO.class))
                ),
                @ApiResponse(responseCode = "404", description = "Albuns or User by UUID not found", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<List<AlbumDTO>> getMyCollection(@PathVariable UUID userId){
        return ResponseEntity.ok(this.albumService.getMyCollection(userId));
    }

    @DeleteMapping("/remove/{id}")
           @Operation(
            summary = "Deletes an album.",
            description = "Receives an album's UUID and removes it from a user's collection",
            tags = {"Album"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "album removed.",
                        content = @Content(schema = @Schema(implementation = String.class))
                ),
                @ApiResponse(responseCode = "404", description = "Album not found", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<String> deleteAlbum(@PathVariable UUID id){
        this.albumService.deleteAlbum(id);
        return ResponseEntity.ok("Album removed");
    }
}
