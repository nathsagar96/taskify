-- Fix tasks table to match entity changes
-- due_date should be nullable, priority and status should be VARCHAR (EnumType.STRING)

ALTER TABLE tasks ALTER COLUMN due_date DROP NOT NULL;

ALTER TABLE tasks ALTER COLUMN priority TYPE VARCHAR(255);
ALTER TABLE tasks ALTER COLUMN status TYPE VARCHAR(255);