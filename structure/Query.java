package structure;

import java.util.Map;
import java.util.Map.Entry;

public class Query {
    private String question;
    private Map<String, Option> options;

    public String getQuestion() {
        return question;
    }

    public Option getOption(String selection) {
        selection = selection.toLowerCase();
        for (Entry<String, Option> optionSet : options.entrySet()) {
            Option option = optionSet.getValue();
            if (optionSet.getKey().toLowerCase().equals(selection) || 
                    option.getShorthand().toLowerCase().equals(selection)) return option;
        }
        return null;
    }

    public Map<String, Option> getOptions() {
        return options;
    }
}
