import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class musicQuiz {
    private static String[][] songs;

    public static void main(String[] args) {
        importSongs();
        game();
    }

    private static void importSongs() {
        try {
            int i = 0,lines = 0;
            String line;
            String[] splits;
            BufferedReader reader = new BufferedReader(new FileReader("songList.txt"));
            while ((line = reader.readLine()) != null) {
               lines++;
            }
            songs = new String[lines][lines];
            while ((line = reader.readLine()) != null) {
                splits = line.split(":");
                songs[i][0] = splits[1];
                songs[i][1] = splits[0];
                // songs.put(splits[1],splits[0]); //the text files stores like songs:artist  - this just makes the artist the key because its easier than searching with the song name
                i++;
            }
        } catch (IOException e) {
            System.out.println("Error importing songs");
        }
    }

    private static void game() {
        int songNum = randomNum();

    }

    private static int randomNum() {
        return (int) (Math.random() * (songs.length+1));
    }
}

