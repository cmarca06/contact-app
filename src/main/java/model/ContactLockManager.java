package model;

import java.util.HashMap;
import java.util.Map;

class ContactLockManager {

    private final Map<String, String> activeLocks = new HashMap<>();

    /**
     * Intenta bloquear un contacto para el propietario indicado.
     *
     * @param contactId ID del contacto a bloquear
     * @param ownerToken Identificador del propietario del bloqueo
     * @return true si el bloqueo se adquirio, false en caso contrario
     */
    public synchronized boolean tryLock(String contactId, String ownerToken) {
        if (contactId == null || ownerToken == null) {
            return false;
        }

        String currentOwner = activeLocks.get(contactId);
        if (currentOwner == null || currentOwner.equals(ownerToken)) {
            activeLocks.put(contactId, ownerToken);
            return true;
        }
        return false;
    }

    /**
     * Libera el bloqueo de un contacto si pertenece al propietario indicado.
     *
     * @param contactId ID del contacto bloqueado
     * @param ownerToken Identificador del propietario del bloqueo
     */
    public synchronized void unlock(String contactId, String ownerToken) {
        if (contactId == null || ownerToken == null) {
            return;
        }

        String currentOwner = activeLocks.get(contactId);
        if (ownerToken.equals(currentOwner)) {
            activeLocks.remove(contactId);
        }
    }

    /**
     * Indica si un contacto esta bloqueado por alguien.
     *
     * @param contactId ID del contacto
     * @return true si esta bloqueado, false si esta libre
     */
    public synchronized boolean isLocked(String contactId) {
        if (contactId == null) {
            return false;
        }
        return activeLocks.containsKey(contactId);
    }

    /**
     * Indica si un contacto esta bloqueado por alguien distinto al token dado.
     *
     * @param contactId ID del contacto
     * @param ownerToken Token del posible propietario
     * @return true si otro propietario posee el bloqueo, false en caso contrario
     */
    public synchronized boolean isLockedByOther(String contactId, String ownerToken) {
        if (contactId == null) {
            return false;
        }
        String currentOwner = activeLocks.get(contactId);
        return currentOwner != null && (ownerToken == null || !currentOwner.equals(ownerToken));
    }
}
