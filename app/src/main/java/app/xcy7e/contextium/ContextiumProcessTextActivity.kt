package app.xcy7e.contextium

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class ContextiumProcessTextActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedText = intent
            .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
            ?.toString()
            ?.trim()
            .orEmpty()

        if (selectedText.isNotEmpty()) {
            startActivity(
                Intent(this, ContextMenuItemPickerActivity::class.java)
                    .putExtra(ContextMenuItemPickerActivity.EXTRA_SELECTED_TEXT, selectedText)
            )
        }

        finish()
    }
}