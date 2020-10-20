package com.oop.intelimenus.config.loader;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.oop.intelimenus.config.wrappers.ConfigWrapper;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JsonConfigLoader {
    private static Gson gson = new GsonBuilder().create();

    /**
     * Convert json file into our config wrapper
     */
    public static ConfigLoader loader = file -> {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        JsonObject object = gson.fromJson(reader, JsonObject.class);
        return new ConfigWrapper(file, deepRead(object, ""));
    };

    private static Map<String, Object> deepRead(JsonObject object, String currentKey) {
        Map<String, Object> objectMap = new LinkedHashMap<>();

        for (Map.Entry<String, JsonElement> ob : object.entrySet()) {
            // Simple check if it's an section with array
            if (ob.getValue() instanceof JsonArray) {
                List<JsonElement> jsonElements = listFrom(ob.getValue().getAsJsonArray().iterator());

                // Is section
                if (jsonElements.stream().anyMatch(o -> o instanceof JsonObject)) {
                    for (JsonElement jsonElement : jsonElements) {
                        JsonObject asJsonObject = jsonElement.getAsJsonObject();
                        Preconditions.checkArgument(asJsonObject.has("key"), "Failed to find section key in json object" + asJsonObject);

                        String tempKey = currentKey.length() == 0 ? ob.getKey() + "." + asJsonObject.get("key").getAsString() : currentKey + "." + ob.getKey() + "." + asJsonObject.get("key").getAsString();
                        asJsonObject.remove("key");
                        objectMap.putAll(deepRead(asJsonObject, tempKey));
                    }
                } else
                    objectMap.put(currentKey.length() == 0 ? ob.getKey() : currentKey + "." + ob.getKey(), ob.getValue());

            } else if (ob.getValue() instanceof JsonObject) {
                objectMap.putAll(deepRead(ob.getValue().getAsJsonObject(), currentKey.length() == 0 ? ob.getKey() : currentKey + "." + ob.getKey()));

            } else {
                objectMap.put(currentKey.length() == 0 ? ob.getKey() : currentKey + "." + ob.getKey(), ob.getValue());
            }
        }

        return objectMap;
    }

    private static <T> List<T> listFrom(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }
}
