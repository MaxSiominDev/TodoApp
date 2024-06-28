package dev.maxsiomin.todoapp.feature.todolist.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create the new table
        db.execSQL("""
            CREATE TABLE todoItems_new (
                id TEXT NOT NULL PRIMARY KEY,
                description TEXT NOT NULL,
                priority TEXT NOT NULL,
                isCompleted INTEGER NOT NULL DEFAULT 0,
                created INTEGER NOT NULL,
                modified INTEGER,
                deadline INTEGER
            )
        """.trimIndent())

        // Copy the data from the old table to the new table, converting progress to isCompleted
        db.execSQL("""
            INSERT INTO todoItems_new (id, description, priority, isCompleted, created, modified, deadline)
            SELECT id, description, priority,
            CASE WHEN progress = 'Completed' THEN 1 ELSE 0 END,
            created, modified, deadline
            FROM todoItems
        """.trimIndent())

        // Remove the old table
        db.execSQL("DROP TABLE todoItems")

        // Rename the new table to the old table name
        db.execSQL("ALTER TABLE todoItems_new RENAME TO todoItems")
    }
}
