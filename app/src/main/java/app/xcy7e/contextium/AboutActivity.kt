package app.xcy7e.contextium

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import androidx.core.net.toUri

class AboutActivity : ComponentActivity() {

    companion object {
        private const val GITHUB_REPO_URL = "https://github.com/xcy7e/Contextium"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val poiretOneBold = Typeface.create(
            resources.getFont(R.font.poiret_one_regular),
            Typeface.BOLD
        )

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val logo = ImageView(this).apply {
            setImageResource(R.mipmap.ic_launcher_foreground)
            contentDescription = getString(R.string.app_name)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }

        val heading = TextView(this).apply {
            text = getString(R.string.app_name)
            textSize = 40f
            gravity = Gravity.CENTER
        }
        heading.setTypeface(poiretOneBold)

        val description = TextView(this).apply {
            text = getString(R.string.about_description)
            textSize = 14f
            gravity = Gravity.FILL_HORIZONTAL
            setLineSpacing(dp(5).toFloat(), 1f)
        }

        val authorText = getString(R.string.about_author)
        val linkText = getString(R.string.about_author_link_text)
        val linkUrl = getString(R.string.about_author_link)

        val author = TextView(this).apply {
            textSize = 14f
            gravity = Gravity.CENTER
            highlightColor = Color.TRANSPARENT
            linksClickable = true
            setLinkTextColor(0xFFB394F7.toInt())

            text = SpannableString(authorText).apply {
                val start = authorText.indexOf(linkText)
                val end = start + linkText.length

                if (start >= 0) {
                    setSpan(
                        object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                widget.context.startActivity(
                                    Intent(Intent.ACTION_VIEW, linkUrl.toUri())
                                )
                            }

                            override fun updateDrawState(textPaint: TextPaint) {
                                super.updateDrawState(textPaint)
                                textPaint.isUnderlineText = false
                                textPaint.color = 0xFFB394F7.toInt()
                            }
                        },
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }

            movementMethod = LinkMovementMethod.getInstance()
        }

        val version = TextView(this).apply {
            text = getString(R.string.about_version, BuildConfig.VERSION_NAME)
            textSize = 14f
            gravity = Gravity.CENTER
        }

        val githubButton = MaterialButton(this).apply {
            text = getString(R.string.about_github)
            textSize = 18f
            cornerRadius = dp(28)

            setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        GITHUB_REPO_URL.toUri()
                    )
                )
            }
        }

        content.addView(
            logo,
            LinearLayout.LayoutParams(dp(120), dp(120)).apply {
                bottomMargin = dp(12)
            }
        )

        content.addView(
            heading,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(0)
            }
        )

        content.addView(
            version,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(32)
                topMargin = dp(0)
            }
        )

        content.addView(
            description,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(28)
            }
        )

        content.addView(
            author,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(32)
                bottomMargin = dp(24)
            }
        )

        content.addView(
            githubButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(56)
            )
        )

        val scrollView = ScrollView(this).apply {
            addView(content)
        }

        setContentView(scrollView)

        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )

            view.setPadding(
                dp(24) + bars.left,
                dp(32) + bars.top,
                dp(24) + bars.right,
                dp(32) + bars.bottom
            )

            insets
        }

        ViewCompat.requestApplyInsets(scrollView)
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}