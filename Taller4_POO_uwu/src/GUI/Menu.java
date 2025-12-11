package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
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

            // ID de la certificación seleccionada
            String id = (String) tablaCert.getValueAt(fila, 0);
            Certificacion c = sistema.buscarCertificacion(id);
            if (c == null) {
                JOptionPane.showMessageDialog(panelCoord,
                        "Certificación no encontrada en el sistema.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ---------- Pedimos los NUEVOS valores ----------
            String nuevoNombre = JOptionPane.showInputDialog(
                    panelCoord,
                    "Nuevo nombre:",
                    c.getNombre()
            );
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

            String nuevaDesc = JOptionPane.showInputDialog(
                    panelCoord,
                    "Nueva descripción:",
                    c.getDescripcion()
            );
            if (nuevaDesc == null || nuevaDesc.trim().isEmpty()) return;

            String txtCred = JOptionPane.showInputDialog(
                    panelCoord,
                    "Créditos necesarios:",
                    c.getCreditosRequeridos()
            );
            if (txtCred == null || txtCred.trim().isEmpty()) return;

            String txtAnios = JOptionPane.showInputDialog(
                    panelCoord,
                    "Años de validez:",
                    c.getAñosValidez()
            );
            if (txtAnios == null || txtAnios.trim().isEmpty()) return;

            try {
                int nuevosCreditos = Integer.parseInt(txtCred.trim());
                int nuevosAnios    = Integer.parseInt(txtAnios.trim());

                // Creamos una certificación modificada con los nuevos datos
                Certificacion modificada = new Certificacion(
                        id,
                        nuevoNombre.trim(),
                        nuevaDesc.trim(),
                        nuevosCreditos,
                        nuevosAnios
                );

                boolean ok = sistema.modificarLineaCertificacion(modificada);
                if (ok) {
                    JOptionPane.showMessageDialog(panelCoord,
                            "Línea de certificación modificada.");
                    refrescarTablaCertificaciones(tablaCert, columnas);
                } else {
                    JOptionPane.showMessageDialog(panelCoord,
                            "No se pudo modificar la certificación.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException exNum) {
                JOptionPane.showMessageDialog(panelCoord,
                        "Créditos y años de validez deben ser números enteros.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 2) Generar certificados (para quienes tienen progreso alto o estado aprobado)
        btnGenerarCertificados.addActionListener(e -> {

            // Elegir certificación desde la tabla
            int fila = tablaCert.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(panelCoord,
                        "Selecciona una certificación.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String idCert = (String) tablaCert.getValueAt(fila, 0);

            // Obtener registros asociados a esa certificación
            ArrayList<RegistroCertificacion> regs =
                    sistema.getRegistrosCertificacion(idCert);

            if (regs.isEmpty()) {
                mostrarTextoEnDialogo(panelCoord,
                        "Certificados",
                        "No hay estudiantes inscritos en esta certificación.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("CERTIFICADOS GENERADOS\n");
            sb.append("Certificación: ").append(idCert).append("\n\n");

            int count = 0;

            for (RegistroCertificacion r : regs) {

                boolean completado =
                        r.getEstado().equalsIgnoreCase("Completada")
                        || r.getProgreso() >= 100;

                if (!completado) continue; // no mostrar los que no han terminado

                Estudiante est = sistema.getEstudiante(r.getRutEstudiante());
                if (est == null) continue;

                count++;

                sb.append("• RUT: ").append(est.getRut()).append("\n");
                sb.append("  Nombre: ").append(est.getNombre()).append("\n");
                sb.append("  Carrera: ").append(est.getCarrera()).append("\n");
                sb.append("  Certificación completada en fecha: ")
                        .append(r.getFechaRegistro()).append("\n\n");
            }

            if (count == 0) {
                mostrarTextoEnDialogo(panelCoord,
                        "Certificados",
                        "Ningún estudiante ha completado esta certificación.");
                return;
            }

            mostrarTextoEnDialogo(panelCoord, "Certificados generados", sb.toString());
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
                    sb.append("• ").append(c.getNcr())
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
 // ======================== ESTUDIANTE ========================
   
 // ======================== ESTUDIANTE ========================
    private void vistaEstudiante(JTabbedPane tabs) {

        // Panel raíz de la pestaña Estudiante
        JPanel panelEst = new JPanel(new BorderLayout(10, 10));
        panelEst.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabs.addTab("Estudiante", panelEst);

        // ---------- ARRIBA: RUT + botón Cargar ----------
        JPanel panelRut = new JPanel(new FlowLayout());
        JLabel lblRut = new JLabel("RUT estudiante:");
        JTextField txtRut = new JTextField(15);
        JButton btnCargar = new JButton("Cargar datos");

        panelRut.add(lblRut);
        panelRut.add(txtRut);
        panelRut.add(btnCargar);

        panelEst.add(panelRut, BorderLayout.NORTH);

        // ---------- CENTRO: subtabs ----------
        JTabbedPane subTabs = new JTabbedPane();
        panelEst.add(subTabs, BorderLayout.CENTER);

        // =====================================================
        // 1) PERFIL + MALLA NORMAL (tabla completa con colores)
        // =====================================================
        JPanel panelPerfilMalla = new JPanel(new BorderLayout(5, 5));

        // Perfil
        JPanel panelPerfil = new JPanel();
        panelPerfil.setLayout(new BoxLayout(panelPerfil, BoxLayout.Y_AXIS));
        panelPerfil.setBorder(BorderFactory.createTitledBorder("Perfil del estudiante"));

        JLabel lblNombre  = new JLabel("Nombre: ");
        JLabel lblCarrera = new JLabel("Carrera: ");
        JLabel lblSemestre= new JLabel("Semestre: ");
        JLabel lblCorreo  = new JLabel("Correo: ");

        panelPerfil.add(lblNombre);
        panelPerfil.add(lblCarrera);
        panelPerfil.add(lblSemestre);
        panelPerfil.add(lblCorreo);

        // Malla (tabla normal)
        JPanel panelMalla = new JPanel(new BorderLayout());
        panelMalla.setBorder(BorderFactory.createTitledBorder("Malla curricular (todas las asignaturas)"));

        String[] colMalla = {"Código/NRC", "Asignatura", "Semestre", "Créditos", "Nota", "Estado"};
        DefaultTableModel modeloMalla = new DefaultTableModel(colMalla, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaMalla = new JTable(modeloMalla);
        tablaMalla.setDefaultRenderer(Object.class,
                new EstadoAsignaturaRenderer(colMalla.length - 1)); // última col = Estado

        JScrollPane scrollMalla = new JScrollPane(tablaMalla);
        panelMalla.add(scrollMalla, BorderLayout.CENTER);

        // Botones de promedio
        JPanel panelPromedios = new JPanel(new FlowLayout());
        JButton btnPromGeneral  = new JButton("Promedio general");
        JButton btnPromSemestre = new JButton("Promedio por semestre");
        panelPromedios.add(btnPromGeneral);
        panelPromedios.add(btnPromSemestre);

        panelMalla.add(panelPromedios, BorderLayout.SOUTH);

        panelPerfilMalla.add(panelPerfil, BorderLayout.WEST);
        panelPerfilMalla.add(panelMalla, BorderLayout.CENTER);

        subTabs.addTab("Perfil y malla", panelPerfilMalla);

        // =====================================================
        // 2) MALLA INTERACTIVA (tabla filtrada por semestre)
        // =====================================================
        JPanel panelMallaInteractiva = new JPanel(new BorderLayout(5, 5));
        panelMallaInteractiva.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panelSemestre = new JPanel(new FlowLayout());
        JLabel lblSemInt = new JLabel("Semestre:");
        Integer[] semestres = {1,2,3,4,5,6,7,8,9,10};
        JComboBox<Integer> comboSemestre = new JComboBox<>(semestres);
        JButton btnVerSemestre = new JButton("Ver malla semestre");
        panelSemestre.add(lblSemInt);
        panelSemestre.add(comboSemestre);
        panelSemestre.add(btnVerSemestre);

        panelMallaInteractiva.add(panelSemestre, BorderLayout.NORTH);

        String[] colMallaInt = {"Código/NRC", "Asignatura", "Semestre", "Créditos", "Nota", "Estado"};
        DefaultTableModel modeloMallaInt = new DefaultTableModel(colMallaInt, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaMallaInt = new JTable(modeloMallaInt);
        tablaMallaInt.setDefaultRenderer(Object.class,
                new EstadoAsignaturaRenderer(colMallaInt.length - 1));

        JScrollPane scrollMallaInt = new JScrollPane(tablaMallaInt);
        scrollMallaInt.setBorder(
                BorderFactory.createTitledBorder("Malla interactiva por semestre")
        );
        panelMallaInteractiva.add(scrollMallaInt, BorderLayout.CENTER);

        subTabs.addTab("Malla interactiva", panelMallaInteractiva);

        // Doble click en malla interactiva -> detalle asignatura
        tablaMallaInt.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablaMallaInt.getSelectedRow();
                    if (fila == -1) return;

                    String rut = txtRut.getText().trim();
                    if (rut.isEmpty()) {
                        JOptionPane.showMessageDialog(panelEst,
                                "Ingrese un RUT y cargue los datos primero.",
                                "Atención",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String nrc = (String) tablaMallaInt.getValueAt(fila, 0); // col 0 = NRC
                    mostrarDetalleAsignaturaInteractiva(rut, nrc, panelEst);
                }
            }
        });

        // =====================================================
        // 3) MALLA GRÁFICA (cajitas por semestre estilo malla UCN)
        // =====================================================
        JPanel panelMallaGraficaWrapper = new JPanel(new BorderLayout());
        JScrollPane scrollMallaGrafica = new JScrollPane();
        scrollMallaGrafica.setBorder(
                BorderFactory.createTitledBorder("Malla gráfica por semestre")
        );
        panelMallaGraficaWrapper.add(scrollMallaGrafica, BorderLayout.CENTER);

        subTabs.addTab("Malla gráfica", panelMallaGraficaWrapper);

        // =====================================================
        // 4) INSCRIPCIÓN A CERTIFICACIONES (TAB PROPIA)
        // =====================================================
        vistaInscripcionCertificaciones(subTabs, txtRut);

        // =====================================================
        // 5) SEGUIMIENTO / DASHBOARD
        // =====================================================
        JPanel panelSeguimiento = new JPanel(new BorderLayout(5, 5));
        JTextArea areaDash = new JTextArea();
        areaDash.setEditable(false);
        areaDash.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollDash = new JScrollPane(areaDash);
        scrollDash.setBorder(BorderFactory.createTitledBorder("Dashboard de progreso"));

        JPanel panelSegBotones = new JPanel(new FlowLayout());
        JButton btnVerInscripciones = new JButton("Ver certificaciones inscritas");
        panelSegBotones.add(btnVerInscripciones);

        panelSeguimiento.add(scrollDash, BorderLayout.CENTER);
        panelSeguimiento.add(panelSegBotones, BorderLayout.SOUTH);

        subTabs.addTab("Seguimiento", panelSeguimiento);

        // =====================================================
        //                   LISTENERS
        // =====================================================

        // Cargar perfil + mallas
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

            // Malla completa con colores
            actualizarMallaParaRut(rut, modeloMalla);

            // Malla gráfica estilo cajitas
            JPanel panelGraf = crearMallaGrafica(rut);
            scrollMallaGrafica.setViewportView(panelGraf);
        });

        // Promedio general
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

        // Promedio por semestre
        btnPromSemestre.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(panelEst,
                        "Ingrese un RUT y cargue datos primero.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer[] opcionesSem = {1,2,3,4,5,6,7,8,9,10};
            Integer semestreSel = (Integer) JOptionPane.showInputDialog(
                    panelEst,
                    "Seleccione semestre:",
                    "Promedio por semestre",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesSem,
                    opcionesSem[0]);

            if (semestreSel == null) return;

            double prom = sistema.calcularPromedioSemestre(rut, semestreSel);
            JOptionPane.showMessageDialog(panelEst,
                    "Promedio del semestre " + semestreSel + ": " + prom);
        });

        // Malla interactiva: ver por semestre
        btnVerSemestre.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(panelEst,
                        "Ingrese un RUT y cargue datos primero.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer sem = (Integer) comboSemestre.getSelectedItem();
            if (sem == null) return;

            actualizarMallaParaRutYSemestre(rut, sem, modeloMallaInt);
        });

        // Seguimiento: dashboard
        btnVerInscripciones.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(panelEst,
                        "Ingrese un RUT primero.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            ArrayList<RegistroCertificacion> regs = sistema.getCertificacionesInscritas(rut);
            StringBuilder sb = new StringBuilder();
            sb.append("Dashboard de certificaciones del estudiante ").append(rut).append("\n\n");
            if (regs.isEmpty()) {
                sb.append("El estudiante no está inscrito en ninguna certificación.");
            } else {
                for (RegistroCertificacion r : regs) {
                    Certificacion c = sistema.buscarCertificacion(r.getIdCertificacion());
                    String nombreCert = (c != null) ? c.getNombre() : r.getIdCertificacion();
                    sb.append("- ").append(nombreCert)
                      .append(" (").append(r.getIdCertificacion()).append(")")
                      .append(" | Estado: ").append(r.getEstado())
                      .append(" | Progreso: ").append(r.getProgreso()).append("%\n");
                }
            }
            areaDash.setText(sb.toString());
        });
    }

    private void refrescarTablaCertificaciones(JTable tabla, String[] columnas) {
        ArrayList<Certificacion> lista = sistema.getCertificaciones();
        Object[][] datos = new Object[lista.size()][columnas.length];

        for (int i = 0; i < lista.size(); i++) {
            Certificacion c = lista.get(i);
            datos[i][0] = c.getId();
            datos[i][1] = c.getNombre();
            datos[i][2] = c.getDescripcion();
            datos[i][3] = c.getCreditosRequeridos();
            datos[i][4] = c.getAñosValidez();
        }

        javax.swing.table.DefaultTableModel modelo =
                new javax.swing.table.DefaultTableModel(datos, columnas) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        tabla.setModel(modelo);
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    }
    private void mostrarTextoEnDialogo(java.awt.Component parent, String titulo, String contenido) {
        javax.swing.JDialog dialog = new javax.swing.JDialog();
        dialog.setTitle(titulo);
        dialog.setSize(600, 450);
        dialog.setLocationRelativeTo(parent);
        dialog.setModal(true);

        javax.swing.JTextArea area = new javax.swing.JTextArea();
        area.setEditable(false);
        area.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        area.setText(contenido);

        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(area);
        dialog.add(scroll);

        dialog.setVisible(true);
    }
 // ================= RENDERER PARA MALLA =================
    private static class EstadoAsignaturaRenderer extends javax.swing.table.DefaultTableCellRenderer {

        private int colEstado;

        public EstadoAsignaturaRenderer(int colEstado) {
            this.colEstado = colEstado;
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            java.awt.Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            // Por defecto
            if (!isSelected) {
                c.setBackground(java.awt.Color.WHITE);
            }

            if (column == colEstado) {
                String estado = (value == null) ? "" : value.toString();
                if (estado.equalsIgnoreCase("Aprobada")) {
                    c.setBackground(new java.awt.Color(198, 239, 206)); // verde suave
                } else if (estado.equalsIgnoreCase("Reprobada")) {
                    c.setBackground(new java.awt.Color(255, 199, 206)); // rojo suave
                } else if (estado.equalsIgnoreCase("Cursando")) {
                    c.setBackground(new java.awt.Color(255, 235, 156)); // amarillo suave
                } else if (estado.equalsIgnoreCase("Pendiente")) {
                    c.setBackground(new java.awt.Color(230, 230, 230)); // gris
                }
            }

            return c;
        }
    }
 // ================= ACTUALIZAR MALLA =================
    private void actualizarMallaParaRut(String rut, javax.swing.table.DefaultTableModel modeloMalla) {
        modeloMalla.setRowCount(0); // limpiar

        // Cursos de la malla (puede ser completa o filtrada según tu implementación)
        java.util.ArrayList<dominio.Curso> cursos = sistema.getMallaCompleta(rut);
        // Notas del estudiante
        java.util.ArrayList<dominio.Nota> notas = sistema.getNotasEstudiante(rut);

        // Mapa códigoAsignatura -> estado
        java.util.HashMap<String, dominio.Nota> mapaNotas = new java.util.HashMap<>();
        for (dominio.Nota n : notas) {
            mapaNotas.put(n.getCodigoAsignatura(), n);
        }

        for (dominio.Curso c : cursos) {
            dominio.Nota n = mapaNotas.get(c.getNcr());
            String estado = "Pendiente";
            Double nota = null;

            if (n != null) {
                estado = n.getEstado();
                nota = n.getCalificacion();
            }

            // Ajusta las columnas según tu modelo de malla
            // Ejemplo: {NRC, Nombre, Semestre, Créditos, Nota, Estado}
            Object[] fila = {
                    c.getNcr(),
                    c.getNombre(),
                    c.getSemestre(),
                    c.getCreditos(),
                    (nota == null ? "-" : nota),
                    estado
            };

            modeloMalla.addRow(fila);
        }
    }
 // Malla SOLO de un semestre específico (para la malla interactiva)
    private void actualizarMallaParaRutYSemestre(String rut,
                                                 int semestreFiltro,
                                                 javax.swing.table.DefaultTableModel modeloMalla) {
        modeloMalla.setRowCount(0); // limpiar

        java.util.ArrayList<dominio.Curso> cursos = sistema.getMallaCompleta(rut);
        java.util.ArrayList<dominio.Nota> notas  = sistema.getNotasEstudiante(rut);

        java.util.HashMap<String, dominio.Nota> mapaNotas = new java.util.HashMap<>();
        for (dominio.Nota n : notas) {
            mapaNotas.put(n.getCodigoAsignatura(), n);
        }

        for (dominio.Curso c : cursos) {
            // filtramos por semestre
            if (c.getSemestre() != semestreFiltro) continue;

            dominio.Nota n = mapaNotas.get(c.getNcr());
            String estado = "Pendiente";
            Double nota   = null;

            if (n != null) {
                estado = n.getEstado();
                nota   = n.getCalificacion();
            }

            Object[] fila = {
                    c.getNcr(),
                    c.getNombre(),
                    c.getSemestre(),
                    c.getCreditos(),
                    (nota == null ? "-" : nota),
                    estado
            };
            modeloMalla.addRow(fila);
        }
    }

 // INSCRIPCIÓN A CERTIFICACIONES 
    private void gestionarInscripcionCertificacion(String rut, java.awt.Component parent) {
        // Lista de certificaciones disponibles
        java.util.ArrayList<dominio.Certificacion> lista = sistema.getCertificaciones();
        if (lista.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(parent,
                    "No hay certificaciones disponibles.",
                    "Información",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] opciones = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            dominio.Certificacion c = lista.get(i);
            opciones[i] = c.getId() + " - " + c.getNombre();
        }

        String seleccion = (String) javax.swing.JOptionPane.showInputDialog(
                parent,
                "Selecciona una certificación para inscribirte:",
                "Inscripción a certificación",
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion == null) return; // canceló

        String idSeleccionado = seleccion.split(" - ")[0];

        // Primero verificar requisitos
        boolean cumple = sistema.verificarRequisitos(rut, idSeleccionado);
        if (!cumple) {
            javax.swing.JOptionPane.showMessageDialog(parent,
                    "No cumples los requisitos académicos para esta certificación.",
                    "Requisitos no cumplidos",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Intentar inscribir
        boolean ok = sistema.inscribirCertificacion(rut, idSeleccionado);
        if (ok) {
            javax.swing.JOptionPane.showMessageDialog(parent,
                    "Inscripción realizada correctamente.",
                    "Éxito",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else {
            javax.swing.JOptionPane.showMessageDialog(parent,
                    "No se pudo realizar la inscripción.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
 // Detalle de una asignatura en la malla interactiva
    private void mostrarDetalleAsignaturaInteractiva(String rut,
                                                     String nrc,
                                                     java.awt.Component parent) {

        // Buscar el curso en la malla completa
        java.util.ArrayList<dominio.Curso> cursos = sistema.getMallaCompleta(rut);
        dominio.Curso cursoSeleccionado = null;

        for (dominio.Curso c : cursos) {
            if (c.getNcr().equals(nrc)) {   // ajusta si usas otro código
                cursoSeleccionado = c;
                break;
            }
        }

        if (cursoSeleccionado == null) {
            JOptionPane.showMessageDialog(parent,
                    "No se encontró la asignatura con NRC " + nrc,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Notas del estudiante para esta asignatura
        java.util.ArrayList<dominio.Nota> notas = sistema.getNotasEstudiante(rut);
        dominio.Nota notaCurso = null;
        for (dominio.Nota n : notas) {
            // en Notas.txt el campo es "codigoAsignatura" -> coincide con el NRC del curso
            if (n.getCodigoAsignatura().equals(nrc)) {
                notaCurso = n;
                break;
            }
        }

        // Prerrequisitos del curso (el último campo del txt de cursos)
        String prereq = cursoSeleccionado.getPrerrequisitos();  // ajusta nombre del getter si difiere
        if (prereq == null || prereq.trim().isEmpty()) {
            prereq = "Sin prerrequisitos registrados.";
        }

        // "Ramos que abre": otros cursos que tienen este NRC en su lista de prerrequisitos
        java.util.ArrayList<dominio.Curso> abre = new java.util.ArrayList<>();
        for (dominio.Curso c : cursos) {
            String txt = c.getPrerrequisitos();
            if (txt != null && !txt.isEmpty() && txt.contains(nrc)) {
                abre.add(c);
            }
        }

        // Armamos el texto
        StringBuilder sb = new StringBuilder();

        sb.append("ASIGNATURA\n");
        sb.append("==========\n");
        sb.append("Nombre   : ").append(cursoSeleccionado.getNombre()).append("\n");
        sb.append("NRC      : ").append(cursoSeleccionado.getNcr()).append("\n");
        sb.append("Área     : ").append(cursoSeleccionado.getArea()).append("\n");
        sb.append("Semestre : ").append(cursoSeleccionado.getSemestre()).append("\n");
        sb.append("Créditos : ").append(cursoSeleccionado.getCreditos()).append("\n\n");

        sb.append("PRERREQUISITOS\n");
        sb.append("==============\n");
        sb.append(prereq).append("\n\n");

        sb.append("ESTADO DEL ESTUDIANTE\n");
        sb.append("======================\n");
        if (notaCurso != null) {
            sb.append("Nota   : ").append(notaCurso.getCalificacion()).append("\n");
            sb.append("Estado : ").append(notaCurso.getEstado()).append("\n");
            sb.append("Semestre cursado : ").append(notaCurso.getSemestre()).append("\n");
        } else {
            sb.append("La asignatura aún no ha sido cursada (pendiente).\n");
        }
        sb.append("\n");

        sb.append("RAMOS QUE HABILITA (que la tienen como prerrequisito)\n");
        sb.append("=====================================================\n");
        if (abre.isEmpty()) {
            sb.append("- Ninguno registrado.\n");
        } else {
            for (dominio.Curso c : abre) {
                sb.append("- ").append(c.getNcr())
                  .append(" : ").append(c.getNombre())
                  .append(" (sem ").append(c.getSemestre()).append(")\n");
            }
        }

        // Mostrar en diálogo con scroll
        mostrarTextoEnDialogo(parent, "Detalle de asignatura", sb.toString());
    }
 // =======================================================
 // MALLA GRÁFICA INTERACTIVA (cajitas por semestre)
 // =======================================================
 private JPanel crearMallaGrafica(String rut) {

     JPanel panel = new JPanel();
     panel.setLayout(new GridLayout(0, 1, 10, 10)); // una fila por semestre
     panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

     ArrayList<Curso> cursos = sistema.getMallaCompleta(rut);
     ArrayList<Nota> notas = sistema.getNotasEstudiante(rut);

     if (cursos == null || cursos.isEmpty()) {
         panel.add(new JLabel("No hay cursos disponibles para mostrar."));
         return panel;
     }

     // Agrupar cursos por semestre
     Map<Integer, ArrayList<Curso>> mapaSemestres = new HashMap<>();

     for (Curso c : cursos) {
         int sem = c.getSemestre();
         mapaSemestres.putIfAbsent(sem, new ArrayList<>());
         mapaSemestres.get(sem).add(c);
     }

     // Crear una sección por semestre
     for (int semestre : mapaSemestres.keySet()) {

         JPanel filaSem = new JPanel(new FlowLayout(FlowLayout.LEFT));
         filaSem.setBorder(BorderFactory.createTitledBorder("Semestre " + semestre));

         for (Curso c : mapaSemestres.get(semestre)) {

             // Determinar estado del curso
             String estado = "Pendiente";
             double nota = 0;

             for (Nota n : notas) {
                 if (n.getCodigoAsignatura().equals(c.getNcr())) {
                     nota = n.getCalificacion();
                     estado = n.getEstado();
                 }
             }

             // Crear cajita visual del curso
             JPanel cajita = new JPanel();
             cajita.setPreferredSize(new Dimension(180, 60));
             cajita.setBorder(BorderFactory.createLineBorder(Color.BLACK));

             JLabel lbl = new JLabel("<html><b>" + c.getNombre() + "</b><br/>NRC: "
                     + c.getNcr() + "<br/>" + estado + "</html>");
             cajita.add(lbl);

             // Colorear por estado
             if (estado.equalsIgnoreCase("Aprobada")) {
                 cajita.setBackground(new Color(135, 255, 135)); // verde
             } else if (estado.equalsIgnoreCase("Reprobada")) {
                 cajita.setBackground(new Color(255, 150, 150)); // rojo
             } else {
                 cajita.setBackground(new Color(230, 230, 255)); // celeste
             }

             // Hacer clic para ver detalle
             cajita.addMouseListener(new MouseAdapter() {
                 @Override
                 public void mouseClicked(MouseEvent e) {
                     mostrarDetalleAsignaturaInteractiva(rut, c.getNcr(), cajita);
                 }
             });

             filaSem.add(cajita);
         }

         panel.add(filaSem);
     }

     return panel;
 }
//================== ESTUDIANTE: Inscripción a certificaciones ==================
 
private void vistaInscripcionCertificaciones(JTabbedPane subTabs, JTextField txtRut) {

  JPanel panel = new JPanel(new BorderLayout(10, 10));

  // --------- TABLA DE LÍNEAS DE CERTIFICACIÓN ----------
  String[] columnas = {"ID", "Nombre", "Descripción", "Créditos req.", "Años validez"};
  JTable tablaCert = new JTable();
  // Usamos tu método existente
  refrescarTablaCertificaciones(tablaCert, columnas);

  JScrollPane scrollTabla = new JScrollPane(tablaCert);
  panel.add(scrollTabla, BorderLayout.CENTER);

  // --------- PANEL DETALLE (requisitos / descripción) ----------
  JTextArea areaDetalle = new JTextArea(6, 40);
  areaDetalle.setEditable(false);
  JScrollPane scrollDetalle = new JScrollPane(areaDetalle);
  scrollDetalle.setBorder(
          BorderFactory.createTitledBorder("Detalle / Requisitos de la certificación"));
  panel.add(scrollDetalle, BorderLayout.SOUTH);

  // Listener para mostrar detalle al seleccionar una fila
  tablaCert.getSelectionModel().addListSelectionListener(e -> {
      if (e.getValueIsAdjusting()) return;

      int fila = tablaCert.getSelectedRow();
      if (fila == -1) {
          areaDetalle.setText("");
          return;
      }

      String id = (String) tablaCert.getValueAt(fila, 0);
      Certificacion c = sistema.buscarCertificacion(id);
      if (c == null) {
          areaDetalle.setText("");
          return;
      }

      StringBuilder sb = new StringBuilder();
      sb.append("ID: ").append(c.getId()).append("\n");
      sb.append("Nombre: ").append(c.getNombre()).append("\n\n");
      sb.append("Descripción:\n").append(c.getDescripcion()).append("\n\n");
      sb.append("Créditos requeridos: ").append(c.getCreditosRequeridos()).append("\n");
      sb.append("Años de validez: ").append(c.getAñosValidez()).append("\n");

      areaDetalle.setText(sb.toString());
  });

  // --------- BOTONES (arriba o abajo, como tú prefieras) ----------
  JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
  JButton btnVerReq = new JButton("Ver requisitos");
  JButton btnInscribir = new JButton("Inscribirse");
  JButton btnVerInscritas = new JButton("Ver inscritas");

  panelBotones.add(btnVerReq);
  panelBotones.add(btnInscribir);
  panelBotones.add(btnVerInscritas);

  panel.add(panelBotones, BorderLayout.NORTH);

  // --------- VER REQUISITOS / DESCRIPCIÓN ----------
  btnVerReq.addActionListener(e -> {
      int fila = tablaCert.getSelectedRow();
      if (fila == -1) {
          JOptionPane.showMessageDialog(panel,
                  "Selecciona una línea de certificación.",
                  "Atención", JOptionPane.WARNING_MESSAGE);
          return;
      }

      // Simplemente mostramos lo que ya está en el área de detalle
      mostrarTextoEnDialogo(panel, "Requisitos y descripción", areaDetalle.getText());
  });

  // --------- INSCRIBIRSE (con validaciones y prerrequisitos) ----------
//--------- INSCRIBIRSE (con validaciones y prerrequisitos) ----------
btnInscribir.addActionListener(e -> {
   String rut = txtRut.getText().trim();
   if (rut.isEmpty()) {
       JOptionPane.showMessageDialog(panel,
               "Ingresa el RUT del estudiante y carga los datos primero.",
               "Atención", JOptionPane.WARNING_MESSAGE);
       return;
   }

   int fila = tablaCert.getSelectedRow();
   if (fila == -1) {
       JOptionPane.showMessageDialog(panel,
               "Selecciona una línea de certificación.",
               "Atención", JOptionPane.WARNING_MESSAGE);
       return;
   }

   String id = (String) tablaCert.getValueAt(fila, 0);

   // 1) Validar prerrequisitos académicos
   boolean cumple = sistema.verificarRequisitos(rut, id);
   if (!cumple) {

       // ← aquí usamos tu método para listar ramos que faltan
       ArrayList<Curso> faltantes =
               sistema.getRamosFaltantesCertificacion(rut, id);

       StringBuilder sb = new StringBuilder();
       sb.append("El estudiante NO cumple los requisitos académicos.\n\n");
       sb.append("Ramos faltantes para inscribir la certificación:\n\n");

       if (faltantes.isEmpty()) {
           sb.append("(No se encontraron ramos faltantes; revisa la configuración de la certificación.)\n");
       } else {
           for (Curso c : faltantes) {
               sb.append("- ").append(c.getNcr())
                 .append("  ").append(c.getNombre())
                 .append(" (Sem ").append(c.getSemestre())
                 .append(", ").append(c.getCreditos()).append(" créditos)\n");
           }
       }

       mostrarTextoEnDialogo(panel,
               "No cumple requisitos",
               sb.toString());
       return;
   }

   // 2) Realizar inscripción
   boolean ok = sistema.inscribirCertificacion(rut, id);
   if (ok) {
       JOptionPane.showMessageDialog(panel,
               "Inscripción realizada correctamente.");
   } else {
       JOptionPane.showMessageDialog(panel,
               "No se pudo realizar la inscripción.",
               "Error", JOptionPane.ERROR_MESSAGE);
   }
});


  // --------- VER CERTIFICACIONES INSCRITAS ----------
  btnVerInscritas.addActionListener(e -> {
      String rut = txtRut.getText().trim();
      if (rut.isEmpty()) {
          JOptionPane.showMessageDialog(panel,
                  "Ingresa el RUT del estudiante primero.",
                  "Atención", JOptionPane.WARNING_MESSAGE);
          return;
      }

      java.util.ArrayList<RegistroCertificacion> lista =
              sistema.getCertificacionesInscritas(rut);

      if (lista.isEmpty()) {
          JOptionPane.showMessageDialog(panel,
                  "El estudiante no tiene certificaciones inscritas.",
                  "Información", JOptionPane.INFORMATION_MESSAGE);
          return;
      }

      String[] cols = {"ID Cert.", "Nombre", "Fecha", "Estado", "Progreso"};
      Object[][] datos = new Object[lista.size()][cols.length];

      int i = 0;
      for (RegistroCertificacion r : lista) {
          Certificacion c = sistema.buscarCertificacion(r.getIdCertificacion());
          datos[i][0] = r.getIdCertificacion();
          datos[i][1] = (c != null ? c.getNombre() : "-");
          datos[i][2] = r.getFechaRegistro();
          datos[i][3] = r.getEstado();
          datos[i][4] = r.getProgreso() + "%";
          i++;
      }

      JTable tabla = new JTable(datos, cols);
      tabla.setDefaultEditor(Object.class, null);
      JScrollPane sp = new JScrollPane(tabla);

      
      // Si prefieres, usa directamente JOptionPane:
      // JOptionPane.showMessageDialog(panel, sp, "Certificaciones inscritas", JOptionPane.INFORMATION_MESSAGE);
      JOptionPane.showMessageDialog(panel, sp,
              "Certificaciones inscritas",
              JOptionPane.INFORMATION_MESSAGE);
  });

  // Añadir pestaña al subTab de Estudiante
  subTabs.addTab("Inscripción certificaciones", panel);
}

 







}
