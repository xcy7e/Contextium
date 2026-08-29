package app.xcy7e.contextium

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "context_menu_items",
    indices = [
        Index(value = ["title"], unique = true),
        Index(value = ["label"], unique = true)
    ]
)
data class ContextMenuItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val label: String,
    val url: String,
    val urlParam: String,

    val enabled: Boolean = true,
    val sortOrder: Int = 0,

    val icon: String? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)