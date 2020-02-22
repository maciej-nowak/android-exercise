package pl.maciejnowak.exercise.matcher

import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.view.WindowManager.LayoutParams.TYPE_TOAST
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class ToastMatcher(private val maxFailures: Int = DEFAULT_MAX_FAILURES) : TypeSafeMatcher<Root>() {

    private var failures = 0

    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    public override fun matchesSafely(root: Root): Boolean {
        val type = root.windowLayoutParams.get().type
        @Suppress("DEPRECATION")
        if (type == TYPE_TOAST || type == TYPE_APPLICATION_OVERLAY) {
            val windowToken = root.decorView.windowToken
            val appToken = root.decorView.applicationWindowToken
            if (windowToken === appToken) {
                return true
            }
        }
        return (++failures >= maxFailures)
    }

    companion object {
        private const val DEFAULT_MAX_FAILURES = 5
        fun onToast(text: String, maxFailures: Int = DEFAULT_MAX_FAILURES): ViewInteraction =
            onView(withText(text)).inRoot(isToast(maxFailures))
        fun onToast(textId: Int, maxFailures: Int = DEFAULT_MAX_FAILURES): ViewInteraction =
            onView(withText(textId)).inRoot(isToast(maxFailures))
        fun isToast(maxFailures: Int = DEFAULT_MAX_FAILURES): Matcher<Root> {
            return ToastMatcher(maxFailures)
        }
    }
}