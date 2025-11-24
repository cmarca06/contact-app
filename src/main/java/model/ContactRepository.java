package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ContactRepository {

    private static final String FILE_NAME = "contacts.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String SEPARATOR = ";";

    /**
     * Carga contactos desde un archivo CSV.
     *
     * @return Lista de contactos cargados.
     */
    public List<Contact> loadContacts() {
        List<Contact> contacts = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return contacts;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(SEPARATOR, -1);
                if (data.length == 8) {
                    Contact contact = new Contact(
                            data[0],
                            unescape(data[1]),
                            unescape(data[2]),
                            unescape(data[3]),
                            unescape(data[4]),
                            unescape(data[6]),
                            Boolean.valueOf(data[7])
                    );
                    contacts.add(contact);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }

    /**
     * Guarda contactos en un archivo CSV.
     *
     * @param contacts Lista de contactos a guardar.
     */
    public void saveContacts(List<Contact> contacts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            bw.write(String.join(SEPARATOR, "id", "firstName", "lastName", "phone", "email", "date", "category", "favorite"));
            bw.newLine();

            for (Contact c : contacts) {
                bw.write(String.join(SEPARATOR,
                        c.getId(),
                        escape(c.getFirstName()),
                        escape(c.getLastName()),
                        escape(c.getPhone()),
                        escape(c.getEmail()),
                        DATE_FORMAT.format(c.getDate()),
                        escape(c.getCategory()),
                        c.getFavorite().toString()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escapar caracteres especiales al escribir CSV.
     *
     * @param s String a escapar
     * @return String escapado
     */
    private String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace(SEPARATOR, "\\" + SEPARATOR);
    }

    /**
     * Desescapar caracteres especiales al leer CSV.
     *
     * @param s String a desescapar
     * @return String desescapado
     */
    private String unescape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\" + SEPARATOR, SEPARATOR);
    }

    /**
     * Importa contactos desde un archivo CSV.
     *
     * @param file Archivo a importar
     * @return Lista de contactos importados
     * @throws IOException si ocurre un error de lectura
     */
    public List<Contact> importContacts(File file) throws IOException {

        // Crear lista para almacenar contactos importados
        List<Contact> contacts = new ArrayList<>();

        // Leer archivo CSV
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            // Leer cada línea del archivo
            while ((line = br.readLine()) != null) {
                // Omitir la primera línea 
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Dividir la línea en campos
                String[] data = line.split(SEPARATOR, -1);
                // Validar que la línea tenga el formato correcto
                if (data.length == 6) {
                    // Crear contacto a partir de los datos
                    Contact contact = new Contact(
                            data[0],
                            data[1],
                            data[2],
                            data[3],
                            data[4],
                            Boolean.valueOf(data[5])
                    );
                    // Agregar contacto a la lista
                    contacts.add(contact);
                }
            }
        }

        // Devolver la lista de contactos importados
        return contacts;
    }

    /**
     * Exporta contactos a un archivo CSV.
     *
     * @param file Archivo a exportar
     * @param contacts Lista de contactos a exportar
     * @throws IOException si ocurre un error de escritura
     */
    public void exportContacts(File file, List<Contact> contacts) throws IOException {

        synchronized (ContactRepository.class) {
            // Escribir archivo CSV
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                // Escribir encabezados
                bw.write(String.join(SEPARATOR, "Nombre", "Apellido", "Teléfono", "Email", "Categoría", "Favorito"));
                // Salto de línea
                bw.newLine();

                // Escribir cada contacto
                for (Contact c : contacts) {
                    // Formatear y escribir los datos del contacto
                    bw.write(String.join(SEPARATOR,
                            c.getFirstName(),
                            c.getLastName(),
                            c.getPhone(),
                            c.getEmail(),
                            c.getCategory(),
                            c.getFavorite().toString()
                    ));

                    // Salto de línea
                    bw.newLine();
                }
            }
        }
    }
}
