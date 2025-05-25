package com.example.vinilos


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
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
class CollectorDetailTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun collectorDetailTest() {
        // Esperar a que la aplicación cargue inicialmente
        Thread.sleep(1000)

        // Clic en botón de visitante
        onView(withId(R.id.visitorButton)).perform(click())

        // Esperar carga inicial y navegar a coleccionistas
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.collectors),
                withContentDescription("Coleccionistas"),
                isDisplayed()
            )
        ).perform(click())

        // Esperar a que se carguen los coleccionistas
        Thread.sleep(2000)

        // Hacer clic en el primer coleccionista
        onView(withId(R.id.recyclerViewCollectors))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        // Esperar a que se cargue el detalle
        Thread.sleep(1000)

        // Verificar que estamos en la vista de detalles
        onView(withId(R.id.tvCollectorDetailHeaderTitle))
            .check(matches(withText("Coleccionistas")))

        // Verificar que se muestra la información del coleccionista
        onView(withId(R.id.tvEmailCollector)).check(matches(isDisplayed()))
        onView(withId(R.id.tvPhoneCollector)).check(matches(isDisplayed()))
        onView(withId(R.id.tvArtistsFav)).check(matches(withText("Artistas favoritos")))
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
