package com.xcy7e.contextium

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContextMenuItemDao {

    @Query("""
        SELECT * FROM context_menu_items
        ORDER BY sortOrder ASC, title COLLATE NOCASE ASC
    """)
    fun getAll(): List<ContextMenuItem>

    @Query("""
        SELECT * FROM context_menu_items
        WHERE enabled = 1
        ORDER BY sortOrder ASC, title COLLATE NOCASE ASC
    """)
    fun getAllEnabled(): List<ContextMenuItem>

    @Query("SELECT * FROM context_menu_items WHERE id = :id LIMIT 1")
    fun getById(id: Long): ContextMenuItem?

    @Query("SELECT COALESCE(MAX(sortOrder), -1) FROM context_menu_items")
    fun getMaxSortOrder(): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: ContextMenuItem): Long

    @Update
    fun update(item: ContextMenuItem)

    @Delete
    fun delete(item: ContextMenuItem)

    @Query("DELETE FROM context_menu_items WHERE id = :id")
    fun deleteById(id: Long)

    @Query("UPDATE context_menu_items SET sortOrder = :sortOrder, updatedAt = :updatedAt WHERE id = :id")
    fun updateSortOrder(id: Long, sortOrder: Int, updatedAt: Long)
}