package com.eventbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank
    @Size( max = 100)
    private String name;

    @NotBlank
    @Email()
    @Size(min=8, max = 100)
    private String email;

    @NotBlank
    @Size(min=8)
    private String password;
}
