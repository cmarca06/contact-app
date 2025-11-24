package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import thread.ContactValidationThread;
import utils.ImportResult;

public class ContactModel {

    private final ContactRepository contactRepository;
    private final Object contactListLock = new Object();
    private final ContactLockManager lockManager = new ContactLockManager();
    private List<Contact> contactList;

    public ContactModel() {
        contactRepository = new ContactRepository();
        contactList = contactRepository.loadContacts();
    }

    /**
     * Obtiene todos los contactos.
     *
     * @return Lista inmutable de contactos.
     */
    public List<Contact> getAllContacts() {
        synchronized (contactListLock) {
            return Collections.unmodifiableList(new ArrayList<>(contactList));
        }
    }

    /**
     * Agrega un nuevo contacto.
     *
     * @param c Contacto a agregar.
     */
    public void addContact(Contact c) {
        synchronized (contactListLock) {
            // Agregar contacto a la lista
            contactList.add(c);
            // Guardar cambios en el repositorio
            contactRepository.saveContacts(contactList);
        }
    }

    /**
     * Busca un contacto por su ID.
     *
     * @param id ID del contacto a buscar.
     * @return Contacto encontrado o null si no existe.
     */
    public Contact findById(String id) {
        // Validar el ID del contacto
        if (id == null || id.isEmpty()) {
            return null;
        }

        synchronized (contactListLock) {
            // Buscar el contacto en la lista
            for (Contact c : contactList) {
                if (c.getId().equals(id)) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * Elimina un contacto por su ID.
     *
     * @param id ID del contacto a eliminar.
     */
    public void deleteContact(String id) {
        // Validar el ID del contacto
        if (id == null || id.isEmpty()) {
            return;
        }

        synchronized (contactListLock) {
            // Eliminar el contacto de la lista
            contactList.removeIf(c -> c.getId().equals(id));
            // Guardar cambios en el repositorio
            contactRepository.saveContacts(contactList);
        }
    }

    /**
     * Actualiza un contacto existente.
     *
     * @param updated Contacto con los datos actualizados.
     */
    public void updateContact(Contact updated) {
        // Validar el contacto actualizado
        if (updated == null || updated.getId() == null || updated.getId().isEmpty()) {
            return;
        }

        synchronized (contactListLock) {
            // Buscar el contacto en la lista y actualizarlo
            for (int i = 0; i < contactList.size(); i++) {
                if (contactList.get(i).getId().equals(updated.getId())) {
                    contactList.set(i, updated);
                    break;
                }
            }
            // Guardar cambios en el repositorio
            contactRepository.saveContacts(contactList);
        }
    }

    /**
     * Importa contactos desde un archivo CSV validando duplicados mediante hilos.
     *
     * @param file Archivo CSV a importar.
     * @return Resultado de la importación.
     * @throws RuntimeException si ocurre un error de lectura o formato.
     */
    public ImportResult importFromCsv(File file) {
        int imported = 0;
        int duplicates = 0;
        List<Contact> pendingContacts = new ArrayList<>();

        try {
            // Importar contactos desde el archivo CSV
            List<Contact> importedContacts = contactRepository.importContacts(file);
            for (Contact contact : importedContacts) {
                // Validar duplicados usando hilo
                ContactValidationThread validationThread = new ContactValidationThread(this, contact);
                // Iniciar la validación
                validationThread.start();
                // Esperar a que termine la validación
                try {
                    validationThread.join();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Validación interrumpida durante la importación", ex);
                }

                // Verificar si es un duplicado
                if (validationThread.isDuplicate()) {
                    duplicates++;
                    continue;
                }

                // Agregar contacto si no es duplicado
                pendingContacts.add(contact);
                imported++;
            }

            if (!pendingContacts.isEmpty()) {
                synchronized (contactListLock) {
                    // Agregar contactos pendientes a la lista
                    contactList.addAll(pendingContacts);
                    // Guardar cambios en el repositorio
                    contactRepository.saveContacts(contactList);
                }
            }

            return new ImportResult(imported, duplicates);
        } catch (IOException ex) {
            // Lanzar excepción en caso de error de lectura
            throw new RuntimeException("Error al importar contactos: " + ex.getMessage(), ex);
        }
    }

    /**
     * Exporta los contactos actuales a un archivo CSV.
     *
     * @param file Archivo destino.
     * @throws RuntimeException si ocurre un error de escritura.
     */
    public void exportToCsv(File file) {
        try {
            // Exportar contactos al archivo CSV
            List<Contact> snapshot;
            synchronized (contactListLock) {
                snapshot = new ArrayList<>(contactList);
            }
            contactRepository.exportContacts(file, snapshot);
        } catch (IOException ex) {
            // Lanzar excepción en caso de error
            throw new RuntimeException("Error al exportar contactos: " + ex.getMessage(), ex);
        }
    }

    /**
     * Intenta bloquear un contacto para edición.
     *
     * @param contactId ID del contacto.
     * @param ownerToken Token del propietario del bloqueo.
     * @return true si el bloqueo se obtuvo, false si ya esta tomado.
     */
    public boolean acquireContactLock(String contactId, String ownerToken) {
        return lockManager.tryLock(contactId, ownerToken);
    }

    /**
     * Libera el bloqueo de edición del contacto dado.
     *
     * @param contactId ID del contacto.
     * @param ownerToken Token del propietario.
     */
    public void releaseContactLock(String contactId, String ownerToken) {
        lockManager.unlock(contactId, ownerToken);
    }

    /**
     * Indica si el contacto esta bloqueado por cualquier usuario.
     *
     * @param contactId ID del contacto.
     * @return true si esta bloqueado, false en caso contrario.
     */
    public boolean isContactLocked(String contactId) {
        return lockManager.isLocked(contactId);
    }
}

