package com.bookiin.service;

import com.bookiin.model.Ejemplar;
import com.bookiin.model.EstadoEjemplar;
import com.bookiin.model.Libro;
import com.bookiin.repository.EjemplarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EjemplarService {

    private final EjemplarRepository ejemplarRepository;
    private final LibroService libroService;

    @Autowired
    public EjemplarService(EjemplarRepository ejemplarRepository, LibroService libroService) {
        this.ejemplarRepository = ejemplarRepository;
        this.libroService = libroService;
    }

    public Ejemplar registrarEjemplar(String isbn, String estanteria, String estante) {
        Libro libro = libroService.registrarLibro(isbn);
        
        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setLibro(libro);
        ejemplar.setEstanteria(estanteria);
        ejemplar.setEstante(estante);
        ejemplar.setEstado(EstadoEjemplar.DISPONIBLE);
        
        return ejemplarRepository.save(ejemplar);
    }

    public List<Ejemplar> listarPorLibro(String isbn) {
        return ejemplarRepository.findByLibroIsbn(isbn);
    }

    public Optional<Ejemplar> buscarPorId(Long id) {
        return ejemplarRepository.findById(id);
    }
    
    public Ejemplar actualizarEstado(Long id, EstadoEjemplar nuevoEstado) {
        Ejemplar ejemplar = ejemplarRepository.findById(id).orElseThrow(() -> new RuntimeException("Ejemplar no encontrado"));
        ejemplar.setEstado(nuevoEstado);
        return ejemplarRepository.save(ejemplar);
    }
}
