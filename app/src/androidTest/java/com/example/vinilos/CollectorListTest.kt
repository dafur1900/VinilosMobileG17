package com.example.vinilos


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.vinilos.ui.views.HomeActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CollectorListTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun collectorListTest() {
        // Esperar a que la aplicación cargue inicialmente
        Thread.sleep(1000)

        // Clic en botón de visitante
        onView(withId(R.id.visitorButton)).perform(click())

        // Esperar carga inicial y navegar a coleccionistas
        Thread.sleep(1000)

        // Navegar a la vista de coleccionistas
        onView(withId(R.id.collectors)).perform(click())

        // Esperar a que se carguen los coleccionistas
        Thread.sleep(2000)

        // Verificar que el título es correcto
        onView(withId(R.id.tvTitle))
            .check(matches(withText("Coleccionistas")))

        // Verificar que el RecyclerView está visible
        onView(withId(R.id.recyclerViewCollectors))
            .check(matches(isDisplayed()))

        // Verificar que el ProgressBar está oculto
        onView(withId(R.id.progressBarCollectors))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        // Verificar que el mensaje de error está oculto
        onView(withId(R.id.textViewCollectorsError))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
