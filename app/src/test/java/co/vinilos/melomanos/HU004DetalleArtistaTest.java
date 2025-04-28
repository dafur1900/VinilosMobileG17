package co.vinilos.melomanos;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HU004DetalleArtistaTest {

    // Se debe Reemplazar con la Activity de lista de artistas
    @Rule
    public ActivityScenarioRule<ListaArtistasActivity> mActivityTestRule = new ActivityScenarioRule<>(ListaArtistasActivity.class);

    @Test
    public void alSeleccionarUnArtistaSeNavegaAlDetalle() {
        // Simula el clic en el primer elemento de la lista de artistas
        onView(withId(R.id.artist_list)) // Se debe reemplazar con el ID real artistas
                .perform(actionOnItemAtPosition(0, click()));

        // Dependiendo del código de la activity presumo dos posibles formas de verificar la navegación:

        // 1. Si la Activity de detalle tiene un título específico:
        // onView(withText("Nombre del Artista Esperado")).check(matches(isDisplayed()));
        // (Se debe reemplazar "Nombre del Artista Esperado" con el título que debería aparecer en la pantalla de detalle)

        // 2. Si la Activity de detalle tiene un ID de vista único:
        // onView(withId(R.id.detalle_artista_container)).check(matches(isDisplayed()));
        // (Se debe reemplazar "detalle_artista_container" con el ID de un contenedor o vista única de la pantalla de detalle del artista)

    }

}
