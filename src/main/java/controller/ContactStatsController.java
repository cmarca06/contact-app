package controller;

import java.util.List;

import model.Contact;
import model.ContactModel;
import view.ContactStats;

public class ContactStatsController {

    private final ContactStats contactStatsView;
    private final ContactModel contactModel;

    public ContactStatsController(ContactStats contactStatsView, ContactModel contactModel) {
        this.contactStatsView = contactStatsView;
        this.contactModel = contactModel;

        // Llenar las estadísticas al inicializar el constructor
        fillStats();
    }

    /**
     * Llena las estadísticas en la vista ContactStats.
     */
    public void fillStats() {
        List<Contact> contacts = contactModel.getAllContacts();

        int total = contacts.size();
        Contact lastContact = total > 0 ? contacts.get(total - 1) : null;

        contactStatsView.getjTextFieldTotalRecords().setText(total > 0 ? total + " registros" : "No se encontraron registros");
        contactStatsView.getjTextAreaLastRecord().setText(total > 0 ? lastContact.toString() : "No se encontraron registros");
    }

}
