package dev.sagar.taskify.repository;

import dev.sagar.taskify.model.Task;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByTaskListId(Long taskListId);

  Optional<Task> findByIdAndTaskListId(Long id, Long taskListId);
}
