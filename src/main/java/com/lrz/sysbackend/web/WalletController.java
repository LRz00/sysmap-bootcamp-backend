/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.web;

import com.lrz.sysbackend.DTOs.CreditDTO;
import com.lrz.sysbackend.DTOs.WalletResponseDTO;
import com.lrz.sysbackend.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lara
 */
@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Wallets", description = "Endpoints for Managing Wallets")
public class WalletController {

    private final WalletService service;

    @PostMapping("/credit")
    @Operation(
            summary = "Adds credit",
            description = "Recives a a positive number(BigDecimal) and adds it to the Authenticated User's wallet",
            tags = {"Wallets"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ok. Credit added",
                        content = @Content(schema = @Schema(implementation = String.class))
                ),
                @ApiResponse(responseCode = "400", description = "Bad Request. Invalid amount", content = @Content),
                @ApiResponse(responseCode = "404", description = "Wallet not found", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<String> credit(@RequestBody CreditDTO amount) {
        this.service.addCredit(amount);
        return ResponseEntity.ok("Sucessfuly added " + amount.getAmount().toString() + " amount");
    }

    @GetMapping("/{walletId}")
    @Operation(
            summary = "Returns a Wallet",
            description = "Receives an UUID and returns the Wallet to which it belongs to",
            tags = {"Wallets"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Wallet found.",
                        content = @Content(schema = @Schema(implementation = String.class))
                ),
                @ApiResponse(responseCode = "404", description = "Wallet by id not found", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<WalletResponseDTO> getWallet(@PathVariable UUID walletId) {
        return ResponseEntity.ok(this.service.getWallet(walletId));
    }

}
