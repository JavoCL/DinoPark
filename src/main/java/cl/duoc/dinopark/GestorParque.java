package cl.duoc.dinopark;
import cl.duoc.dinopark.model.DatoInvalidoException;
import cl.duoc.dinopark.model.EspecieCarnivoro;
import cl.duoc.dinopark.model.EspecieDinosaurio;

import java.util.ArrayList;
import java.util.List;

public class GestorParque {
    private static GestorParque instancia;

    private List<EspecieDinosaurio> listaEspecies;


    private GestorParque() {
        listaEspecies = new ArrayList<>();

        // Datos de prueba iniciales opcionales
//        try {
//            EspecieDinosaurio tRex = new EspecieCarnivoro("A1","Tyrannosaurus Rex", "Cretácico", 1000f, 10);
//            listaEspecies.add(tRex);
//        } catch (DatoInvalidoException e) {
//            e.printStackTrace();
//        }
    }

    public static synchronized GestorParque getInstancia() {
        if (instancia == null) {
            instancia = new GestorParque();
        }
        return instancia;
    }

    public void agregarEspecie(EspecieDinosaurio especie) throws DatoInvalidoException {
        if (especie == null) {
            throw new DatoInvalidoException("La especie no es válida.");
        }
        listaEspecies.add(especie);
    }

    public List<EspecieDinosaurio> getListaEspecies() {
        return listaEspecies;
    }
}