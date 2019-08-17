import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class dice {
    private static String player1name = "";
    private static String player2name = "";
    private static int players[][] = new int[2][3]; //score, rego's
    private static HashMap <String, String> users= new HashMap <>();
    private static int loginAttempts =3;

    private static void importValidUsers() {
        try {
            String fileLine;
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            while ((fileLine = reader.readLine()) != null) {
                String[] splits = fileLine.split(":");
                users.put(splits[0], splits[1]);
            }
        } catch (IOException e) {
            System.out.println("Error importing valid users, Program will Exit");
            System.exit(0);
        }
    }

    private static void login() {
        breaker();
        Scanner scan = new Scanner(System.in);
        String[][] inputtedDetails = new String[2][2];
        for (int i = 0; i < 2; i++) {   // for loop that lets them enter said values
            System.out.print("What's the username for Player " + (i + 1) + " ? >>> ");
            inputtedDetails[i][0] = scan.nextLine();
            System.out.print("What's the password for Player " + (i + 1) + " ? >>> ");
            inputtedDetails[i][1] = scan.nextLine();
        }

        if (inputtedDetails[0][1].equals(users.get(inputtedDetails[0][0])) && inputtedDetails[1][1].equals(users.get(inputtedDetails[1][0]))) {
            nameAssigner(inputtedDetails[0][0], inputtedDetails[1][0]);
            game();
        } else {
            loginAttempts--;
            loginFail();
        }
    }

    private static void loginFail() {
        if (loginAttempts == 0) {
            breaker();
            System.out.println("Sorry, you've attempted to login too many times, exiting program!");
            System.exit(0);
        } else {
            breaker();
            System.out.println("Login Failed! You have " + loginAttempts + " more attempt(s)");
            login();
        }
    }

    public static void main(String[] args) {
        importValidUsers();
        menu();
    }

    private static void menu() {
        Scanner scan = new Scanner(System.in);
        breaker();
        System.out.print("Welcome to the Dice Game, what would you like to do(type help if unknown) >>> ");
        String option = scan.nextLine();
        switch (option.toUpperCase()) {
            case ("HELP"):
                breaker();
                System.out.println("Here are the syntaxes:\n[HELP]For Help Menu\n[LOGIN]To Login and Play\n[SCORES]To View ScoreBoard\n[REGISTER]To create an account\n[EXIT]To Exit the program");
                menu();
                break;
            case ("LOGIN"):
                login();
                break;
            case ("SCORES"):
                //viewScores();
                break;
            case ("REGISTER"):
                //register();
                break;
            case("EXIT"):
                System.exit(0);
                break;
            default:
                breaker();
                System.out.println("Unknown Command, please try again");
                menu();
        }
    }


    private static void game() {    //

        for (int j = 0; j < 5; j++) {   //sets rounds
            breaker();
            System.out.println("This is round number " + (j + 1));


            for (int i = 0; i < 2; i++) {   //allows both die to be rolled for each player
                actualDiceGame(i);
            }
        }
        checkingForDoubles();
        declareWinner();
    }
    private static void nameAssigner(String user1, String user2){
        player1name = user1;
        player2name = user2;
    }

    private static void declareWinner() {
        breaker();
        if (players[0][1] == players[1][1]) {
            System.out.println("OOOOOO, there seems to be a tie! We will re roll one dice each, and the highest one wins!");
            boolean winnerFound = false;
            int[] points = new int[2];
            do {
                for (int i = 0; i < 2; i++) {
                    breaker();
                    String name = getPlayerName(i);
                    points[1] += dice();
                    System.out.println(name + "Scored a " + points[i]);
                }
                if (points[0] != points[1]) {
                    winnerFound = true;
                }
            } while (!winnerFound);
        } else {
            System.out.println("We have found a winner, Drum roll please...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            int winnerID;
            if (players[0][1] > players[1][1]) {
                winnerID = 0;
            } else {
                winnerID = 1;
            }
            breaker();
            System.out.println("The winner is " + getPlayerName(winnerID) + " with a score of " + players[winnerID][1]);
            menu();
        }
    }

    private static void checkingForDoubles() {
        int extraGos;
        breaker();
        if (players[0][2] > 0) {
            extraGos = players[0][2];
            players[0][2] = 0;
            String name = getPlayerName(0);
            System.out.println(name + " has " + extraGos + " extra go(s)");
            for (int i = 0; i < extraGos; i++) {
                breaker();
                actualDiceGame(0);
            }
        } else if (players[1][2] > 0) {
            extraGos = players[1][2];
            players[1][2] = 0;
            String name = getPlayerName(1);
            System.out.println(name + " has " + extraGos + " extra go(s)");
            for (int i = 0; i < extraGos; i++) {
                breaker();

                actualDiceGame(1);
            }
        }
        // so one time i managed to get a double in a double and it really fucked my brain over, so here's just a checker because ya know why not
        if (players[0][2] > 0 || players[1][2] > 0) {
            checkingForDoubles();
        }
    }

    private static String getPlayerName(int playerNumber) {
        String name;
        if (playerNumber == 0) {
            name = player1name;
        } else {
            name = player2name;
        }
        return name;
    }

    private static void actualDiceGame(int i) {
        String playerName = getPlayerName(i);

        int d1 = dice();
        int d2 = dice();

        System.out.println(playerName + " rolled a " + d1 + " and a " + d2);

        if (d1 == d2) {
            players[i][2] += 1;
            System.out.println("Congratulations, " + playerName + " scored a double this round");
        }

        players[i][1] += (d1 + d2);
        players[i][1] = scoreCheck(players[i][1]);
        System.out.println("After game rules, " + playerName + " has " + players[i][1] + " points!");
    }

    private static int scoreCheck(int score) {
        if (score < 0) {
            score = 0;
        } else if (score % 2 == 0) {
            score += 10;
        } else {
            score -= 5;
        }
        return score;
    }

    private static int dice() {
        Random random = new Random();
        int dice = random.nextInt(6) + 1;
        return dice;
    }

    private static void breaker() {
        System.out.println("--------------------");
    }
}
