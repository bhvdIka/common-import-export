package com.importexport.repository;

import com.importexport.entity.Robot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RobotRepository extends JpaRepository<Robot, Long> {
    
    List<Robot> findByIsActiveTrue();
    
    List<Robot> findByModel(String model);
    
    List<Robot> findByManufacturer(String manufacturer);
    
    Optional<Robot> findByName(String name);
    
    Optional<Robot> findBySerialNumber(String serialNumber);
    
    List<Robot> findByModelAndManufacturer(String model, String manufacturer);
    
    @Query("SELECT r FROM Robot r WHERE r.model LIKE %:model% OR r.manufacturer LIKE %:manufacturer%")
    List<Robot> findByModelOrManufacturerContaining(@Param("model") String model, @Param("manufacturer") String manufacturer);
    
    boolean existsByName(String name);
    
    boolean existsBySerialNumber(String serialNumber);
}