/* AP CSA 1 Chose Your Path Project
 * - By Brendan Aeria and Reza Bagheri
 * - 12/21/21
 * 
 * Description: Play a dungeons style chose your path game with at least 9 possible paths. You can load up custom games by creating
 *              "cartridges" which are json files in our format. You can check out "dishes.json" to see an example game.
 * Preconditions: A valid game exists in the format that is similar to the "dishes.json" example json file. YOU ALSO NEED GSON ON YOUR CLASSPATH, if you don't have that
 *                download GSON from https://search.maven.org/artifact/com.google.code.gson/gson/2.8.9/jar and drag the Jar to your referenced libraries in VSCode, do that
 *                by going down expanding "Java Projects -> Choose Your Path" in the bottom left and using the + button next to "Referenced Libraries" to add the GSON Jar.
 *                I know using Maven, Gradle, or some actually good Java package manager is better than this but this is a lot faster than setting up a whole project.
 * Postconditions: Your game will be played with you being able to chose your path as you play.
 * 
 * NOTICE: All comments that include info on where program requirements have the REQUIREMENT tag (use ctr+f)
 * NOTICE: There aren't that many "nested if clauses" BUT that's because our program is dynamic. It doesn't have only one story
 *         so hardcoding nested ifs isn't a good idea, it uses recursion with the askQuery method to do what you wanted nested if
 *         clauses to do but in a dynamic way that changes based on the game.
 * REQUIREMENT: Comments throughout
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Random;
import java.util.Scanner;
import java.util.Map.Entry;

import com.google.gson.Gson;

import structure.Game;
import structure.Option;
import structure.Query;

class Main {
    // Our static scanner, no need to create and close a new one in every function
    static Scanner scn = new Scanner(System.in);
    public static void main(String[] args) {
        // Program entry point
        Main main = new Main();
        System.out.println("Please enter the name of a game:");
        main.initGame();
    }

    private void initGame() {
        /* This starts a game, it firsts asks you what game you want to play
         *  - Games are dynamically loaded from different json files, this means you can make a new json file
         *    and if you follow our format, you can create a new game at any point with a full story line and
         *    as many paths as possible
        */
        // REQUIREMENT: Loads a game with several choices, the games are loaded in .json files
        // REQUIREMENT: At least 9 possible paths, each game (.json file w/ a game in it) can have at as many paths as possible,
        //              the built in example has far more than 9 possible paths
        String game = scn.nextLine();
        File story = new File(game + ".json");

        // REQUIREMENT: At least one compound boolean expression
        if (story.exists() && !game.toLowerCase().equals("random")) {
            // If the game you chose exists, it'll start it
            runner(story);
        } else if (game.toLowerCase().equals("random")) {
            // REQUIREMENT: At least one else if ^^
            /* If you ask for a random game, it'll chose a random game and let you play it
             *  - This is done by creating an array of files in a directory and filtering them for files
             *    that end with .json
            */
            File dir = new File(".");
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".json");
                }
            });
            // It then choses a random game from that array and runs it
            runner(files[new Random().nextInt(files.length)]);
        } else {
            // If you don't chose a random game or a valid game, it'll make you chose a valid game to play
            System.out.println("Sorry, that's not a valid directory for a game. Please enter another one:");
            initGame();
        }
    }

    private void runner(File story) {
        // Breakpoint for visual clarity when playing the game, it just makes the game look better
        System.out.println("------------------------------------------------\n" + story.getName() + "\n");
        
        /* Initiates GSON, you will need this in order to run the game
         *  - Incase you don't know, GSON is a library by Google which allows for easy parsing of JSON
         *    in Java
         *    * In order to use GSON, I create classes (in the structure directory, in this case) which GSON
         *      will then instantiate and fill out using reflection
        */
        Gson gson = new Gson();
        String data = loadData(story);

        // This is where I ask GSON to convert my raw JSON into an object I can use
        Game game = gson.fromJson(data, Game.class);

        /* Starts the game
         *  - I input the game, the entry point of the game, and whether or not this is the first run of askQuery
         *    * The first run matters since if someone enters an invalid option once it asks a question, it'll
         *      rerun the method but include a message prompting the user to pick a VALID option.
        */
        askQuery(game, game.getEntry(), true);
    }

    /* The bulk of the game
     *  - AskQuery will take a query object and ask it by asking the user the question and prompting them to enter
     *    one of the options.
     *  - If the user picks a valid option, it then checks that options "link", the link tells the program where to go next
     *     * The "link" is actually the name of another query in the JSON so it'll go to that query and use that
     *     * The function is then reran but with firstTime set to true (since its the first time asking the new question its about to ask)
     *       and the query parameter set to the new query which it gets from the "link"
     *     * In the case that the link is set to END, it'll end the program instead of going to a new link.
     *  - If the user picks an invalid option it'll rerun prompting them to pick a valid option
    */
    private void askQuery(Game game, Query query, boolean firstTime) {
        // Prompt the user to enter a valid option if the need to
        if (!firstTime) System.out.println("Sorry, that's not a valid option.");
        
        // Print questions
        System.out.println(query.getQuestion());

        // Print options
        // REQUIREMENT: Print statements, current choices, prints out all of the current choices
        for (Entry<String, Option> optionSet : query.getOptions().entrySet()) System.out.println("- " + optionSet.getKey());

        // Reads the input of the user and use that to get the option they pick
        String input = scn.nextLine();
        Option optionPicked = query.getOption(input);
        
        // If they pick an invalid option, it will ask them to pick a valid option w/ a rerun of the method
        if (optionPicked == null) askQuery(game, query, false);
        else {
            // If they pick a valid option, it first prints a break in order to show where the new question starts and then asks the next question
            System.out.println("------------------------------------------------");
            // REQUIREMENT: Print statements, previous choices, the trigger is defined in every option and is dependant on what path the user takes
            System.out.println(optionPicked.getTrigger());
            Query nextQuery = optionPicked.getLink(game);
            if (nextQuery == null) {
                // End the game by printing the end game message and ending the program
                scn.close();
                System.exit(0);
            } else {
                // REQUIREMENT: At least two nested if clauses, read the notice at the top, we use recursion instead of nested if clauses (although
                //              there is still one nested if clause to show that we do know how to use them)
                askQuery(game, nextQuery, true);
            }
        }
    }

    // Reads a text from a file as a string and returns that
    // This is a very simple function so I won't comment how it works
    private String loadData(File story) {
        String result = "";
        try {
            Scanner storyScn = new Scanner(story);
            while (storyScn.hasNextLine()) {
                result += storyScn.nextLine();
            }
            storyScn.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }
}