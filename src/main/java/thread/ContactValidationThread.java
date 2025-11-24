package thread;

import model.Contact;
import model.ContactModel;

public class ContactValidationThread extends Thread {
    private final ContactModel contactModel;
    private final Contact contactToValidate;
    private String ignoreId = null;
    private boolean isDuplicate = false;

    public ContactValidationThread(ContactModel contactModel, Contact contactToValidate) {
        this.contactModel = contactModel;
        this.contactToValidate = contactToValidate;
    }

    public ContactValidationThread(ContactModel contactModel, Contact contactToValidate, String ignoreId) {
        this.contactModel = contactModel;
        this.contactToValidate = contactToValidate;
        this.ignoreId = ignoreId;
    }

    /**
     * Ejecuta la validación del contacto para detectar duplicados.
     */
    @Override
    public void run() {
        for (Contact c : contactModel.getAllContacts()) {
            // Ignorar el contacto que se está editando
            if (ignoreId != null && c.getId().equals(ignoreId)) {
                continue;
            }
            if (c.getEmail().equalsIgnoreCase(contactToValidate.getEmail()) ||
                (c.getFirstName().equalsIgnoreCase(contactToValidate.getFirstName()) &&
                 c.getLastName().equalsIgnoreCase(contactToValidate.getLastName()))) {
                isDuplicate = true;
                break;
            }
        }
    }

    /**
     * Indica si se encontró un contacto duplicado.
     * 
     * @return true si es duplicado, false en caso contrario.
     */
    public boolean isDuplicate() {
        return isDuplicate;
    }
}
