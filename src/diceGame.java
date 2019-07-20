import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class diceGame {
    private static Scanner scan = new Scanner(System.in);
    private static HashMap<String, String> authorisedUsers = new HashMap<>();
    private static int loginAttempts = 3;
    private static String[] inputUser = new String[2], inputPass = new String[2];  //array to hold usernames and passwords entered by usser
    private static int[] points ={0,0};
    private static int[] extraGo = new int[2];

    public static void main(String[] args) {
        importValidUsers();
       // menu();
        game();
    }

    private static void menu() {
        divider();
        System.out.print("Welcome to the Dice Game, what would you like to do(type help if unknown) >>> ");
        String option = scan.nextLine();
        switch (option.toUpperCase()) {
            case ("HELP"):
                divider();
                System.out.println("Here are the syntaxes:\n[HELP]For Help Menu\n[LOGIN]To Login and Play\n[SCORES]To View ScoreBoard");
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
            default:
                divider();
                System.out.println("Unknown Command, please try again");
                menu();
        }
    }

    private static void importValidUsers() {
        try {
            String fileLine;
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            while ((fileLine = reader.readLine()) != null) {
                String[] splits = fileLine.split(":");
                authorisedUsers.put(splits[0], splits[1]);
            }
        } catch (IOException e) {
            System.out.println("Error importing valid users, Program will Exit");
            System.exit(0);
        }
    }

    private static void login() {
        divider();
        for (int i = 0; i < 2; i++) {   // for loop that lets them enter said values
            System.out.print("What's the username for Player " + (i + 1) + " ? >>> ");
            inputUser[i] = scan.nextLine();
            System.out.print("What's the password for Player " + (i + 1) + " ? >>> ");
            inputPass[i] = scan.nextLine();
        }

        if (inputPass[0].equals(authorisedUsers.get(inputUser[0])) && inputPass[1].equals(authorisedUsers.get(inputUser[1]))) {
            game();
        } else {
            loginAttempts--;
            loginFail();
        }
    }

    private static void loginFail() {
        if (loginAttempts == 0) {
            divider();
            System.out.println("Sorry, you've attempted to login too many times, exiting program!");
            System.exit(0);
        } else {
            divider();
            System.out.println("Login Failed! You have " + loginAttempts + " more attempt(s)");
            login();
        }
    }

    private static void game() {
        for (int i = 0; i < 5; i++) {
            divider();
            System.out.println("                ROUND " + (i + 1));
            for (int j = 0; j < 2; j++) {
                diceRoll(j);
            }
        }

            for (int i = 0; i < 2; i++) {
                divider();
                if (extraGo[i] > 0) {
                    System.out.println(inputUser[i] + " has " + extraGo[i] + " extra go's");
                    extraGos(extraGo[i], i);
                    extraGo[i] = 0;
                }
            }
            winner();
    }

    private static void diceRoll(int playerID) {
        int dice1, dice2;
        boolean isDouble;
        pause();
        divider();
        System.out.println("This is " + inputUser[playerID] + "'s roll");
        dice1 = roleDice();
        dice2 = roleDice();
        System.out.println(inputUser[playerID] + " rolled a " + dice1 + " and a " + dice2);
        points[playerID] += (dice1 + dice2);
        points[playerID] = rulesCheck(points[playerID]);
        isDouble = rollDouble(dice1, dice2);
        if (isDouble) {
            System.out.println("Congrats " + inputUser[playerID] + " gets an extra roll ");
            extraGo[playerID] += 1;
        }
        System.out.println(inputUser[playerID] + "'s Total score for the round is " + points[playerID]);
    }

    private static void extraGos(int numberOfTimes, int playerID) {
        divider();
        for (int i = 0; i < numberOfTimes; i++) {
            diceRoll(playerID);
        }
        winner();
    }

    private static void pause() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static boolean rollDouble(int d1, int d2) {
        return d1 == d2;
    }

    private static int roleDice() {
        int score = (int) (Math.random() * 6 + 1);
        return score;
    }

    private static int rulesCheck(int points) {
        if (points % 2 == 0) {
            points += 10;
        } else  {
            points -= 5;
        }
        if (points < 0) {
            points = 0;
        }
        return points;
    }
    private static void winner() {
        if (points[0] == points[1]) {
            draw();
            int winner = (points[0] > points[1] ? 0 : 1);
            divider();
            System.out.println("THE WINNER IS " + (inputUser[winner].toUpperCase()) + " WITH THE SCORE OF " + points[winner]);
        }
    }
    private static void draw(){
        points[0] = roleDice();
        points[1] = roleDice();
        divider();
        System.out.println("Points were equal, reroll has done");
            winner();
        }



    private static void divider() {
        System.out.println("----------------------------------------");
    }
}