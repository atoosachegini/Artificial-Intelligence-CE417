import javax.sound.midi.Soundbank;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.util.*;

public class Driver2 {

    static String[][] data = new String[500][9];//csv data line count=0 initially
    static String[][] test = new String[269][9];
    static char[] testCut = new char[269];//dar har sotun javabe nahayie.

//   static void testResult(DefaultMutableTreeNode node, DecisionTree.DataSet dataSet2, int j) {
//        Enumeration en1 = node.depthFirstEnumeration();
//        while (en1.hasMoreElements()) {
//
//            if (node.toString().trim().equals("BMI") || node.toString().trim().equals("diabetes")) {
//                continue;
//            }
//            if (node.toString().contains(":")) {
//                int index2 = node.toString().indexOf(":");
//                if (test[j][dataSet2.getColNumb(node.toString().trim().substring(0, index2))].equals(node.toString().trim().substring(index2 + 1).trim())) {
//                    continue;
//                } else {
//                    while (en1.hasMoreElements()) {
//                        DefaultMutableTreeNode node4 = (DefaultMutableTreeNode) en1.nextElement();
//                        if (node4.toString().contains(node.toString().trim().substring(0, index2))) {
//                            testResult(node4, dataSet2,j);
//                        }
//                    }
//                }
//                if (node.toString().contains("[")) {
//                    int index = node.toString().trim().indexOf("=");
//                    testCut[j] = node.toString().trim().charAt(index + 1);
//                    break;
//                }
//            }
//        }
//    }

    void processDataSet(DecisionTree.DataSet dataset, DefaultMutableTreeNode node, String featureValueName) {
        dataset.calculateEntropy();
        dataset.calculateInfoGains();
        dataset.findSplitOnFeature();
        if (dataset.toString() != null) System.out.println(dataset.typeString());
        node.add(new DefaultMutableTreeNode(dataset.typeString()));
        System.out.print("information gains for this branch are : ");
        dataset.infoGains.keySet().forEach(feature -> {
            System.out.print(dataset.infoGains.get(feature) + " ,");
        });
        System.out.println();
        System.out.print("remainders for this branch are : ");
        dataset.remainder.keySet().forEach(feature -> {
            System.out.print(dataset.remainder.get(feature) + " ,");
        });
        System.out.println();
        System.out.println("entropy : " + dataset.entropy);
        if (dataset.getEntropy() != 0 && dataset.getData()[0].length != 1) {
            System.out.println("Best feature to split on is " + dataset.getSplitOnFeature().getName());
            HashMap<String, DecisionTree.DataSet> featureDataSets = new HashMap<String, DecisionTree.DataSet>();
            dataset.getSplitOnFeature().getFeatureValues().forEach(featureValue ->
                    featureDataSets.put(featureValue.getName(), dataset.createDataSet(dataset.getSplitOnFeature(), featureValue, dataset.getData()))
            );
            processDataSets(featureDataSets, node);
        } else {
            String[][] data = dataset.getData();
            String decision = " [" + data[0][data[0].length - 1] + " =" + data[1][data[0].length - 1] + "]";
            node.add(new DefaultMutableTreeNode(decision));
            System.out.println("Decision ==> " + decision);
        }
    }

    void processDataSets(HashMap<String, DecisionTree.DataSet> dataSets, DefaultMutableTreeNode node) {
        dataSets.keySet().forEach(dataSet -> {

            if (dataSets.get(dataSet).getEntropy() != 0) {

                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
                        dataSet + "   :   [" + dataSets.get(dataSet).getSplitOnFeature().getName() + "]");
                node.add(newNode);
                processDataSet(dataSets.get(dataSet), newNode, dataSet);
            } else {
                processDataSet(dataSets.get(dataSet), node, dataSet);
//                    System.out.println("fuck");
            }
        });
    }


    public static void main(String[] args) throws IOException {
        String fName = "C:\\Users\\ASUS ZENBOOK\\Desktop\\diabetes.csv";
        String thisLine;
        int count = 0;
        FileInputStream fis = new FileInputStream(fName);
        DataInputStream myInput = new DataInputStream(fis);
        int i = 1;

        thisLine = myInput.readLine();
        List<String> temp1 = Arrays.asList(thisLine.split(","));
        for (int j = 0; j < 9; j++) {
            data[0][j] = temp1.get(j);
            test[0][j] = temp1.get(j);
        }
//        System.out.println(data[0][1]);
        while ((thisLine = myInput.readLine()) != null && i < 500) {
            List<String> temp = Arrays.asList(thisLine.split(","));
            for (int j = 0; j < 9; j++) {//gosaste sazie dade haye peivaste
                if (j == 0) {
                    if (Integer.parseInt(temp.get(j)) <= 4)
                        data[i][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 8)
                        data[i][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 12)
                        data[i][j] = "3";
                    else
                        data[i][j] = "4";
                }
                if (j == 1) {
                    if (Integer.parseInt(temp.get(j)) <= 40)
                        data[i][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 80)
                        data[i][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 120)
                        data[i][j] = "3";
                    else if (Integer.parseInt(temp.get(j)) <= 160)
                        data[i][j] = "4";
                    else
                        data[i][j] = "5";
                }
                if (j == 2) {
                    if (Integer.parseInt(temp.get(j)) <= 20)
                        data[i][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 40)
                        data[i][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 60)
                        data[i][j] = "3";
                    else if (Integer.parseInt(temp.get(j)) <= 80)
                        data[i][j] = "4";
                    else if (Integer.parseInt(temp.get(j)) <= 100)
                        data[i][j] = "5";
                    else
                        data[i][j] = "6";
                }
                if (j == 3) {
                    if (Integer.parseInt(temp.get(j)) <= 10)
                        data[i][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 20)
                        data[i][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 30)
                        data[i][j] = "3";
                    else if (Integer.parseInt(temp.get(j)) <= 40)
                        data[i][j] = "4";
                    else if (Integer.parseInt(temp.get(j)) <= 50)
                        data[i][j] = "5";
                    else
                        data[i][j] = "6";
                }
                if (j == 4) {
                    if (Integer.parseInt(temp.get(j)) <= 40)
                        data[i][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 100)
                        data[i][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 200)
                        data[i][j] = "3";
                    else if (Integer.parseInt(temp.get(j)) <= 300)
                        data[i][j] = "4";
                    else
                        data[i][j] = "5";
                }
                if (j == 5) {
                    if (Float.parseFloat(temp.get(j)) <= 10)
                        data[i][j] = "1";
                    else if (Float.parseFloat(temp.get(j)) <= 20)
                        data[i][j] = "2";
                    else if (Float.parseFloat(temp.get(j)) <= 30)
                        data[i][j] = "3";
                    else if (Float.parseFloat(temp.get(j)) <= 40)
                        data[i][j] = "4";
                    else if (Float.parseFloat(temp.get(j)) <= 50)
                        data[i][j] = "5";
                    else
                        data[i][j] = "6";
                }
                if (j == 6) {
                    if (Float.parseFloat(temp.get(j)) <= 0.2)
                        data[i][j] = "1";
                    else if (Float.parseFloat(temp.get(j)) <= 0.4)
                        data[i][j] = "2";
                    else if (Float.parseFloat(temp.get(j)) <= 0.6)
                        data[i][j] = "3";
                    else if (Float.parseFloat(temp.get(j)) <= 0.8)
                        data[i][j] = "4";
                    else if (Float.parseFloat(temp.get(j)) <= 1.0)
                        data[i][j] = "5";
                    else
                        data[i][j] = "6";
                }
                if (j == 7) {
                    if (Integer.parseInt(temp.get(j)) <= 10)
                        data[i][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 20)
                        data[i][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 30)
                        data[i][j] = "3";
                    else if (Integer.parseInt(temp.get(j)) <= 40)
                        data[i][j] = "4";
                    else
                        data[i][j] = "5";
                } else if (j == 8)
                    data[i][j] = temp.get(j);
            }
            i++;
        }//datahaye mojud dar file exel dakhele araye gharar gereftan.
        int k = 501;
        while ((thisLine = myInput.readLine()) != null && k <= 769) {
            List<String> temp = Arrays.asList(thisLine.split(","));
            for (int j = 0; j < 9; j++) {//gosaste sazie dade haye peivaste
                if (j == 0) {
                    if (Integer.parseInt(temp.get(j)) <= 4)
                        test[k - 500][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 8)
                        test[k - 500][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 12)
                        test[k - 500][j] = "3";
                    else
                        test[k - 500][j] = "4";
                }
                if (j == 1) {
                    if (Integer.parseInt(temp.get(j)) <= 40)
                        test[k - 500][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 80)
                        test[k - 500][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 120)
                        test[k - 500][j] = "3";
                    else if (Integer.parseInt(temp.get(j)) <= 160)
                        test[k - 500][j] = "4";
                    else
                        test[k - 500][j] = "5";
                }
                if (j == 2) {
                    if (Integer.parseInt(temp.get(j)) <= 20)
                        test[k - 500][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 40)
                        test[k - 500][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 60)
                        test[k - 500][j] = "3";
                    else if (Integer.parseInt(temp.get(j)) <= 80)
                        test[k - 500][j] = "4";
                    else if (Integer.parseInt(temp.get(j)) <= 100)
                        test[k - 500][j] = "5";
                    else
                        test[k - 500][j] = "6";
                }
                if (j == 3) {
                    if (Integer.parseInt(temp.get(j)) <= 10)
                        test[k - 500][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 20)
                        test[k - 500][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 30)
                        test[k - 500][j] = "3";
                    else if (Integer.parseInt(temp.get(j)) <= 40)
                        test[k - 500][j] = "4";
                    else if (Integer.parseInt(temp.get(j)) <= 50)
                        test[k - 500][j] = "5";
                    else
                        test[k - 500][j] = "6";
                }
                if (j == 4) {
                    if (Integer.parseInt(temp.get(j)) <= 40)
                        test[k - 500][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 100)
                        test[k - 500][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 200)
                        test[k - 500][j] = "3";
                    else if (Integer.parseInt(temp.get(j)) <= 300)
                        test[k - 500][j] = "4";
                    else
                        test[k - 500][j] = "5";
                }
                if (j == 5) {
                    if (Float.parseFloat(temp.get(j)) <= 10)
                        test[k - 500][j] = "1";
                    else if (Float.parseFloat(temp.get(j)) <= 20)
                        test[k - 500][j] = "2";
                    else if (Float.parseFloat(temp.get(j)) <= 30)
                        test[k - 500][j] = "3";
                    else if (Float.parseFloat(temp.get(j)) <= 40)
                        test[k - 500][j] = "4";
                    else if (Float.parseFloat(temp.get(j)) <= 50)
                        test[k - 500][j] = "5";
                    else
                        test[k - 500][j] = "6";
                }
                if (j == 6) {
                    if (Float.parseFloat(temp.get(j)) <= 0.2)
                        test[k - 500][j] = "1";
                    else if (Float.parseFloat(temp.get(j)) <= 0.4)
                        test[k - 500][j] = "2";
                    else if (Float.parseFloat(temp.get(j)) <= 0.6)
                        test[k - 500][j] = "3";
                    else if (Float.parseFloat(temp.get(j)) <= 0.8)
                        test[k - 500][j] = "4";
                    else if (Float.parseFloat(temp.get(j)) <= 1.0)
                        test[k - 500][j] = "5";
                    else
                        test[k - 500][j] = "6";
                }
                if (j == 7) {
                    if (Integer.parseInt(temp.get(j)) <= 10)
                        test[k - 500][j] = "1";
                    else if (Integer.parseInt(temp.get(j)) <= 20)
                        test[k - 500][j] = "2";
                    else if (Integer.parseInt(temp.get(j)) <= 30)
                        test[k - 500][j] = "3";
                    else if (Integer.parseInt(temp.get(j)) <= 40)
                        test[k - 500][j] = "4";
                    else
                        test[k - 500][j] = "5";
                } else if (j == 8)
                    test[k - 500][j] = temp.get(j);
            }//test sotune akharesh khalie baraye dadehayi k az derakhte tasmim b dast miad.
            k++;
        }
        System.out.println(data[499][0]);
        Scanner scanner = new Scanner(System.in);
        Driver2 driver = new Driver2();
        System.out.println("build tree ?");
        String command = scanner.nextLine();
        if (command.equals("build tree")) {
            DecisionTree.DataSet dataSet1 = new DecisionTree.DataSet("diabetes", data);
            dataSet1.findSplitOnFeature();
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(dataSet1.getSplitOnFeature().getName());
            DecisionTree.DataSet dataSet2 = new DecisionTree.DataSet("diabetesTest", test);
            driver.processDataSet(dataSet1, node, "");
            System.out.println("and to show all tree nodes in depth first search we have :");
            Enumeration en = node.depthFirstEnumeration();
            while (en.hasMoreElements()) {
                DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) en.nextElement();
                System.out.println(node1.toString());
            }

            for(int j = 1; j< 268;j++) {
                Enumeration en1 = node.depthFirstEnumeration();
                while (en1.hasMoreElements()) {
                    DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) en1.nextElement();
                    if (node1.toString().trim().equals("BMI") || node1.toString().trim().equals("diabetes")) {
                        continue;
                    }
                    if (node1.toString().contains(":")) {
                        int index2 = node1.toString().indexOf(":");
                        if (test[j][dataSet2.getColNumb(node1.toString().trim().substring(0, index2))].equals(node1.toString().trim().substring(index2 + 1).trim())) {
                            continue;
                        } else {
                            while (!en1.nextElement().toString().contains(node1.toString().trim().substring(0, index2))) {
                                continue;
                            }
                        }
                    }
                    if (node1.toString().contains("[")) {
                        int index = node1.toString().trim().indexOf("=");
                        testCut[j] = node1.toString().trim().charAt(index + 1);
                        break;
                    }
                }
            }
            int counter=0;
            for(int m=0; m<testCut.length;m++){
                if(String.valueOf(testCut[m]).equals(test[m][8])){
                    counter++;
                }
            }
            System.out.println("this algorithm is true in " + ((float)counter/266 )*100 +"percents of times");

        }
    }
    }