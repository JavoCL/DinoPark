package cl.duoc.dinopark.model;

public class EspecieCarnivoro extends EspecieDinosaurio {

    // region ATRIBUTOS
    private int nivelPeligrosidad; // Escala del 1 al 10
    // endregion

    // region CONSTRUCTORES
    public EspecieCarnivoro(String nuevoId, String nuevaEspecie, String nuevoPeriodo, float nuevoPeso, int nuevoNivelPeligrosidad) throws DatoInvalidoException {
        super(nuevoId, nuevaEspecie, nuevoPeriodo,nuevoPeso);
        this.setNivelPeligrosidad(nuevoNivelPeligrosidad);
    }
    // endregion

    // region GETTERS
    public int getNivelPeligrosidad() { return nivelPeligrosidad; }
    // endregion

    // region SETTERS
    public void setNivelPeligrosidad(int nuevoNivelPeligrosidad) throws DatoInvalidoException{
        if(nuevoNivelPeligrosidad < 1 || nuevoNivelPeligrosidad > 10)
        {
            throw new DatoInvalidoException("Nivel de peligrosidad inválido. Debe ser un valor entre 1 y 10");
        }
        this.nivelPeligrosidad = nuevoNivelPeligrosidad;
    }
    // endregion

    // region METODOS
    @Override
    public String emitirRugido() {
        return "¡GRRR! Rugido feroz de carnívoro con nivel de alerta " + nivelPeligrosidad;
    }

    @Override
    public String toString()  {
        return "Especie: '" + this.getNombreEspecie() + '\'' + ", periodo: " + this.getPeriodo() + ", nivelPeligrosidad: " + nivelPeligrosidad +'}';
    }
    // endregion
}