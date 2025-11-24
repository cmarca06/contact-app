package notifications;

import java.awt.Window;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.SwingUtilities;

import utils.Toast;

public class NotificationHandler extends Thread {

    private static NotificationHandler instance;
    private final BlockingQueue<NotificationMessage> queue = new LinkedBlockingQueue<>();
    private final Window owner;
    private volatile boolean running = true;

    private NotificationHandler(Window owner) {
        super("notification-handler");
        setDaemon(true);
        this.owner = owner;
    }

    /**
     * Inicializa (o reutiliza) el manejador global para la ventana recibida.
     *
     * @param owner ventana sobre la cual se mostrarán las notificaciones
     * @return instancia lista para usarse
     */
    public static synchronized NotificationHandler init(Window owner) {
        if (instance == null) {
            instance = new NotificationHandler(owner);
            instance.start();
        }
        return instance;
    }

    /**
     * Publica una notificación de información.
     * 
     * @param message Mensaje a desplegar
     */
    public void post(String message) {
        post(message, Toast.ToastType.INFO);
    }

    /**
     * Publica una notificación de error.
     * 
     * @param message Mensaje a desplegar
     */
    public void postError(String message) {
        post(message, Toast.ToastType.ERROR);
    }

    /**
     * Publica una notificación con el tipo especificado.
     * 
     * @param message Mensaje a desplegar
     * @param type    Tipo de notificación
     */
    public void post(String message, Toast.ToastType type) {
        if (message == null || message.isBlank()) {
            return;
        }
        queue.offer(new NotificationMessage(message, type));
    }

    /**
     * Detiene el manejador de notificaciones.
     */
    public void shutdown() {
        running = false;
        interrupt();
    }

    /**
     * Hilo principal del manejador de notificaciones.
     */
    @Override
    public void run() {
        while (running) {
            try {
                // Esperar hasta que haya una notificación disponible
                NotificationMessage notification = queue.take();
                // Mostrar la notificación en el EDT
                SwingUtilities.invokeLater(() -> Toast.show(owner, notification.getMessage(), notification.getType()));
                // Esperar el tiempo de visualización antes de procesar la siguiente
                Thread.sleep(Toast.getDisplayTime() + 200);
            } catch (InterruptedException e) {
                // Verificar si se debe terminar el hilo
                if (!running) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
