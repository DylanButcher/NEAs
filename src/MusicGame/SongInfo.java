package MusicGame;

public class SongInfo {

    public String name;
    public String artist;
    public String nameCensored;

    public SongInfo(String artist, String name) {
        this.name = name;
        this.artist = artist;
        this.nameCensored = censorName(name);
    }

    public static String censorName(String name) {
        String[] splitName = name.split(" ");
        String censoredName = "";
        for (String word : splitName) {
            for (int j = 0; j < word.length(); j++) {
                char a = word.charAt(j);
                if (j == 0 || !(Character.isLetter(a))) {
                    censoredName += a;
                } else {
                    censoredName += "_ ";
                }
                censoredName += " ";
            }
        }
        return censoredName;
    }
}
