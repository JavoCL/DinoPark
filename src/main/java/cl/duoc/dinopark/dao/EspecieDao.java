package cl.duoc.dinopark.dao;

import cl.duoc.dinopark.model.EspecieDinosaurio;
import java.util.List;

/**
 * Interfaz DAO que define los contratos de persistencia
 * para las especies de dinosaurios del parque.
 */
public interface EspecieDao {

    /**
     * Guarda la lista actual de especies en el medio de persistencia.
     * @param listaEspecies Lista con las especies a guardar.
     * @throws Exception Si ocurre un error durante la escritura.
     */
    void guardarEspecies(List<EspecieDinosaurio> listaEspecies) throws Exception;

    /**
     * Lee y recupera la lista de especies desde el medio de persistencia.
     * @return Lista de especies recuperadas.
     * @throws Exception Si ocurre un error durante la lectura.
     */
    List<EspecieDinosaurio> cargarEspecies() throws Exception;
}