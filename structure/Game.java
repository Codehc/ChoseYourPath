package structure;

import java.util.Map;

// Game class, this is a data class used to encode json data into a format I can use
// using GSON
public class Game {
    private String entry;
    private Map<String, Query> queries;

    public Query getEntry() {
        return queries.get(entry);
    }

    public Map<String, Query> getQueries() {
        return queries;
    }
}
