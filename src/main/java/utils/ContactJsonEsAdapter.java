package utils;

import com.google.gson.*;
import model.Contact;
import java.lang.reflect.Type;

/**
 * Adaptador personalizado para serializar y deserializar Contact usando campos en español.
 */
public class ContactJsonEsAdapter implements JsonSerializer<Contact>, JsonDeserializer<Contact> {
    @Override
    public JsonElement serialize(Contact src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("Nombre", src.getFirstName());
        obj.addProperty("Apellido", src.getLastName());
        obj.addProperty("Teléfono", src.getPhone());
        obj.addProperty("Email", src.getEmail());
        obj.addProperty("Categoría", src.getCategory());
        obj.addProperty("Favorito", src.getFavorite());
        return obj;
    }

    @Override
    public Contact deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String firstName = getAsString(obj, "Nombre");
        String lastName = getAsString(obj, "Apellido");
        String phone = getAsString(obj, "Teléfono");
        String email = getAsString(obj, "Email");
        String category = getAsString(obj, "Categoría");
        Boolean favorite = obj.has("Favorito") ? obj.get("Favorito").getAsBoolean() : false;
        return new Contact(firstName, lastName, phone, email, category, favorite);
    }

    private String getAsString(JsonObject obj, String member) {
        return obj.has(member) && !obj.get(member).isJsonNull() ? obj.get(member).getAsString() : "";
    }
}
