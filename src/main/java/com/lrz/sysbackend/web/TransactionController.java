/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.web;

import com.lrz.sysbackend.DTOs.TransactionDTO;
import com.lrz.sysbackend.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lara
 */
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Endpoints for Managing Transactions")
public class TransactionController {

    private final TransactionService service;

    @GetMapping("/metrics")
    @Operation(
            summary = "Gets all metrics",
            description = "Get all transaction metrics",
            tags = {"Transactions"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Metrics Found",
                        content = @Content(schema = @Schema(implementation = TransactionDTO.class))
                ),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<List<TransactionDTO>> getMetrics() {
        return ResponseEntity.ok(this.service.getMetrics());
    }

    @GetMapping("/user/metrics")
    @Operation(
            summary = "Gets all one user's metrics",
            description = "Get all metrics of transactions made by authenticated user",
            tags = {"Transactions"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Metrics Found",
                        content = @Content(schema = @Schema(implementation = TransactionDTO.class))
                ),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<List<TransactionDTO>> getUserMetrics() {
        return ResponseEntity.ok(this.service.getUserMetrics());
    }

    @GetMapping("/metrics/high")
        @Operation(
            summary = "Gets all metrics of high value transactions",
            description = "Get all metrics of transactions above 50 value",
            tags = {"Transactions"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Metrics Found",
                        content = @Content(schema = @Schema(implementation = TransactionDTO.class))
                ),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<List<TransactionDTO>> getMetricsAboveFifityValue() {
        return ResponseEntity.ok(this.service.getMetricsAboveFiftyValue());
    }

    @GetMapping("/metrics/low")
        @Operation(
            summary = "Gets all low metrics",
            description = "Get all metrics of transactions bellow 50 value",
            tags = {"Transactions"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Metrics Found",
                        content = @Content(schema = @Schema(implementation = TransactionDTO.class))
                ),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<List<TransactionDTO>> getMetricsBellowFifityValue() {
        return ResponseEntity.ok(this.service.getMetricsBellowFiftyValue());
    }

    @GetMapping("/metrics/sunday-hits")
        @Operation(
            summary = "Gets all sunday metrics",
            description = "Get all metrics of transactions made on a sunday",
            tags = {"Transactions"},
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Metrics Found",
                        content = @Content(schema = @Schema(implementation = TransactionDTO.class))
                ),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<List<TransactionDTO>> getSundayMetrics() {
        return ResponseEntity.ok(this.service.getAllSundayMetrics());
    }
}
