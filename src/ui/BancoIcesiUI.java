package ui;

import java.util.Scanner;
import model.*;

public class BancoIcesiUI {

    private Scanner input;
    private BancoIcesiController controller;

    public static void main(String[] args) {
        BancoIcesiUI ui = new BancoIcesiUI();
        ui.menu();
    }

    public BancoIcesiUI() {
        input = new Scanner(System.in);
    }

    // Muestra las cuentas de un cliente con sus índices
    private void listarCuentasDe(String cedula) {
        Cliente cl = controller.buscarClientePorCedula(cedula);
        if (cl == null) {
            System.out.println("No existe un cliente con esa cédula.");
            return;
        }
        int n = cl.getCuentasOcupadas();
        if (n == 0) {
            System.out.println("El cliente no tiene cuentas registradas.");
            return;
        }
        System.out.println("Cuentas del cliente:");
        for (int i = 0; i < n; i++) {
            Cuenta cta = cl.getCuentaPorIndice(i);
            System.out.println("  [" + i + "] " + cta.getNombreBanco() + " | " + cta.getTipo() + " | saldo: " + cta.getSaldo());
        }
    }

    public void menu() {

        System.out.println("Bienvenido a BancoIcesi");

        System.out.print("¿Cuántos clientes máximo manejará el sistema? ");
        int capacidad = input.nextInt();
        input.nextLine(); // limpiar salto
        controller = new BancoIcesiController(capacidad);

        int option = -1;
        do {
            System.out.println();
            System.out.println("Menu Principal");
            System.out.println("Digite alguna de las siguientes opciones");
            System.out.println("1) Registrar cliente");
            System.out.println("2) Asignar cuenta bancaria a cliente");
            System.out.println("3) Depositar dinero en cuenta bancaria de un cliente");
            System.out.println("4) Retirar dinero de una cuenta bancaria de un cliente");
            System.out.println("5) Consultar cliente por numero de cédula");
            System.out.println("6) Consultar el saldo total de todas las cuentas");
            System.out.println("0) Salir del sistema");
            option = input.nextInt();
            input.nextLine(); // bug del scanner

            if (option == 1) {
                registrarCliente();
            } else if (option == 2) {
                asignarCuentaBancariaCliente();
            } else if (option == 3) {
                depositarDineroCuenta();
            } else if (option == 4) {
                retirarDineroCuenta();
            } else if (option == 5) {
                consultarCliente();
            } else if (option == 6) {
                consultarSaldoTotal();
            } else if (option == 0) {
                System.out.println();
                System.out.println("Gracias por usar nuestros servicios. ¡Adios!");
            } else {
                System.out.println();
                System.out.println("Opción invalida. Intente nuevamente.");
            }

        } while (option != 0);
    }

    private void registrarCliente() {
        System.out.print("Nombre completo: ");
        String nombre = input.nextLine().trim();

        System.out.print("Edad (>0): ");
        int edad = input.nextInt();
        input.nextLine();

        System.out.print("Cedula: ");
        String cedula = input.nextLine().trim();

        boolean ok = controller.registrarCliente(new Cliente(nombre, edad, cedula));
        if (ok) {
            System.out.println("Cliente registrado");
        } else {
            System.out.println("No se pudo (capacidad llena o cedula duplicada).");
        }
    }

    private void asignarCuentaBancariaCliente() {
        System.out.print("Cedula del cliente: ");
        String cedula = input.nextLine().trim();

        System.out.print("Nombre del banco: ");
        String banco = input.nextLine().trim();

        System.out.print("Tipo de cuenta (1=AHORROS, 2=CORRIENTE): ");
        int t = input.nextInt();
        input.nextLine();
        TipoCuenta tipo;
        if (t == 1) tipo = TipoCuenta.AHORROS;
        else tipo = TipoCuenta.CORRIENTE;

        System.out.print("Saldo inicial (>=0): ");
        double saldo = input.nextDouble();
        input.nextLine();

        boolean ok = controller.agregarCuentaCliente(cedula, banco, tipo, saldo);
        if (ok) {
            System.out.println("Cuenta agregada");
        } else {
            System.out.println("No se pudo (cliente no existe o llego al límite de 10).");
        }
    }

    private void depositarDineroCuenta() {
        System.out.print("Cedula del cliente: ");
        String cedula = input.nextLine().trim();

        Cliente cl = controller.buscarClientePorCedula(cedula);
        if (cl == null) {
            System.out.println("No existe un cliente con esa cedula.");
            return;
        }
        if (cl.getCuentasOcupadas() == 0) {
            System.out.println("El cliente no tiene cuentas registradas.");
            return;
        }

        listarCuentasDe(cedula);

        System.out.print("Indice de cuenta (empieza en 0): ");
        int idx = input.nextInt();
        input.nextLine();

        if (cl.getCuentaPorIndice(idx) == null) {
            System.out.println("Indice inválido.");
            return;
        }

        System.out.print("Monto a depositar (>0): ");
        double monto = input.nextDouble();
        input.nextLine();

        boolean ok = controller.depositarDineroCuenta(cedula, idx, monto);
        if (ok) {
            System.out.println("Deposito realizado");
        } else {
            System.out.println("No se pudo depositar.");
        }
    }

    private void retirarDineroCuenta() {
        System.out.print("Cedula del cliente: ");
        String cedula = input.nextLine().trim();

        Cliente cl = controller.buscarClientePorCedula(cedula);
        if (cl == null) {
            System.out.println("No existe un cliente con esa cedula.");
            return;
        }
        if (cl.getCuentasOcupadas() == 0) {
            System.out.println("El cliente no tiene cuentas registradas.");
            return;
        }

        listarCuentasDe(cedula);

        System.out.print("Indice de cuenta (empieza en 0): ");
        int idx = input.nextInt();
        input.nextLine();

        if (cl.getCuentaPorIndice(idx) == null) {
            System.out.println("Indice inválido.");
            return;
        }

        System.out.print("Monto a retirar (>0): ");
        double monto = input.nextDouble();
        input.nextLine();

        boolean ok = controller.retirarDineroCuenta(cedula, idx, monto);
        if (ok) {
            System.out.println("Retiro realizado");
        } else {
            System.out.println("No se pudo retirar (sin fondos o datos invalidos).");
        }
    }

    private void consultarCliente() {
        System.out.print("Cedula: ");
        String cedula = input.nextLine().trim();

        Cliente cl = controller.buscarClientePorCedula(cedula);
        if (cl == null) {
            System.out.println("No existe un cliente con esa cedula.");
            return;
        }

        System.out.println(cl);
        int n = cl.getCuentasOcupadas();
        for (int i = 0; i < n; i++) {
            Cuenta cta = cl.getCuentaPorIndice(i);
            System.out.println("  [" + i + "] " + cta.getNombreBanco() + " | " + cta.getTipo() + " | saldo: " + cta.getSaldo());
        }
        System.out.println("Saldo total cliente: " + cl.saldoTotalCliente());
    }

    private void consultarSaldoTotal() {
        System.out.println("Saldo total del sistema: " + controller.calcularSaldoTotalSistema());
    }
}
