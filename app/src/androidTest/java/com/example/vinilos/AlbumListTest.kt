package co.vinilos.melomanos

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import co.vinilos.melomanos.ui.views.HomeActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AlbumListTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun shouldDisplayAlbumListAfterVisitorLogin() {
        tapVisitorButton()

        onView(allOf(withId(R.id.tvTitle), withText("Álbumes")))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayAlbumSearchResultsWhenFound() {
        tapVisitorButton()
        enterSearchQuery("A Day")

        onView(withId(R.id.albumRv))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowNoResultsMessageWhenSearchFails() {
        tapVisitorButton()
        enterSearchQuery("white")

        onView(allOf(
            withId(R.id.tvNoResults),
            withText("No se encontraron álbumes que coincidan con tu búsqueda")
        )).check(matches(isDisplayed()))
    }

    // -------------------------------
    // Helpers (evitan repetición)
    // -------------------------------

    private fun tapVisitorButton() {
        onView(allOf(
            withId(R.id.visitorButton),
            withText("Soy visitante"),
            isDisplayed()
        )).perform(click())
    }

    private fun enterSearchQuery(query: String) {
        onView(withId(R.id.searchBar))
            .perform(replaceText(query), closeSoftKeyboard())
    }

    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
