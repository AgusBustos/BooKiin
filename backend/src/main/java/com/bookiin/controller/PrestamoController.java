package com.bookiin.controller;

import com.bookiin.model.Prestamo;
import com.bookiin.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@CrossOrigin(origins = "*")
public class PrestamoController {

    private final PrestamoService prestamoService;

    @Autowired
    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<Prestamo> registrarPrestamo(
            @RequestParam String dniSocio,
            @RequestParam Long idEjemplar,
            @RequestParam(defaultValue = "14") int dias) {
        return ResponseEntity.ok(prestamoService.registrarPrestamo(dniSocio, idEjemplar, dias));
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> devolverPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(prestamoService.devolverPrestamo(id));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Prestamo>> listarActivos() {
        return ResponseEntity.ok(prestamoService.listarPrestamosActivos());
    }
}
