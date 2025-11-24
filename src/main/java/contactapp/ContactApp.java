package contactapp;

import controller.ContactViewController;
import model.ContactModel;
import view.ContactView;

public class ContactApp {

    public static void main(String[] args) {
        // Inicializar Modelo y Vista
        ContactModel contactModel = new ContactModel();
        ContactView contactView = new ContactView();
        utils.I18nUtils.setLanguage("es");

        // Inicializar Constructor de la vista
        new ContactViewController(contactView, contactModel);

        // Hacer visible la vista
        contactView.setVisible(true);
    }
}
