package com.bookiin.service;

import com.bookiin.dto.OpenLibraryResponse;
import com.bookiin.model.Libro;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OpenLibraryService {

    private static final String API_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:%s&format=json&jscmd=data";

    public Optional<Libro> buscarLibroPorIsbn(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(API_URL, isbn);

        try {
            OpenLibraryResponse response = restTemplate.getForObject(url, OpenLibraryResponse.class);
            if (response != null && response.getBooks() != null && response.getBooks().containsKey("ISBN:" + isbn)) {
                OpenLibraryResponse.BookData data = response.getBooks().get("ISBN:" + isbn);
                
                Libro libro = new Libro();
                libro.setIsbn(isbn);
                libro.setTitulo(data.getTitle());
                
                if (data.getAuthors() != null && !data.getAuthors().isEmpty()) {
                    libro.setAutor(data.getAuthors().stream()
                            .map(OpenLibraryResponse.Author::getName)
                            .collect(Collectors.joining(", ")));
                }
                
                if (data.getPublishers() != null && !data.getPublishers().isEmpty()) {
                    libro.setEditorial(data.getPublishers().stream()
                            .map(OpenLibraryResponse.Publisher::getName)
                            .collect(Collectors.joining(", ")));
                }

                if (data.getSubjects() != null && !data.getSubjects().isEmpty()) {
                    libro.setCategoria(data.getSubjects().stream()
                            .map(OpenLibraryResponse.Subject::getName)
                            .limit(3) // Limit to 3 to avoid super long strings
                            .collect(Collectors.joining(", ")));
                }
                
                if (data.getCover() != null) {
                    if (data.getCover().getMedium() != null) {
                        libro.setUrlPortada(data.getCover().getMedium());
                    } else if (data.getCover().getLarge() != null) {
                        libro.setUrlPortada(data.getCover().getLarge());
                    }
                }
                
                return Optional.of(libro);
            }
        } catch (Exception e) {
            System.err.println("Error consultando OpenLibrary API: " + e.getMessage());
        }
        return Optional.empty();
    }
}
