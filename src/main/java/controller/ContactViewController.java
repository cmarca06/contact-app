package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.ContactForm;
import view.ContactList;
import view.ContactStats;
import view.ContactView;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.ContactModel;
import notifications.NotificationHandler;
import utils.I18nUtils;
import utils.TableUtils;
import utils.UIUtils;

public class ContactViewController {

    private final ContactView contactView;
    private final ContactModel contactModel;
    private final NotificationHandler notificationHandler;
    private ContactStatsController contactStatsController;
    private ContactList contactList;
    private ContactForm contactForm;
    private ContactFormController contactFormController;

    /**
     * Constructor para la vista principal de contactos.
     *
     * @param contactView Vista principal de contactos
     * @param contactModel Modelo de datos de contactos
     */
    public ContactViewController(ContactView contactView, ContactModel contactModel) {
        this.contactView = contactView;
        this.contactModel = contactModel;
        this.notificationHandler = NotificationHandler.init(contactView);

        // Inicialmente, mostrar la lista de contactos y las estadísticas
        showContactList();
        showContactStats();

        // Añadir un ChangeListener al JTabbedPane para detectar cambios de pestaña
        contactView.getjTabbedPaneMain().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = contactView.getjTabbedPaneMain().getSelectedIndex();
                if (selectedIndex == 0 && contactStatsController != null) {
                    contactStatsController.fillStats();
                }
            }
        });

        // Configurar el ActionListener para el JComboBox de selección de idioma
        contactView.getjComboBoxLanguage().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cambiar el idioma según la selección del usuario
                int selectLanguage = (int) contactView.getjComboBoxLanguage().getSelectedIndex();
                switch (selectLanguage) {
                    case 1:
                        I18nUtils.setLanguage("en_US");
                        break;
                    case 2:
                        I18nUtils.setLanguage("fr_FR");
                        break;
                    default:
                        I18nUtils.setLanguage("default");
                        break;
                }

                // Relocalizar toda la vista al nuevo idioma
                I18nUtils.localizeContainer(contactView, "ContactView");
                if (contactView.getjPanelContacts().getComponentCount() > 0) {
                    Component currentPanel = contactView.getjPanelContacts().getComponent(0);
                    if (currentPanel instanceof ContactList) {
                        I18nUtils.localizeContainer(currentPanel, "ContactList");
                    } else if (currentPanel instanceof ContactForm) {
                        I18nUtils.localizeContainer(currentPanel, "ContactForm");
                    }

                }
                // Traducir panel de estadísticas si está visible
                if (contactView.getjPanelStats().getComponentCount() > 0) {
                    I18nUtils.localizeContainer(contactView.getjPanelStats().getComponent(0), "ContactStats");
                }

            }
        });
    }

    /**
     * Muestra la lista de contactos en el panel de contactos de la vista
     */
    public void showContactList() {
        // Reutilizar instancia si ya existe
        if (contactList == null) {
            contactList = new ContactList();
            new ContactListController(contactList, this, contactModel, notificationHandler);
        }

        TableUtils.fillContactsTable(contactList.getjTableList(), contactModel.getAllContacts());
        UIUtils.showPanel(contactView.getjPanelContacts(), contactList);
    }

    /**
     * Muestra el formulario de contacto en el panel de contactos de la vista
     *
     * @param id Identificador del contacto a mostrar (null para nuevo contacto)
     */
    public void showContactForm(String id) {
        // Reutilizar instancia si ya existe
        if (contactForm == null) {
            contactForm = new ContactForm();
            contactFormController = new ContactFormController(contactForm, this, contactModel, notificationHandler);
        }

        if (!contactFormController.setIdContact(id)) {
            return;
        }

        UIUtils.showPanel(contactView.getjPanelContacts(), contactForm);
        I18nUtils.localizeContainer(contactForm, "ContactForm");
    }

    /**
     * Muestra las estadísticas de contactos en el panel de estadísticas de la
     * vista
     */
    private void showContactStats() {
        ContactStats contactStats = new ContactStats();
        contactStatsController = new ContactStatsController(contactStats, contactModel);
        UIUtils.showPanel(contactView.getjPanelStats(), contactStats);
    }
}
