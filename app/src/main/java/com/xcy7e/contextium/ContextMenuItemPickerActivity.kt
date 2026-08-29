package app.xcy7e.contextium

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.Executors
import androidx.core.net.toUri
import androidx.core.graphics.drawable.toDrawable

class ContextMenuItemPickerActivity : Activity() {

    companion object {
        const val EXTRA_SELECTED_TEXT = "selected_text"
    }

    private val databaseExecutor = Executors.newSingleThreadExecutor()

    private val dao by lazy {
        ContextiumDatabase.getInstance(applicationContext).contextMenuItemDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedText = intent
            .getStringExtra(EXTRA_SELECTED_TEXT)
            ?.trim()
            .orEmpty()

        if (selectedText.isEmpty()) {
            finish()
            return
        }

        loadItems(selectedText)
    }

    private fun loadItems(selectedText: String) {
        databaseExecutor.execute {
            val items = dao.getAllEnabled()

            runOnUiThread {
                showContent(items, selectedText)
            }
        }
    }

    private fun showContent(items: List<ContextMenuItem>, selectedText: String) {
        val root = FrameLayout(this)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val heading = TextView(this).apply {
            text = getString(R.string.app_name)
            textSize = 28f
            gravity = Gravity.CENTER
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }

        content.addView(
            heading,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(64)
            ).apply {
                topMargin = dp(32)
                bottomMargin = dp(32)
            }
        )

        if (items.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = getString(R.string.empty_active_item_list)
                textSize = 17f
                gravity = Gravity.CENTER
            }

            content.addView(
                emptyText,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
            )
        } else {
            val listView = ListView(this).apply {
                divider = 0xAA333333.toInt().toDrawable()
                dividerHeight = dp(1)
            }
            val topBorder = View(this).apply {
                setBackgroundColor(0xAA333333.toInt())
            }

            listView.addHeaderView(
                topBorder.apply {
                    layoutParams = AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT,
                        dp(1)
                    )
                },
                null,
                false
            )

            listView.adapter = object : ArrayAdapter<ContextMenuItem>(
                this,
                android.R.layout.simple_list_item_1,
                items
            ) {
                override fun getView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val textView = (convertView as? TextView)
                        ?: TextView(context)

                    textView.text = getItem(position)?.label.orEmpty()
                    textView.textSize = 17f
                    textView.setTextColor(Color.rgb(232, 225, 234))
                    textView.setBackgroundColor(Color.rgb(28, 27, 31))
                    textView.gravity = Gravity.CENTER_VERTICAL
                    textView.setPadding(dp(18), dp(16), dp(18), dp(16))

                    textView.layoutParams = AbsListView.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT,
                        AbsListView.LayoutParams.WRAP_CONTENT
                    )

                    return textView
                }
            }

            listView.setOnItemClickListener { _, _, position, _ ->
                val itemPosition = position - listView.headerViewsCount

                if (itemPosition in items.indices) {
                    openItem(items[itemPosition], selectedText)
                }
            }

            content.addView(
                listView,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1f
                )
            )
        }

        root.addView(
            content,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            content.setPadding(
                dp(20),
                bars.top + dp(16),
                dp(20),
                bars.bottom + dp(16)
            )

            insets
        }

        setContentView(root)
        ViewCompat.requestApplyInsets(root)
    }

    private fun openItem(item: ContextMenuItem, selectedText: String) {
        val targetUrl = item.url.toUri()
            .buildUpon()
            .appendQueryParameter(item.urlParam, selectedText)
            .build()

        startActivity(Intent(Intent.ACTION_VIEW, targetUrl))
        finish()
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}