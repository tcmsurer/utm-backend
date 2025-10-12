-- Step 1: Add the new column but allow it to be NULL temporarily.
ALTER TABLE usta ADD COLUMN is_active BOOLEAN;

-- Step 2: Update all existing rows to set a default value for the new column.
-- We'll assume all existing craftsmen should be active.
UPDATE usta SET is_active = TRUE;

-- Step 3: Now that all rows have a value, alter the column to be NOT NULL.
ALTER TABLE usta ALTER COLUMN is_active SET NOT NULL;