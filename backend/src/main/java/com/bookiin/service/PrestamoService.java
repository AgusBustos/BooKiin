package com.bookiin.service;

import com.bookiin.model.Ejemplar;
import com.bookiin.model.EstadoEjemplar;
import com.bookiin.model.EstadoPrestamo;
import com.bookiin.model.Prestamo;
import com.bookiin.model.Socio;
import com.bookiin.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final EjemplarService ejemplarService;
    private final SocioService socioService;

    @Autowired
    public PrestamoService(PrestamoRepository prestamoRepository, EjemplarService ejemplarService, SocioService socioService) {
        this.prestamoRepository = prestamoRepository;
        this.ejemplarService = ejemplarService;
        this.socioService = socioService;
    }

    @Transactional
    public Prestamo registrarPrestamo(String socioDni, Long ejemplarId, int diasPrestamo) {
        Socio socio = socioService.buscarPorDni(socioDni)
                .orElseGet(() -> {
                    Socio nuevo = new Socio();
                    nuevo.setDni(socioDni);
                    nuevo.setNombre("Socio " + socioDni);
                    nuevo.setApellido("Desconocido");
                    return socioService.registrarSocio(nuevo);
                });
                
        Ejemplar ejemplar = ejemplarService.buscarPorId(ejemplarId)
                .orElseThrow(() -> new RuntimeException("Ejemplar no encontrado"));

        if (ejemplar.getEstado() != EstadoEjemplar.DISPONIBLE) {
            throw new RuntimeException("El ejemplar no está disponible para préstamo");
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setSocio(socio);
        prestamo.setEjemplar(ejemplar);
        prestamo.setFechaRetiro(LocalDate.now());
        prestamo.setFechaVencimiento(LocalDate.now().plusDays(diasPrestamo));
        prestamo.setEstado(EstadoPrestamo.ACTIVO);

        // Actualizar estado del ejemplar
        ejemplarService.actualizarEstado(ejemplarId, EstadoEjemplar.PRESTADO);

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo devolverPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        prestamo.setFechaDevolucion(LocalDate.now());
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        // Liberar el ejemplar
        ejemplarService.actualizarEstado(prestamo.getEjemplar().getId(), EstadoEjemplar.DISPONIBLE);

        return prestamoRepository.save(prestamo);
    }

    public List<Prestamo> listarPrestamosActivos() {
        return prestamoRepository.findByEstado(EstadoPrestamo.ACTIVO);
    }

    public List<Prestamo> listarHistorialPorLibro(String isbn) {
        return prestamoRepository.findByEjemplar_Libro_IsbnOrderByFechaRetiroDesc(isbn);
    }
}
