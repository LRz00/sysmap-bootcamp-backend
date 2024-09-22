/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import com.lrz.sysbackend.DTOs.AlbumDTO;
import com.lrz.sysbackend.exceptions.UserNotFoundException;
import com.lrz.sysbackend.exceptions.RepeatedAlbumPurchaseException;
import com.lrz.sysbackend.models.Album;
import com.lrz.sysbackend.models.Transaction;
import com.lrz.sysbackend.models.User;
import com.lrz.sysbackend.repository.AlbumRepository;
import com.lrz.sysbackend.repository.TransactionRepository;
import com.lrz.sysbackend.repository.UserRepository;

/**
 *
 * @author lara
 */
@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {

    @Mock
    private TransactionRepository transactionRepo;

    @Mock
    private AlbumRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private AlbumService albumService;

    @Test
    public void testGetMyCollection_Success() {
 
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        List<Album> albums = Collections.singletonList(new Album());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.findByUserIdAndDeletedTimeIsNull(userId)).thenReturn(albums);
        List<AlbumDTO> result = albumService.getMyCollection(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository).findById(userId);
        verify(repository).findByUserIdAndDeletedTimeIsNull(userId);
    }

    @Test
    public void testGetMyCollection_UserNotFoundException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            albumService.getMyCollection(userId);
        });

        assertEquals("No user found of matching id", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(repository, never()).findByUserIdAndDeletedTimeIsNull(userId);
    }
    
    @Test
    public void testSale_Success() {
        UUID userId = UUID.randomUUID();
        String spotifyId = "spotify123";
        BigDecimal albumValue = BigDecimal.valueOf(10);
        Album album = new Album();
        album.setIdSpotify(spotifyId);
        album.setValue(albumValue);

        User user = new User();
        user.setId(userId);

        when(userService.getUserByContext()).thenReturn(user);
        when(repository.findByIdSpotifyAndUserId(spotifyId, userId)).thenReturn(Optional.empty());
        when(walletService.getPointsToAdd(any(LocalDateTime.class))).thenReturn(10);
        when(repository.save(album)).thenReturn(album);
        when(transactionRepo.save(any(Transaction.class))).thenReturn(null);

        AlbumDTO result = albumService.sale(album);

        assertNotNull(result);
        verify(userService).getUserByContext();
        verify(repository).findByIdSpotifyAndUserId(spotifyId, userId);
        verify(transactionRepo).save(any(Transaction.class));
        verify(repository).save(album);
    }
    
    @Test
    public void testSale_AlbumAlreadyOwned() {
        User user = new User();
        user.setId(UUID.randomUUID());
        Album album = new Album();
        album.setIdSpotify("spotify123");
        album.setValue(BigDecimal.TEN);

        when(userService.getUserByContext()).thenReturn(user);
        when(repository.findByIdSpotifyAndUserId(album.getIdSpotify(), user.getId()))
                .thenReturn(Optional.of(album));

        Exception exception = assertThrows(RepeatedAlbumPurchaseException.class, () -> {
            albumService.sale(album);
        });

        assertEquals("You already own this album", exception.getMessage());
        verify(userService).getUserByContext();
        verify(repository).findByIdSpotifyAndUserId(album.getIdSpotify(), user.getId());
        verify(walletService, never()).debit(any());
        verify(repository, never()).save(any(Album.class));
        verify(transactionRepo, never()).save(any(Transaction.class));
    }
    
    @Test
    public void testDeleteAlbum_Success() {
        UUID albumId = UUID.randomUUID();
        Album album = new Album();
        album.setId(albumId);

        when(repository.findById(albumId)).thenReturn(Optional.of(album));
        when(repository.save(album)).thenReturn(album);

        albumService.deleteAlbum(albumId);

        verify(repository).findById(albumId);
        verify(repository).save(album);
        assertNotNull(album.getDeletedTime());
    }

}
