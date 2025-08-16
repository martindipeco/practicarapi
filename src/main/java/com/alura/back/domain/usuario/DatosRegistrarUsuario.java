package com.alura.back.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record DatosRegistrarUsuario(
        String login,
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$",
                message = "La clave debe contener al menos 8 caracteres, incluyendo 1 numero y 1 mayúscula"
        )
        String clave, @Email String email, Rol rol) {
}
