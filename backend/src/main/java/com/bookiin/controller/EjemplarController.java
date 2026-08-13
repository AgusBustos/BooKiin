package com.bookiin.controller;

import com.bookiin.model.Ejemplar;
import com.bookiin.model.EstadoEjemplar;
import com.bookiin.service.EjemplarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ejemplares")
@CrossOrigin(origins = "*")
public class EjemplarController {

    private final EjemplarService ejemplarService;

    @Autowired
    public EjemplarController(EjemplarService ejemplarService) {
        this.ejemplarService = ejemplarService;
    }

    @PostMapping
    public ResponseEntity<Ejemplar> registrarEjemplar(
            @RequestParam String isbn,
            @RequestParam String estanteria,
            @RequestParam String estante) {
        return ResponseEntity.ok(ejemplarService.registrarEjemplar(isbn, estanteria, estante));
    }

    @GetMapping("/libro/{isbn}")
    public ResponseEntity<List<Ejemplar>> listarPorLibro(@PathVariable String isbn) {
        return ResponseEntity.ok(ejemplarService.listarPorLibro(isbn));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Ejemplar> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoEjemplar estado) {
        return ResponseEntity.ok(ejemplarService.actualizarEstado(id, estado));
    }
}
