package persistence;

import org.json.JSONObject;

// Interface for toJson() command: Code structure is based on the project JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
