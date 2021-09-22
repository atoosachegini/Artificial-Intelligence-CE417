import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;
import java.util.Scanner;

public class Driver extends JFrame {
    private static final long serialVersionUID = 1L;
    static String[][] restaurant = {{"Alternate", "Bar", "Weekend", "Hungry", "Patrons", "Price", "Raining", "Reservation", "Type", "WaitEstimate", "result"},
            {"yes", "no", "no", "yes", "some", "3", "no", "yes", "french", "1", "yes"},
            {"yes", "no", "no", "yes", "full", "1", "no", "no", "thai", "3", "no"},
            {"no", "yes", "no", "no", "some", "1", "no", "no", "burger", "1", "yes"},
            {"yes", "no", "yes", "yes", "full", "1", "yes", "no", "thai", "2", "yes"},
            {"yes", "no", "yes", "no", "full", "3", "no", "yes", "french", "4", "no"},
            {"no", "yes", "no", "yes", "some", "2", "yes", "yes", "italian", "1", "yes"},
            {"no", "yes", "no", "no", "none", "1", "yes", "no", "burger", "1", "no"},
            {"no", "no", "no", "yes", "some", "2", "yes", "yes", "thai", "1", "yes"},
            {"no", "yes", "yes", "no", "full", "1", "yes", "no", "burger", "4", "no"},
            {"yes", "yes", "yes", "yes", "full", "3", "no", "yes", "italian", "2", "no"},
            {"no", "no", "no", "no", "none", "1", "no", "no", "thai", "1", "no"},
            {"yes", "yes", "yes", "yes", "full", "1", "no", "no", "burger", "3", "yes"}};


    void processDataSet(DecisionTree.DataSet dataset, DefaultMutableTreeNode node, String featureValueName) {
        dataset.calculateEntropy();
        dataset.calculateInfoGains();
        dataset.findSplitOnFeature();
        if (dataset.toString() != null) System.out.println(dataset.typeString());
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
//        System.out.print("[");
//        for(int i =0 ; i< dataset.infoGains.size(); i++){
//            System.out.print(dataset.getInfoGains() + ",");}
//        System.out.println("]");
//        System.out.println(dataset.data[0].length);
        System.out.println("entropy : " + dataset.entropy);
        if (dataset.getEntropy() != 0) {
            System.out.println("Best feature to split on is " + dataset.getSplitOnFeature().getName() );
            HashMap<String, DecisionTree.DataSet> featureDataSets = new HashMap<String, DecisionTree.DataSet>();
            dataset.getSplitOnFeature().getFeatureValues().forEach(featureValue ->
                    featureDataSets.put(featureValue.getName(), dataset.createDataSet(dataset.getSplitOnFeature(), featureValue, dataset.getData()))
            );
            processDataSets(featureDataSets, node);
        } else {
            String[][] data = dataset.getData();
            String decision = " [" + data[0][data[0].length - 1] + " = " + data[1][data[0].length - 1] + "]";
            System.out.println("Decision ==> " + decision);
        }
    }

    void processDataSets(HashMap<String, DecisionTree.DataSet> dataSets, DefaultMutableTreeNode node) {
        dataSets.keySet().forEach(dataSet -> {
//                System.out.println(dataSets.get(dataSet).data[2][8]);
//                System.out.println(dataSets.get(dataSet).entropy);
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


    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        Driver driver = new Driver();
        System.out.println("build tree ?");
        String command = scanner.nextLine();
        if (command.equals("build tree")) {
            DecisionTree.DataSet dataSet = new DecisionTree.DataSet("restaurant", restaurant);
            dataSet.findSplitOnFeature();
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(dataSet.getSplitOnFeature().getName());
            driver.processDataSet(dataSet, node, "");

        }
    }
}