package Finding42069;

import java.util.Random;

public class finding42069V2 {
    public static void main(String[] args) {
        int totalGos = 0, singularTime = 0;
        int repeatTimes = 10;
        long[] times = new long[repeatTimes];

        for (int i = 0; i <repeatTimes ; i++) {
            long StartTime = System.nanoTime();
            for ( totalGos = 0; singularTime!=42069; totalGos++) {
                boolean found42069Number =false;
                for ( singularTime = 0; !found42069Number; singularTime++) {
                    int num = GetRandomNum();
                    if(num==42069){
                        found42069Number = true;
                    }
                }
            }
            breaker();
            long endTime= System.nanoTime();
            times[i] = (endTime-StartTime);
            System.out.println("found 42069 on the 42069th roll after "+totalGos+" attempts. It took "+times[i]+" seconds to find this or "+(times[i]/60)+" minutes");
        }
        int totalSeconds =0;
        for (int i = 0; i <repeatTimes ; i++) {
            totalSeconds+= (int) times[i];
        }
        int avgTime = (totalSeconds/repeatTimes);
        breaker();
        System.out.println("The average time to find the thingy ma bob was "+avgTime);

    }
    public static int GetRandomNum(){
        Random rand = new Random();
        return (rand.nextInt(69420));
    }
    public static void breaker(){
        System.out.println("--------------------------------------------------------------------------------------");
    }

}
