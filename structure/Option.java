package structure;

import java.util.Map;

public class Option {
    private String shorthand;
    private String link;

    public String getShorthand() {
        return shorthand;
    }

    public Query getLink(Game game) {
        if (link.startsWith("ENDING: ")) {
            // End game
            return null;
        }
        Map<String, Query> queries = game.getQueries();
        return queries.get(link);
    }

    public String getEnding() {
        if (link.startsWith("ENDING: ")) {
            // End game
            return link.substring(8);
        }
        return null;
    }
}
