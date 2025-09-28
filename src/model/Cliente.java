package model;

/**
 * Representa un cliente con hasta 10 cuentas.
 */
public class Cliente {

    public static final int MAX_CUENTAS = 10;

    private String nombreCompleto;
    private int    edad;
    private String cedula;

    private Cuenta[] cuentas;
    private int cuentasOcupadas;

    /**
     * (Modificador) Crea el cliente sin cuentas.
     * @param nombreCompleto no vacío
     * @param edad > 0
     * @param cedula no vacía
     * @throws IllegalArgumentException si hay datos inválidos
     */
    public Cliente(String nombreCompleto, int edad, String cedula){
        if (nombreCompleto == null || nombreCompleto.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (edad <= 0)
            throw new IllegalArgumentException("La edad debe ser > 0.");
        if (cedula == null || cedula.isBlank())
            throw new IllegalArgumentException("La cédula es obligatoria.");

        this.nombreCompleto = nombreCompleto.trim();
        this.edad = edad;
        this.cedula = cedula.trim();

        this.cuentas = new Cuenta[MAX_CUENTAS];
        this.cuentasOcupadas = 0;
    }

    /** (Analizador) */
    public String getNombreCompleto(){ return nombreCompleto; }
    /** (Analizador) */
    public int getEdad(){ return edad; }
    /** (Analizador) */
    public String getCedula(){ return cedula; }

    /** (Analizador) cantidad de cuentas creadas. */
    public int getCuentasOcupadas(){ return cuentasOcupadas; }

    /**
     * (Modificador) Agrega una cuenta si hay espacio.
     * @param c cuenta a agregar
     * @return true si se agregó; false si ya llegó al límite
     */
    public boolean agregarCuenta(Cuenta c){
        if (c == null) return false;
        if (cuentasOcupadas >= MAX_CUENTAS) return false;
        cuentas[cuentasOcupadas++] = c;
        return true;
    }

    /**
     * (Analizador) Obtiene una cuenta por índice válido.
     * @param index 0..cuentasOcupadas-1
     * @return la cuenta o null si el índice no es válido
     */
    public Cuenta getCuentaPorIndice(int index){
        if (index < 0 || index >= cuentasOcupadas) return null;
        return cuentas[index];
    }

    /** (Analizador) Suma de saldos de todas las cuentas del cliente. */
    public double saldoTotalCliente(){
        double total = 0;
        for (int i = 0; i < cuentasOcupadas; i++) total += cuentas[i].getSaldo();
        return total;
    }

    @Override
    public String toString(){
        return String.format("%s (edad %d) - CC %s", nombreCompleto, edad, cedula);
    }
}

