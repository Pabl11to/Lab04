import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.PriorityQueue;
import java.util.Queue;

// Clase Paciente que implementa Comparable para poder ser ordenada por prioridad
public class Paciente implements Comparable<Paciente> {
    private String nombre;
    private int edad;
    private String afiliacion;  
    private String condicionEspecial; 

    public Paciente(String nombre, int edad, String afiliacion, String condicionEspecial) {
        this.nombre = nombre;
        this.edad = edad;
        this.afiliacion = afiliacion;
        this.condicionEspecial = condicionEspecial;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getAfiliacion() {
        return afiliacion;
    }

    public String getCondicionEspecial() {
        return condicionEspecial;
    }

    @Override
    public int compareTo(Paciente otro) {
        // Reglas de prioridad:
        // 1. Afiliados a Plan Complementario (PC) tienen mayor prioridad
        if (this.afiliacion.equals("PC") && !otro.afiliacion.equals("PC")) return -1;
        if (!this.afiliacion.equals("PC") && otro.afiliacion.equals("PC")) return 1;

        // 2. Embarazo o limitación motriz tienen prioridad
        if (!this.condicionEspecial.isEmpty() && otro.condicionEspecial.isEmpty()) return -1;
        if (this.condicionEspecial.isEmpty() && !otro.condicionEspecial.isEmpty()) return 1;

        // 3. Personas mayores de 60 o menores de 12 tienen prioridad
        if ((this.edad >= 60 || this.edad < 12) && !(otro.edad >= 60 || otro.edad < 12)) return -1;
        if (!(this.edad >= 60 || this.edad < 12) && (otro.edad >= 60 || otro.edad < 12)) return 1;

        // Si no hay prioridad, se mantienen en el orden de llegada
        return 0;
    }
}

// Clase que gestiona la cola de turnos de los pacientes
class Turnos {
    private Queue<Paciente> cola;

    public Turnos() {
        this.cola = new PriorityQueue<>();
    }

    public void agregarPaciente(Paciente paciente) {
        cola.offer(paciente);  
    }

    public Paciente siguientePaciente() {
        return cola.poll(); 
    }

    public boolean hayPacientes() {
        return !cola.isEmpty();
    }
}

// Clase principal que maneja la interfaz gráfica y la lógica de la aplicación
public class InterfazTurnos extends JFrame {
    private Turnos turnos = new Turnos();
    private JLabel lblTurnoActual;
    private JLabel lblTiempoRestante;
    private Timer timer;
    private int tiempoRestante = 5;  // 5 segundos por paciente

    public InterfazTurnos() {
        setTitle("Asignación de Turnos EPS");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Crear componentes de la interfaz
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 10, 100, 25);
        add(lblNombre);

        JTextField txtNombre = new JTextField();
        txtNombre.setBounds(120, 10, 200, 25);
        add(txtNombre);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setBounds(10, 50, 100, 25);
        add(lblEdad);

        JTextField txtEdad = new JTextField();
        txtEdad.setBounds(120, 50, 200, 25);
        add(txtEdad);

        JLabel lblAfiliacion = new JLabel("Afiliación:");
        lblAfiliacion.setBounds(10, 90, 100, 25);
        add(lblAfiliacion);

        JComboBox<String> cbAfiliacion = new JComboBox<>(new String[]{"POS", "PC"});
        cbAfiliacion.setBounds(120, 90, 200, 25);
        add(cbAfiliacion);

        JLabel lblCondicion = new JLabel("Condición Especial:");
        lblCondicion.setBounds(10, 130, 120, 25);
        add(lblCondicion);

        JComboBox<String> cbCondicion = new JComboBox<>(new String[]{"Ninguna", "Embarazo", "Limitación Motriz"});
        cbCondicion.setBounds(140, 130, 180, 25);
        add(cbCondicion);

        JButton btnAgregar = new JButton("Agregar Paciente");
        btnAgregar.setBounds(120, 170, 150, 25);
        add(btnAgregar);

        lblTurnoActual = new JLabel("Turno actual: Ninguno");
        lblTurnoActual.setBounds(10, 210, 300, 25);
        add(lblTurnoActual);

        lblTiempoRestante = new JLabel("Tiempo restante: 5 seg");
        lblTiempoRestante.setBounds(10, 240, 300, 25);
        add(lblTiempoRestante);

        JButton btnExtenderTiempo = new JButton("Extender Tiempo");
        btnExtenderTiempo.setBounds(120, 270, 150, 25);
        add(btnExtenderTiempo);

        // Acción del botón para agregar pacientes
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText();
                int edad = Integer.parseInt(txtEdad.getText());
                String afiliacion = (String) cbAfiliacion.getSelectedItem();
                String condicionEspecial = (String) cbCondicion.getSelectedItem();
                if (condicionEspecial.equals("Ninguna")) condicionEspecial = "";

                Paciente paciente = new Paciente(nombre, edad, afiliacion, condicionEspecial);
                turnos.agregarPaciente(paciente);
                JOptionPane.showMessageDialog(null, "Paciente agregado: " + nombre);
            }
        });

        // Timer para simular el paso del tiempo y el cambio de turnos
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tiempoRestante > 0) {
                    tiempoRestante--;
                    lblTiempoRestante.setText("Tiempo restante: " + tiempoRestante + " seg");
                } else {
                    Paciente siguiente = turnos.siguientePaciente();
                    if (siguiente != null) {
                        lblTurnoActual.setText("Turno actual: " + siguiente.getNombre());
                        tiempoRestante = 5;
                    } else {
                        lblTurnoActual.setText("Turno actual: Ninguno");
                    }
                }
            }
        });
        timer.start();

        // Acción del botón para extender el tiempo del paciente en curso
        btnExtenderTiempo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tiempoRestante += 5;
                lblTiempoRestante.setText("Tiempo restante: " + tiempoRestante + " seg");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazTurnos().setVisible(true);
        });
    }
}
