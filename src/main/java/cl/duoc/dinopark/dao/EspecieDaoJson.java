package cl.duoc.dinopark.dao;

import cl.duoc.dinopark.model.EspecieDinosaurio;
import cl.duoc.dinopark.model.EspecieCarnivoro;
import cl.duoc.dinopark.model.EspecieHerbivoro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta de EspecieDao utilizando un contenedor
 * estructurado con Gson estándar para evitar dependencias complejas.
 */
public class EspecieDaoJson implements EspecieDao {

    private static final String RUTA_ARCHIVO = "especies_dino.json";
    private final Gson gson;

    public EspecieDaoJson() {
        // Inicializamos Gson con formato legible (pretty printing)
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Clase auxiliar interna para organizar la persistencia separando
     * las especies según su tipo concreto de forma limpia.
     */
    private static class ContenedorEspecies {
        List<EspecieCarnivoro> carnivoros = new ArrayList<>();
        List<EspecieHerbivoro> herbivoros = new ArrayList<>();
    }

    @Override
    public void guardarEspecies(List<EspecieDinosaurio> listaEspecies) throws Exception {
        ContenedorEspecies contenedor = new ContenedorEspecies();

        // Clasificamos cada especie de la lista general en su respectiva sub-lista
        for (EspecieDinosaurio especie : listaEspecies) {
            if (especie instanceof EspecieCarnivoro) {
                contenedor.carnivoros.add((EspecieCarnivoro) especie);
            } else if (especie instanceof EspecieHerbivoro) {
                contenedor.herbivoros.add((EspecieHerbivoro) especie);
            }
        }

        // Guardamos el objeto contenedor completo en el archivo JSON
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            gson.toJson(contenedor, writer);
        } catch (IOException e) {
            throw new Exception("Error al guardar las especies en el archivo JSON: " + e.getMessage());
        }
    }

    @Override
    public List<EspecieDinosaurio> cargarEspecies() throws Exception {
        File archivo = new File(RUTA_ARCHIVO);

        // Si el archivo no existe todavía, retornamos una lista vacía
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            ContenedorEspecies contenedor = gson.fromJson(reader, ContenedorEspecies.class);
            List<EspecieDinosaurio> listaUnificada = new ArrayList<>();

            if (contenedor != null) {
                // Reconstruimos la lista general uniendo carnívoros y herbívoros
                if (contenedor.carnivoros != null) {
                    listaUnificada.addAll(contenedor.carnivoros);
                }
                if (contenedor.herbivoros != null) {
                    listaUnificada.addAll(contenedor.herbivoros);
                }
            }

            return listaUnificada;
        } catch (IOException e) {
            throw new Exception("Error al leer las especies desde el archivo JSON: " + e.getMessage());
        }
    }
}