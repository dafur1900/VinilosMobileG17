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
public class HU02DetalleAlbumTest {

    @Rule
    public ActivityScenarioRule<ListaAlbumesActivity> mActivityTestRule = new ActivityScenarioRule<>(ListaAlbumesActivity.class); // Debo Reemplazar con la Activity de lista de álbumes

    @Test
    public void alSeleccionarUnAlbumSeNavegaAlDetalle() {
        // Asumiendo que tu RecyclerView de álbumes tiene el ID "album_list"
        // y que al hacer clic en un item, se inicia una nueva Activity o se muestra un Fragment.

        // Simula el clic en el primer elemento de la lista de álbumes
        onView(withId(R.id.album_list)) // Se debe reemplazar con el ID real de la RecyclerView
                .perform(actionOnItemAtPosition(0, click()));
        /*A la espera de ver el código de album_list se puede obtener la aserciones de las siguientes maneras*/

        // Si se inicia una nueva Activity con un título específico:
        // onView(withText("Título del Álbum Esperado")).check(matches(isDisplayed()));
        // (Se debe reemplazar "Título del Álbum Esperado" con el título que debería aparecer en la pantalla de detalle)

        // Si la nueva Activity tiene un ID de vista único:
        // onView(withId(R.id.detalle_album_container)).check(matches(isDisplayed()));
        // (Se debe reemplazar "detalle_album_container" con el ID de un contenedor)

    }
}
