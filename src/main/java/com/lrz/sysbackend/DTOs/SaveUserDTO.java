/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lrz.sysbackend.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 *
 * @author lara
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaveUserDTO {
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private String password;
}
