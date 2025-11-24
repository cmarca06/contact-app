package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public final class Toast {

    // Tipos de toast soportados
    public enum ToastType {
        INFO,
        ERROR
    }

    private static final int DISPLAY_TIME = 2500;

    /**
     * Muestra un mensaje flotante cerca de la parte inferior de la ventana propietaria.
     *
     * @param owner Ventana propietaria
     * @param message Mensaje a desplegar
     */
    public static void show(Window owner, String message) {
        show(owner, message, ToastType.INFO);
    }

    /**
     * Muestra un mensaje flotante cerca de la parte inferior de la ventana propietaria.
     * 
     * @param owner Ventana propietaria
     * @param message Mensaje a desplegar
     * @param type Tipo de toast
     */
    public static void show(Window owner, String message, ToastType type) {
        if (owner == null || message == null || message.isBlank()) {
            return;
        }

        // Crear contenido del toast
        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        panel.setBackground(resolveColor(type));

        // Configurar ventana del toast
        JWindow toastWindow = new JWindow(owner);
        toastWindow.setBackground(new Color(0, 0, 0, 0));
        toastWindow.add(panel);
        toastWindow.pack();

        // Posicionar centrado horizontal y unos píxeles arriba del borde inferior
        Dimension ownerSize = owner.getSize();
        int x = owner.getX() + (ownerSize.width - toastWindow.getWidth()) / 2;
        int y = owner.getY() + ownerSize.height - toastWindow.getHeight() - 60;
        toastWindow.setLocation(x, y);

        toastWindow.setAlwaysOnTop(true);
        toastWindow.setVisible(true);

        // Ocultar automáticamente después del tiempo configurado
        Timer timer = new Timer(DISPLAY_TIME, e -> {
            toastWindow.setVisible(false);
            toastWindow.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Resuelve el color de fondo según el tipo de toast.
     * 
     * @param type Tipo de toast
     * @return Color de fondo correspondiente
     */
    private static Color resolveColor(ToastType type) {
        if (type == ToastType.ERROR) {
            return new Color(198, 40, 40, 230);
        }
        return new Color(33, 33, 33, 220);
    }

    /**
     * Obtiene el tiempo de visualización del toast en milisegundos.
     * 
     * @return Tiempo de visualización en milisegundos
     */
    public static int getDisplayTime() {
        return DISPLAY_TIME;
    }

    /**
     * Asegura que la invocación se ejecute en el EDT, útil para pruebas
     * puntuales.
     */
    public static void showLater(Window owner, String message) {
        SwingUtilities.invokeLater(() -> show(owner, message));
    }

    /**
     * Asegura que la invocación se ejecute en el EDT, útil para pruebas
     * puntuales.
     */
    public static void showLater(Window owner, String message, ToastType type) {
        SwingUtilities.invokeLater(() -> show(owner, message, type));
    }
}
