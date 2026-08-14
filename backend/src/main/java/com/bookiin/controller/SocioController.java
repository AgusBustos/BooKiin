package com.bookiin.controller;

import com.bookiin.model.Socio;
import com.bookiin.service.SocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/socios")
@CrossOrigin(origins = "*")
public class SocioController {

    private final SocioService socioService;

    @Autowired
    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @PostMapping
    public ResponseEntity<Socio> registrarSocio(@RequestBody Socio socio) {
        return ResponseEntity.ok(socioService.registrarSocio(socio));
    }

    @GetMapping
    public ResponseEntity<List<Socio>> listarSocios() {
        return ResponseEntity.ok(socioService.listarTodos());
    }

    @GetMapping("/{dni}")
    public ResponseEntity<Socio> buscarSocio(@PathVariable String dni) {
        return socioService.buscarPorDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{dni}")
    public ResponseEntity<Socio> actualizarSocio(@PathVariable String dni, @RequestBody Socio socioUpdates) {
        return socioService.buscarPorDni(dni).map(socioExistente -> {
            socioExistente.setNombre(socioUpdates.getNombre());
            socioExistente.setApellido(socioUpdates.getApellido());
            socioExistente.setEmailTelefono(socioUpdates.getEmailTelefono());
            return ResponseEntity.ok(socioService.registrarSocio(socioExistente));
        }).orElse(ResponseEntity.notFound().build());
    }
}
