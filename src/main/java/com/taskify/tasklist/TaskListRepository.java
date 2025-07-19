package com.taskify.tasklist;

import com.taskify.tasklist.domain.entities.TaskList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, UUID> {

  @Override
  @EntityGraph(attributePaths = "tasks")
  Optional<TaskList> findById(UUID id);

  @Override
  @EntityGraph(attributePaths = "tasks")
  List<TaskList> findAll();
}
