package cl.duoc.dinopark.model;

public class EspecieHerbivoro extends EspecieDinosaurio {

    // regionATRIBUTOS
    private String tipoPlantaPreferida;
    // endregion

    // region CONSTRUCTORES
    public EspecieHerbivoro(String nuevoId, String nuevaEspecie, String nuevoPeriodo, float nuevoPeso, String nuevoTipoPlantaPreferida) throws DatoInvalidoException {
        super(nuevoId,nuevaEspecie,nuevoPeriodo,nuevoPeso);
        this.setTipoPlantaPreferida(nuevoTipoPlantaPreferida);
    }
    // endregion

    // region GETTERS
    public String getTipoPlantaPreferida() { return tipoPlantaPreferida; }
    // endregion

    // region SETTERS
    public void setTipoPlantaPreferida(String nuevoTipoPlantaPreferida) throws DatoInvalidoException{
        if(nuevoTipoPlantaPreferida == null || nuevoTipoPlantaPreferida.trim().isEmpty())
        {
            throw new DatoInvalidoException("El tipo de planta preferida no puede ser vacío.");
        }
        this.tipoPlantaPreferida = tipoPlantaPreferida;
    }
    // endregion

    // region METODOS
    @Override
    public String emitirRugido() {
        return "¡Muuuu! Sonido grave y pacífico de herbívoro.";
    }

    @Override
    public String toString()  {
        return "Especie: '" + this.getNombreEspecie() + '\'' + ", periodo: " + this.getPeriodo() + ", tipo planta favorita: " + tipoPlantaPreferida +'}';
    }

    // endregion
}
