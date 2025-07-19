CREATE TABLE task_lists (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);

CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    due_date TIMESTAMP NOT NULL,
    priority INT NOT NULL,
    status INT NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    task_list_id UUID NOT NULL,
    FOREIGN KEY (task_list_id) REFERENCES task_lists(id)
);