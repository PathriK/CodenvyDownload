package in.pathri.codenvydownload.utilities;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by keerthi on 24-12-2016.
 */
public class TableGsonAdapter implements JsonDeserializer<Table> {
    private static final String className = "TableGsonAdapter";

    @SuppressWarnings("unchecked")
    @Override
    public Table deserialize(JsonElement json,
                             Type type,
                             JsonDeserializationContext context)
            throws JsonParseException {
        Table table = HashBasedTable.create();
        if (json != null) {
            CustomLogger.d(className, "Table Deserialize", "json", json.toString());
            JsonObject object = (JsonObject) json;
            JsonElement element = object.get("backingMap");
            if (element != null) {
                for (Map.Entry<String, JsonElement> entry : ((JsonObject) element).entrySet()) {
                    String row = entry.getKey();
                    for (Map.Entry<String, JsonElement> column : ((JsonObject) entry.getValue()).entrySet()) {
                        table.put(row, column.getKey(), column.getValue());
                    }
                }
            } else {
                CustomLogger.i(className, "Table Deserialize", "element is null");
            }
        } else {
            CustomLogger.i(className, "Table Deserialize", "json is null");
        }
        return table;
    }
}
