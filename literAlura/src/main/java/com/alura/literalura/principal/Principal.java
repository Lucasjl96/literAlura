package com.alura.literalura.principal;
import com.alura.literalura.model.*;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.alura.literalura.service.LibroService;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner leer = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private static LibroService libroService;
    private static AutorService autorService;

    public Principal(LibroService libroService, AutorService autorService) {
        Principal.libroService = libroService;
        Principal.autorService = autorService;
    }

    public void menu() {
        var opcion = -1;

        while (opcion != 0) {
            System.out.println("""
                      Bienvenidos a LiterAlura:
                      Por favor seleccione la opcion que desee:
                      
                      *****************************
                      
                    1- Buscar libro por titulo
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    0- Salir
                    ******************************
                    """);
            opcion = leer.nextInt();
            leer.nextLine();

            switch (opcion) {
                case 1:
                    obtenerLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Muchas gracias por utilizar LiterAlura");
                    break;
                default:
                    System.out.println("Opcion incorrecta, por favor seleccione otra.");
            }

        }

    }

    private void obtenerLibroPorTitulo() {
        System.out.println("Ingrese el libro que desea buscar:");
        var nombreLibro = leer.nextLine();

        var json = consumoApi.obtenerDatos(URL_BASE+"?search="+normalizarTextoParaURL(nombreLibro));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> normalizarTexto(l.titulo()).contains(normalizarTexto(nombreLibro)))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("Libro encontrado.");
            manejarLibro(libroBuscado.get());
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private String normalizarTexto(String texto) {
        return texto.toUpperCase().trim();
    }
    private String normalizarTextoParaURL(String texto) {
        return texto.replace(" ", "+");
    }


    private void manejarLibro(DatosLibro datosLibro) {
        Libro libroExistente = libroService.obtenerLibroPorNombre(datosLibro.titulo());
        if (libroExistente != null) {
            System.out.println("El libro solicitado se encuentra registrado.");
            System.out.println(libroExistente);
            return;
        }

        Libro libro = new Libro(datosLibro);
        datosLibro.autor().forEach(datosAutor -> libro.agregarAutor(obtenerOGuardarAutor(datosAutor)));
        libroService.guardarLibro(libro);
        System.out.println("Libro guardado en la biblioteca.");
        System.out.println(libro);
    }

    private Autor obtenerOGuardarAutor(DatosAutor datosAutor) {
        Autor autor = autorService.obtenerAutorPorNombre(datosAutor.nombre());
        if (autor != null) {
            System.out.println("El autor se encuentra registrado. ");
            return autor;
        }

        autor = new Autor(datosAutor);
        autorService.guardarAutor(autor);
        return autor;
    }

    public void listarLibrosRegistrados() {
        List<Libro> libros = libroService.obtenerLibros();
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorService.obtenerAutores();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año que desee buscar:");
        var anio = leer.nextInt();
        leer.nextLine();

        List<Autor> autoresVivos = autorService.obtenerAutoresVivosEnDeterminadoAnio(anio);
        autoresVivos.forEach(System.out::println);
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Por favor selecciona el idioma que desees buscar:
                1- Español
                2- Ingles
                3- Portugues
                
                
                
                """);
        var opc = leer.nextInt();
        leer.nextLine();
        String idioma = "";

        switch (opc) {
            case 1:
                idioma = "es";
                break;
            case 2:
                idioma = "en";
                break;
            case 3:
                idioma = "pt";
                break;
            default:
                System.out.println("Opción incorrecta, por favor intentelo nuevamente.");
        }
        List<Libro> librosIdioma = libroService.obtenerLibrosPorIdioma(idioma);
        librosIdioma.forEach(System.out::println);
    }

}
