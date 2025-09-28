package model;

public class BancoIcesiController {

    private Cliente[] clientes;    // arreglo fijo
    private int clientesOcupados;  // cuántos hay usados

    /**
     * (Modificador) Crea el controlador con un arreglo fijo de clientes.
     * Precondición: capacidadClientes > 0.
     * Postcondición: se inicializa un arreglo vacío de tamaño dado y clientesOcupados = 0.
     *
     * @param capacidadClientes capacidad total del arreglo de clientes.
     * @throws IllegalArgumentException si capacidadClientes <= 0.
     */
    public BancoIcesiController(int capacidadClientes){
        if (capacidadClientes <= 0) {
            throw new IllegalArgumentException("Capacidad inválida");
        }
        clientes = new Cliente[capacidadClientes];
        clientesOcupados = 0;
    }

    /**
     * (Analizador) Busca un cliente por su número de cédula.
     * Precondición: la cédula no es null.
     * Postcondición: no modifica el estado interno; retorna el cliente encontrado o null.
     *
     * @param cedula cédula a buscar.
     * @return el cliente con esa cédula, o null si no existe.
     */
    public Cliente buscarClientePorCedula(String cedula){
        if (cedula == null) return null;
        String cc = cedula.trim();
        for (int i = 0; i < clientesOcupados; i++) {
            if (clientes[i].getCedula().equals(cc)) {
                return clientes[i];
            }
        }
        return null;
    }

    /**
     * (Modificador) Registra un nuevo cliente si hay espacio y no está repetido.
     * Precondición: nuevo != null; la cédula de nuevo no debe existir ya en el sistema; hay espacio disponible.
     * Postcondición: si se registra, clientesOcupados aumenta en 1 y el cliente queda almacenado en la siguiente posición libre; si falla, no hay cambios.
     *
     * @param nuevo cliente a registrar.
     * @return true si se agregó; false si no había espacio o la cédula ya existía.
     */
    public boolean registrarCliente(Cliente nuevo){
        if (nuevo == null) return false;
        if (clientesOcupados >= clientes.length) return false;
        if (buscarClientePorCedula(nuevo.getCedula()) != null) return false;
        clientes[clientesOcupados] = nuevo;
        clientesOcupados++;
        return true;
    }

    /**
     * (Modificador) Agrega una cuenta a un cliente existente.
     * Precondición: existe un cliente con la cédula dada; banco no vacío; tipo != null; saldoInicial >= 0; el cliente no ha superado su límite de cuentas.
     * Postcondición: si se agrega, el cliente tendrá una nueva cuenta y su número de cuentas aumenta en 1; si falla, no hay cambios.
     *
     * @param cedula cédula del cliente destino.
     * @param banco  nombre del banco.
     * @param tipo   tipo de cuenta.
     * @param saldoInicial saldo inicial de la cuenta.
     * @return true si la cuenta fue agregada; false si el cliente no existe o no hay cupo para más cuentas.
     * @throws IllegalArgumentException si banco es vacío, tipo es null o saldoInicial < 0 (propagado desde el constructor de Cuenta).
     */
    public boolean agregarCuentaCliente(String cedula, String banco, TipoCuenta tipo, double saldoInicial){
        Cliente c = buscarClientePorCedula(cedula);
        if (c == null) return false;
        Cuenta cuenta = new Cuenta(banco, tipo, saldoInicial);
        return c.agregarCuenta(cuenta);
    }

    /**
     * (Modificador) Deposita dinero en una cuenta específica de un cliente.
     * Precondición: existe el cliente; el índice de cuenta es válido (0..n-1); monto > 0.
     * Postcondición: si el depósito procede, el saldo de esa cuenta aumenta en monto; si el cliente o la cuenta no existen, no hay cambios.
     *
     * @param cedula cédula del cliente.
     * @param indexCuenta índice de la cuenta (empieza en 0).
     * @param monto monto a depositar.
     * @return true si se depositó; false si cliente/cuenta no válidos.
     * @throws IllegalArgumentException si monto <= 0 (propagado desde Cuenta.depositar).
     */
    public boolean depositarDineroCuenta(String cedula, int indexCuenta, double monto){
        Cliente c = buscarClientePorCedula(cedula);
        if (c == null) return false;
        Cuenta cuenta = c.getCuentaPorIndice(indexCuenta);
        if (cuenta == null) return false;
        cuenta.depositar(monto);
        return true;
    }

    /**
     * (Modificador) Retira dinero de una cuenta específica de un cliente (no permite saldo negativo).
     * Precondición: existe el cliente; el índice de cuenta es válido; monto > 0; la cuenta tiene fondos suficientes.
     * Postcondición: si procede, el saldo de la cuenta disminuye en monto; si no hay fondos o cliente/cuenta no válidos, no hay cambios.
     *
     * @param cedula cédula del cliente.
     * @param indexCuenta índice de la cuenta (empieza en 0).
     * @param monto monto a retirar.
     * @return true si se retiró; false si no había fondos o cliente/cuenta no válidos.
     * @throws IllegalArgumentException si monto <= 0 (propagado desde Cuenta.retirar).
     */
    public boolean retirarDineroCuenta(String cedula, int indexCuenta, double monto){
        Cliente c = buscarClientePorCedula(cedula);
        if (c == null) return false;
        Cuenta cuenta = c.getCuentaPorIndice(indexCuenta);
        if (cuenta == null) return false;
        return cuenta.retirar(monto);
    }

    /**
     * (Analizador) Retorna una copia de los clientes almacenados (solo el rango usado).
     * Precondición: ninguna.
     * Postcondición: no modifica el estado interno; retorna un nuevo arreglo de tamaño clientesOcupados con las mismas referencias a Cliente.
     *
     * @return un nuevo arreglo con los clientes válidos.
     */
    public Cliente[] getClienteList(){
        Cliente[] copia = new Cliente[clientesOcupados];
        for (int i = 0; i < clientesOcupados; i++) {
            copia[i] = clientes[i];
        }
        return copia;
    }

    /**
     * (Analizador) Suma los saldos de todas las cuentas de todos los clientes.
     * Precondición: ninguna.
     * Postcondición: no modifica el estado interno; retorna un valor >= 0.
     *
     * @return suma total de saldos.
     */
    public double calcularSaldoTotalSistema(){
        double total = 0;
        for (int i = 0; i < clientesOcupados; i++) {
            total += clientes[i].saldoTotalCliente();
        }
        return total;
    }

    /**
     * (Analizador) Retorna la capacidad total del arreglo de clientes.
     * Precondición: ninguna.
     * Postcondición: no modifica el estado interno.
     *
     * @return tamaño máximo del arreglo interno.
     */
    public int getCapacidad(){
        return clientes.length;
    }

    /**
     * (Analizador) Retorna cuántas posiciones están usadas.
     * Precondición: ninguna.
     * Postcondición: no modifica el estado interno.
     *
     * @return número de clientes almacenados (0..capacidad).
     */
    public int getClientesOcupados(){
        return clientesOcupados;
    }
}
