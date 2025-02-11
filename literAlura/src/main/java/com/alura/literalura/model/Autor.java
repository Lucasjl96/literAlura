package com.alura.literalura.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int fechaNacimiento;
    private int fechaFallecimiento;

    @ManyToMany(mappedBy = "autores", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    public Autor() {
    }

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = Optional.of(datosAutor.fechaNacimiento()).orElse(0);
        this.fechaFallecimiento = datosAutor.fechaFallecimiento();
    }

    public void agregarLibro(Libro libro) {

        if (!libros.contains(libro)) {
            libros.add(libro);//Relacionar autor con libro
        }
    }

    public Long getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public List<Libro> getLibros() {
        return libros;
    }
    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
    public int getFechaFallecimiento() {
        return fechaFallecimiento;
    }
    public void setFechaFallecimiento(int fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    @Override
    public String toString() {
        String fechaFallecimientoStr =(fechaFallecimiento==0)?"N/A":String.valueOf(fechaFallecimiento);
        return "Datos del autor:\n" +
                "Nombre:" + nombre + "\n" +
                "Fecha de nacimiento:" + fechaNacimiento + "\n" +
                "Fecha de fallecimiento:" + fechaFallecimientoStr + "\n" +
                "Obras:" + libros.stream().map(Libro::getTitulo).toList() + "\n" +
                "* * * * * * * * * * * * *";
    }
}
