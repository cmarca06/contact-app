# Proyecto: Contact App

## Autor
- Nombre: Carlos Marca
- Estudiante de tercer ciclo de la carrera de Ingeniería de Software
- **Universidad Politécnica Salesiana**

## Descripción
Pequeña aplicación de escritorio basada en java para crear, listar, editar, eliminar, importar y exportar contactos. Implementa un patrón MVC con un "modelo delegado".

---

## Características principales
- CRUD de contactos (crear, leer, actualizar, eliminar).
- Importar y exportar contactos (CSV) con ejecución en background.
- Búsqueda/filtrado en lista (debounce con Timer).
- Vista de estadísticas básica.
- Patrón MVC claro y desacoplamiento de persistencia (Repositorio).
- Utilidades de UI y tabla para mantener controladores limpios.
- Todo el diseño se realizó con GUI Builder de Apache Netbeans. 
---

## Flujo principal de la aplicación
1. Inicio: ContactModel carga contactos desde ContactRepository.
2. ContactViewController muestra la lista y estadísticas.
3. Usuario interactúa (nuevo/editar/eliminar/importar/exportar).
4. Controladores validan y llaman a ContactModel.
5. ContactModel actualiza colección y usa ContactRepository para persistir.
6. Controladores/vistas se actualizan tras las notificaciones del modelo.
---

## Importación / Exportación
- Los botones Importar / Exportar de ContactList inician un JFileChooser.
- La lectura/escritura se realiza en background usando SwingWorker para no bloquear el EDT.
- Durante la operación se muestra jProgressBar en modo indeterminado.
- ContactModel implementa importFromCsv(file) y exportToCsv(file) — ContactListController delega en esos métodos.
- Al finalizar, ContactListController llama a TableUtils.fillContactsTable() para refrescar la JTable y muestra mensajes de éxito/error mediante UIUtils.
---

## Explicación de las clases

### controladores
- ContactViewController.java  
  Controlador raíz/orquestador. Inicializa la vista principal, muestra la lista y estadísticas, crea controladores de sub-vistas y gestiona navegación entre lista y formulario. Escucha cambios de pestañas para refrescar estadísticas.

- ContactListController.java  
  Controlador de la lista (ContactList). Gestiona la JTable, filtros de búsqueda, botones (Agregar / Editar / Eliminar), y operaciones de importación/exportación. Ejecuta I/O en background (SwingWorker), muestra barra de progreso y actualiza la vista mediante TableUtils.

- ContactFormController.java  
  Controlador del formulario (ContactForm). Carga datos para edición, valida entrada y llama a ContactModel para add/update. Tramita navegación de vuelta a la lista.

- ContactStatsController.java  
  Calcula y rellena estadísticas en la vista ContactStats (total de contactos, agrupaciones simples). Puede ser invocado por el ContactViewController al mostrarse la pestaña.

### modelo 
- Contact.java  
  Entidad que representa un contacto. Campos típicos: id, nombre, apellido, email, teléfono, dirección. Contiene getters/setters, equals/hashCode y toString.

- ContactModel.java  
  Fuente de verdad en memoria: mantiene la colección de Contact y expone operaciones (getAll, findById, add, update, delete, importFromCsv, exportToCsv). Notifica cambios a observadores/controladores. Delegación de persistencia a ContactRepository.

- ContactRepository.java  
  Encapsula la lógica de persistencia (lectura/escritura a archivo — CSV u otro formato). Usado por ContactModel para cargar y guardar datos. Debe manejar I/O y excepciones.

### vistas 
- ContactView.java / ContactView.form  
  Ventana/panel principal con JTabbedPane que contiene la zona de contactos y la de estadísticas. Paneles jPanelContacts y jPanelStats actúan como contenedores dinámicos.

- ContactList.java / ContactList.form  
  Panel con JTable, botones (Agregar, Editar, Eliminar, Importar, Exportar), campo de búsqueda y JProgressBar. Provee getters a los controladores.

- ContactForm.java / ContactForm.form  
  Formulario con campos para crear/editar un contacto y botones Guardar/Cancelar.

- ContactStats.java / ContactStats.form  
  Panel que muestra métricas simples (ej. total de contactos). Actualizado por ContactStatsController.

### utilidades 
- UIUtils.java  
  Funciones de apoyo para manipular paneles Swing (p. ej. showPanel para reemplazar contenido), mostrar diálogos de info/error, centrado de ventanas, etc.

- TableUtils.java  
  Funciones para construir/llenar modelos de JTable a partir de listas de Contact, formateo de columnas y ajustes visuales.
---

## Estructura de carpetas
- src/
  - contactapp/
    - ContactApp.java
  - controller/
    - ContactFormController.java
    - ContactListController.java
    - ContactStatsController.java
    - ContactViewController.java
  - model/
    - Contact.java
    - ContactModel.java
    - ContactRepository.java
  - view/
    - ContactForm.form
    - ContactForm.java
    - ContactList.form
    - ContactList.java
    - ContactStats.form
    - ContactStats.java
    - ContactView.form
    - ContactView.java
  - utils/
    - TableUtils.java
    - UIUtils.java
---

## Relación entre clases
- ContactApp crea ContactRepository y ContactModel; luego instancia la vista ContactView y el ContactViewController(contactView, contactModel).
- ContactViewController crea sub-vistas y sub-controladores:
  - ContactListController ← ContactList + ContactModel + referencia a ContactViewController (para navegación).
  - ContactFormController ← ContactForm + ContactModel + ContactViewController.
  - ContactStatsController ← ContactStats + ContactModel.
- Los controladores solicitan operaciones al ContactModel (no al repositorio directamente). ContactModel delega persistencia a ContactRepository.
- ContactModel notifica a controladores/vistas mediante listeners/observadores para que refresquen la UI (tabla, estadísticas).
- TableUtils y UIUtils son llamados por controladores para manipular tablas y paneles sin mezclar lógica de UI.
---

## Instrucciones para Clonar y Ejecutar el Proyecto
1. Abre una terminal o consola de comandos.
2. Clona el repositorio ejecutando:
   ```bash
   git clone https://github.com/cmarca26/contact-app.git
   cd contact-app
   ```
3. Abre el proyecto en tu IDE favorito y compílalo como un proyecto Java Maven.
4. Ejecuta la clase principal `ContactApp` para iniciar el programa.
---

## Notas Adicionales
- El proyecto es educativo y puede ser extendido para agregar más funcionalidades.
- El sistema está diseñado para facilitar la gestión y visualización de contactos.