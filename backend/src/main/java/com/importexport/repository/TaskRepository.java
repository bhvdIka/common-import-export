package com.importexport.repository;

import com.importexport.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByIsActiveTrue();
    
    List<Task> findByType(String type);
    
    List<Task> findByStatus(String status);
    
    List<Task> findByAssignedTo(String assignedTo);
    
    Optional<Task> findByName(String name);
    
    List<Task> findByPriorityOrderByPriorityAsc(Integer priority);
    
    List<Task> findByDueDateBefore(LocalDateTime date);
    
    List<Task> findByDueDateAfter(LocalDateTime date);
    
    @Query("SELECT t FROM Task t WHERE t.assignedTo = :assignedTo AND t.isActive = true ORDER BY t.priority ASC, t.dueDate ASC")
    List<Task> findActiveTasksByAssignedToOrderByPriorityAndDueDate(@Param("assignedTo") String assignedTo);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    boolean existsByName(String name);
}