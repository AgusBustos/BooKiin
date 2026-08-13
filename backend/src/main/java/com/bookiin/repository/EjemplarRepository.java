package com.bookiin.repository;

import com.bookiin.model.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long> {
    List<Ejemplar> findByLibroIsbn(String isbn);
}
