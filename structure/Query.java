package structure;

import java.util.Map;
import java.util.Map.Entry;

// Query class, this is a data class used to encode json data into a format I can use
// using GSON
public class Query {
    private String question;
    private Map<String, Option> options;

    public String getQuestion() {
        return question;
    }

    // Returns a specific option based on the string selection
    // Also i'm 99% sure jdk 11 should have the @Nullable annotation but it doesn't...
    public Option getOption(String selection) {
        selection = selection.toLowerCase();
        // Iterate through all the options in this query
        for (Entry<String, Option> optionSet : options.entrySet()) {
            Option option = optionSet.getValue();
            // Return this option if either it's shorthand or it's name matches the selection string
            if (optionSet.getKey().toLowerCase().equals(selection) || 
                    option.getShorthand().toLowerCase().equals(selection)) return option;
        }
        // If no matching option is found, return null
        return null;
    }

    public Map<String, Option> getOptions() {
        return options;
    }
}
