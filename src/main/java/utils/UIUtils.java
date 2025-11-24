package utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import notifications.NotificationHandler;

public class UIUtils {

    /**
     * Limpia todos los campos de texto, combos y checkbox dentro de un
     * contenedor.
     *
     * @param container Contenedor que contiene los campos de texto.
     */
    public static void clearTextFields(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JTextField textField) {
                textField.setText("");
            } else if (c instanceof JComboBox<?> comboBox) {
                comboBox.setSelectedIndex(0);
            } else if (c instanceof JCheckBox checkBox) {
                checkBox.setSelected(false);
            }
        }
    }

    /**
     * Valida que los campos de texto y combos no estén vacíos.
     *
     * @param container Contenedor que contiene los campos de texto.
     * @return Mensaje de error si hay campos vacíos, null si todo está bien.
     */
    public static String validateFormFields(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JTextField textField) {
                String name = textField.getName();
                if (textField.getText().trim().isEmpty()) {
                    return "El campo " + name + " está vacío.";
                }
            } else if (c instanceof JComboBox<?> comboBox) {
                String name = comboBox.getName();
                if (comboBox.getSelectedIndex() == -1) {
                    return "Debe seleccionar una opción en " + name;
                }
            }
        }
        return null;
    }

    /**
     * Extrae la información del formulario en un Map<String, Object>
     *
     * @param container Contenedor que contiene los campos de texto.
     * @return Mapa con los nombres y valores de los campos del formulario.
     */
    public static Map<String, Object> extractFormData(Container container) {
        Map<String, Object> data = new HashMap<>();
        for (Component c : container.getComponents()) {
            if (c instanceof JTextField textField) {
                data.put(c.getName(), textField.getText().trim());
            } else if (c instanceof JComboBox<?> comboBox) {
                data.put(c.getName(), comboBox.getSelectedItem());
            } else if (c instanceof JCheckBox checkBox) {
                data.put(c.getName(), checkBox.isSelected());
            }
        }
        return data;
    }

    /**
     * Llena los campos del formulario con los datos proporcionados en el mapa.
     *
     * @param container Contenedor que contiene los campos de texto.
     * @param data Mapa con los nombres y valores de los campos del formulario.
     */
    public static void fillFormData(Container container, Map<String, Object> data) {
        for (Component c : container.getComponents()) {
            if (c instanceof JTextField textField) {
                Object value = data.get(c.getName());
                textField.setText(value != null ? value.toString() : "");
            } else if (c instanceof JComboBox<?> comboBox) {
                Object value = data.get(c.getName());
                if (value != null) {
                    comboBox.setSelectedItem(value);
                }
            } else if (c instanceof JCheckBox checkBox) {
                Object value = data.get(c.getName());
                if (value instanceof Boolean) {
                    checkBox.setSelected((Boolean) value);
                } else if (value != null) {
                    checkBox.setSelected(Boolean.parseBoolean(value.toString()));
                }
            }
        }
    }

    /**
     * Muestra un cuadro de mensaje informativo.
     *
     * @param message Texto del mensaje.
     */
    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un cuadro de mensaje de error.
     *
     * @param message Texto del mensaje.
     */
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static int showConfirm(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Confirmar",
                JOptionPane.YES_NO_OPTION);
    }

    /**
     * Muestra un JPanel dentro de otro JPanel, reemplazando su contenido.
     *
     * @param parent JPanel contenedor.
     * @param jpanel JPanel a mostrar.
     */
    public static void showPanel(JPanel parent, JPanel jpanel) {
        parent.removeAll();
        parent.add(jpanel, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }

    /**
     * Habilita o deshabilita un JButton.
     *
     * @param button JButton a modificar.
     * @param status true para habilitar, false para deshabilitar.
     */
    public static void setButtonVisible(JButton button, boolean status) {
        button.setVisible(status);
    }

    /**
     * Notifica un mensaje de información usando el manejador de notificaciones si
     * está disponible, o un cuadro de diálogo en caso contrario.
     * 
     * @param message Mensaje a notificar
     * @param notificationHandler Manejador de notificaciones (puede ser null)
     */
    public static void notifyInfo(String message, NotificationHandler notificationHandler) {
        if (message == null || message.isBlank()) {
            return;
        }
        if (notificationHandler != null) {
            notificationHandler.post(message);
        } else {
            showInfo(message);
        }
    }

    /**
     * Notifica un mensaje de error usando el manejador de notificaciones si
     * está disponible, o un cuadro de diálogo en caso contrario.
     * 
     * @param message  Mensaje a notificar
     * @param notificationHandler Manejador de notificaciones (puede ser null)
     */
    public static void notifyError(String message, NotificationHandler notificationHandler) {
        if (message == null || message.isBlank()) {
            return;
        }
        if (notificationHandler != null) {
            notificationHandler.postError(message);
        } else {
            showError(message);
        }
    }
}
