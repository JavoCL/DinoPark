package cl.duoc.dinopark;
import cl.duoc.dinopark.model.DatoInvalidoException;
import cl.duoc.dinopark.model.EspecieCarnivoro;
import cl.duoc.dinopark.model.EspecieDinosaurio;

import java.util.ArrayList;
import java.util.List;

public class GestorParque {
    // Instancia unica del gestor.
    // Al ser 'static' puedo acceder sin necesidad de crear un objeto de la clase 'GestorParque'
    private static GestorParque instancia;

    // Lista de las especies
    private List<EspecieDinosaurio> listaEspecies;


    // Constructor del GestorParque
    // Inicializa la lista de especies
    private GestorParque() {
        listaEspecies = new ArrayList<>();
    }

    // Metodo estatico que no necesita un objeto.
    // 'synchronized' lo hace "único" al momento de ser llamado este metodo
    public static synchronized GestorParque getInstancia() {
        // Si no existe la instancia actual (o 'singleton')
        // lo creará
        if (instancia == null) {
            instancia = new GestorParque();
        }
        // Retorna la instancia
        return instancia;
    }

    // Agrega la especie a la lista del gestor.
    // Podría arrojar excepcion si la especie es nula
    public void agregarEspecie(EspecieDinosaurio especie) throws DatoInvalidoException {
        if (especie == null) {
            throw new DatoInvalidoException("La especie no es válida.");
        }
        listaEspecies.add(especie);
    }

    // Obtiene la lista de especies
    public List<EspecieDinosaurio> getListaEspecies() {
        return listaEspecies;
    }
}