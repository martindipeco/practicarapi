package com.alura.back.controller;

import com.alura.back.domain.usuario.DatosRegistrarUsuario;
import com.alura.back.domain.usuario.Rol;
import com.alura.back.domain.usuario.Usuario;
import com.alura.back.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity registrarUsuario(@RequestBody @Valid DatosRegistrarUsuario datosRegistrarUsuario){
        if (usuarioRepository.findByLogin(datosRegistrarUsuario.login()) != null) {
            return ResponseEntity.badRequest().build();
        }

        Rol rol;
        if (datosRegistrarUsuario.rol() != null) {
            rol = datosRegistrarUsuario.rol();
        } else {
            rol = Rol.USER;
        }

        String encryptedPassword = passwordEncoder.encode(datosRegistrarUsuario.clave());
        Usuario nuevoUsuario = new Usuario(datosRegistrarUsuario.login(), encryptedPassword,
                datosRegistrarUsuario.email(),datosRegistrarUsuario.rol());
        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok().build();
    }

}
