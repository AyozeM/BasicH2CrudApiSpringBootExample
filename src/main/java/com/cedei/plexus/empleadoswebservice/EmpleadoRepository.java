package com.cedei.plexus.empleadoswebservice;

import com.cedei.plexus.empleadoswebservice.pojos.Empleado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EmpleadoRepository
 */
@Repository
public interface EmpleadoRepository  extends JpaRepository<Empleado,Long>{


    
}