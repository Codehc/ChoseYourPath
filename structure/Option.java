package structure;

import java.util.Map;

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
