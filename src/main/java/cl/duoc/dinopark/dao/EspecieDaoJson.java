package cl.duoc.dinopark.dao;

import cl.duoc.dinopark.model.EspecieDinosaurio;
import cl.duoc.dinopark.model.EspecieCarnivoro;
import cl.duoc.dinopark.model.EspecieHerbivoro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
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
    private static final String RUTA_ARCHIVO_C = "especies_carnivoros.json";
    private static final String RUTA_ARCHIVO_H = "especies_herbivoros.json";
    private final Gson gson;

    public EspecieDaoJson() {
        // Inicializamos Gson con formato legible (pretty printing)
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void guardarEspecies(List<EspecieDinosaurio> listaEspecies) throws IOException {
        //ContenedorEspecies contenedor = new ContenedorEspecies();

        // Clasificamos cada especie de la lista general en su respectiva sub-lista
        List<EspecieCarnivoro> carnivoros = new ArrayList<>();
        List<EspecieHerbivoro> herbivoros = new ArrayList<>();
        // Recorremos la lista
        for (EspecieDinosaurio especie : listaEspecies) {
            // Si la especie actual es carnivoro, lo añade a la coleccion de carnivoros
            if (especie instanceof EspecieCarnivoro) {
                carnivoros.add((EspecieCarnivoro) especie);
            }
            // Si la especie actual es herbivoro, lo añade a la coleccion de carnivoros
            else if (especie instanceof EspecieHerbivoro) {
                herbivoros.add((EspecieHerbivoro) especie);
            }
        }

        // Guardamos la colección de especies carnivoras
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO_C)) {
            // 'gson.toJson()' transforma el entrante 'contenedor' en un objeto Json estándar válido
            gson.toJson(carnivoros, writer);
        }
        // Si hay un error de escritura, se lanzara excepcion de lectura-escritura (IOException)
        catch (IOException e) {
            throw new IOException("Error al guardar las especies carnívoras en el archivo JSON: " + e.getMessage());
        }

        // Guardamos la colección de especies carnivoras
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO_H)) {
            // 'gson.toJson()' transforma el entrante 'contenedor' en un objeto Json estándar válido
            gson.toJson(herbivoros, writer);
        }
        // Si hay un error de escritura, se lanzara excepcion de lectura-escritura (IOException)
        catch (IOException e) {
            throw new IOException("Error al guardar las especies herbívoras en el archivo JSON: " + e.getMessage());
        }
    }

    @Override
    public List<EspecieDinosaurio> cargarEspecies() throws Exception {
        // Lista general unificada que vamos a retornar
        List<EspecieDinosaurio> listaUnificada = new ArrayList<>();

        // 1. CARGAR CARNÍVOROS DESDE SU ARCHIVO
        File archivoCarnivoros = new File(RUTA_ARCHIVO_C);
        if (archivoCarnivoros.exists()) {
            try (FileReader reader = new FileReader(archivoCarnivoros)) {
                Type tipoListaCarnivoros = new TypeToken<List<EspecieCarnivoro>>(){}.getType();
                List<EspecieCarnivoro> listaCarnivoros = gson.fromJson(reader, tipoListaCarnivoros);

                if (listaCarnivoros != null) {
                    listaUnificada.addAll(listaCarnivoros); // Las añadimos a la lista unificada
                }
            } catch (IOException e) {
                throw new Exception("Error al leer el archivo de carnívoros: " + e.getMessage());
            }
        }

        // 2. CARGAR HERBÍVOROS DESDE SU ARCHIVO
        File archivoHerbivoros = new File(RUTA_ARCHIVO_H);
        if (archivoHerbivoros.exists()) {
            try (FileReader reader = new FileReader(archivoHerbivoros)) {
                Type tipoListaHerbivoros = new TypeToken<List<EspecieHerbivoro>>(){}.getType();
                List<EspecieHerbivoro> listaHerbivoros = gson.fromJson(reader, tipoListaHerbivoros);

                if (listaHerbivoros != null) {
                    listaUnificada.addAll(listaHerbivoros); // Las añadimos a la lista unificada
                }
            } catch (IOException e) {
                throw new Exception("Error al leer el archivo de herbívoros: " + e.getMessage());
            }
        }

        // 3. Retornamos la lista combinada con todos los dinosaurios
        return listaUnificada;
    }
}