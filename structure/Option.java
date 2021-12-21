package structure;

import java.util.Map;

// Option class, this is a data class used to encode json data into a format I can use
// using GSON
public class Option {
    private String trigger;
    private String shorthand;
    private String link;

    public String getShorthand() {
        return shorthand;
    }

    public String getTrigger() {
        return trigger;
    }

    public Query getLink(Game game) {
        if (link.equals("END")) {
            // End game
            return null;
        }
        Map<String, Query> queries = game.getQueries();
        return queries.get(link);
    }
}
