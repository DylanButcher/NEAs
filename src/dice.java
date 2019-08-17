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

    private static void importValidUsers() {    //imports users from text file called users
        try {
            String fileLine;
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            while ((fileLine = reader.readLine()) != null) {
                String[] splits = fileLine.split(":");  //usernames and passwords are splits up by a colon in the text file so it splits them into username and password
                users.put(splits[0], splits[1]);        //assigns them to a hashmap to be used in the login process
            }
        } catch (IOException e) {   //some gay errors
            System.out.println("Error importing valid users, Program will Exit");
            System.exit(0);
        }
    }

    private static void login() {
        breaker();
        Scanner scan = new Scanner(System.in);
        String[][] inputtedDetails = new String[2][2];  //an array for holding the username and passwords entereed
        for (int i = 0; i < 2; i++) {   // for loop that lets them enter said values // does it twice cause obviously theres two users
            System.out.print("What's the username for Player " + (i + 1) + " ? >>> ");
            inputtedDetails[i][0] = scan.nextLine();
            System.out.print("What's the password for Player " + (i + 1) + " ? >>> ");
            inputtedDetails[i][1] = scan.nextLine();
        }

        if (inputtedDetails[0][1].equals(users.get(inputtedDetails[0][0])) && inputtedDetails[1][1].equals(users.get(inputtedDetails[1][0]))) { //uses the username they entered as a key and tries to find the key in the map, it then uses the value assigtned to that key and compares it to the password to see if they are an authorised user
            nameAssigner(inputtedDetails[0][0], inputtedDetails[1][0]); // so if thats all good, then it assigns their usernames to playerID 0 and 1 to be used later
            game(); //takes them to the dice game
        } else {        // if they cant enter authorised credentials then
            loginAttempts--;    //they get one less login attempt
            loginFail();
        }
    }

    private static void loginFail() {
        if (loginAttempts == 0) {   //checks if login attempts is at 0 and if it is
            breaker();
            System.out.println("Sorry, you've attempted to login too many times, exiting program!");
            System.exit(0); //theyve maxed out on login attempts and qutis program
        } else {
            breaker();
            System.out.println("Login Failed! You have " + loginAttempts + " more attempt(s)");
            login();    //allows them to try again
        }
    }

    public static void main(String[] args) {
        importValidUsers();     //imports authorised user credentials
        menu(); //takes them to the main menu
    }

    private static void menu() {
        Scanner scan = new Scanner(System.in);  //alows user input
        breaker();
        System.out.print("Welcome to the Dice Game, what would you like to do(type help if unknown) >>> ");
        String option = scan.nextLine();    //the option they select is stored
        switch (option.toUpperCase()) { //what they entered is taken to uppercase for easy comparison
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


    private static void game() {   //shows what round is on

        for (int j = 0; j < 5; j++) {   //sets rounds
            breaker();
            System.out.println("This is round number " + (j + 1));


            for (int i = 0; i < 2; i++) {   //allows both die to be rolled for each player
                actualDiceGame(i);          //does proper game logic and dice rolling
            }
        }
        checkingForDoubles();
        declareWinner();
    }
    private static void nameAssigner(String user1, String user2){ //since playernames are global, it sets the usernames which they are authorised with to the global variable
        player1name = user1;
        player2name = user2;
    }

    private static void declareWinner() {
        breaker();
        if (players[0][1] == players[1][1]) {   // if there scores are equal
            System.out.println("OOOOOO, there seems to be a tie! We will re roll one dice each, and the highest one wins!");
            boolean winnerFound = false;
            int[] points = new int[2];
            do {
                for (int i = 0; i < 2; i++) {   //rolls one dice for each player while the player scores are equal
                    breaker();
                    String name = getPlayerName(i);
                    points[1] += dice();
                    System.out.println(name + "Scored a " + points[i]);
                }
                if (points[0] != points[1]) {   //if theyre not equal
                    winnerFound = true;         //then a winner has been found
                }
            } while (!winnerFound);
        } else {
            System.out.println("We have found a winner, Drum roll please...");
            try {
                Thread.sleep(3000); //for intensity innit
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            int winnerID;
            if (players[0][1] > players[1][1]) {    //finds which person has the higher score and sets the winnerID to their value within the array.
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
        if (players[0][2] > 0) {    //checks to see if they have any extra gis
            extraGos = players[0][2];   //sets the variable to that many times
            players[0][2] = 0;      //sets it to zero incase they get another during their extra go
            String name = getPlayerName(0); //gets their player name
            System.out.println(name + " has " + extraGos + " extra go(s)");
            for (int i = 0; i < extraGos; i++) {    //rolls the dice for how many extra go's they've got
                breaker();
                actualDiceGame(0);
            }
        } else if (players[1][2] > 0) { //does the same as above for player two
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

    private static String getPlayerName(int playerNumber) { //checks the playerID so they can get that players username
        String name;
        if (playerNumber == 0) {
            name = player1name;
        } else {
            name = player2name;
        }
        return name;
    }

    private static void actualDiceGame(int i) { //so heres  the game logic and stuff
        String playerName = getPlayerName(i);

        int d1 = dice();    //rolls two dices
        int d2 = dice();

        System.out.println(playerName + " rolled a " + d1 + " and a " + d2);

        if (d1 == d2) { // if those two dices are equal then
            players[i][2] += 1; //adds on extra go to the player
            System.out.println("Congratulations, " + playerName + " scored a double this round");
        }

        players[i][1] += (d1 + d2); //then adds the points to the players total score
        players[i][1] = scoreCheck(players[i][1]);  //then runs logic
        System.out.println("After game rules, " + playerName + " has " + players[i][1] + " points!");   //shows them their total points
    }

    private static int scoreCheck(int score) {  //based upon whats said in the guidebook thing
        if (score < 0) {    //totals cant be less than 0 so if it is,, it sets it back to zero
            score = 0;
        } else if (score % 2 == 0) {    // even totals get an extra 10 points
            score += 10;
        } else {    //if its not even or below zero then its odd so they get 5 points deducted
            score -= 5;
        }
        return score;      //returns their score after logic
    }

    private static int dice() { //heres the actuall dice
        Random random = new Random();   //random number thing
        int dice = random.nextInt(6) + 1;   //sets the bounds for the random number +1 because its between 0 and 5 and we need it between 1 and 6
        return dice;    //returns that number
    }

    private static void breaker() {
        System.out.println("--------------------");
    }
}
