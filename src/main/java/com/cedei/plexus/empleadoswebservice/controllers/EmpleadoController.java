package com.cedei.plexus.empleadoswebservice.controllers;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import com.cedei.plexus.empleadoswebservice.EmpleadoRepository;
import com.cedei.plexus.empleadoswebservice.exceptions.EmpleadoNotFoundException;
import com.cedei.plexus.empleadoswebservice.pojos.Empleado;
import com.cedei.plexus.empleadoswebservice.pojos.ErrorRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * EmpleadoController
 */
@RestController
@RequestMapping("/api")
public class EmpleadoController {

    Properties messages;

    public EmpleadoController() {
        //TODO: Leer fichero messages.properties
    }


    @Autowired
    EmpleadoRepository empleadoRepository;

    @GetMapping("/empleados")
    public List<Empleado> list() {
        List<Empleado> result = empleadoRepository.findAll();
        if (result != null) {
            return result;
        } else {
            throw new EmpleadoNotFoundException("No existen empleados");
        }
    }

    @GetMapping("/empleado/{id}")
    public Empleado getEmpleado(@PathVariable Long id) {
        Optional<Empleado> result = empleadoRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new EmpleadoNotFoundException(String.format("El emplado con identificador %d no existe", id));
        }
    }

    // Manera de añadir un usuario 1
    @PostMapping("/empleado")
    public Empleado addEmpleado(@RequestBody Empleado empleado, HttpServletResponse response) {
        Empleado newEmpleado = new Empleado(empleado.getNombre(), empleado.getApellidos(),
                empleado.getFechaNacimiento());
        response.setStatus(201);
        return empleadoRepository.save(newEmpleado);
    }

    // Manera de añadir un usuario 2
    @PostMapping("/empleado2")
    public ResponseEntity<?> addEmpleado(RequestEntity<Empleado> reqEmpleado) {
        ResponseEntity<?> response;
        Empleado empleado = reqEmpleado.getBody();
        if (empleado == null) {
            response = new ResponseEntity<ErrorRest>(
                    new ErrorRest("Formato de  peticón invalido", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        } else {
            if (empleado.getId() != null && empleadoRepository.findById(empleado.getId()).isPresent()) {
                /*
                 * Es mejor eviar un json con informacion que un string response = new
                 * ResponseEntity<String>(String.
                 * format("El empleado con el identificador %d ya existe en la base de datos",
                 * empleado.getId()),HttpStatus.CONFLICT);
                 */
                response = new ResponseEntity<ErrorRest>(
                        new ErrorRest(String.format("El empleado con el identificador %d ya existe en la base de datos",
                                empleado.getId()), HttpStatus.CONFLICT),
                        HttpStatus.CONFLICT);
            } else {
                response = new ResponseEntity<Empleado>(empleadoRepository.save(empleado), HttpStatus.CREATED);
            }
        }
        return response;
    }

    @PutMapping("/empleado")
    public ResponseEntity<?> updateEmpleado(RequestEntity<Empleado> reqEmpleado) {
        ResponseEntity<?> response;
        Empleado updatedEmpleado = reqEmpleado.getBody();
        if (updatedEmpleado != null) {
            if (empleadoRepository.findById(updatedEmpleado.getId()).isPresent()) {
                response = new ResponseEntity<>(empleadoRepository.save(updatedEmpleado), HttpStatus.OK);
            } else {
                response = new ResponseEntity<ErrorRest>(new ErrorRest(
                        String.format("El usuario con identificador %d no existe", updatedEmpleado.getId()),
                        HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
            }
        } else {
            response = new ResponseEntity<ErrorRest>(
                    new ErrorRest("Formato de petición invalido", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @DeleteMapping("/empleado/{id}")
    public ResponseEntity<?> removeEmpleado(@PathVariable Long id) {
        ResponseEntity<?> response;
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        if (empleado.isPresent()) {
            Empleado toDelete = (Empleado) empleado.get();
            empleadoRepository.delete(toDelete);
            response = new ResponseEntity<Empleado>(toDelete, HttpStatus.OK);
        } else {
            response = new ResponseEntity<ErrorRest>(
                    new ErrorRest(String.format("El usuario con identificador %d no existe", id), HttpStatus.NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
        return response;
    }
}