package com.importexport.repository;

import com.importexport.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapRepository extends JpaRepository<Map, Long> {
    
    List<Map> findByIsActiveTrue();
    
    List<Map> findByType(String type);
    
    Optional<Map> findByName(String name);
    
    List<Map> findByResolution(Double resolution);
    
    List<Map> findByTypeAndIsActive(String type, Boolean isActive);
    
    @Query("SELECT m FROM Map m WHERE m.width >= :minWidth AND m.height >= :minHeight")
    List<Map> findByMinimumDimensions(@Param("minWidth") Integer minWidth, @Param("minHeight") Integer minHeight);
    
    @Query("SELECT m FROM Map m WHERE m.resolution BETWEEN :minResolution AND :maxResolution")
    List<Map> findByResolutionRange(@Param("minResolution") Double minResolution, @Param("maxResolution") Double maxResolution);
    
    boolean existsByName(String name);
}