package com.bookiin.repository;

import com.bookiin.model.Prestamo;
import com.bookiin.model.EstadoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findBySocioDniAndEstado(String dni, EstadoPrestamo estado);
    List<Prestamo> findByEstado(EstadoPrestamo estado);
    List<Prestamo> findByEjemplar_Libro_IsbnOrderByFechaRetiroDesc(String isbn);
}
