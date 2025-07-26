CREATE INDEX idx_tasks_task_list_id ON tasks (task_list_id);
CREATE INDEX idx_tasks_status ON tasks (status);
CREATE INDEX idx_tasks_priority ON tasks (priority);
CREATE INDEX idx_tasks_due_date ON tasks (due_date);
CREATE INDEX idx_task_lists_title ON task_lists (title);