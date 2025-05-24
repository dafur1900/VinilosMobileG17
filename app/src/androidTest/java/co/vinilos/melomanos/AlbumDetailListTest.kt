package com.example.vinilos


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.vinilos.ui.views.HomeActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource

@LargeTest
@RunWith(AndroidJUnit4::class)
class AlbumDetailListTest {

    private val idlingResource = CountingIdlingResource("DataLoader")

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    fun albumDetailActivityTest() {
        // Simular carga de datos con idlingResource y esperar explícitamente
        idlingResource.increment()

        // Agregar un delay para asegurar que la actividad se inicializa completamente
        try {
            Thread.sleep(2000) // Dar 2 segundos para que todo se inicialice
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        idlingResource.decrement()

        // Hacer clic en el botón "Soy visitante"
        val appCompatButton = onView(
            allOf(
                withId(R.id.visitorButton), withText("Soy visitante"),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        // Esperar a que se cargue la lista de álbumes
        try {
            Thread.sleep(2000) // Dar 2 segundos para que la lista se cargue
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // Buscar el RecyclerView por su ID directamente, sin depender de la posición
        val recyclerView = onView(withId(R.id.albumRv))

        // Verificar que el RecyclerView está visible antes de interactuar con él
        recyclerView.check(matches(isDisplayed()))

        // Hacer clic en el primer elemento de la lista
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        // Verificar que estamos en la pantalla de detalles del álbum
        val textView = onView(allOf(withId(R.id.tvAlbumDetailTitle), isDisplayed()))
        textView.check(matches(withText("A Day at the Races")))

        // Las siguientes verificaciones pueden permanecer iguales
        val textView2 = onView(
            allOf(
                withId(R.id.tvArtistName), withText("Queen"),
                withParent(withParent(withId(R.id.rvArtistList))),
                isDisplayed()
            )
        )
        textView2.check(matches(isDisplayed()))

        val textView3 = onView(
            allOf(
                withId(R.id.tvAlbumDetailDescription),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(isDisplayed()))

        val textView4 = onView(
            allOf(
                withId(R.id.tvComments), withText("Comentarios"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Comentarios")))
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
