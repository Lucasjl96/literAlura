package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository repository;

    public Autor obtenerAutorPorNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    public void guardarAutor(Autor autor) {
        repository.save(autor);
    }

    public List<Autor> obtenerAutores() {
        return repository.findAll();
    }

    public List<Autor> obtenerAutoresVivosEnDeterminadoAnio(int anio) {
        return repository.autoresVivosEnDeterminadoAnio(anio);
    }
}
