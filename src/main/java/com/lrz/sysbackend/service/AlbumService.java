/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.service;

import com.lrz.sysbackend.DTOs.AlbumDTO;
import com.lrz.sysbackend.DTOs.SpotifyAlbumDTO;
import com.lrz.sysbackend.exceptions.AlbumNotFoundException;
import com.lrz.sysbackend.exceptions.MethodException;
import com.lrz.sysbackend.exceptions.UserNotFoundException;
import com.lrz.sysbackend.exceptions.InvalidCredentialsException;
import com.lrz.sysbackend.exceptions.RepeatedAlbumPurchaseException;
import com.lrz.sysbackend.models.Album;
import com.lrz.sysbackend.models.Transaction;
import com.lrz.sysbackend.models.User;
import com.lrz.sysbackend.repository.AlbumRepository;
import com.lrz.sysbackend.repository.TransactionRepository;
import com.lrz.sysbackend.repository.UserRepository;
import com.lrz.sysbackend.service.integration.SpotifyApiClient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author lara
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumService {
    private final TransactionRepository transactionRepo;
    private final SpotifyApiClient apiClient;
    private final AlbumRepository repository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final WalletService walletService;
    //had a conflict trying to utilize mapper i usually use so i'm doing it by hand
    public AlbumDTO toAlbumDTO(Album album){
        return AlbumDTO.builder().id(album.getId())
                .artistName(album.getArtistName())
                .name(album.getName()).idSpotify(album.getIdSpotify())
                .imageUrl(album.getImageUrl()).spotifyUrl(album.getSpotifyUrl())
                .value(album.getValue()).build();
                }
    
    public List<AlbumDTO> toDTOList(List<Album> albuns){
        return albuns.stream().map(this::toAlbumDTO).collect(Collectors.toList());
    }

    public List<SpotifyAlbumDTO> getAlbums(String search) {
        if (search.isBlank() || search.isEmpty()) {
            log.error("Search String for Spotify Client is empty");
            throw new InvalidCredentialsException("Search String cannot be empty");
        }
        return this.apiClient.getAlbums(search);
    }


        public List<AlbumDTO> getMyCollection(UUID id) {
            this.userRepository.findById(id).orElseThrow(()
                    -> new UserNotFoundException("No user found of matching id"));
            log.info("Getting user Album collection");
            List<Album> albuns = this.repository.findByUserIdAndDeletedTimeIsNull(id);

            if (albuns.isEmpty()) {
                throw new AlbumNotFoundException("User has no albuns! Consider buying some");
            }

            List<AlbumDTO> response = this.toDTOList(albuns);

            return response;
    }
    
        public AlbumDTO sale(Album album){
        
        User user = this.userService.getUserByContext();
        String id = album.getIdSpotify();
        
        Optional<Album> exists = this.repository.findByIdSpotifyAndUserId(id, user.getId());
        
        if(exists.isPresent()){
            throw new RepeatedAlbumPurchaseException("You already own this album");
        }
        
        album.setUser(user);
        this.walletService.debit(album.getValue());
        
        Transaction transaction = Transaction.builder().album(album)
                .createdAt(LocalDateTime.now())
                .pointsEarned(this.walletService.getPointsToAdd(LocalDateTime.now())).user(user)
                .value(album.getValue()).build();
        
        this.repository.save(album);
        this.transactionRepo.save(transaction);
        
        return this.toAlbumDTO(album);
        
    }


    

    
    public void deleteAlbum(UUID id){
        try {
        log.info("Searching for album");     
        Album album =  this.repository.findById(id).orElseThrow(() -> new AlbumNotFoundException("album not found"));
        
        log.info("Deleting album");
        album.setDeletedTime(LocalDateTime.now());
        
        this.repository.save(album);
        log.info("Album deleted");
        } catch (Exception e) {
            throw new MethodException("Error while deleting album");
        }
       
    }

    }
