package com.xcy7e.contextium

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.Executors
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.app.AlertDialog
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter

class ContextMenuItemManagerActivity : ComponentActivity() {

    private val databaseExecutor = Executors.newSingleThreadExecutor()

    private val dao by lazy {
        ContextiumDatabase.getInstance(applicationContext).contextMenuItemDao()
    }

    private lateinit var rootView: FrameLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var adapter: ContextMenuItemAdapter
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        drawerLayout = DrawerLayout(this)

        rootView = FrameLayout(this)

        drawerLayout.addView(
            rootView,
            DrawerLayout.LayoutParams(
                DrawerLayout.LayoutParams.MATCH_PARENT,
                DrawerLayout.LayoutParams.MATCH_PARENT
            )
        )

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val poiretOneBold = Typeface.create(
            resources.getFont(R.font.poiret_one_regular),
            Typeface.BOLD
        )

        val header = FrameLayout(this)
        val headerContent = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val menuButton = TextView(this).apply {
            text = "☰"
            textSize = 30f
            gravity = Gravity.CENTER
            contentDescription = getString(R.string.action_menu)
            isClickable = true
            isFocusable = true

            setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val logo = ImageView(this).apply {
            setImageResource(R.mipmap.ic_launcher_foreground)
            contentDescription = getString(R.string.about_title)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            isClickable = true
            isFocusable = true

            setOnClickListener {
                startActivity(
                    Intent(
                        this@ContextMenuItemManagerActivity,
                        AboutActivity::class.java
                    )
                )
            }
        }

        val heading = TextView(this).apply {
            text = getString(R.string.app_name)
            textSize = 36f
            gravity = Gravity.CENTER
            typeface = Typeface.create(typeface, Typeface.BOLD)
        }
        heading.setTypeface(poiretOneBold)

        headerContent.addView(
            logo,
            LinearLayout.LayoutParams(dp(72), dp(72))
        )

        headerContent.addView(
            heading,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(64)
            )
        )

        header.addView(
            headerContent,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        header.addView(
            menuButton,
            FrameLayout.LayoutParams(dp(56), dp(56), Gravity.START or Gravity.TOP)
        )

        recyclerView = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@ContextMenuItemManagerActivity)
            clipToPadding = true
            clipChildren = true
            setBackgroundColor(Color.TRANSPARENT)
        }

        emptyText = TextView(this).apply {
            text = getString(R.string.empty_item_list)
            textSize = 17f
            gravity = Gravity.CENTER
            visibility = View.GONE
        }

        val addButton = FloatingActionButton(this).apply {
            contentDescription = getString(R.string.action_add)
            setImageResource(android.R.drawable.ic_input_add)

            backgroundTintMode = PorterDuff.Mode.SRC_IN
            backgroundTintList = ColorStateList.valueOf(0xFF7652C8.toInt())
            imageTintList = ColorStateList.valueOf(0xFFFFFFFF.toInt())

            compatElevation = 0f
            compatPressedTranslationZ = 0f

            setOnClickListener {
                startActivity(
                    Intent(
                        this@ContextMenuItemManagerActivity,
                        ContextMenuItemSettingsActivity::class.java
                    )
                )
            }
        }

        content.addView(
            header,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(152)
            )
        )

        content.addView(
            recyclerView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        rootView.addView(
            content,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        rootView.addView(
            emptyText,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        val fabParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.END or Gravity.BOTTOM
        )

        rootView.addView(addButton, fabParams)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )
            val sideMargin = dp(20)

            content.setPadding(
                sideMargin + bars.left,
                bars.top,
                sideMargin + bars.right,
                bars.bottom
            )

            headerContent.setPadding(
                0,
                dp(24),
                0,
                0
            )

            menuButton.setPadding(
                0,
                dp(16),
                0,
                0
            )

            recyclerView.setPadding(
                0,
                dp(12),
                0,
                dp(112)
            )

            emptyText.setPadding(
                sideMargin + bars.left,
                bars.top + dp(152),
                sideMargin + bars.right,
                bars.bottom
            )

            fabParams.setMargins(
                0,
                0,
                sideMargin + dp(4),
                bars.bottom + dp(28)
            )

            addButton.layoutParams = fabParams
            insets
        }

        adapter = ContextMenuItemAdapter { item ->
            startActivity(
                Intent(
                    this,
                    ContextMenuItemSettingsActivity::class.java
                ).putExtra(ContextMenuItemSettingsActivity.EXTRA_ITEM_ID, item.id)
            )
        }

        recyclerView.adapter = adapter
        attachItemTouchHelper()

        addNavigationDrawer()
        setContentView(drawerLayout)
        ViewCompat.requestApplyInsets(rootView)
    }

    override fun onResume() {
        super.onResume()
        loadItems()
    }

    private val exportLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            exportItems(uri)
        }
    }

    private val importLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            confirmImport(uri)
        }
    }

    private fun addNavigationDrawer() {
        val poiretOneBold = Typeface.create(
            resources.getFont(R.font.poiret_one_regular),
            Typeface.BOLD
        )

        val drawerContent = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(30, 28, 33))
            setPadding(dp(24), dp(48), dp(24), dp(24))
        }

        val drawerTitle = TextView(this).apply {
            text = getString(R.string.app_name)
            textSize = 26f
            setTypeface(poiretOneBold, Typeface.BOLD)
            setPadding(0, 0, 0, dp(24))
        }

        val exportButton = drawerButton(getString(R.string.action_export)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            exportLauncher.launch(getString(R.string.export_file_name))
        }

        val importButton = drawerButton(getString(R.string.action_import)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            importLauncher.launch(
                arrayOf("application/json", "text/*")
            )
        }

        drawerContent.addView(drawerTitle)
        drawerContent.addView(exportButton)
        drawerContent.addView(importButton)

        drawerLayout.addView(
            drawerContent,
            DrawerLayout.LayoutParams(
                dp(260),
                DrawerLayout.LayoutParams.MATCH_PARENT,
                GravityCompat.START
            )
        )
    }

    private fun drawerButton(
        text: String,
        action: () -> Unit
    ): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 18f
            gravity = Gravity.CENTER_VERTICAL
            isClickable = true
            isFocusable = true
            setPadding(dp(16), 0, dp(16), 0)
            setOnClickListener { action() }

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(56)
            )
        }
    }

    private fun attachItemTouchHelper() {
        val deleteBackground = ColorDrawable(Color.rgb(183, 28, 28))
        val deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!

        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    source: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    adapter.move(
                        source.bindingAdapterPosition,
                        target.bindingAdapterPosition
                    )
                    return true
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ) {
                    super.clearView(recyclerView, viewHolder)
                    saveSortOrder()
                }

                override fun onChildDraw(
                    canvas: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        drawDeleteBackground(
                            canvas,
                            viewHolder.itemView,
                            dX,
                            deleteBackground,
                            deleteIcon
                        )
                    }

                    super.onChildDraw(
                        canvas,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val deletedItem = adapter.removeAt(viewHolder.bindingAdapterPosition)

                    databaseExecutor.execute {
                        dao.delete(deletedItem)
                    }

                    val snackbar = Snackbar.make(
                        rootView,
                        R.string.item_deleted,
                        Snackbar.LENGTH_LONG
                    )

                    snackbar.setBackgroundTint(Color.WHITE)
                    snackbar.setTextColor(Color.BLACK)
                    snackbar.setActionTextColor(0xFF190940.toInt())

                    snackbar.setAction(R.string.action_undo) {
                        databaseExecutor.execute {
                            dao.insert(deletedItem)
                            loadItems()
                        }
                    }

                    snackbar.show()
                }
            }
        ).attachToRecyclerView(recyclerView)
    }

    private fun drawDeleteBackground(
        canvas: Canvas,
        itemView: View,
        dX: Float,
        background: ColorDrawable,
        icon: Drawable
    ) {
        if (dX == 0f) return

        if (dX > 0) {
            background.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom
            )
        } else {
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
        }

        background.draw(canvas)

        val iconSize = dp(28)
        val iconTop = itemView.top + (itemView.height - iconSize) / 2
        val margin = dp(24)

        val iconLeft = if (dX > 0) {
            itemView.left + margin
        } else {
            itemView.right - margin - iconSize
        }

        icon.setBounds(
            iconLeft,
            iconTop,
            iconLeft + iconSize,
            iconTop + iconSize
        )

        icon.draw(canvas)
    }

    private fun loadItems() {
        databaseExecutor.execute {
            val items = dao.getAll()

            runOnUiThread {
                adapter.submitItems(items)
                emptyText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun saveSortOrder() {
        val items = adapter.getItems()

        databaseExecutor.execute {
            val now = System.currentTimeMillis()

            items.forEachIndexed { index, item ->
                dao.updateSortOrder(item.id, index, now)
            }
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    private fun exportItems(uri: Uri) {
        databaseExecutor.execute {
            try {
                val jsonItems = JSONArray()

                dao.getAll().forEach { item ->
                    jsonItems.put(
                        JSONObject().apply {
                            put("title", item.title)
                            put("label", item.label)
                            put("url", item.url)
                            put("urlParam", item.urlParam)
                            put("enabled", item.enabled)
                            put("sortOrder", item.sortOrder)
                        }
                    )
                }

                val root = JSONObject().apply {
                    put("format", "contextium-backup")
                    put("version", 1)
                    put("items", jsonItems)
                }

                contentResolver.openOutputStream(uri)?.use { output ->
                    OutputStreamWriter(output, Charsets.UTF_8).use { writer ->
                        writer.write(root.toString(2))
                    }
                } ?: error("Ausgabedatei kann nicht geöffnet werden.")

                runOnUiThread {
                    Toast.makeText(this, R.string.export_success, Toast.LENGTH_SHORT).show()
                }
            } catch (_: Exception) {
                runOnUiThread {
                    Toast.makeText(this, R.string.export_error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun confirmImport(uri: Uri) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.import_confirm_title)
            .setMessage(R.string.import_confirm_message)
            .setNegativeButton(R.string.action_cancel, null)
            .setPositiveButton(R.string.action_replace) { _, _ ->
                importItems(uri)
            }
            .create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.WHITE)

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(0xFFB598FA.toInt())
        }

        dialog.show()
    }

    private fun importItems(uri: Uri) {
        databaseExecutor.execute {
            try {
                val jsonText = contentResolver.openInputStream(uri)?.use { input ->
                    BufferedReader(input.reader()).use { reader ->
                        reader.readText()
                    }
                } ?: error("Eingabedatei kann nicht geöffnet werden.")

                val backup = JSONObject(jsonText)

                if (
                    backup.optString("format") != "contextium-backup" ||
                    backup.optInt("version") != 1
                ) {
                    error("Unbekanntes Backup-Format.")
                }

                val jsonItems = backup.getJSONArray("items")
                val importedItems = mutableListOf<ContextMenuItem>()
                val now = System.currentTimeMillis()

                for (index in 0 until jsonItems.length()) {
                    val jsonItem = jsonItems.getJSONObject(index)

                    val title = jsonItem.getString("title").trim()
                    val label = jsonItem.getString("label").trim()
                    val url = jsonItem.getString("url").trim()
                    val urlParam = jsonItem.getString("urlParam").trim()

                    if (
                        title.isEmpty() ||
                        label.isEmpty() ||
                        url.isEmpty() ||
                        urlParam.isEmpty()
                    ) {
                        error("Ungültiger Eintrag.")
                    }

                    importedItems += ContextMenuItem(
                        title = title,
                        label = label,
                        url = url,
                        urlParam = urlParam,
                        enabled = jsonItem.optBoolean("enabled", true),
                        sortOrder = index,
                        createdAt = now,
                        updatedAt = now
                    )
                }

                dao.deleteAll()
                importedItems.forEach { dao.insert(it) }

                runOnUiThread {
                    loadItems()

                    Toast.makeText(
                        this,
                        getString(R.string.import_success, importedItems.size),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (_: Exception) {
                runOnUiThread {
                    Toast.makeText(this, R.string.import_error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}

private class ContextMenuItemAdapter(
    private val onClick: (ContextMenuItem) -> Unit
) : RecyclerView.Adapter<ContextMenuItemAdapter.ViewHolder>() {

    private val items = mutableListOf<ContextMenuItem>()

    fun submitItems(newItems: List<ContextMenuItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun getItems(): List<ContextMenuItem> = items.toList()

    fun move(from: Int, to: Int) {
        val item = items.removeAt(from)
        items.add(to, item)
        notifyItemMoved(from, to)
    }

    fun removeAt(position: Int): ContextMenuItem {
        val item = items.removeAt(position)
        notifyItemRemoved(position)
        return item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val padding = dp(context, 18)

        val root = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(padding, padding, padding, padding)
            setBackgroundColor(Color.rgb(28, 27, 31))

            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 1
            }
        }

        val title = TextView(context).apply {
            textSize = 20f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(Color.rgb(232, 225, 234))
        }

        val label = TextView(context).apply {
            textSize = 15f
            setTextColor(Color.rgb(205, 194, 212))
        }

        val url = TextView(context).apply {
            textSize = 14f
            setTextColor(Color.rgb(170, 165, 176))
            setPadding(0, dp(context, 3), 0, 0)
        }

        root.addView(title)
        root.addView(label)
        root.addView(url)

        return ViewHolder(root, title, label, url)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.title
        holder.label.text = holder.itemView.context.getString(
            R.string.item_label,
            item.label
        )
        holder.url.text = shortenUrl(item.url)

        holder.itemView.alpha = if (item.enabled) 1.0f else 0.40f
        holder.itemView.setOnClickListener { onClick(item) }
    }

    private fun shortenUrl(url: String): String {
        val trimmedUrl = url.trim()
        val schemeEnd = trimmedUrl.indexOf("://")

        if (schemeEnd == -1) {
            return trimmedUrl
        }

        val authorityStart = schemeEnd + 3
        val pathStart = trimmedUrl.indexOf('/', authorityStart)
        val queryStart = trimmedUrl.indexOf('?', authorityStart)
        val fragmentStart = trimmedUrl.indexOf('#', authorityStart)

        val firstSuffixStart = listOf(pathStart, queryStart, fragmentStart)
            .filter { it >= 0 }
            .minOrNull()

        return when {
            // Nur Schema + Domain/TLD.
            firstSuffixStart == null -> trimmedUrl

            // URL endet direkt nach dem Slash, beispielsweise https://example.org/
            firstSuffixStart == trimmedUrl.lastIndex &&
                    trimmedUrl[firstSuffixStart] == '/' -> trimmedUrl

            // Alles nach Domain/TLD abschneiden.
            trimmedUrl[firstSuffixStart] == '/' ->
                trimmedUrl.substring(0, firstSuffixStart + 1) + "..."

            // Query oder Fragment direkt nach der TLD.
            else ->
                trimmedUrl.substring(0, firstSuffixStart) + "..."
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        view: View,
        val title: TextView,
        val label: TextView,
        val url: TextView
    ) : RecyclerView.ViewHolder(view)

    private fun dp(context: android.content.Context, value: Int): Int {
        return (value * context.resources.displayMetrics.density).toInt()
    }
}