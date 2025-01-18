package com.alura.literalura.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Idioma idiomas;
    private Double numDescargas;
    public Libro() {
    }

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idiomas = Idioma.fromString(datosLibro.idiomas().get(0));
        this.numDescargas = datosLibro.numDescargas();
        this.autores = new ArrayList<>();
    }

    public void agregarAutor(Autor autor) {
        if (!autores.contains(autor)) {
            autores.add(autor);
        }
        if (!autor.getLibros().contains(this)) {
            autor.agregarLibro(this);
        }
    }

    public Long getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public List<Autor> getAutores() {
        return autores;
    }
    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }
    public void setIdiomas(Idioma idiomas) {
        this.idiomas = idiomas;
    }
    public Idioma getIdiomas() {
        return idiomas;
    }
    public Double getNumDescargas() {
        return numDescargas;
    }
    public void setNumDescargas(Double numDescargas) {
        this.numDescargas = numDescargas;
    }

    @Override
    public String toString() {
        return "Datos Libro\n" +
                "Titulo:'" + titulo + "\n" +
                "Autor:" + autores.stream().map(Autor::getNombre).toList() + "\n" +
                "Idioma:" + idiomas + "\n" +
                "Numero de descargas" + numDescargas + "\n" +
                "* * * * * * * * * * * * * * * *";
    }
}
