package com.bookiin.service;

import com.bookiin.model.Libro;
import com.bookiin.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final GoogleBooksService googleBooksService;
    private final OpenLibraryService openLibraryService;

    @Autowired
    public LibroService(LibroRepository libroRepository, GoogleBooksService googleBooksService, OpenLibraryService openLibraryService) {
        this.libroRepository = libroRepository;
        this.googleBooksService = googleBooksService;
        this.openLibraryService = openLibraryService;
    }

    public Libro registrarLibro(String isbn) {
        Optional<Libro> existente = libroRepository.findById(isbn);
        if (existente.isPresent()) {
            return existente.get();
        }

        Optional<Libro> libroDesdeApi = googleBooksService.buscarLibroPorIsbn(isbn);
        if (libroDesdeApi.isPresent()) {
            return libroRepository.save(libroDesdeApi.get());
        } else {
            // If not found in API, save a placeholder to be edited later
            Libro nuevo = new Libro();
            nuevo.setIsbn(isbn);
            nuevo.setTitulo("Libro Desconocido");
            return libroRepository.save(nuevo);
        }
    }

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Optional<Libro> buscarPorIsbn(String isbn) {
        return libroRepository.findById(isbn);
    }
    
    public Libro actualizarLibro(Libro libro) {
        return libroRepository.save(libro);
    }
    
    public Optional<Libro> buscarLibroExterno(String isbn) {
        Optional<Libro> googleResult = googleBooksService.buscarLibroPorIsbn(isbn);
        if (googleResult.isPresent()) {
            return googleResult;
        }
        // Fallback to OpenLibrary
        return openLibraryService.buscarLibroPorIsbn(isbn);
    }
}
