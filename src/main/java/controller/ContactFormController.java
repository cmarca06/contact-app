package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import model.ContactModel;
import notifications.NotificationHandler;
import model.Contact;
import utils.UIUtils;
import thread.ContactValidationThread;
import view.ContactForm;

public class ContactFormController implements ActionListener, MouseListener {

    private final ContactForm contactForm;
    private final ContactViewController contactViewController;
    private final ContactModel contactModel;
    private final NotificationHandler notificationHandler;
    private final String lockToken = UUID.randomUUID().toString();
    private String idContact;

    /**
     * Constructor para el formulario de contacto.
     *
     * @param contactForm Formulario de contacto
     * @param contactViewController Constructor de la vista de contactos
     * @param contactModel Modelo de datos de contactos
     * @param notificationHandler Manejador de notificaciones
     */
    public ContactFormController(ContactForm contactForm, ContactViewController contactViewController,
            ContactModel contactModel, NotificationHandler notificationHandler) {
        this.contactForm = contactForm;
        this.contactViewController = contactViewController;
        this.contactModel = contactModel;
        this.notificationHandler = notificationHandler;

        // Configurar listeners
        this.contactForm.getjButtonBack().addActionListener(this);
        this.contactForm.getjButtonClean().addActionListener(this);
        this.contactForm.getjButtonSave().addActionListener(this);

        // Inicializar atajos de teclado y de mouse
        SwingUtilities.invokeLater(this::initKeyBindings);
    }

    public String getIdContact() {
        return idContact;
    }

    /**
     * Establece el ID del contacto a editar, adquiriendo el bloqueo
     * correspondiente.
     *
     * @param idContact ID del contacto a editar (null si es nuevo)
     * @return
     */
    public boolean setIdContact(String idContact) {

        // Si es un nuevo contacto, limpiar campos y devolver true
        if (idContact == null) {
            cleanFields();
            return true;
        }

        // Intentar adquirir el bloqueo para el contacto a editar
        boolean acquired = contactModel.acquireContactLock(idContact, lockToken);
        // Si no se pudo adquirir, mostrar error y devolver false
        if (!acquired) {
            UIUtils.notifyError("El contacto seleccionado se esta editando por otro usuario", notificationHandler);
            return false;
        }

        // Establecer el ID del contacto y llenar los datos en el formulario y devolver true
        this.idContact = idContact;
        fillData(idContact);
        return true;
    }

    /**
     * Libera el bloqueo del contacto actualmente editado, si existe.
     */
    private void releaseCurrentLock() {
        if (this.idContact != null) {
            contactModel.releaseContactLock(this.idContact, lockToken);
            this.idContact = null;
        }
    }

    /**
     * Inicializa los atajos de teclado globales del formulario. ENTER = Guardar
     * ESC = Volver atrás DELETE = Limpiar campos
     */
    private void initKeyBindings() {
        // Obtener el panel principal del formulario
        JRootPane rootPane = contactForm.getRootPane();

        // Configurar input y action map para los atajos de teclado
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        // Vincular la tecla ENTER con nombrar acción "saveData"
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "saveData");
        // Definir la acción "saveData" para guardar los datos
        actionMap.put("saveData", new AbstractAction() {
            // Llamar al método saveData() cuando se presione ENTER
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });

        // Vincular la tecla ESCAPE con nombrar acción "backView"
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "backView");
        // Definir la acción "backView" para volver a la vista anterior
        actionMap.put("backView", new AbstractAction() {
            // Llamar al método backView() cuando se presione ESCAPE
            @Override
            public void actionPerformed(ActionEvent e) {
                backView();
            }
        });

        // Agregar listener de mouse al panel de campos para detectar los clics
        contactForm.getjPanelFields().addMouseListener(this);
    }

    /**
     * Método para manejar eventos de clic del mouse.
     *
     * @param e Evento de mouse No se utiliza en esta implementación.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Método para manejar eventos de presión del mouse.
     *
     * @param e Evento de mouse No se utiliza en esta implementación.
     */
    @Override
    public void mousePressed(MouseEvent e) {
    }

    /**
     * Método para manejar eventos de liberación del mouse.
     *
     * @param e Evento de mouse Si se detecta un clic derecho, limpia los campos
     * del formulario.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            cleanFields();
        }
    }

    /**
     * Método para manejar eventos de entrada del mouse.
     *
     * @param e Evento de mouse No se utiliza en esta implementación.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Método para manejar eventos de salida del mouse.
     *
     * @param e Evento de mouse No se utiliza en esta implementación.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Maneja los eventos de los botones en el formulario de contacto.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == contactForm.getjButtonBack()) {
            backView();
        } else if (e.getSource() == contactForm.getjButtonClean()) {
            cleanFields();
        } else if (e.getSource() == contactForm.getjButtonSave()) {
            saveData();
        }
    }

    /**
     * Regresa a la vista de lista de contactos y limpia los campos del
     * formulario.
     */
    private void backView() {
        // Liberar el bloqueo del contacto editado
        // releaseCurrentLock();
        cleanFields();
        contactViewController.showContactList();
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void cleanFields() {
        UIUtils.clearTextFields(contactForm.getjPanelFields());
    }

    /**
     * Guarda un nuevo contacto con los datos del formulario.
     */
    private void saveData() {

        // Validar campos del formulario
        String error = UIUtils.validateFormFields(contactForm.getjPanelFields());

        // Si hay error, mostrar mensaje y salir
        if (error != null) {
            UIUtils.notifyError(error, notificationHandler);
            return;
        }

        // Extraer datos del formulario
        Map<String, Object> formData = UIUtils.extractFormData(contactForm.getjPanelFields());

        // Crear nuevo contacto
        Contact c = new Contact(
                (String) formData.get("nombre"),
                (String) formData.get("apellido"),
                (String) formData.get("telefono"),
                (String) formData.get("email"),
                (String) formData.get("categoria"),
                (Boolean) formData.get("jCheckBoxFavorite"));

        // Validación en segundo plano para evitar duplicados
        ContactValidationThread validationThread;

        if (idContact == null) {
            // Nuevo contacto: validar todos
            validationThread = new ContactValidationThread(contactModel, c);
        } else {
            // Edición: ignorar el propio id
            validationThread = new ContactValidationThread(contactModel, c, idContact);
        }

        // Iniciar la validación
        validationThread.start();

        // Esperar a que termine la validación
        try {
            validationThread.join();
        } catch (InterruptedException ex) {
            UIUtils.notifyError("Error en la validación de duplicados", notificationHandler);
            return;
        }

        // Verificar si se encontró un duplicado
        if (validationThread.isDuplicate()) {
            UIUtils.notifyError("El contacto ya está registrado", notificationHandler);
            return;
        }

        String message = null;
        if (idContact == null) {
            // Guardar contacto en el modelo
            contactModel.addContact(c);
            message = "Contacto agregado correctamente";
        } else {
            // Actualizar contacto existente
            c.setId(idContact);
            contactModel.updateContact(c);
            message = "Contacto actualizado correctamente";
        }

        // Liberar el bloqueo del contacto editado
        releaseCurrentLock();

        // Limpiar campos del formulario
        cleanFields();

        // Mostrar notificación no bloqueante
        UIUtils.notifyInfo(message, notificationHandler);

        // Regresar a la vista de lista de contactos
        contactViewController.showContactList();

    }

    /**
     * Llena los campos del formulario con los datos del contacto a editar.
     *
     * @param idContact ID del contacto a editar.
     */
    private void fillData(String idContact) {
        // Buscar contacto en el modelo
        Contact contact = contactModel.findById(idContact);

        if (contact == null) {
            UIUtils.notifyError("No se encontró el contacto con ID: " + idContact, notificationHandler);
            // Liberar el bloqueo si no se encontró el contacto
            releaseCurrentLock();

            return;
        }

        // Crear mapa con los datos del contacto encontrado
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", contact.getFirstName());
        data.put("apellido", contact.getLastName());
        data.put("telefono", contact.getPhone());
        data.put("email", contact.getEmail());
        data.put("categoria", contact.getCategory());
        data.put("jCheckBoxFavorite", contact.getFavorite());

        // Llenar campos del formulario con los datos del contacto encontrado
        UIUtils.fillFormData(contactForm.getjPanelFields(), data);
    }
}
