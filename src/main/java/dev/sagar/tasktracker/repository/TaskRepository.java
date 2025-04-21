package dev.sagar.tasktracker.repository;

import dev.sagar.tasktracker.model.Task;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByTaskListId(Long taskListId);

  Optional<Task> findByIdAndTaskListId(Long id, Long taskListId);
}
