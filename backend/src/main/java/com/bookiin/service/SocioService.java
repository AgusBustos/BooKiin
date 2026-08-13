package com.bookiin.service;

import com.bookiin.model.Socio;
import com.bookiin.repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SocioService {

    private final SocioRepository socioRepository;

    @Autowired
    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    public Socio registrarSocio(Socio socio) {
        return socioRepository.save(socio);
    }

    public List<Socio> listarTodos() {
        return socioRepository.findAll();
    }

    public Optional<Socio> buscarPorDni(String dni) {
        return socioRepository.findById(dni);
    }
}
