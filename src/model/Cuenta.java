package model;

// Cuenta bancaria simple
public class Cuenta {

    private String nombreBanco;
    private TipoCuenta tipo;
    private double saldo;

    // Crea la cuenta
    public Cuenta(String nombreBanco, TipoCuenta tipo, double saldoInicial){
        if (nombreBanco == null || nombreBanco.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del banco es obligatorio");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de cuenta es obligatorio");
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
        }
        this.nombreBanco = nombreBanco.trim();
        this.tipo = tipo;
        this.saldo = saldoInicial;
    }

    public String getNombreBanco(){ return nombreBanco; }
    public TipoCuenta getTipo(){ return tipo; }
    public double getSaldo(){ return saldo; }

    // Deposita (monto > 0)
    public void depositar(double monto){
        if (monto <= 0) {
            throw new IllegalArgumentException("El depósito debe ser > 0");
        }
        saldo += monto;
    }

    // Retira (monto > 0 y sin dejar negativo)
    public boolean retirar(double monto){
        if (monto <= 0) {
            throw new IllegalArgumentException("El retiro debe ser > 0");
        }
        if (saldo - monto < 0) {
            return false;
        }
        saldo -= monto;
        return true;
    }

    @Override
    public String toString(){
        return nombreBanco + " | " + tipo + " | saldo: " + saldo;
    }
}
