package com.bookiin.service;

import com.bookiin.dto.GoogleBooksResponse;
import com.bookiin.model.Libro;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GoogleBooksService {

    @Value("${google.books.api.url}")
    private String apiUrl;

    public Optional<Libro> buscarLibroPorIsbn(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        String url = apiUrl + "?q=isbn:" + isbn;

        try {
            GoogleBooksResponse response = restTemplate.getForObject(url, GoogleBooksResponse.class);
            if (response != null && response.getItems() != null && !response.getItems().isEmpty()) {
                GoogleBooksResponse.VolumeInfo info = response.getItems().get(0).getVolumeInfo();
                
                Libro libro = new Libro();
                libro.setIsbn(isbn);
                libro.setTitulo(info.getTitle());
                if (info.getAuthors() != null && !info.getAuthors().isEmpty()) {
                    libro.setAutor(String.join(", ", info.getAuthors()));
                }
                if (info.getPublisher() != null) {
                    libro.setEditorial(info.getPublisher());
                }

                if (info.getCategories() != null && !info.getCategories().isEmpty()) {
                    libro.setCategoria(String.join(", ", info.getCategories()));
                }

                if (info.getImageLinks() != null && info.getImageLinks().getThumbnail() != null) {
                    // Replace http with https for better security
                    libro.setUrlPortada(info.getImageLinks().getThumbnail().replace("http:", "https:"));
                }
                return Optional.of(libro);
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error consultando Google Books API: " + e.getMessage());
        }
        return Optional.empty();
    }
}
