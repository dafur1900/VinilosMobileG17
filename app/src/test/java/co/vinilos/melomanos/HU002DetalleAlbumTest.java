package co.vinilos.melomanos;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class HU002DetalleAlbumTest {

    @Rule
    public ActivityScenarioRule<ListaAlbumesActivity> mActivityTestRule = new ActivityScenarioRule<>(ListaAlbumesActivity.class); // Debo Reemplazar con la Activity de lista de álbumes

    @Test
    public void alSeleccionarUnAlbumSeNavegaAlDetalle() {

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

    @Test
    public void alSeleccionarUnAlbumSeMuestraSuInformacionDetallada() {

        // Datos del primer álbum (se debe reemplazar con datos reales)
        String primerAlbumNombre = "Nombre del Álbum";
        String primerArtistaNombre = "Nombre del Artista";
        String primerFechaLanzamiento = "2023";


        // Simula el clic en el primer elemento de la lista de álbumes
        onView(withId(R.id.album_list)) // Se debe reemplazar con el ID determinado por mi compañero en el RecyclerView
                .perform(actionOnItemAtPosition(0, click()));

        // Aserciones para verificar que la información detallada se muestra
        // Asumimos que en la pantalla de detalle hay TextViews con los siguientes IDs:
        // - detail_album_name
        // - detail_artist_name
        // - detail_release_date
        // - detail_song_count

        onView(withId(R.id.detail_album_name)) // Se debe reemplazar con el ID real del TextView del nombre del álbum en el detalle
                .check(matches(withText(primerAlbumNombre)));

        onView(withId(R.id.detail_artist_name)) // Se debe reemplazar con el ID real del TextView del nombre del artista en el detalle
                .check(matches(withText(primerArtistaNombre)));

        onView(withId(R.id.detail_release_date)) // Se debe reemplazar con el ID real del TextView de la fecha de lanzamiento en el detalle
                .check(matches(withText(primerFechaLanzamiento)));


    }

    @Test
    public void alSeleccionarUnAlbumSeMuestraLaListaDeCanciones() {

        // Datos del primer álbum y su lista de canciones esperada (reemplaza con datos reales)
        String primerAlbumNombre = "Nombre del Álbum 1";
        List<String> primerasCanciones = Arrays.asList("Canción 1", "Canción 2", "Canción 3");

        // Simula el clic en el primer elemento de la lista de álbumes
        onView(withId(R.id.album_list)) // Se debe reemplazar con el ID real
                .perform(actionOnItemAtPosition(0, click()));

        // Asumiendo que la lista de canciones se muestra en un RecyclerView con el ID "song_list"
        // en la pantalla de detalle.

        // Verifica que el RecyclerView de la lista de canciones esté presente
        onView(withId(R.id.song_list)) // Reemplaza con el ID real de tu RecyclerView de canciones
                .check(matches(isDisplayed()));

        // Verificamos que cada canción esperada esté presente en la lista
        for (int i = 0; i < primerasCanciones.size(); i++) {
            onView(withId(R.id.song_list)) // Reemplaza con el ID real de tu RecyclerView de canciones
                    .perform(scrollToPosition(i)) // Asegura que el elemento esté visible
                    .check(matches(hasDescendant(withText(primerasCanciones.get(i)))));
        }
    }

    @Test
    public void alSeleccionarUnAlbumSeMuestraUnBotonParaVolverAlCatalogo() {

        // Simula el clic en el primer elemento de la lista de álbumes
        onView(withId(R.id.album_list)) // Reemplaza con el ID
                .perform(actionOnItemAtPosition(0, click()));

        // Asumiendo que hay un Button en la pantalla de detalle con el ID "button_volver_catalogo"

        // Verifica que el botón esté presente y visible
        onView(withId(R.id.button_volver_catalogo)) // Se debe reemplazar con el ID real del botón "Volver"
                .check(matches(isDisplayed()))
                .check(matches(withText("Volver al Catálogo"))); // Se debe reemplazar con el texto real del botón
    }

    @Test
    public void alHacerClickEnElBotonVolverSeNavegaAlCatalogo() {

        // Simula el clic en el primer elemento de la lista de álbumes
        onView(withId(R.id.album_list)) // Se debe reemplazar con el ID real de tu RecyclerView de álbumes
                .perform(actionOnItemAtPosition(0, click()));

        // Asumiendo que hay un Button en la pantalla de detalle con el ID "button_volver_catalogo"

        // Simula el clic en el botón "Volver al Catálogo"
        onView(withId(R.id.button_volver_catalogo)) // Se debe reemplazar con el ID real del botón "Volver"
                .perform(click());

    }
}
