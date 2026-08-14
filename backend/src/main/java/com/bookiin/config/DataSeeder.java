package com.bookiin.config;

import com.bookiin.model.*;
import com.bookiin.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final LibroRepository libroRepository;
    private final EjemplarRepository ejemplarRepository;

    public DataSeeder(LibroRepository libroRepository, EjemplarRepository ejemplarRepository) {
        this.libroRepository = libroRepository;
        this.ejemplarRepository = ejemplarRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (libroRepository.count() == 0) {
            String[] titulos = {
                "Don Quijote de la Mancha", "Cien años de soledad", "Ficciones", "El Aleph", 
                "Rayuela", "Pedro Páramo", "El túnel", "Sobre los héroes y las tumbas", 
                "El amor en los tiempos del cólera", "La ciudad y los perros", "Conversación en La Catedral", 
                "Los detectives salvajes", "2666", "La sombra del viento", "La casa de los espíritus", 
                "Crónica de una muerte anunciada", "El laberinto de la soledad", "Niebla", 
                "Bodas de sangre", "La casa de Bernarda Alba", "Romancero gitano", "Platero y yo", 
                "Cuentos de amor de locura y de muerte", "El gaucho Martín Fierro", "Facundo", 
                "La invención de Morel", "Los ríos profundos", "Aura", "La muerte de Artemio Cruz", 
                "El señor presidente", "Hombres de maíz", "El astillero", "Juntacadáveres", 
                "La tregua", "Las venas abiertas de América Latina", "El beso de la mujer araña", 
                "Boquitas pintadas", "Santa Evita", "El entenado", "Zama", "Respiración artificial", 
                "La vida breve", "El obsceno pájaro de la noche", "Coronación", "La amortajada", 
                "El lugar sin límites", "Las batallas en el desierto", "El complot mongol", 
                "El coronel no tiene quien le escriba", "Relato de un náufrago", "El otoño del patriarca", 
                "El general en su laberinto", "Doce cuentos peregrinos", "Del amor y otros demonios", 
                "Vivir para contarla", "La mala hora", "La hojarasca", "Los funerales de la Mamá Grande", 
                "Isabel viendo llover en Macondo", "Ojos de perro azul", "El evangelio según Jesucristo", 
                "Ensayo sobre la ceguera", "El hombre duplicado", "Las intermitencias de la muerte", 
                "La caverna", "El viaje del elefante", "El año de la muerte de Ricardo Reis", 
                "Historia del cerco de Lisboa", "Levantado del suelo", "Memorial del convento", 
                "Viaje a Portugal", "Cuadernos de Lanzarote", "El cuento de la isla desconocida", 
                "El año del diluvio", "Últimas tardes con Teresa", "Si te dicen que caí", 
                "La muchacha de las bragas de oro", "Rabos de lagartija", "El amante bilingüe", 
                "El embrujo de Shanghai", "El pianista", "El nombre de la rosa", "El péndulo de Foucault", 
                "La isla del día de antes", "Baudolino", "La misteriosa llama de la reina Loana", 
                "El cementerio de Praga", "Número cero", "Obra abierta", "Apocalípticos e integrados", 
                "Tratado de semiótica general", "Lector in fabula", "Los límites de la interpretación", 
                "Seis paseos por los bosques narrativos", "Kant y el ornitorrinco", "Historia de la belleza", 
                "Historia de la fealdad", "El vértigo de las listas", "Nadie acaba con los libros", 
                "Confesiones de un joven novelista"
            };

            for (int i = 0; i < titulos.length; i++) {
                Libro libro = new Libro();
                libro.setIsbn("97800000" + String.format("%03d", i));
                libro.setTitulo(titulos[i]);
                libro.setAutor("Clásico Universal");
                libro.setCategoria("Novela Clásica");
                libroRepository.save(libro);

                Ejemplar ejemplar = new Ejemplar();
                ejemplar.setLibro(libro);
                ejemplar.setEstado(EstadoEjemplar.DISPONIBLE);
                ejemplar.setEstanteria("Clásicos");
                ejemplar.setEstante("A");
                ejemplarRepository.save(ejemplar);
            }
            System.out.println("DataSeeder: 100 libros clásicos insertados con éxito.");
        }
    }
}
