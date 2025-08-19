package com.importexport.repository;

import com.importexport.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {
    
    List<Camera> findByIsActiveTrue();
    
    List<Camera> findByType(String type);
    
    Optional<Camera> findByName(String name);
    
    List<Camera> findByTypeAndIsActive(String type, Boolean isActive);
    
    @Query("SELECT c FROM Camera c WHERE c.location LIKE %:location%")
    List<Camera> findByLocationContaining(@Param("location") String location);
    
    boolean existsByName(String name);
    
    boolean existsByIpAddress(String ipAddress);
}