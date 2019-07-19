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

    public static void main(String[] args) {
        importValidUsers();
        menu();
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
        if ((authorisedUsers.get(inputUser[0])) == (null) || (authorisedUsers.get(inputUser[1])) == (null)) {  //uses the usernames as keys to see if they are found in the hashmap and if theyre not, it returns null so heres a work around it
            loginAttempts--;
            loginFail();
        } else if ((authorisedUsers.get(inputUser[0])).equals(inputPass[0]) && (authorisedUsers.get(inputUser[1])).equals(inputPass[1])) { //if both usernames are found in hashmap it says all good :)
            game();
        } else {
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
        int[] points = new int[2];
        int dice1, dice2;
        boolean isDouble;
        int[] extraGo = new int[2];

        for (int i = 0; i < 5; i++) {
            divider();
            for (int j = 0; j < 2; j++) {
                divider();
                System.out.println("This is "+inputUser[j]+"'s roll");
                dice1 = roleDice();
                dice2 = roleDice();
                System.out.println(inputUser[j] + " rolled a " + dice1 + " and a " + dice2);
                points[j] = rulesCheck(j);
                isDouble = rollDouble(dice1, dice2);
                if(isDouble){
                    System.out.println("Congrats "+inputUser[j]+" gets an extra roll ");
                    extraGo[j]+=1;
                }
                points[j] += (dice1+dice2);
                System.out.println(inputUser[j]+"'s Total score for the round is "+points[j]);
            }
        }


    }

    private static boolean rollDouble(int d1, int d2) {
        return ((d1 == d2) ? true : false);
    }

    private static int roleDice() {
        int score = (int) (Math.random() * 6 + 1);
        return score;
    }

    private static int rulesCheck(int points) {
        if (points < 0) {
            points = 0;
        } else if (points % 2 == 0) {
            points += 10;
        } else {
            points -= -5;
        }
        return points;
    }

    private static void divider() {
        System.out.println("-----------------------------------------");
    }
}


