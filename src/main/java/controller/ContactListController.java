package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import java.util.List;
import java.util.concurrent.ExecutionException;
import view.ContactList;
import model.ContactModel;
import notifications.NotificationHandler;
import model.Contact;
import utils.TableUtils;
import utils.UIUtils;
import utils.ImportResult;

public class ContactListController implements ActionListener, KeyListener, MouseListener {

    private final ContactList contactList;
    private final ContactViewController contactViewController;
    private final ContactModel contactModel;
    private final NotificationHandler notificationHandler;
    private final Timer searchTimer;
    private final int SEARCH_DELAY = 400;

    /**
     * Constructor para la vista de lista de contactos.
     *
     * @param contactList Vista de lista de contactos
     * @param contactViewController Constructor principal de la vista
     * @param contactModel Modelo de datos de contactos
     * @param notificationHandler
     */
    public ContactListController(ContactList contactList, ContactViewController contactViewController,
            ContactModel contactModel, NotificationHandler notificationHandler) {
        this.contactList = contactList;
        this.contactViewController = contactViewController;
        this.contactModel = contactModel;
        this.notificationHandler = notificationHandler;

        // Timer para retrasar la búsqueda mientras el usuario escribe
        searchTimer = new Timer(SEARCH_DELAY, _ -> filterData());
        searchTimer.setRepeats(false);

        // Inicialmente, los botones Editar y Eliminar están deshabilitados
        UIUtils.setButtonVisible(contactList.getjButtonEdit(), false);
        UIUtils.setButtonVisible(contactList.getjButtonDelete(), false);

        // Cargar datos en la tabla
        TableUtils.fillContactsTable(contactList.getjTableList(), contactModel.getAllContacts());

        // Configurar listeners
        this.contactList.getjButtonAdd().addActionListener(this);
        this.contactList.getjButtonEdit().addActionListener(this);
        this.contactList.getjButtonDelete().addActionListener(this);
        this.contactList.getjButtonImport().addActionListener(this);
        this.contactList.getjButtonExport().addActionListener(this);

        // Listener para habilitar/deshabilitar botones según selección en la tabla
        contactList.getjTableList().getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            boolean selected = contactList.getjTableList().getSelectedRow() != -1;
            UIUtils.setButtonVisible(contactList.getjButtonEdit(), selected);
            UIUtils.setButtonVisible(contactList.getjButtonDelete(), selected);
        });

        contactList.getjTextFieldSearch().addKeyListener(this);

        // Agregar listener de mouse al panel de la tabla para detectar los clics
        contactList.getjTableList().addMouseListener(this);
    }

    /**
     * Maneja los eventos de los botones Agregar, Editar, Eliminar, Exportar e
     * Importar.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == contactList.getjButtonAdd()) {
            add();
        } else if (e.getSource() == contactList.getjButtonEdit()) {
            edit();
        } else if (e.getSource() == contactList.getjButtonDelete()) {
            delete();
        } else if (e.getSource() == contactList.getjButtonImport()) {
            importData();
        } else if (e.getSource() == contactList.getjButtonExport()) {
            exportData();
        }
    }

    /**
     * Método para manejar el eventos de tipado de teclas.
     *
     * @param e Evento de teclado No se utiliza en esta implementación.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Método para manejar el eventos de presión de teclas.
     *
     * @param e Evento de teclado No se utiliza en esta implementación.
     */
    @Override
    public void keyPressed(KeyEvent e) {
    }

    /**
     * Método para manejar el eventos de liberación de teclas.
     *
     * @param e Evento de teclado
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == contactList.getjTextFieldSearch()) {
            // Reiniciar el temporizador de búsqueda al soltar una tecla
            searchTimer.restart();
        }
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
            delete();
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
     * Muestra el formulario para agregar un nuevo contacto.
     */
    private void add() {
        contactViewController.showContactForm(null);
    }

    /**
     * Edita el contacto seleccionado en la tabla.
     */
    private void edit() {
        // Obtener ID del contacto seleccionado
        String id = TableUtils.getSelectedCellValue(contactList.getjTableList(), 0);

        // Si el ID no es nulo, mostrar el formulario de edición
        if (id != null) {
            contactViewController.showContactForm(id);
        }
    }

    /**
     * Elimina el contacto seleccionado en la tabla después de confirmar.
     */
    private void delete() {
        // Obtener ID del contacto seleccionado
        String id = TableUtils.getSelectedCellValue(contactList.getjTableList(), 0);

        // Si el ID no es nulo, mostrar cuadro de confirmación y eliminar el registro
        if (id != null) {
            // Verificar si el contacto está bloqueado
            if (contactModel.isContactLocked(id)) {
                UIUtils.notifyError("El contacto se encuentra en edición por otro usuario", notificationHandler);
                return;
            }
            int confirm = UIUtils.showConfirm("¿Desea eliminar este registro?");

            // Si el usuario confirma, eliminar el contacto y actualizar la tabla
            if (confirm == JOptionPane.YES_OPTION) {
                contactModel.deleteContact(id);
                TableUtils.fillContactsTable(contactList.getjTableList(), contactModel.getAllContacts());
                UIUtils.notifyInfo("Contacto eliminado correctamente", notificationHandler);
            }
        }
    }

    /**
     * Filtra los datos en la tabla según el texto de búsqueda.
     */
    private void filterData() {
        // Mostrar barra de progreso
        contactList.getjProgressBar().setVisible(true);
        contactList.getjProgressBar().setIndeterminate(true);

        // Búsqueda en segundo plano usando SwingWorker
        new SwingWorker<List<Contact>, Void>() {
            @Override
            protected List<Contact> doInBackground() {
                // Esperar un momento para simular tiempo de procesamiento
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String query = contactList.getjTextFieldSearch().getText().trim().toLowerCase();
                if (query.isEmpty()) {
                    return contactModel.getAllContacts();
                }
                return contactModel.getAllContacts().stream().filter(
                        c -> c.getFirstName().toLowerCase().contains(query)
                        || c.getLastName().toLowerCase().contains(query)
                        || c.getEmail().toLowerCase().contains(query)
                        || c.getPhone().toLowerCase().contains(query))
                        .collect(Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    List<Contact> filtered = get();
                    TableUtils.fillContactsTable(contactList.getjTableList(), filtered);
                } catch (Exception ex) {
                    // Manejo de error: mostrar todos los contactos si falla la búsqueda
                    TableUtils.fillContactsTable(contactList.getjTableList(), contactModel.getAllContacts());
                } finally {
                    contactList.getjProgressBar().setIndeterminate(false);
                    contactList.getjProgressBar().setVisible(false);
                }
            }
        }.execute();

    }

    /**
     * Importa datos de un archivo seleccionado por el usuario.
     */
    private void importData() {
        String[] options = {"CSV", "JSON"};
        String selected = (String) JOptionPane.showInputDialog(
                contactList,
                "Selecciona el formato a importar:",
                "Importar contactos",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (selected == null) return;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona el archivo a importar");
        if (selected.equals("CSV")) {
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos CSV", "csv"));
        } else {
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos JSON", "json"));
        }
        int option = chooser.showOpenDialog(contactList);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            boolean isJson = selected.equals("JSON");
            new SwingWorker<ImportResult, Integer>() {
                @Override
                protected ImportResult doInBackground() {
                    contactList.getjProgressBar().setVisible(true);
                    contactList.getjProgressBar().setIndeterminate(true);
                    try {
                        Thread.sleep(500);
                        if (isJson) {
                            return contactModel.importFromJson(file);
                        } else {
                            return contactModel.importFromCsv(file);
                        }
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Error inesperado durante la importación: ", e);
                    }
                }
                @Override
                protected void done() {
                    contactList.getjProgressBar().setIndeterminate(false);
                    contactList.getjProgressBar().setVisible(false);
                    try {
                        ImportResult result = get();
                        TableUtils.fillContactsTable(contactList.getjTableList(), contactModel.getAllContacts());
                        StringBuilder message = new StringBuilder("Importación realizada correctamente. Nuevos registros: ")
                                .append(result.getImportedCount());
                        if (result.getDuplicateCount() > 0) {
                            message.append(". Duplicados omitidos: ").append(result.getDuplicateCount());
                        }
                        UIUtils.notifyInfo(message.toString(), notificationHandler);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    } catch (ExecutionException ex) {
                        UIUtils.notifyError(ex.getCause() != null ? ex.getCause().getMessage() : "Error al importar contactos", notificationHandler);
                    }
                }
            }.execute();
        }
    }

    /**
     * Exporta datos a un archivo seleccionado por el usuario.
     */
    private void exportData() {
        String[] options = {"CSV", "JSON"};
        String selected = (String) JOptionPane.showInputDialog(
                contactList,
                "Selecciona el formato a exportar:",
                "Exportar contactos",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
        if (selected == null) return;
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona el archivo de destino");
        if (selected.equals("CSV")) {
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos CSV", "csv"));
        } else {
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos JSON", "json"));
        }
        int option = chooser.showSaveDialog(contactList);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            boolean isJson = selected.equals("JSON");
            // Sugerir extensión si no está presente
            String filePath = file.getAbsolutePath();
            final File exportFile;
            if (isJson && !filePath.toLowerCase().endsWith(".json")) {
                exportFile = new File(filePath + ".json");
            } else if (!isJson && !filePath.toLowerCase().endsWith(".csv")) {
                exportFile = new File(filePath + ".csv");
            } else {
                exportFile = file;
            }
            new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() {
                    contactList.getjProgressBar().setVisible(true);
                    contactList.getjProgressBar().setIndeterminate(true);
                    try {
                        Thread.sleep(500);
                        if (isJson) {
                            contactModel.exportToJson(exportFile);
                        } else {
                            contactModel.exportToCsv(exportFile);
                        }
                    } catch (InterruptedException e) {
                        UIUtils.notifyError("Error inesperado durante la exportación: " + e.getMessage(), notificationHandler);
                    }
                    return null;
                }
                @Override
                protected void done() {
                    contactList.getjProgressBar().setIndeterminate(false);
                    contactList.getjProgressBar().setVisible(false);
                    UIUtils.notifyInfo("Exportación realizada correctamente", notificationHandler);
                }
            }.execute();
        }
    }
}
