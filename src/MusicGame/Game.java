package MusicGame;


import java.io.*;
import java.util.*;

public class Game {

    /////////////////////////////////////////////////////////
    //                 all global variables                //
    /////////////////////////////////////////////////////////

    private String currentName;
    private int currentScore;
    private int prevNum;
    private HashMap<String, String> users;
    private List<SongInfo> songLibrary;
    private Map<Integer, String> scoreboard;

    Game() {
        this.currentName = null;
        this.currentScore = 0;

        this.users = loadUsers();
        this.songLibrary = loadSongs();
        this.scoreboard = loadScoreboard();
        this.prevNum = 0;

        login();
    }

    /////////////////////////////////////////////////////////
    //                  loading functions                  //
    /////////////////////////////////////////////////////////

    private HashMap<String, String> loadUsers() {
        HashMap<String, String> users = new HashMap<>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.csv"));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] splits = line.split(",");
                users.put(splits[0].toLowerCase(), splits[1]);
            }
        } catch (IOException ex) {
            System.out.println("Error, reading from user text file.");
        }
        return users;
    }

    private TreeMap<Integer, String> loadScoreboard() {
        TreeMap<Integer, String> scoreboard = new TreeMap<>(Collections.reverseOrder());
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("scoreboard.csv"));
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] splits = line.split(",");
                int score = Integer.parseInt(splits[1]);
                scoreboard.put(score, splits[0]);
            }
        } catch (IOException ex) {
            System.out.println("Error, reading scoreboard");
        }
        return scoreboard;
    }

    private List<SongInfo> loadSongs() {
        List<SongInfo> songLibrary = new ArrayList<>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("songLibrary.csv"));
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] splits = line.split(",");
                songLibrary.add(new SongInfo(splits[0], splits[1]));
            }
        } catch (IOException ex) {
            System.out.println("Error, reading from songLibrary text file.");
        }
        return songLibrary;
    }

    /////////////////////////////////////////////////////////
    //                    main functions                   //
    /////////////////////////////////////////////////////////


    private void login() {
        String enteredUsername = "";
        Scanner scan = new Scanner(System.in);
        int i;
        breaker();
        for (i = 3; i > 0; i--) {
            if (i < 3) {
                System.out.println("You have " + i + " more tries to login");
            }
            System.out.println("Please enter your username >>> ");
            enteredUsername = scan.nextLine().toLowerCase();
            System.out.println("Please enter your password >>> ");
            String enteredPassword = scan.nextLine();

            if (users.get(enteredUsername) != null && users.get(enteredUsername).equals(enteredPassword)) {
                breaker();
                System.out.println("Login successful!");
                currentName = (enteredUsername);
                loadSongs();
                break;
            } else {
                breaker();
                System.out.println("Login Cridentials invalid. ");
            }
        }
        if (i > 0) {
            menu();
        }
        if (i == 0) {
            breaker();
            System.out.println("You have reached the limit for unsuccessful login attempts!");
            exitProgram();
        }
    }

    private void menu() {
        Scanner scan = new Scanner(System.in);  //alows user input
        breaker();
        System.out.println(
                ",--.   ,--.,--. ,--. ,---.  ,--. ,-----.     ,----.     ,---.  ,--.   ,--.,------. \n" +
                        "|   `.'   ||  | |  |'   .-' |  |'  .--./    '  .-./    /  O  \\ |   `.'   ||  .---' \n" +
                        "|  |'.'|  ||  | |  |`.  `-. |  ||  |        |  | .---.|  .-.  ||  |'.'|  ||  `--,  \n" +
                        "|  |   |  |'  '-'  '.-'    ||  |'  '--'\\    '  '--'  ||  | |  ||  |   |  ||  `---. \n" +
                        "`--'   `--' `-----' `-----' `--' `-----'     `------' `--' `--'`--'   `--'`------'   ");
        breaker();
        System.out.println("[Play]Plays the Game\n[Scoreboard]To View ScoreBoard\n[HELP]For help\n[EXIT]To Exit the program");
        System.out.println("Welcome " + currentName + ", To get started enter one item from the list below (type help if unknown) >>> ");
        String option = scan.nextLine();    //the option they select is stored
        switch (option.toUpperCase()) { //what they entered is taken to uppercase for easy comparison
            case ("PLAY"):
                playGame();
                sleep(3000);
                break;
            case ("HELP"):
                breaker();
                System.out.println("Here are the syntaxes:\n[Play]Plays the Game\n[Scoreboard]To View ScoreBoard\n[HELP]For help\n[EXIT]To Exit the program");
                sleep(5000);
                menu();
                break;
            case ("SCOREBOARD"):
                printScoreboard();
                break;
            case ("EXIT"):
                exitProgram();
                break;
            case ("RELOG"):
                if(currentName.equalsIgnoreCase("dylan")){
                    breaker();
                    System.out.println("Log Out successful!");
                    login();
                }else{
                   menu();
                }
            default:
                breaker();
                System.out.println("Unknown Command, please try again");
                sleep(2000);
                menu();
        }
    }

    private void playGame() {
        Scanner scanner = new Scanner(System.in);
        currentScore = 0;
        boolean playerExit = false;
        int roundNumber = 1;
        do {
            breaker();
            int songNum = randomNum();
            for (int i = 1; i <= 2; i++) {

                String songArtist = songLibrary.get(songNum).artist;
                String songName = songLibrary.get(songNum).name;
                String censoredName = songLibrary.get(songNum).nameCensored;
                if (i == 1) {
                    System.out.println("("+roundNumber+")"+"This song is by " + songArtist + " and is called: " + censoredName);
                }

                System.out.println("Guess number " + i + " >>> ");
                String userGuess = scanner.nextLine();
                boolean answer = songName.equalsIgnoreCase(userGuess);
                breaker();
                if (answer) {
                    if (i == 1) {
                        currentScore += 3;
                        System.out.println("Congratulations you got that correct! You scored 3 points.");
                        break;
                    }
                    if(i==2){
                        System.out.println("Congratulations you got that correct! You scored 1 point");
                        currentScore += 1;
                    }

                }
                if (i == 2 && !answer) {
                    System.out.println("I'm sorry, you guess incorrectly twice. You scored " + currentScore + " points. Taking you back to the main menu");
                    playerExit = true;
                }
            }
            roundNumber++;
        } while (!playerExit);
        writeToScoreboard(currentName, Integer.toString(currentScore), true);
        sleep(2000);
        menu();
    }

    /////////////////////////////////////////////////////////
    //                  scoreboard functions               //
    /////////////////////////////////////////////////////////


    private void writeToScoreboard(String name, String score, boolean append) {
        try {
            StringBuilder entry = new StringBuilder();
            BufferedWriter writer = new BufferedWriter(new FileWriter("scoreboard.csv", append));
            entry.append(name);
            entry.append(",");
            entry.append(score);
            entry.append("\n");
            writer.write(entry.toString());
            writer.close();
        } catch (IOException ex) {
            System.out.println("Error adding player to scoreboard");
        }
    }

    private void printScoreboard() {
        loadScoreboard();
        sortScoreBoard();
        breaker();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("scoreboard.csv"));
            reader.readLine();
            for (int i = 1; i <= 5; i++) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String[] splits = line.split(",");
                System.out.println("[" + i + "]" + splits[0] + ":" + splits[1]);
            }
        } catch (IOException ex) {
            System.out.println("Error printing from scoreboard");
        }
        menu();
    }


    private void sortScoreBoard() {
        writeToScoreboard("Name", "Score", false);
        for (Map.Entry<Integer, String> entry : scoreboard.entrySet()) {
            writeToScoreboard(entry.getValue(), Integer.toString(entry.getKey()), true);
        }
    }

    /////////////////////////////////////////////////////////
    //                  helper functions                   //
    /////////////////////////////////////////////////////////

    private void breaker() {
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    private void exitProgram() {
        breaker();
        System.out.println("Exiting Program");
        breaker();
        System.exit(0);
    }

    private int randomNum() {
        int randNum;
        do {
            Random random = new Random();
            int songLength = songLibrary.size();
            randNum = random.nextInt(songLength);
        } while (randNum == prevNum);
        prevNum = randNum;
        return randNum;
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.out.println("Error when using sleep Function");
        }
    }
}

