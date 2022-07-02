
import com.google.gson.*;

import java.lang.reflect.Type;

public class AbstractAccionAdapter implements JsonSerializer<Accion>, JsonDeserializer<Accion> {

    @Override
    public JsonElement serialize(Accion src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));

        return result;
    }

    @Override
    public Accion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            return context.deserialize(element, Class.forName(type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException(Colores.rojo() + "Unknown element type: " + Colores.blanco() + type, cnfe);
        }
    }
}