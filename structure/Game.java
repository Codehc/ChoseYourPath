package structure;

import java.util.Map;

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
