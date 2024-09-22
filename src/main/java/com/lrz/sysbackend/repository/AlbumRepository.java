/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lrz.sysbackend.repository;

import com.lrz.sysbackend.models.Album;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 *
 * @author lara
 */

@Repository
public interface AlbumRepository extends JpaRepository<Album, UUID>{
    List<Album> findByUserIdAndDeletedTimeIsNull(UUID userId);
    
    Optional<Album> findByIdSpotifyAndUserId(String idSpotify, UUID userId);
}
