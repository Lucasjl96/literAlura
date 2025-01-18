package com.alura.literalura.repository;
import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombre(String nombre);
    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :anio AND (a.fechaFallecimiento > :anio OR a.fechaFallecimiento IS NULL)")
    List<Autor> autoresVivosEnDeterminadoAnio(int anio);
}
