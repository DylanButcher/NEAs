package Finding42069;

import java.util.Random;

public class finding42069 {
    public static void main(String[] args) {
        int i,totalAttempts=0;
        boolean foundOnAttempts = false;

        for (int j = 0; foundOnAttempts; j++) {
            boolean numFound = false;
            for (i = 0; !numFound ; i++) {
                int num = GetRandomNum();
                numFound = (num==42069);
            }
            System.out.println("Found 42069 after "+i+" attemps");
            totalAttempts += i;

        }
    }
    public static int GetRandomNum(){
        Random rand = new Random();
        return (rand.nextInt(69420));
    }

}
