package com.alura.back.controller;

import com.alura.back.domain.usuario.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity registrarUsuario(@RequestBody @Valid DatosRegistrarUsuario datosRegistrarUsuario
    , UriComponentsBuilder uriComponentsBuilder){
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
                datosRegistrarUsuario.email(),rol);
        usuarioRepository.save(nuevoUsuario);

        // return ResponseEntity.ok().build();
        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(nuevoUsuario.getId());
        URI url = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(nuevoUsuario.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaUsuario);
    }

}
