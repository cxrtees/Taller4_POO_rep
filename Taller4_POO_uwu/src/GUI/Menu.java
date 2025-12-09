package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dominio.Certificacion;
import dominio.Curso;
import dominio.Estudiante;
import dominio.Nota;
import dominio.RegistroCertificacion;
import dominio.Usuario;
import logica.SistemaIn;

public class Menu {

    private SistemaIn sistema;

    public Menu(SistemaIn sistema) {
        this.sistema = sistema;
    }

    public void iniciar() {
        // Cargar todos los datos desde los txt
        sistema.iniciarSistema();

        // Crear ventana principal
        JFrame ventana = new JFrame("AcademiCore");
        ventana.setSize(1000, 700);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);

        // Pestañas principales
        JTabbedPane tabs = new JTabbedPane();

        vistaAdministrador(tabs);
        vistaCoordinador(tabs);
        vistaEstudiante(tabs);

        ventana.add(tabs);
        ventana.setVisible(true);
    }

    // ============================================================
    //                         ADMINISTRADOR
    // ============================================================
    private void vistaAdministrador(JTabbedPane tabs) {
        JPanel panelAdmin = new JPanel(new BorderLayout(10, 10));
        panelAdmin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla de usuarios
        String[] columnas = {"Usuario", "Rol", "Info extra"};
        JTable tablaUsuarios = new JTable();
        refrescarTablaUsuarios(tablaUsuarios, columnas);

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        panelAdmin.add(scroll, BorderLayout.CENTER);

        // Panel de botones (CRUD)
        JPanel panelBotones = new JPanel(new FlowLayout());

        JButton btnCrear = new JButton("Crear usuario");
        JButton btnModificar = new JButton("Modificar usuario");
        JButton btnEliminar = new JButton("Eliminar usuario");
        JButton btnReset = new JButton("Restablecer contraseña");

        // CREAR
        btnCrear.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(panelAdmin, "Nombre de usuario:");
            if (nombre == null || nombre.trim().isEmpty()) return;

            String pass = JOptionPane.showInputDialog(panelAdmin, "Contraseña:");
            if (pass == null || pass.trim().isEmpty()) return;

            String rol = JOptionPane.showInputDialog(panelAdmin,
                    "Rol (COORDINADOR / ESTUDIANTE):");
            if (rol == null || rol.trim().isEmpty()) return;

            String extra = JOptionPane.showInputDialog(panelAdmin,
                    "Información adicional (opcional):");
            if (extra == null) extra = "";

            Usuario nuevo = new Usuario(nombre.trim(), pass.trim(), rol.trim(), extra.trim());
            boolean ok = sistema.crearCuenta(nuevo);

            if (ok) {
                JOptionPane.showMessageDialog(panelAdmin, "Usuario creado correctamente.");
                refrescarTablaUsuarios(tablaUsuarios, columnas);
            } else {
                JOptionPane.showMessageDialog(panelAdmin,
                        "No se pudo crear el usuario.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // MODIFICAR
     // MODIFICAR
        btnModificar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(panelAdmin,
                        "Selecciona un usuario en la tabla.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombreActual = (String) tablaUsuarios.getValueAt(fila, 0);
            String rolActual    = (String) tablaUsuarios.getValueAt(fila, 1);
            String extraActual  = (String) tablaUsuarios.getValueAt(fila, 2);

            // ---- RESTRICCIÓN: solo se pueden modificar ESTUDIANTE o COORDINADOR ----
            if (!rolActual.equalsIgnoreCase("ESTUDIANTE") &&
                !rolActual.equalsIgnoreCase("COORDINADOR")) {

                JOptionPane.showMessageDialog(panelAdmin,
                        "Solo se pueden modificar cuentas de ESTUDIANTE o COORDINADOR.",
                        "Operación no permitida",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nuevoRol = JOptionPane.showInputDialog(panelAdmin,
                    "Nuevo rol (ESTUDIANTE / COORDINADOR):", rolActual);

            if (nuevoRol == null || nuevoRol.trim().isEmpty())
                return;

            nuevoRol = nuevoRol.trim().toUpperCase();

            // Opcional: también restringimos el rol nuevo
            if (!nuevoRol.equals("ESTUDIANTE") && !nuevoRol.equals("COORDINADOR")) {
                JOptionPane.showMessageDialog(panelAdmin,
                        "El rol debe ser ESTUDIANTE o COORDINADOR.",
                        "Rol inválido",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nuevoExtra = JOptionPane.showInputDialog(panelAdmin,
                    "Nueva info adicional:", extraActual);
            if (nuevoExtra == null) nuevoExtra = "";

            // Pedimos nueva contraseña opcionalmente
            String nuevaPass = JOptionPane.showInputDialog(panelAdmin,
                    "Nueva contraseña (deja vacío para no cambiar):");

            Usuario usuarioBase = buscarUsuarioPorNombre(nombreActual);
            if (usuarioBase == null) {
                JOptionPane.showMessageDialog(panelAdmin,
                        "Usuario no encontrado en el sistema.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String passFinal = usuarioBase.getContraseña();
            if (nuevaPass != null && !nuevaPass.trim().isEmpty()) {
                passFinal = nuevaPass.trim();
            }

            Usuario modificado = new Usuario(
                    nombreActual,
                    passFinal,
                    nuevoRol,
                    nuevoExtra.trim()
            );

            boolean ok = sistema.modificarCuenta(modificado);

            if (ok) {
                JOptionPane.showMessageDialog(panelAdmin, "Usuario modificado.");
                refrescarTablaUsuarios(tablaUsuarios, columnas);
            } else {
                JOptionPane.showMessageDialog(panelAdmin,
                        "No se pudo modificar el usuario.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // ELIMINAR
        btnEliminar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(panelAdmin,
                        "Selecciona un usuario en la tabla.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombre = (String) tablaUsuarios.getValueAt(fila, 0);
            String rol    = (String) tablaUsuarios.getValueAt(fila, 1);

            // ---- RESTRICCIÓN: no permitir eliminar administradores ----
            if (rol.equalsIgnoreCase("ADMIN")) {
                JOptionPane.showMessageDialog(panelAdmin,
                        "No se pueden eliminar cuentas de ADMINISTRADOR.",
                        "Operación no permitida",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int resp = JOptionPane.showConfirmDialog(panelAdmin,
                    "¿Eliminar usuario '" + nombre + "'?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (resp != JOptionPane.YES_OPTION)
                return;

            boolean ok = sistema.eliminarCuenta(nombre);

            if (ok) {
                JOptionPane.showMessageDialog(panelAdmin, "Usuario eliminado.");
                refrescarTablaUsuarios(tablaUsuarios, columnas);
            } else {
                JOptionPane.showMessageDialog(panelAdmin,
                        "No se pudo eliminar el usuario.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });


        // RESET CONTRASEÑA
        btnReset.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(panelAdmin,
                        "Selecciona un usuario en la tabla.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nombre = (String) tablaUsuarios.getValueAt(fila, 0);
            boolean ok = sistema.restablecerContrasena(nombre);
            if (ok) {
                JOptionPane.showMessageDialog(panelAdmin,
                        "Contraseña restablecida a '1234'.");
            } else {
                JOptionPane.showMessageDialog(panelAdmin,
                        "No se pudo restablecer la contraseña.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panelBotones.add(btnCrear);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnReset);

        panelAdmin.add(panelBotones, BorderLayout.SOUTH);

        tabs.addTab("Administrador", panelAdmin);
    }

    private void refrescarTablaUsuarios(JTable tabla, String[] columnas) {
        ArrayList<Usuario> lista = sistema.getUsuarios();
        Object[][] datos = new Object[lista.size()][columnas.length];

        for (int i = 0; i < lista.size(); i++) {
            Usuario u = lista.get(i);
            datos[i][0] = u.getNombreUsuario();
            datos[i][1] = u.getRol();
            datos[i][2] = u.getArea();
        }

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla.setModel(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private Usuario buscarUsuarioPorNombre(String nombre) {
        for (Usuario u : sistema.getUsuarios()) {
            if (u.getNombreUsuario().equals(nombre)) return u;
        }
        return null;
    }

    // ============================================================
    //                         COORDINADOR
    // ============================================================
 // ================== COORDINADOR ==================
    private void vistaCoordinador(JTabbedPane tabs) {
        JPanel panelCoord = new JPanel(new BorderLayout(10, 10));
        panelCoord.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla de certificaciones
        String[] columnas = {"ID", "Nombre", "Descripción", "Créditos req.", "Años validez"};
        JTable tablaCert = new JTable();
        refrescarTablaCertificaciones(tablaCert, columnas);
        JScrollPane scroll = new JScrollPane(tablaCert);
        panelCoord.add(scroll, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        JButton btnModificarLinea       = new JButton("Modificar línea");
        JButton btnGenerarCertificados  = new JButton("Generar certificados");
        JButton btnEstadisticas         = new JButton("Mostrar estadísticas");
        JButton btnAsignCriticas        = new JButton("Asignaturas críticas");
        JButton btnPerfiles             = new JButton("Perfiles estudiantes");
        JButton btnValidarAvance        = new JButton("Validar avance académico");

        panelBotones.add(btnModificarLinea);
        panelBotones.add(btnGenerarCertificados);
        panelBotones.add(btnEstadisticas);
        panelBotones.add(btnAsignCriticas);
        panelBotones.add(btnPerfiles);
        panelBotones.add(btnValidarAvance);

        panelCoord.add(panelBotones, BorderLayout.SOUTH);

        // ================== LISTENERS ==================

        // 1) Modificar línea de certificación
        btnModificarLinea.addActionListener(e -> {
            int fila = tablaCert.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(panelCoord,
                        "Selecciona una certificación.",
                        "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = (String) tablaCert.getValueAt(fila, 0);
            Certificacion c = sistema.buscarCertificacion(id);
            if (c == null) {
                JOptionPane.showMessageDialog(panelCoord,
                        "Certificación no encontrada en el sistema.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nuevoNombre = JOptionPane.showInputDialog(panelCoord,
                    "Nuevo nombre:", c.getNombre());
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

            String nuevaDesc = JOptionPane.showInputDialog(panelCoord,
                    "Nueva descripción:", c.getDescripcion());
            if (nuevaDesc == null || nuevaDesc.trim().isEmpty()) return;

            String txtCred = JOptionPane.showInputDialog(panelCoord,
                    "Créditos requeridos:", c.getCreditosRequeridos());
            if (txtCred == null || txtCred.trim().isEmpty()) return;

            String txtAnios = JOptionPane.showInputDialog(panelCoord,
                    "Años de validez:", c.getAñosValidez());
            if (txtAnios == null || txtAnios.trim().isEmpty()) return;

            try {
                int cred = Integer.parseInt(txtCred.trim());
                int anios = Integer.parseInt(txtAnios.trim());

                Certificacion modificada = new Certificacion(
                        id,
                        nuevoNombre.trim(),
                        nuevaDesc.trim(),
                        cred,
                        anios
                );

                boolean ok = sistema.modificarLineaCertificacion(modificada);
                if (ok) {
                    JOptionPane.showMessageDialog(panelCoord,
                            "Certificación modificada.");
                    refrescarTablaCertificaciones(tablaCert, columnas);
                } else {
                    JOptionPane.showMessageDialog(panelCoord,
                            "No se pudo modificar la certificación.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panelCoord,
                        "Créditos y años deben ser números.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 2) Generar certificados (para quienes tienen progreso alto o estado aprobado)
        btnGenerarCertificados.addActionListener(e -> {
            int fila = tablaCert.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(panelCoord,
                        "Selecciona una certificación.",
                        "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = (String) tablaCert.getValueAt(fila, 0);
            ArrayList<RegistroCertificacion> regs = sistema.getRegistrosCertificacion(id);

            if (regs.isEmpty()) {
                JOptionPane.showMessageDialog(panelCoord,
                        "No hay inscritos para generar certificados.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Certificados generados para ").append(id).append(":\n\n");

            for (RegistroCertificacion r : regs) {
                // criterio simple: progreso >= 100 o estado ya aprobado/completado
                if (r.getProgreso() >= 100 ||
                    r.getEstado().equalsIgnoreCase("APROBADA") ||
                    r.getEstado().equalsIgnoreCase("COMPLETADA")) {

                    Estudiante es = sistema.getEstudiante(r.getRutEstudiante());
                    String nombreEst = (es != null) ? es.getNombre() : "(desconocido)";
                    sb.append("• ").append(r.getRutEstudiante())
                      .append(" - ").append(nombreEst)
                      .append("  -> CERTIFICADO\n");
                }
            }

            mostrarTextoEnDialogo(panelCoord, "Generar certificados", sb.toString());
        });

        // 3) Mostrar estadísticas (inscritos y progreso promedio por certificación)
        btnEstadisticas.addActionListener(e -> {
            ArrayList<Certificacion> lista = sistema.getCertificaciones();
            StringBuilder sb = new StringBuilder();
            sb.append("Estadísticas de certificaciones\n\n");

            for (Certificacion c : lista) {
                ArrayList<RegistroCertificacion> regs =
                        sistema.getRegistrosCertificacion(c.getId());
                int inscritos = regs.size();
                double sumaProg = 0;
                for (RegistroCertificacion r : regs) {
                    sumaProg += r.getProgreso();
                }
                double promedioProg = inscritos == 0 ? 0 : sumaProg / inscritos;

                sb.append(c.getId()).append(" - ").append(c.getNombre())
                  .append(" | Inscritos: ").append(inscritos)
                  .append(" | Progreso promedio: ")
                  .append(String.format("%.1f", promedioProg)).append("%\n");
            }

            mostrarTextoEnDialogo(panelCoord, "Estadísticas", sb.toString());
        });

        // 4) Análisis de asignaturas críticas
        btnAsignCriticas.addActionListener(e -> {
            int fila = tablaCert.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(panelCoord,
                        "Selecciona una certificación.",
                        "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = (String) tablaCert.getValueAt(fila, 0);
            ArrayList<Curso> criticas = sistema.obtenerAsignaturasCriticas(id);

            StringBuilder sb = new StringBuilder();
            sb.append("Asignaturas críticas para ").append(id).append(":\n\n");

            if (criticas.isEmpty()) {
                sb.append("(No hay asignaturas críticas definidas)");
            } else {
                for (Curso c : criticas) {
                    sb.append("• ").append(c.getNrc())
                      .append(" - ").append(c.getNombre()).append("\n");
                }
            }

            mostrarTextoEnDialogo(panelCoord, "Asignaturas críticas", sb.toString());
        });

        // 5) Consultar perfiles (ya lo tenías como "Perfiles estudiantes")
        btnPerfiles.addActionListener(e -> {
            int fila = tablaCert.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(panelCoord,
                        "Selecciona una certificación.",
                        "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = (String) tablaCert.getValueAt(fila, 0);
            ArrayList<Estudiante> ests = sistema.obtenerPerfilesDeCertificacion(id);

            StringBuilder sb = new StringBuilder();
            sb.append("Perfiles de estudiantes inscritos en ").append(id).append(":\n\n");
            if (ests.isEmpty()) {
                sb.append("(Sin estudiantes inscritos)");
            } else {
                for (Estudiante es : ests) {
                    sb.append(es.getRut()).append(" - ").append(es.getNombre())
                      .append(" (").append(es.getCarrera()).append(")\n");
                }
            }

            mostrarTextoEnDialogo(panelCoord, "Perfiles estudiantes", sb.toString());
        });

        // 6) Revisar y validar avances académicos
        btnValidarAvance.addActionListener(e -> {
            int fila = tablaCert.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(panelCoord,
                        "Selecciona una certificación.",
                        "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = (String) tablaCert.getValueAt(fila, 0);
            ArrayList<RegistroCertificacion> regs = sistema.getRegistrosCertificacion(id);

            if (regs.isEmpty()) {
                JOptionPane.showMessageDialog(panelCoord,
                        "No hay estudiantes inscritos en esta certificación.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Revisión de avance académico para ").append(id).append(":\n\n");

            for (RegistroCertificacion r : regs) {
                Estudiante es = sistema.getEstudiante(r.getRutEstudiante());
                String nombreEst = (es != null) ? es.getNombre() : "(desconocido)";

                // lógica simple: si progreso >= 100, marcamos completado
                if (r.getProgreso() >= 100 &&
                    !r.getEstado().equalsIgnoreCase("COMPLETADA")) {
                    r.setEstado("COMPLETADA");   // necesita setEstado(...) en RegistroCertificacion
                }

                sb.append(es.getRut()).append(" - ").append(nombreEst)
                  .append(" | Progreso: ").append(r.getProgreso()).append("%")
                  .append(" | Estado: ").append(r.getEstado()).append("\n");
            }

            mostrarTextoEnDialogo(panelCoord, "Validación de avance", sb.toString());
        });

        tabs.addTab("Coordinador", panelCoord);
    }


    // ============================================================
    //                         ESTUDIANTE
    // ============================================================
    private void vistaEstudiante(JTabbedPane tabs) {
        JPanel panelEst = new JPanel(new BorderLayout(10, 10));
        panelEst.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: ingresar RUT
        JPanel panelRut = new JPanel(new FlowLayout());
        JLabel lblRut = new JLabel("RUT estudiante:");
        JTextField txtRut = new JTextField(15);
        JButton btnCargar = new JButton("Cargar datos");

        panelRut.add(lblRut);
        panelRut.add(txtRut);
        panelRut.add(btnCargar);

        panelEst.add(panelRut, BorderLayout.NORTH);

        // Panel centro: perfil + notas
        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));

        // Perfil
        JPanel panelPerfil = new JPanel();
        panelPerfil.setLayout(new BoxLayout(panelPerfil, BoxLayout.Y_AXIS));
        panelPerfil.setBorder(BorderFactory.createTitledBorder("Perfil"));

        JLabel lblNombre = new JLabel("Nombre: ");
        JLabel lblCarrera = new JLabel("Carrera: ");
        JLabel lblSemestre = new JLabel("Semestre: ");
        JLabel lblCorreo = new JLabel("Correo: ");

        panelPerfil.add(lblNombre);
        panelPerfil.add(lblCarrera);
        panelPerfil.add(lblSemestre);
        panelPerfil.add(lblCorreo);

        // Notas
        JPanel panelNotas = new JPanel(new BorderLayout());
        panelNotas.setBorder(BorderFactory.createTitledBorder("Notas"));

        String[] colNotas = {"Asignatura", "Nota", "Estado", "Semestre"};
        JTable tablaNotas = new JTable();
        DefaultTableModel modeloNotas = new DefaultTableModel(colNotas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaNotas.setModel(modeloNotas);
        JScrollPane scrollNotas = new JScrollPane(tablaNotas);
        panelNotas.add(scrollNotas, BorderLayout.CENTER);

        panelCentro.add(panelPerfil, BorderLayout.WEST);
        panelCentro.add(panelNotas, BorderLayout.CENTER);

        panelEst.add(panelCentro, BorderLayout.CENTER);

        // Panel inferior: promedios
        JPanel panelInferior = new JPanel(new FlowLayout());
        JButton btnPromGeneral = new JButton("Promedio general");
        JButton btnPromSemestre = new JButton("Promedio por semestre");

        panelInferior.add(btnPromGeneral);
        panelInferior.add(btnPromSemestre);

        panelEst.add(panelInferior, BorderLayout.SOUTH);

        // ------------ ACCIONES --------------
        btnCargar.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(panelEst,
                        "Ingrese un RUT.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Estudiante es = sistema.getEstudiante(rut);
            if (es == null) {
                JOptionPane.showMessageDialog(panelEst,
                        "No se encontró estudiante con rut " + rut,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            lblNombre.setText("Nombre: " + es.getNombre());
            lblCarrera.setText("Carrera: " + es.getCarrera());
            lblSemestre.setText("Semestre: " + es.getSemestre());
            lblCorreo.setText("Correo: " + es.getCorreo());

            // cargar notas
            ArrayList<Nota> notas = sistema.getNotasEstudiante(rut);
            modeloNotas.setRowCount(0); // limpiar
            for (Nota n : notas) {
                Object[] fila = {
                        n.getCodigoAsignatura(),
                        n.getCalificacion(),
                        n.getEstado(),
                        n.getSemestre()
                };
                modeloNotas.addRow(fila);
            }
        });

        btnPromGeneral.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(panelEst,
                        "Ingrese un RUT y cargue datos primero.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            double prom = sistema.calcularPromedioGeneral(rut);
            JOptionPane.showMessageDialog(panelEst,
                    "Promedio general del estudiante: " + prom);
        });

        btnPromSemestre.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(panelEst,
                        "Ingrese un RUT y cargue datos primero.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String s = JOptionPane.showInputDialog(panelEst,
                    "Ingrese semestre (número):");
            if (s == null || s.trim().isEmpty()) return;

            try {
                int sem = Integer.parseInt(s.trim());
                double prom = sistema.calcularPromedioSemestre(rut, sem);
                JOptionPane.showMessageDialog(panelEst,
                        "Promedio del semestre " + sem + ": " + prom);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panelEst,
                        "Debe ser un número entero.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        tabs.addTab("Estudiante", panelEst);
    }
}
