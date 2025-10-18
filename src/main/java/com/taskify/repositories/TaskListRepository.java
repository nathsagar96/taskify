package com.taskify.repositories;

import com.taskify.entities.TaskList;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, UUID> {

    @Override
    @EntityGraph(attributePaths = "tasks")
    Optional<TaskList> findById(UUID id);

    @Override
    @EntityGraph(attributePaths = "tasks")
    List<TaskList> findAll();
}
