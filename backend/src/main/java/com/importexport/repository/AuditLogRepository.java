package com.importexport.repository;

import com.importexport.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    List<AuditLog> findByOperationType(String operationType);
    
    List<AuditLog> findByModuleType(String moduleType);
    
    List<AuditLog> findByUserId(String userId);
    
    List<AuditLog> findByStatus(String status);
    
    List<AuditLog> findByOperationTypeAndModuleType(String operationType, String moduleType);
    
    @Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    List<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM AuditLog a WHERE a.userId = :userId AND a.createdAt >= :fromDate ORDER BY a.createdAt DESC")
    List<AuditLog> findRecentByUser(@Param("userId") String userId, @Param("fromDate") LocalDateTime fromDate);
}