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

    // La ruta del archivo donde se guardarn/leeran los datos
    // 'static' significa que no requiere un objeto de la clase 'EspecieDaoJson' para existir (Pertenece a la clase, no a un objeto)
    // 'final' significa que su valor será constante y no puede modificarse en ejecución
    private static final String RUTA_ARCHIVO = "especies_dino.json";
    private final Gson gson;

    public EspecieDaoJson() {
        // Inicializamos Gson con formato legible (pretty printing)
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Clase auxiliar interna para organizar la persistencia separando
     * las especies según su tipo concreto de forma limpia.
     * Esto ayudará que las especies se guarden por separado en el JSON resultante.
     */
    private static class ContenedorEspecies {
        List<EspecieCarnivoro> carnivoros = new ArrayList<>();
        List<EspecieHerbivoro> herbivoros = new ArrayList<>();
    }

    @Override
    public void guardarEspecies(List<EspecieDinosaurio> listaEspecies) throws Exception {
        ContenedorEspecies contenedor = new ContenedorEspecies();

        // Clasificamos cada especie de la lista general en su respectiva sub-lista
        // Recorremos la lista
        for (EspecieDinosaurio especie : listaEspecies) {
            // Si la especie actual es carnivoro, lo añade a la coleccion de carnivoros
            if (especie instanceof EspecieCarnivoro) {
                contenedor.carnivoros.add((EspecieCarnivoro) especie);
            }
            // Si la especie actual es herbivoro, lo añade a la coleccion de carnivoros
            else if (especie instanceof EspecieHerbivoro) {
                contenedor.herbivoros.add((EspecieHerbivoro) especie);
            }
        }

        // Guardamos el objeto contenedor completo en el archivo JSON
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO)) {
            // 'gson.toJson()' transforma el entrante 'contenedor' en un objeto Json estándar válido
            gson.toJson(contenedor, writer);
        }
        // Si hay un error de escritura, se lanzara excepcion de lectura-escritura (IOException)
        catch (IOException e) {
            throw new Exception("Error al guardar las especies en el archivo JSON: " + e.getMessage());
        }
    }

    @Override
    public List<EspecieDinosaurio> cargarEspecies() throws Exception {
        // Se abre un nuevo objeto que cargara los datos desde el archivo
        File archivo = new File(RUTA_ARCHIVO);

        // Si el archivo no existe todavía, retornamos una lista vacía
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        // Intentamos leer el archivo
        // Si lo logramos...
        try (FileReader reader = new FileReader(RUTA_ARCHIVO)) {
            // ... transformamos lo que leemos dentro del archivo a la clase 'ContenedorEspecies'
            // (que es el objeto guardado en el Json)
            ContenedorEspecies contenedor = gson.fromJson(reader, ContenedorEspecies.class);
            // Lista unificada de especies leidas desde el archivo
            List<EspecieDinosaurio> listaUnificada = new ArrayList<>();

            // Si lo pudo leer...
            if (contenedor != null) {
                // ... reconstruimos la lista general uniendo carnívoros y herbívoros
                if (contenedor.carnivoros != null) {
                    listaUnificada.addAll(contenedor.carnivoros);
                }
                if (contenedor.herbivoros != null) {
                    listaUnificada.addAll(contenedor.herbivoros);
                }
            }

            // Retorna la lista leida
            return listaUnificada;
        }
        // Si hay un error de lectura, se lanzara excepcion de lectura-escritura (IOException)
        catch (IOException e) {
            throw new Exception("Error al leer las especies desde el archivo JSON: " + e.getMessage());
        }
    }
}