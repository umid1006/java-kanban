package manager;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(localDateTime.toString());
    }

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) {
        return LocalDateTime.parse(jsonElement.getAsString());
    }
}
