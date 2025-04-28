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

    @Test
    public void alNavegarADetalleSeMuestraFotoYNombreEnEncabezado() {

        // Datos del primer artista (reemplaza con datos reales o una forma de obtenerlos)
        String primerArtistaNombre = "Nombre del Artista 1";

        // Simula el clic en el primer elemento de la lista de artistas para navegar al detalle
        onView(withId(R.id.artist_list)) // Reemplaza con el ID real de tu RecyclerView de artistas
                .perform(actionOnItemAtPosition(0, click()));

        // Asumiendo que en la pantalla de detalle se tiene:
        // - Un ImageView para la foto del artista con el ID "artist_header_photo"
        // - Un TextView para el nombre del artista en el encabezado con el ID "artist_header_name"

        // Verificamos que la foto del artista esté visible
        onView(withId(R.id.artist_header_photo)) // Se debe reemplazar con el ID real de la ImageView de la foto del encabezado
                .check(matches(isDisplayed()));

        // Verifica que el nombre del artista en el encabezado se muestre con el texto esperado
        onView(withId(R.id.artist_header_name)) // Se debe reemplazarcon el ID real de la TextView del nombre en el encabezado
                .check(matches(withText(primerArtistaNombre)));
    }

    @Test
    public void alNavegarADetalleSeMuestraInformacionPersonal() {

        // Datos del primer artista (reemplaza con datos reales)
        String primerArtistaNombreCompleto = "Nombre Completo del Artista 1";
        String primerArtistaFechaNacimiento = "01/01/1970";//Se debe tener en cuenta el formato de fecha
        String primerArtistaNacionalidad = "Colombiana";

        String infoPersonalEsperada = "Nombre: " + primerArtistaNombreCompleto + "\n" +
                "Nacimiento: " + primerArtistaFechaNacimiento + "\n" +
                "Nacionalidad: " + primerArtistaNacionalidad;

        // Simula el clic en el primer elemento de la lista de artistas para navegar al detalle
        onView(withId(R.id.artist_list)) // Se debe reemplazar con el ID real  de artistas
                .perform(actionOnItemAtPosition(0, click()));

        // Asumiendo que en la pantalla de detalle tenemos un TextView con el ID "artist_personal_info"
        // donde se muestra toda la información personal.

        // Verificamos que el TextView de información personal se muestre con el texto esperado
        onView(withId(R.id.artist_personal_info)) // Se debe reemplazar con el ID real del TextView de información personal
                .check(matches(withText(infoPersonalEsperada)));
    }

    @Test
    public void alNavegarADetalleSeMuestraLaBiografia() {

        // Datos del primer artista (reemplaza con datos reales)
        String primerArtistaBiografia = "Esta es una biografía de prueba para el Artista 1. " +
                "Incluye detalles sobre su carrera musical, influencias y logros.";

        // Simula el clic en el primer elemento de la lista de artistas para navegar al detalle
        onView(withId(R.id.artist_list)) // Se debe reemplazar con el ID real de artistas
                .perform(actionOnItemAtPosition(0, click()));

        // Asumiendo que en la pantalla de detalle tenemos un TextView con el ID "artist_biography"
        // donde se muestra la biografía del artista.

        // Verifica que el TextView de la biografía se muestre con el texto esperado
        onView(withId(R.id.artist_biography)) // Reemplaza con el ID real de tu TextView de la biografía
                .check(matches(withText(primerArtistaBiografia)));

    }

}
