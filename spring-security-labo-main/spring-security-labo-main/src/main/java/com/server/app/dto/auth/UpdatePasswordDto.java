package com.server.app.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordDto {

    @NotBlank(message = "La contraseña actual no puede estar vacía")
    private String oldpassword;

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "La contraseña debe incluir mayúscula, minúscula, número y carácter especial"
    )
    private String newpassword;

    @NotBlank(message = "La confirmación de contraseña no puede estar vacía")
    private String confirmpassword;
}