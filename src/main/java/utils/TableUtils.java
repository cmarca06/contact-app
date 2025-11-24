package utils;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.Contact;

public class TableUtils {

    /**
     * Llena una JTable con la lista de contactos
     *
     * @param table JTable a llenar
     * @param contacts Lista de contactos
     */
    public static void fillContactsTable(JTable table, List<Contact> contacts) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Contact c : contacts) {
            model.addRow(new Object[]{
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getPhone(),
                c.getEmail(),
                c.getCategory(),
                c.getFavorite() ? "Sí" : "No"
            });
        }
    }

    /**
     * Obtiene el valor de la columna seleccionada de la fila seleccionada en la
     * tabla.
     *
     * @param table JTable de donde obtener el valor
     * @param columnIndex índice de la columna que quieres obtener
     * @return valor de la celda seleccionada como String, o null si no hay
     * selección
     */
    public static String getSelectedCellValue(JTable table, int columnIndex) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1 && columnIndex >= 0 && columnIndex < table.getColumnCount()) {
            TableModel model = table.getModel();
            Object value = model.getValueAt(selectedRow, columnIndex);
            return value != null ? value.toString() : null;
        }
        return null;
    }

}
