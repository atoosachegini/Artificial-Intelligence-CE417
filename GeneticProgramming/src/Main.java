import java.nio.channels.FileLock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static ArrayList<ArrayList<Float>> trainingPoints = new ArrayList<>();
    public static List<String> first;
    public static List<String> second;
//    public static List<String> third = new ArrayList<>();
//    public static List<String> forth = new ArrayList<>();

    static class Node {
        String value;
        Node left, right;

        public Node(String item) {
            value = item;
            left = right = null;
        }
    }

    static int Priority(String operator) {
        switch (operator) {
            case "(":
                return 0;
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            case "sin":
            case "cos":
                return 4;
            default:
                return -1;
        }
    }

    private static List<String> tokenize(String input) {
        ArrayList<String> tokens = new ArrayList<>();
        Pattern p = Pattern.compile("\\(|\\)|[A-Za-z]+|\\d*\\.\\d*|\\d+|\\S+?");
        Matcher m = p.matcher(input);
        while (m.find()) {
            tokens.add(m.group());
        }
        return tokens;
    }

    static void makingTrainingList(Node root) {
        ArrayList<Float> help;
        int min = 0;
        int max = 1000;
        float xRandom;
        float yRandom;
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            help = new ArrayList<>();
            xRandom = rand.nextFloat() * (max - min) + min;
            yRandom = evaluateExpressionTree(root, xRandom);
            help.add(xRandom);
//            System.out.println(help.get(0));
            help.add(yRandom);
//            System.out.println(help.get(1));
            trainingPoints.add(help);
        }
    }

    static ArrayList<ArrayList<Float>> findYsOfFunctions(Node root) {
        ArrayList<Float> help;
        ArrayList<ArrayList<Float>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            help = new ArrayList<>();
            help.add(trainingPoints.get(i).get(0));
            help.add(evaluateExpressionTree(root, trainingPoints.get(i).get(0)));
            list.add(help);
        }
        return list;
    }

    static float fitnessCalculator(ArrayList<ArrayList<Float>> list) {
        float fitness = 0;
        for (int i = 0; i < 10; i++) {
            fitness += Math.abs(list.get(i).get(1) - trainingPoints.get(i).get(1));
        }
        return fitness;
    }

    static List<String> infixToPostfix(List<String> exp) {

        Stack<String> st = new Stack<>();
        ArrayList<String> out = new ArrayList<>();
        for (String s : exp) {
            if (s.equals("(")) {
                st.push(s);
            } else if (s.equals(")")) {
                while (!st.empty()) {
                    String s2 = st.pop();
                    if (s2.equals("(")) break;
                    out.add(s2);
                }
            } else if (Priority(s) > 0) {
                int p = Priority(s);
                while (!st.isEmpty() && Priority(st.peek()) >= p) out.add(st.pop());
                st.push(s);
            } else {
                out.add(s);
            }
        }
        while (!st.isEmpty()) out.add(st.pop());
        return out;
    }

    static class ExpressionTree {
        boolean isOperator(String st) {
            return st.equals("+") || st.equals("-") || st.equals("*") || st.equals("/")
                    || st.equals("^") || st.equals("sin") || st.equals("cos");
        }

        void inorder(Node t) {
            if (t != null) {
                inorder(t.left);
                System.out.print(t.value + " ");
                inorder(t.right);
            }
        }


        //return root of constructed tree for given postfix expression.
        Node constructTree(List<String> postfix) {
            Stack<Node> stack = new Stack<>();
            Node t, t1, t2;

            for (int i = 0; i < postfix.size(); i++) {
                if (!isOperator(postfix.get(i))) {
                    t = new Node(postfix.get(i));
                    stack.push(t);


                } else if (postfix.get(i).equals("sin") || postfix.get(i).equals("cos")) {
                    t = new Node(postfix.get(i));
                    try {
                        t1 = stack.pop();
                    } catch (EmptyStackException ex) {
                        return null;
                    }
                    t.left = t1;
                    stack.push(t);

                } else // is operator!
                {
                    t = new Node(postfix.get(i));
                    try {
                        t1 = stack.pop();
                    } catch (EmptyStackException ex) {
                        return null;
                    }
                    try {
                        t2 = stack.pop();
                    } catch (EmptyStackException ex) {
                        return null;
                    }
                    t.right = t1;
                    t.left = t2;
                    stack.push(t);

                }
            }
            try {
                t = stack.peek();
            } catch (EmptyStackException ex) {
                return null;
            }
            try {
                stack.pop();
            } catch (EmptyStackException ex) {
                return null;
            }
            return t;
        }
    }

    static float evaluateExpressionTree(Node root, float randomX) {
        float leftSum;
        float rightSum;
        if (root == null)
            return 0;
        if (root.value.equals("x"))
            root.value = String.valueOf(randomX);
        if (root.left == null && root.right == null)
            try {
                return Float.parseFloat(root.value);
            } catch (NumberFormatException ex) {
                return (float) 0.0000001;
            }
        leftSum = evaluateExpressionTree(root.left, randomX);
        rightSum = evaluateExpressionTree(root.right, randomX);

        if (root.value.equals("+"))
            return leftSum + rightSum;
        if (root.value.equals("-"))
            return leftSum - rightSum;
        if (root.value.equals("*"))
            return leftSum * rightSum;
        if (root.value.equals("/"))
            return leftSum / rightSum;
        if (root.value.equals("^"))
            return (float) Math.pow(leftSum, rightSum);
        if (root.value.equals("sin"))
            return (float) Math.sin(leftSum);
        if (root.value.equals("cos"))
            return (float) Math.cos(leftSum);
        return 0;
    }

    static String randomGen() {
        Random random = new Random();
        float number;
        int number2;
        number = random.nextFloat();
//            System.out.println(number);
        if (number < (float) 1 / 11)
            return "x";
        else if (number < (float) 2 / 11)
            return "^";
        else if (number < (float) 3 / 11)
            return "+";
        else if (number < (float) 4 / 11)
            return "*";
        else if (number < (float) 5 / 11)
            return "-";
        else if (number < (float) 6 / 11)
            return "/";
        else if (number < (float) 7 / 11) {
            number2 = random.nextInt(10);
            return String.valueOf(number2);
        } else if (number < (float) 8 / 11) {
            return "sin";
        } else if (number < (float) 9 / 11)
            return "(";
        else if (number < (float) 10 / 11)
            return ")";
        else {
            return "cos";
        }
    }

    static void randomGeneration(List<String> list, int length) {
        for (int i = 0; i < length; i++) {
            list.add(randomGen());
        }
    }

    static void randomFirstGeneration(int length) {
        first = new ArrayList<>();
        randomGeneration(first, length);
        List<String> postfixFirst = infixToPostfix(first);
        ExpressionTree etFirst = new ExpressionTree();
        while (etFirst.constructTree(postfixFirst) == null || evaluateExpressionTree(etFirst.constructTree(postfixFirst), 1) == 0.0000001) {
            first = new ArrayList<>();
            randomGeneration(first, length);
            postfixFirst = infixToPostfix(first);
            etFirst = new ExpressionTree();
        }
        second = new ArrayList<>();
        randomGeneration(second, length);
        List<String> postfixSecond = infixToPostfix(second);
        ExpressionTree etSecond = new ExpressionTree();
        while (etSecond.constructTree(postfixSecond) == null || evaluateExpressionTree(etSecond.constructTree(postfixSecond), 1) == 0.0000001) {
            second = new ArrayList<>();
            randomGeneration(second, length);
            postfixSecond = infixToPostfix(second);
            etSecond = new ExpressionTree();
        }
    }

    static void crossOverOrMutation
            (List<String> firstChild, List<String> secondChild, int length) {
        Random random = new Random();
        float randomFirst;
        float randomSecond;
        for (int i = 0; i < length; i++) {
            randomFirst = random.nextFloat();
            if (randomFirst < (float) (1 / 4))
                firstChild.add(first.get(i));
            else if (randomFirst < (float) (2 / 4))
                firstChild.add(second.get(i));
            else
                firstChild.add(randomGen());
        }
        for (int i = 0; i < length; i++) {
            randomSecond = random.nextFloat();
            if (randomSecond < (float) (1 / 4))
                secondChild.add(first.get(i));
            else if (randomSecond < (float) (2 / 4))
                secondChild.add(second.get(i));
            else
                secondChild.add(randomGen());
        }
    }

    static void chooseMomAndDadForNextGeneration(List<String> firstChild, List<String> secondChild) {
        List<String> best1 = new ArrayList<>();
        List<String> best2 = new ArrayList<>();
        float[] findBests = new float[4];
        List<String> postfixFirst = infixToPostfix(first);
        ExpressionTree etFirst = new ExpressionTree();
        Node rootFirst = etFirst.constructTree(postfixFirst);
        findBests[0] = fitnessCalculator(findYsOfFunctions(rootFirst));

        List<String> postfixSecond = infixToPostfix(second);
        ExpressionTree etSecond = new ExpressionTree();
        Node rootSecond = etSecond.constructTree(postfixSecond);
        findBests[1] = fitnessCalculator(findYsOfFunctions(rootSecond));

        List<String> postfixFirstChild = infixToPostfix(firstChild);
        ExpressionTree etFirstChild = new ExpressionTree();
        Node rootFirstChild = etFirstChild.constructTree(postfixFirstChild);
        findBests[2] = fitnessCalculator(findYsOfFunctions(rootFirstChild));

        List<String> postfixSecondChild = infixToPostfix(secondChild);
        ExpressionTree etSecondChild = new ExpressionTree();
        Node rootSecondChild = etSecondChild.constructTree(postfixSecondChild);
        findBests[3] = fitnessCalculator(findYsOfFunctions(rootSecondChild));
//        System.out.println(findBests[0] +" "+findBests[1] +" "+ findBests[2] +" "+ findBests[3]);
        float minAmount1 = Integer.MAX_VALUE;
        float minIndex1 = 5;
        for (int i = 0; i < findBests.length; i++) {
            if (minAmount1 > findBests[i]) {
                minAmount1 = findBests[i];
                minIndex1 = i;
            }
        }

        float minAmount2 = Integer.MAX_VALUE;
        float minIndex2 = 5;
        for (int i = 0; i < findBests.length; i++) {
            if (i != minIndex1) {
                if (minAmount2 > findBests[i]) {
                    minAmount2 = findBests[i];
                    minIndex2 = i;
                }
            }
        }
        if (minIndex1 == 0)
            best1 = first;
        if (minIndex1 == 1)
            best1 = second;
        if (minIndex1 == 2)
            best1 = firstChild;
        if (minIndex1 == 3)
            best1 = secondChild;
        if (minIndex2 == 0)
            best2 = first;
        if (minIndex2 == 1)
            best2 = second;
        if (minIndex2 == 2)
            best2 = firstChild;
        if (minIndex2 == 3)
            best2 = secondChild;
//        System.out.println(minIndex1 + " "+minIndex2);
        first = best1;
        second = best2;
    }

    static void geneticAlgorithm(int length) {
        List<String> firstChild = new ArrayList<>();
        List<String> secondChild = new ArrayList<>();
        randomFirstGeneration(length);//first and second are ready now
        List<String> postfix1 = infixToPostfix(first);
        ExpressionTree et1 = new ExpressionTree();
        List<String> postfix2 = infixToPostfix(second);
        ExpressionTree et2 = new ExpressionTree();
        int counter = 1;
        while (fitnessCalculator(findYsOfFunctions(et1.constructTree(postfix1))) != 0 && fitnessCalculator(findYsOfFunctions(et2.constructTree(postfix2))) != 0) {
            if (counter >= 300000) {
                System.out.println("Last Generation" + " " + (counter) + ":");
                System.out.println("Inorder traversal of best goal function is :");
                et1.inorder(et1.constructTree(postfix1));
                System.out.println();
                System.out.println("fitness of goal function is :");
                System.out.println(fitnessCalculator(findYsOfFunctions(et1.constructTree(postfix1))));
                break;
            }
            System.out.println("Generation" + " " + (counter) + ":");
            System.out.println("Inorder traversal of these two functions are :");
            et1.inorder(et1.constructTree(postfix1));
            System.out.println();
            et2.inorder(et2.constructTree(postfix2));
            System.out.println();
            firstChild = new ArrayList<>();
            secondChild = new ArrayList<>();
            crossOverOrMutation(firstChild, secondChild, length);
//            System.out.println(firstChild);
//            System.out.println(secondChild);
//            System.out.println(first);
//            System.out.println(second);
            chooseMomAndDadForNextGeneration(firstChild, secondChild);// first and second are updated now.
//            System.out.println(first);
//            System.out.println(second);
            postfix1 = infixToPostfix(first);
            postfix2 = infixToPostfix(second);
            counter++;
        }
        if (fitnessCalculator(findYsOfFunctions(et1.constructTree(postfix1))) == 0) {
            System.out.println("Last Generation" + " " + (counter) + ":");
            System.out.println("Inorder traversal of goal function is :");
            et1.inorder(et1.constructTree(postfix1));
//            System.out.println(evaluateExpressionTree(et1.constructTree(postfix1), 1));
            System.out.println();
            System.out.println("fitness of goal function is :");
            System.out.println(fitnessCalculator(findYsOfFunctions(et1.constructTree(postfix1))));
        }
        if (fitnessCalculator(findYsOfFunctions(et2.constructTree(postfix2))) == 0) {
            System.out.println("Last Generation" + " " + (counter) + ":");
            System.out.println("Inorder traversal of goal function is :");
            et2.inorder(et2.constructTree(postfix2));
            System.out.println("fitness of goal function is :");
            System.out.println(fitnessCalculator(findYsOfFunctions(et2.constructTree(postfix2))));
        }
    }

    public static void main(String[] args) {
        String input;
        int length;
        Scanner scanner = new Scanner(System.in);
        //input string should not have any space.
        System.out.println("Type your goal function :");
        input = scanner.nextLine();
        System.out.println("Type the length of your goal function :");
        length = scanner.nextInt();
        Instant firstTime = Instant.now();
        List<String> tokens = tokenize(input);
        List<String> postfix = infixToPostfix(tokens);
        ExpressionTree et = new ExpressionTree();
        Node root = et.constructTree(postfix);// root is the main function we are looking for .
        makingTrainingList(root);// trainingPoint is ready now ! the first element of each row is  random x and the second is y.
        geneticAlgorithm(length);
//        System.out.println(trainingPoints.get(0).get(0));
//        System.out.print(first);
//        System.out.println(second);
        Instant secondTime = Instant.now();
        Duration duration = Duration.between(firstTime, secondTime);
        System.out.println(duration.toMillis() + " "+"milli second");


    }
}