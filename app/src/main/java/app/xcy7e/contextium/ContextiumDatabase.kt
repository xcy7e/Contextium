package app.xcy7e.contextium

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ContextMenuItem::class],
    version = 3,
    exportSchema = false
)
abstract class ContextiumDatabase : RoomDatabase() {

    abstract fun contextMenuItemDao(): ContextMenuItemDao

    companion object {
        @Volatile
        private var instance: ContextiumDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `context_menu_items_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `title` TEXT NOT NULL,
                        `label` TEXT NOT NULL,
                        `url` TEXT NOT NULL,
                        `enabled` INTEGER NOT NULL,
                        `sortOrder` INTEGER NOT NULL,
                        `icon` TEXT,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO `context_menu_items_new` (
                        `id`, `title`, `label`, `url`, `enabled`, `sortOrder`, `icon`, `createdAt`, `updatedAt`
                    )
                    SELECT
                        `id`,
                        `title`,
                        `label`,
                        CASE
                            WHEN `urlParam` IS NOT NULL AND TRIM(`urlParam`) != '' THEN
                                `url` || (CASE WHEN `url` LIKE '%?%' THEN '&' ELSE '?' END) || `urlParam` || '=%s'
                            ELSE
                                `url`
                        END AS `url`,
                        `enabled`,
                        `sortOrder`,
                        `icon`,
                        `createdAt`,
                        `updatedAt`
                    FROM `context_menu_items`
                    """.trimIndent()
                )

                db.execSQL("DROP TABLE `context_menu_items` ")
                db.execSQL("ALTER TABLE `context_menu_items_new` RENAME TO `context_menu_items` ")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_context_menu_items_title` ON `context_menu_items` (`title`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_context_menu_items_label` ON `context_menu_items` (`label`)")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `context_menu_items_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `title` TEXT NOT NULL,
                        `label` TEXT,
                        `url` TEXT NOT NULL,
                        `enabled` INTEGER NOT NULL,
                        `sortOrder` INTEGER NOT NULL,
                        `icon` TEXT,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO `context_menu_items_new` (
                        `id`, `title`, `label`, `url`, `enabled`, `sortOrder`, `icon`, `createdAt`, `updatedAt`
                    )
                    SELECT
                        `id`, `title`, `label`, `url`, `enabled`, `sortOrder`, `icon`, `createdAt`, `updatedAt`
                    FROM `context_menu_items`
                    """.trimIndent()
                )

                db.execSQL("DROP TABLE `context_menu_items` ")
                db.execSQL("ALTER TABLE `context_menu_items_new` RENAME TO `context_menu_items` ")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_context_menu_items_title` ON `context_menu_items` (`title`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_context_menu_items_label` ON `context_menu_items` (`label`)")
            }
        }

        fun getInstance(context: Context): ContextiumDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ContextiumDatabase::class.java,
                    "contextium.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build().also { instance = it }
            }
        }
    }
}