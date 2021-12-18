import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Map.Entry;

import com.google.gson.Gson;

import structure.Game;
import structure.Option;
import structure.Query;

class Main {
    static Scanner scn = new Scanner(System.in);;
    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    private void start() {
        System.out.println("Please enter the name of a game:");
        initGame();
    }

    private void initGame() {
        String game = scn.nextLine();
        File story = new File(game + ".json");
        if (story.exists()) {
            runner(story);
        } else {
            System.out.println("Sorry, that's not a valid directory for a game. Please enter another one:");
            initGame();
        }
    }

    private void runner(File story) {
        Gson gson = new Gson();
        String data = loadData(story);
        Game game = gson.fromJson(data, Game.class);

        askQuery(game, game.getEntry(), true);
    }

    private void askQuery(Game game, Query query, boolean firstTime) {
        if (!firstTime) System.out.print("Sorry, that's not a valid option. ");
        System.out.println(query.getQuestion());

        // Print options
        for (Entry<String, Option> optionSet : query.getOptions().entrySet()) System.out.println("- " + optionSet.getKey());

        String input = scn.nextLine();
        Option optionPicked = query.getOption(input);
        
        if (optionPicked == null) askQuery(game, query, false);
        else {
            System.out.println("------------------------------------------------");
            Query nextQuery = optionPicked.getLink(game);
            if (nextQuery == null) {
                // End the game
                endGame(game, optionPicked.getEnding());
            } else {
                askQuery(game, nextQuery, true);
            }
        }
    }

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

    private void endGame(Game game, String ending) {
        System.out.println(ending);
    }
}