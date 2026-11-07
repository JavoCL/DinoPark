package cl.duoc.dinopark.model;

public abstract class EspecieDinosaurio {
    // region ATRIBUTOS
    private String id;
    private String nombreEspecie;
    private String periodo;
    private float peso;
    // endregion

    // region CONSTRUCTORES
    public EspecieDinosaurio(String nuevoId, String nuevaEspecie, String nuevoPeriodo, float nuevoPeso) throws DatoInvalidoException {
        this.setId(nuevoId);
        this.setNombreEspecie(nuevaEspecie);
        this.setPeriodo(nuevoPeriodo);
        this.setPeso(nuevoPeso);
    }
    // endregion

    // region GETTERS
    public String getId() { return id; }
    public double getPeso() { return peso; }
    public String getNombreEspecie() {return nombreEspecie;}
    public String getPeriodo() {return periodo;}
    // endregion

    // region SETTERS

    public void setId(String nuevoId) throws DatoInvalidoException{
        if (nuevoId == null || nuevoId.trim().isEmpty()) {
            throw new DatoInvalidoException("El ID no puede estar vacío.");
        }

        this.id = nuevoId;
    }

    public void setNombreEspecie (String nuevoNombreEspecie) throws DatoInvalidoException{
        if (nuevoNombreEspecie == null || nuevoNombreEspecie.trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre de la nueva especie de dinosaurio no puede estar vacío.");
        }

        this.nombreEspecie = nuevoNombreEspecie;
    }

    public void setPeriodo(String nuevoPeriodo) throws DatoInvalidoException {
        if (nuevoPeriodo == null || nuevoPeriodo.trim().isEmpty()) {
            throw new DatoInvalidoException("El período al que perteneció la especie de dinosaurio no puede ser vacío.");
        }

        this.periodo = nuevoPeriodo;
    }

    public void setPeso(float nuevoPeso) throws DatoInvalidoException {
        if (nuevoPeso < 0) {
            throw new DatoInvalidoException("El peso no puede ser valor negativo.");
        }

        this.peso = nuevoPeso;
    }

    // endregion

    //region METODOS

    // Método abstracto obligatorio para las subclases
    public abstract String emitirRugido();

    @Override
    public abstract String toString();

    // endregion
}
