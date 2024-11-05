import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

class Vehiculo {
    private String placa;
    private String tipo;
    private int horaIngreso;
    private int numero;
    private int tarifaPorMinuto;

    public Vehiculo(String placa, String tipo, int horaIngreso, int numero, int tarifaPorMinuto) {
        this.placa = placa;
        this.tipo = tipo;
        this.horaIngreso = horaIngreso;
        this.numero = numero;
        this.tarifaPorMinuto = tarifaPorMinuto;
    }

    public String getPlaca() {
        return placa;
    }

    public String getTipo() {
        return tipo;
    }

    public int getHoraIngreso() {
        return horaIngreso;
    }

    public int getNumero() {
        return numero;
    }

    public int getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }

    public int calcularValor(int horaActual) {
        int minutos = (horaActual - horaIngreso) * 60;
        return minutos * tarifaPorMinuto;
    }
}

public class Parqueadero extends JFrame {
    private List<Vehiculo> vehiculos;
    private Stack<Vehiculo> dosRuedas;
    private Stack<Vehiculo> cuatroRuedas;
    private DefaultTableModel modeloTabla;
    private int numeroVehiculo;

    public Parqueadero() {
        vehiculos = new ArrayList<>();
        dosRuedas = new Stack<>();
        cuatroRuedas = new Stack<>();
        numeroVehiculo = 1;

        setTitle("Administración de Parqueadero");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

      
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones");
        JMenuItem ingreso = new JMenuItem("1. Ingreso de vehículo");
        JMenuItem visualizarTabla = new JMenuItem("2. Visualizar tabla");
        JMenuItem visualizarDosRuedas = new JMenuItem("3. Vehículos de 2 ruedas");
        JMenuItem visualizarCuatroRuedas = new JMenuItem("4. Vehículos de 4 ruedas");
        JMenuItem cantidadValorTotal = new JMenuItem("5. Cantidad y valor total");
        JMenuItem eliminarVehiculo = new JMenuItem("6. Eliminar vehículo");
        JMenuItem salir = new JMenuItem("7. Salir");

        menu.add(ingreso);
        menu.add(visualizarTabla);
        menu.add(visualizarDosRuedas);
        menu.add(visualizarCuatroRuedas);
        menu.add(cantidadValorTotal);
        menu.add(eliminarVehiculo);
        menu.add(salir);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Tabla para mostrar los vehículos
        String[] columnas = {"Placa", "Tipo", "Hora Ingreso", "Número", "Valor a Pagar"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaVehiculos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaVehiculos);
        scrollPane.setBounds(50, 50, 500, 200);
        add(scrollPane);

        // Acción para ingresar un vehículo
        ingreso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
                String[] tipos = {"Bicicleta", "Ciclomotor", "Motocicleta", "Carro"};
                String tipo = (String) JOptionPane.showInputDialog(null, "Tipo de vehículo:", "Seleccionar",
                        JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
                int horaIngreso = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la hora de ingreso (0-24):"));
                int tarifaPorMinuto = 0;

                switch (tipo) {
                    case "Bicicleta":
                    case "Ciclomotor":
                        tarifaPorMinuto = 20;
                        break;
                    case "Motocicleta":
                        tarifaPorMinuto = 30;
                        break;
                    case "Carro":
                        tarifaPorMinuto = 60;
                        break;
                }

                Vehiculo vehiculo = new Vehiculo(placa, tipo, horaIngreso, numeroVehiculo++, tarifaPorMinuto);
                vehiculos.add(vehiculo);

                if (tipo.equals("Bicicleta") || tipo.equals("Ciclomotor") || tipo.equals("Motocicleta")) {
                    dosRuedas.push(vehiculo);
                } else {
                    cuatroRuedas.push(vehiculo);
                }

                JOptionPane.showMessageDialog(null, "Vehículo ingresado exitosamente.");
            }
        });

        // Acción para visualizar la tabla con la información actualizada
        visualizarTabla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeloTabla.setRowCount(0);  // Limpiar tabla
                int horaActual = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la hora actual (0-24):"));
                for (Vehiculo vehiculo : vehiculos) {
                    int valor = vehiculo.calcularValor(horaActual);
                    modeloTabla.addRow(new Object[]{vehiculo.getPlaca(), vehiculo.getTipo(),
                            vehiculo.getHoraIngreso(), vehiculo.getNumero(), valor});
                }
            }
        });

        
        visualizarDosRuedas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stack<Vehiculo> copiaDosRuedas = (Stack<Vehiculo>) dosRuedas.clone();
                int horaActual = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la hora actual (0-24):"));
                StringBuilder sb = new StringBuilder();
                int total = 0;

                while (!copiaDosRuedas.isEmpty()) {
                    Vehiculo vehiculo = copiaDosRuedas.pop();
                    int valor = vehiculo.calcularValor(horaActual);
                    sb.append("Placa: ").append(vehiculo.getPlaca()).append(", Valor a pagar: ").append(valor).append(" COP\n");
                    total += valor;
                }

                sb.append("\nTotal a pagar por vehículos de 2 ruedas: ").append(total).append(" COP");
                JOptionPane.showMessageDialog(null, sb.toString());
            }
        });

        
        visualizarCuatroRuedas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stack<Vehiculo> copiaCuatroRuedas = (Stack<Vehiculo>) cuatroRuedas.clone();
                int horaActual = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la hora actual (0-24):"));
                StringBuilder sb = new StringBuilder();
                int total = 0;

                while (!copiaCuatroRuedas.isEmpty()) {
                    Vehiculo vehiculo = copiaCuatroRuedas.pop();
                    int valor = vehiculo.calcularValor(horaActual);
                    sb.append("Placa: ").append(vehiculo.getPlaca()).append(", Valor a pagar: ").append(valor).append(" COP\n");
                    total += valor;
                }

                sb.append("\nTotal a pagar por vehículos de 4 ruedas: ").append(total).append(" COP");
                JOptionPane.showMessageDialog(null, sb.toString());
            }
        });

       
        cantidadValorTotal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cantidad = vehiculos.size();
                int horaActual = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la hora actual (0-24):"));
                int total = 0;

                for (Vehiculo vehiculo : vehiculos) {
                    total += vehiculo.calcularValor(horaActual);
                }

                JOptionPane.showMessageDialog(null, "Cantidad de vehículos en el parqueadero: " + cantidad +
                        "\nValor total a pagar: " + total + " COP");
            }
        });

        
        eliminarVehiculo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo a eliminar:");
                Vehiculo vehiculoAEliminar = null;

                for (Vehiculo vehiculo : vehiculos) {
                    if (vehiculo.getPlaca().equals(placa)) {
                        vehiculoAEliminar = vehiculo;
                        break;
                    }
                }

                if (vehiculoAEliminar != null) {
                    vehiculos.remove(vehiculoAEliminar);
                    JOptionPane.showMessageDialog(null, "Vehículo eliminado.");
                } else {
                    JOptionPane.showMessageDialog(null, "Vehículo no encontrado.");
                }
            }
        });

     
        salir.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Parqueadero().setVisible(true));
    }
}
