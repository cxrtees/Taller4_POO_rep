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
    private void vistaEstudiante(JTabbedPane tabs) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --------- ARRIBA: RUT + botón Cargar (común a todas las subtabs) ----------
        JPanel panelRut = new JPanel(new FlowLayout());
        JLabel lblRut = new JLabel("RUT estudiante:");
        JTextField txtRut = new JTextField(15);
        JButton btnCargar = new JButton("Cargar datos");
        panelRut.add(lblRut);
        panelRut.add(txtRut);
        panelRut.add(btnCargar);
        root.add(panelRut, BorderLayout.NORTH);

        // --------- CENTRO: subtabs ---------
        JTabbedPane subTabs = new JTabbedPane();
        root.add(subTabs, BorderLayout.CENTER);

        // ============ 1) PERFIL Y MALLA ============
        JPanel panelPerfilMalla = new JPanel(new BorderLayout(5, 5));

        // Perfil
        JPanel panelPerfil = new JPanel();
        panelPerfil.setLayout(new BoxLayout(panelPerfil, BoxLayout.Y_AXIS));
        panelPerfil.setBorder(BorderFactory.createTitledBorder("Perfil del estudiante"));

        JLabel lblNombre = new JLabel("Nombre: ");
        JLabel lblCarrera = new JLabel("Carrera: ");
        JLabel lblSemestre = new JLabel("Semestre: ");
        JLabel lblCorreo = new JLabel("Correo: ");

        panelPerfil.add(lblNombre);
        panelPerfil.add(lblCarrera);
        panelPerfil.add(lblSemestre);
        panelPerfil.add(lblCorreo);

        // Malla (tabla)
        JPanel panelMalla = new JPanel(new BorderLayout());
        panelMalla.setBorder(BorderFactory.createTitledBorder("Malla curricular"));

        String[] colMalla = {"Código/NRC", "Nombre asignatura", "Semestre", "Créditos", "Estado"};
        DefaultTableModel modeloMalla = new DefaultTableModel(colMalla, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaMalla = new JTable(modeloMalla);
        // renderer para colorear por estado
        tablaMalla.setDefaultRenderer(Object.class,
                new EstadoAsignaturaRenderer(colMalla.length - 1)); // última col = Estado

        JScrollPane scrollMalla = new JScrollPane(tablaMalla);
        panelMalla.add(scrollMalla, BorderLayout.CENTER);

        // Panel inferior: botones de promedio
        JPanel panelPromedios = new JPanel(new FlowLayout());
        JButton btnPromGeneral = new JButton("Promedio general");
        JButton btnPromSemestre = new JButton("Promedio por semestre");
        panelPromedios.add(btnPromGeneral);
        panelPromedios.add(btnPromSemestre);

        panelMalla.add(panelPromedios, BorderLayout.SOUTH);

        panelPerfilMalla.add(panelPerfil, BorderLayout.WEST);
        panelPerfilMalla.add(panelMalla, BorderLayout.CENTER);

        subTabs.addTab("Perfil y malla", panelPerfilMalla);

        // ============ 2) INSCRIPCIÓN CERTIFICACIONES ============
        JPanel panelInscripcion = new JPanel(new BorderLayout(5, 5));
        panelInscripcion.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JTextArea areaCerts = new JTextArea();
        areaCerts.setEditable(false);
        areaCerts.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollCerts = new JScrollPane(areaCerts);
        scrollCerts.setBorder(BorderFactory.createTitledBorder("Líneas / certificaciones disponibles"));

        JPanel panelInsBotones = new JPanel(new FlowLayout());
        JButton btnListarLineas = new JButton("Listar líneas");
        JButton btnVerRequisitos = new JButton("Mostrar requisitos");
        JButton btnInscribir = new JButton("Inscribirse");
        JButton btnVerificarReq = new JButton("Verificar requisitos");

        panelInsBotones.add(btnListarLineas);
        panelInsBotones.add(btnVerRequisitos);
        panelInsBotones.add(btnInscribir);
        panelInsBotones.add(btnVerificarReq);

        panelInscripcion.add(scrollCerts, BorderLayout.CENTER);
        panelInscripcion.add(panelInsBotones, BorderLayout.SOUTH);

        subTabs.addTab("Inscripción certificaciones", panelInscripcion);

        // ============ 3) SEGUIMIENTO ============
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

        // ======================== LISTENERS ========================

        // Cargar Perfil + Malla (usa notas para saber estado)
        btnCargar.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(root,
                        "Ingrese un RUT.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Estudiante es = sistema.getEstudiante(rut);
            if (es == null) {
                JOptionPane.showMessageDialog(root,
                        "No se encontró estudiante con rut " + rut,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            lblNombre.setText("Nombre: " + es.getNombre());
            lblCarrera.setText("Carrera: " + es.getCarrera());
            lblSemestre.setText("Semestre: " + es.getSemestre());
            lblCorreo.setText("Correo: " + es.getCorreo());

            // Actualizar malla con colores según estado (Aprobada/Reprobada/Pendiente)
            actualizarMallaParaRut(rut, modeloMalla);
        });

        // Promedio general
        btnPromGeneral.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(root,
                        "Ingrese un RUT y cargue datos primero.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            double prom = sistema.calcularPromedioGeneral(rut);
            JOptionPane.showMessageDialog(root,
                    "Promedio general del estudiante: " + prom);
        });

        // Promedio por semestre (con combo de números)
        btnPromSemestre.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(root,
                        "Ingrese un RUT y cargue datos primero.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer[] opciones = {1,2,3,4,5,6,7,8,9,10};
            Integer semestre = (Integer) JOptionPane.showInputDialog(
                    root,
                    "Seleccione semestre:",
                    "Promedio por semestre",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            if (semestre == null) return;

            double prom = sistema.calcularPromedioSemestre(rut, semestre);
            JOptionPane.showMessageDialog(root,
                    "Promedio del semestre " + semestre + ": " + prom);
        });

        // ---- INSCRIPCIÓN: listar líneas ----
        btnListarLineas.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            ArrayList<Certificacion> certs = sistema.getCertificaciones();
            sb.append("Certificaciones disponibles:\n\n");
            for (Certificacion c : certs) {
                sb.append(c.getId()).append(" - ").append(c.getNombre()).append("\n");
            }
            if (certs.isEmpty()) {
                sb.append("(No hay certificaciones)");
            }
            areaCerts.setText(sb.toString());
        });

        // Mostrar requisitos y descripción
        btnVerRequisitos.addActionListener(e -> {
            ArrayList<Certificacion> certs = sistema.getCertificaciones();
            if (certs.isEmpty()) {
                JOptionPane.showMessageDialog(root,
                        "No hay certificaciones disponibles.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String[] opciones = new String[certs.size()];
            for (int i = 0; i < certs.size(); i++) {
                opciones[i] = certs.get(i).getId() + " - " + certs.get(i).getNombre();
            }
            String seleccion = (String) JOptionPane.showInputDialog(
                    root,
                    "Seleccione certificación:",
                    "Requisitos",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);
            if (seleccion == null) return;
            String idSel = seleccion.split(" - ")[0];
            Certificacion c = sistema.buscarCertificacion(idSel);
            if (c == null) return;

            StringBuilder sb = new StringBuilder();
            sb.append("ID: ").append(c.getId()).append("\n");
            sb.append("Nombre: ").append(c.getNombre()).append("\n\n");
            sb.append("Descripción:\n").append(c.getDescripcion()).append("\n\n");
            sb.append("Créditos requeridos: ").append(c.getCreditosRequeridos()).append("\n");
            sb.append("Años de validez: ").append(c.getAñosValidez()).append("\n");

            areaCerts.setText(sb.toString());
        });

        // Inscribirse
        btnInscribir.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(root,
                        "Ingrese un RUT primero.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            gestionarInscripcionCertificacion(rut, root);
        });

        // Verificar requisitos
        btnVerificarReq.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(root,
                        "Ingrese un RUT primero.",
                        "Atención",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            ArrayList<Certificacion> certs = sistema.getCertificaciones();
            if (certs.isEmpty()) {
                JOptionPane.showMessageDialog(root,
                        "No hay certificaciones disponibles.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String[] opciones = new String[certs.size()];
            for (int i = 0; i < certs.size(); i++) {
                opciones[i] = certs.get(i).getId() + " - " + certs.get(i).getNombre();
            }
            String seleccion = (String) JOptionPane.showInputDialog(
                    root,
                    "Seleccione certificación:",
                    "Verificar requisitos",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);
            if (seleccion == null) return;
            String idSel = seleccion.split(" - ")[0];

            boolean cumple = sistema.verificarRequisitos(rut, idSel);
            if (cumple) {
                JOptionPane.showMessageDialog(root,
                        "El estudiante CUMPLE los requisitos.",
                        "Requisitos", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(root,
                        "El estudiante NO cumple los requisitos.",
                        "Requisitos", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Seguimiento: dashboard
        btnVerInscripciones.addActionListener(e -> {
            String rut = txtRut.getText().trim();
            if (rut.isEmpty()) {
                JOptionPane.showMessageDialog(root,
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

        tabs.addTab("Estudiante", root);
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



}
