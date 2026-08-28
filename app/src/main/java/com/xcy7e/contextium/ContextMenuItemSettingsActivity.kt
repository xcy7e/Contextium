package com.xcy7e.contextium

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.Executors

class ContextMenuItemSettingsActivity : ComponentActivity() {

    companion object {
        const val EXTRA_ITEM_ID = "context_menu_item_id"
    }

    private val databaseExecutor = Executors.newSingleThreadExecutor()

    private val dao by lazy {
        ContextiumDatabase.getInstance(applicationContext).contextMenuItemDao()
    }

    private var itemId: Long = 0
    private var existingItem: ContextMenuItem? = null

    private lateinit var titleInput: TextInputEditText
    private lateinit var labelInput: TextInputEditText
    private lateinit var urlInput: TextInputEditText
    private lateinit var urlParamInput: TextInputEditText
    private lateinit var enabledInput: MaterialCheckBox
    private lateinit var deleteButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemId = intent.getLongExtra(EXTRA_ITEM_ID, 0)

        buildUi()

        if (itemId > 0) {
            title = getString(R.string.settings_edit_title)
            loadItem()
        } else {
            title = getString(R.string.settings_new_title)
        }
    }

    private fun buildUi() {
        val form = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        val montserratBold = Typeface.create(
            resources.getFont(R.font.montserrat_variable),
            Typeface.BOLD
        )
        val montserratNormal = Typeface.create(
            resources.getFont(R.font.montserrat_variable),
            Typeface.NORMAL
        )

        titleInput = addInput(
            form,
            R.string.field_title,
            R.string.field_title_helper
        )
        titleInput.setTypeface(montserratBold)

        labelInput = addInput(
            form,
            R.string.field_label,
            R.string.field_label_helper
        )
        labelInput.setTypeface(montserratBold)

        urlInput = addInput(
            form,
            R.string.field_url,
            R.string.field_url_helper,
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI
        )
        urlInput.setTypeface(montserratBold)

        urlParamInput = addInput(
            form,
            R.string.field_url_param,
            R.string.field_url_param_helper
        )
        urlParamInput.setTypeface(montserratBold)

        enabledInput = MaterialCheckBox(this).apply {
            text = getString(R.string.field_enabled)
            isChecked = true
            setPadding(0, dp(17), 0, dp(16))
        }
        enabledInput.setTypeface(montserratBold)

        val saveButton = MaterialButton(
            this,
            null,
            com.google.android.material.R.attr.materialButtonStyle
        ).apply {
            text = getString(R.string.action_save)
            textSize = 16f
            typeface = montserratBold
            cornerRadius = dp(28)
            backgroundTintList = ColorStateList.valueOf(0xFF7652C8.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener { saveItem() }
        }

        deleteButton = MaterialButton(
            this,
            null,
            com.google.android.material.R.attr.materialButtonStyle
        ).apply {
            text = getString(R.string.action_delete)
            textSize = 16f
            typeface = montserratBold
            cornerRadius = dp(28)
            backgroundTintList = ColorStateList.valueOf(0xFF353238.toInt())
            setTextColor(0xFFE8E1EA.toInt())
            visibility = View.GONE
            setOnClickListener { deleteItem() }
        }

        form.addView(enabledInput)

        form.addView(
            saveButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(56)
            ).apply {
                topMargin = dp(32)
                bottomMargin = dp(12)
            }
        )

        form.addView(
            deleteButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(56)
            )
        )

        val scrollView = ScrollView(this).apply {
            addView(
                form,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            scrollView.setPadding(
                dp(24),
                bars.top + dp(32),
                dp(24),
                bars.bottom + dp(32)
            )

            insets
        }

        setContentView(scrollView)
    }

    private fun addInput(
        form: LinearLayout,
        labelRes: Int,
        helperRes: Int,
        inputType: Int = InputType.TYPE_CLASS_TEXT
    ): TextInputEditText {
        val wrapper = TextInputLayout(this).apply {
            hint = getString(labelRes)
            helperText = getString(helperRes)
            isHintEnabled = true

            hintTextColor = ContextCompat.getColorStateList(
                this@ContextMenuItemSettingsActivity,
                R.color.text_input_hint_color
            )
        }

        val input = TextInputEditText(wrapper.context).apply {
            this.inputType = inputType
            setSingleLine(true)
        }

        wrapper.addView(
            input,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        form.addView(
            wrapper,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(12)
            }
        )

        return input
    }

    private fun loadItem() {
        databaseExecutor.execute {
            val item = dao.getById(itemId)

            runOnUiThread {
                if (item == null) {
                    finish()
                    return@runOnUiThread
                }

                existingItem = item
                titleInput.setText(item.title)
                labelInput.setText(item.label)
                urlInput.setText(item.url)
                urlParamInput.setText(item.urlParam)
                enabledInput.isChecked = item.enabled
                deleteButton.visibility = View.VISIBLE
            }
        }
    }

    private fun saveItem() {
        val itemTitle = titleInput.text?.toString()?.trim().orEmpty()
        val label = labelInput.text?.toString()?.trim().orEmpty()
        val url = urlInput.text?.toString()?.trim().orEmpty()
        val urlParam = urlParamInput.text?.toString()?.trim().orEmpty()

        if (itemTitle.isEmpty() || label.isEmpty() || url.isEmpty() || urlParam.isEmpty()) {
            Toast.makeText(this, R.string.validation_error, Toast.LENGTH_LONG).show()
            return
        }

        databaseExecutor.execute {
            try {
                val now = System.currentTimeMillis()
                val oldItem = existingItem

                if (oldItem == null) {
                    dao.insert(
                        ContextMenuItem(
                            title = itemTitle,
                            label = label,
                            url = url,
                            urlParam = urlParam,
                            enabled = enabledInput.isChecked,
                            sortOrder = dao.getMaxSortOrder() + 1,
                            createdAt = now,
                            updatedAt = now
                        )
                    )
                } else {
                    dao.update(
                        oldItem.copy(
                            title = itemTitle,
                            label = label,
                            url = url,
                            urlParam = urlParam,
                            enabled = enabledInput.isChecked,
                            updatedAt = now
                        )
                    )
                }

                runOnUiThread {
                    Toast.makeText(this, R.string.item_saved, Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (_: Exception) {
                runOnUiThread {
                    Toast.makeText(this, R.string.duplicate_error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deleteItem() {
        val item = existingItem ?: return

        databaseExecutor.execute {
            dao.delete(item)

            runOnUiThread {
                finish()
            }
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}