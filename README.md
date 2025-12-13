# AcademiCore – Taller N°4 POO

## Descripción del proyecto

AcademiCore es un sistema académico desarrollado en Java que permite la gestión de usuarios, estudiantes, cursos y certificaciones académicas.
El sistema soporta distintos roles (Administrador, Coordinador y Estudiante) y permite:

Administrar cuentas de usuario

Gestionar líneas de certificación

Inscribir estudiantes en certificaciones

Validar requisitos académicos

Visualizar mallas curriculares, notas y progreso

Analizar asignaturas críticas

Generar dashboards de seguimiento académico

El proyecto fue desarrollado aplicando Programación Orientada a Objetos y patrones de diseño, según lo solicitado en el Taller N°4. 

## Integrantes 

Catalina Isidora Rojas Macaya | 21.953.080-3 | @Lncata

Benjamín Ismael Cortés Acuña | 21.890.703-2 | @cxrtees

##  Estructura del proyecto

El proyecto se organiza en paquetes según su responsabilidad dentro del sistema:

###  dominio
Contiene las clases del modelo y la lógica principal del sistema.

- **Sistema**: clase central del sistema (Singleton), coordina todas las operaciones.
- **Usuario, Estudiante, Curso, Nota, Certificacion, RegistroCertificacion, AsignaturaCertificacion**: clases de dominio.
- **VisitorAccionesCertificacion**: implementación del patrón Visitor.
- **EstrategiaVerificacionCreditos**: implementación del patrón Strategy.
- **Factories**:
  - UsuarioFactory
  - EstudianteFactory
  - CursoFactory
  - CertificacionFactory
  - NotaFactory
  - RegistroCertificacionFactory
  - AsignaturaCertificacionFactory

---

###  logica
Contiene las interfaces que definen los contratos del sistema y los patrones de diseño.

- **SistemaIn**: interfaz del sistema (usada por la GUI).
- **EstrategiaVerificacion**: interfaz del patrón Strategy.
- **CertificacionVisitor**: interfaz del patrón Visitor.

---

###  GUI
Contiene la interfaz gráfica del sistema.

- **Menu**: interfaz principal de la aplicación, permite interactuar según el rol del usuario.

---

###  Archivos de datos
Archivos de texto utilizados como fuente de datos del sistema:

- usuarios.txt  
- estudiantes.txt  
- cursos.txt  
- certificaciones.txt  
- registros.txt  
- notas.txt  
- asignaturas_certificaciones.txt


##  Patrones de diseño implementados

En el desarrollo del sistema se utilizaron los siguientes patrones de diseño:

###  Singleton
- **Clase:** `Sistema`
- Se utiliza para asegurar que exista una única instancia del sistema durante toda la ejecución.
- Implementación:
  - Constructor privado.
  - Atributo estático `instancia`.
  - Método estático `getInstancia()` para acceder a la instancia.

---

###  Factory
- **Clases Factory implementadas:**
  - UsuarioFactory
  - EstudianteFactory
  - CursoFactory
  - CertificacionFactory
  - NotaFactory
  - RegistroCertificacionFactory
  - AsignaturaCertificacionFactory
- Cada Factory se encarga de crear objetos de dominio a partir de líneas leídas desde archivos `.txt`.
- Permite separar la lógica de creación de objetos del resto del sistema.

---

###  Strategy
- **Interfaz:** `EstrategiaVerificacion`
- **Implementación:** `EstrategiaVerificacionCreditos`
- Permite validar dinámicamente los requisitos académicos para inscribir una certificación.
- La clase `Sistema` delega la verificación de requisitos a la estrategia configurada, permitiendo cambiar la lógica sin modificar el sistema.

---

###  Visitor
- **Interfaz:** `CertificacionVisitor`
- **Implementación:** `VisitorAccionesCertificacion`
- Permite ejecutar acciones sobre certificaciones sin modificar la clase `Certificacion`.
- Se utiliza para generar análisis y recomendaciones según el estado y progreso del estudiante en cada certificación.

##  Instrucciones de ejecución

1. Abrir el proyecto en un IDE Java (Eclipse o IntelliJ IDEA).
2. Verificar que los archivos `.txt` se encuentren en la ruta configurada del proyecto.
3. Ejecutar la clase principal:

   dominio.App

4. Se abrirá la interfaz gráfica principal del sistema.
5. Navegar por el sistema según el rol del usuario:
   - Administrador
   - Coordinador
   - Estudiante



