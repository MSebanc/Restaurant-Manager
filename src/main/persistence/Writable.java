package persistence;

import org.json.JSONObject;

// Interface for toJson() command
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
