import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class HillClimbing {
    static List<ArrayList<Float>> factors = new ArrayList<ArrayList<Float>>();
    static String line;
    static int lowerBound = 0;
    static int upperBound = 0;
    static float stepLength = 0;

    static void getRandomSolution(ArrayList arrayList, int size) {
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            double x = upperBound * rand.nextDouble();
            double cmp = rand.nextDouble();
            if (cmp < 0.5)
                arrayList.add(x);
            else
                arrayList.add((-1) * x);
        }
    }

    static float howGoodItIs(ArrayList<Float> arrayList) {
        float function = 0;
        float result = 0;
        for (ArrayList<Float> factor : factors) {
            for (int i = 0; i < arrayList.size(); i++) {
                function += Float.parseFloat(String.valueOf(arrayList.get(i))) * factor.get(i);
//                System.out.println(function);
            }
            //System.out.println(function);
            result += function - factor.get(factor.size() - 1);
            //System.out.println(result);
            function = 0;
        }
        return Math.abs(result);
    }  //the function of "howGoodItIs"is lower when the condition is better.

    static boolean checkUpperAndLower(ArrayList<Float> arrayList, int first, int second) {
        boolean flag = true;

        if (second == 0) {
            if (lowerBound > Float.parseFloat(String.valueOf(arrayList.get(first))) - stepLength || upperBound < Float.parseFloat(String.valueOf(arrayList.get(first))) - stepLength) {
                flag = false;
            }
        } else if (second == 1) {
            if (lowerBound > Float.parseFloat(String.valueOf(arrayList.get(first))) + stepLength || upperBound < Float.parseFloat(String.valueOf(arrayList.get(first))) + stepLength) {
                flag = false;
            }
        }

        return flag;
    }

    static ArrayList<Float> neighborSelect(ArrayList<Float> current) {
        int first = -1;
        int second = -1;
        ArrayList<Float> help = new ArrayList<>();
        float currentPosition = howGoodItIs(current);
        ArrayList<ArrayList<Float>> function = new ArrayList<ArrayList<Float>>();
//        System.out.println(currentPosition);
        ArrayList<Float> bestNeighbor = new ArrayList<>();
        ArrayList<Float> rowOfFunction = new ArrayList<>();
        //System.out.println(current.size());
        for (int i = 0; i < current.size(); i++) {
            help.clear();
            help.addAll(current);
            help.set(i, Float.parseFloat(String.valueOf(current.get(i))) - stepLength);
//            System.out.println(howGoodItIs(help));
            rowOfFunction.add(howGoodItIs(help));
            help.clear();
            help.addAll(current);
            help.set(i, Float.parseFloat(String.valueOf(current.get(i))) + stepLength);
//            System.out.println(howGoodItIs(help));
            rowOfFunction.add(howGoodItIs(help));
            function.add(rowOfFunction);
        }//function of all neighbors are ready now.
        int flag = 0;
        float min = currentPosition;
        for (int j = 0; j < current.size(); j++) {
            if (function.get(j).get(0) < min) {
                first = j;
                second = 0;
                if (checkUpperAndLower(current, first, second)) {
                    min = function.get(j).get(0);
                    flag = 1;
                }
            }
            if (function.get(j).get(1) < min) {
                first = j;
                second = 1;
                if (checkUpperAndLower(current, first, second)) {
                    min = function.get(j).get(1); //best quality amount
                    flag = 1;
                }
            }
        }
        if (flag == 0) {
            first = -1;
            second = -1;
            bestNeighbor = current;
        } else {
            bestNeighbor.addAll(current);
            if (second == 0)
                bestNeighbor.set(first, Float.parseFloat(String.valueOf(current.get(first))) - stepLength);
            else
                bestNeighbor.set(first, Float.parseFloat(String.valueOf(current.get(first))) + stepLength);
        }
        return bestNeighbor;   //best neighbor of current position.
    }

    static ArrayList<Float> solution(ArrayList<Float> current) {
        while (neighborSelect(current) != current) {
            current = neighborSelect(current);
        }
        return current;
    }


    public static void main(String[] args) throws Exception {
        int n = 0;
        ArrayList<Float> initial = new ArrayList<Float>();
        ArrayList<Float> result = new ArrayList<Float>();
        File file = new File("C:\\Users\\ASUS ZENBOOK\\Desktop\\example.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        int counter = 0;
        while ((line = br.readLine()) != null) {
            ArrayList rows = new ArrayList(Arrays.asList(line.split("\\,")));
            factors.add(rows);
            counter += 1;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the maximum amount of unknowns:");
        upperBound = scanner.nextInt();
        System.out.println("Enter the minimum amount of unknowns:");
        lowerBound = scanner.nextInt();
        System.out.println("Enter the step length:");
        stepLength = scanner.nextFloat();
        Instant first = Instant.now();
        //factors contain the coefficients of equation.(string type)
        for (ArrayList<Float> factor : factors)
            for (int i = 0; i < factor.size(); i++)
                factor.set(i, Float.parseFloat(String.valueOf(factor.get(i))));
        n = factors.get(0).size() - 1; //numbers of unknowns.
//        System.out.println(upperBound);
        getRandomSolution(initial, n);
        result = solution(initial);
        for (int i = 0; i < result.size(); i++)
            System.out.println("unknown " + (i + 1) + "  -->  " + result.get(i));
        System.out.println();
        System.out.println("Answer error rate");
        System.out.println(howGoodItIs(result)); // error percentage
//        System.out.println(howGoodItIs(initial));
        Instant second = Instant.now();

        Duration duration = Duration.between(first, second);
        System.out.println("Duration of solving the Equation");
        System.out.println(duration.toMillis());  //duration of solving the problem

    }
}
