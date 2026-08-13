package com.bookiin.controller;

import com.bookiin.model.Libro;
import com.bookiin.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/libros")
@CrossOrigin(origins = "*") // Para desarrollo PWA
public class LibroController {

    private final LibroService libroService;

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping("/{isbn}")
    public ResponseEntity<Libro> registrarLibroPorIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(libroService.registrarLibro(isbn));
    }

    @PostMapping
    public ResponseEntity<Libro> guardarLibro(@RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.actualizarLibro(libro));
    }

    @GetMapping("/externo/{isbn}")
    public ResponseEntity<Libro> buscarLibroExterno(@PathVariable String isbn) {
        return libroService.buscarLibroExterno(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Libro>> listarLibros() {
        return ResponseEntity.ok(libroService.listarTodos());
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Libro> buscarLibro(@PathVariable String isbn) {
        return libroService.buscarPorIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
