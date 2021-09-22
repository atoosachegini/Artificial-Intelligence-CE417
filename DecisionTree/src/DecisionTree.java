import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class DecisionTree {

    static class FeatureValue {
        String name;
        int occurences;//tedade dafaAti k oon feature value etefagh miofte

        FeatureValue(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        int getOccurences() {
            return occurences;
        }

        void setOccurences(int occurences) {
            this.occurences = occurences;
        }

        int hashcode() {
            return name.hashCode();
        }

        public boolean equals(Object object) {
            boolean returnValue = true;
            if (object == null || (getClass() != object.getClass())) returnValue = false;
            if (name == null) if (((FeatureValue) object).name != null) returnValue = false;
            else if (!name.equals(((FeatureValue) object).name)) returnValue = false;
            return returnValue;
        }

        String typeString() {
            return name;
        }
    }

    static class Feature {
        private String name = null;
        private HashSet<FeatureValue> featureValues = new HashSet<FeatureValue>();

        String getName() {
            return name;
        }

        HashSet<FeatureValue> getFeatureValues() {
            return featureValues;
        }

        String typeString() {
            return name;
        }//kelase feature value marbut b meghdadre hr kodam az feature ha dar dade hast amma kelase feature marboot har vizhgei ast k dar dade ha barresi mishavand.

        Feature(String[][] data, int column) {
            this.name = data[0][column];
            IntStream.range(1, data.length).forEach(row -> featureValues.add(new FeatureValue(data[row][column])));//meghdare har feature dar dade hayi k darim baraye train.
            featureValues.stream().forEach(featureValue -> {
                int counter = 0;
                for (int row = 1; row < data.length; row++)
                    if (featureValue.getName() == data[row][column])
                        featureValue.setOccurences(++counter);//feature value meghdare har kodam az yeki az feature hast.
            });
        }

        FeatureValue findFeature(DataSet dataSet,String featureName,String name){
            FeatureValue returnFeatureValue = null;
            for (FeatureValue featureValue : dataSet.getFeatureFromName(featureName).getFeatureValues()) {
                if(featureValue.getName() == name){
                    returnFeatureValue = featureValue;
                }
            }
            return returnFeatureValue;
        }
    }

    static class DataSet {
        String name;
        String[][] data = null;
        HashMap<Feature, Double> infoGains = new HashMap<Feature, Double>();
        HashMap<Feature, Double> remainder = new HashMap<Feature, Double>();
        double entropy ;
        Feature splitOnFeature = null;

        DataSet(String name, String[][] data) {
            this.data = data;
            this.name = name;

        }

        String[][] deleteColumn(String[][] data, int toDeleteColNumb) {
            String[][] returnData = new String[data.length][data[0].length - 1];
            IntStream.range(0, data.length).forEach(row -> {
                int columnCounter = 0;
                for (int column = 0; column < data[0].length; column++)
                    if (column != toDeleteColNumb) returnData[row][columnCounter++] = data[row][column];
            });

            return returnData;
        }
        Feature getFeatureFromName(String name){
            Feature feature = new Feature(data, getColNumb(name));
            return feature;
        }

        int getColNumb(String colName) {
            int returnValue = -1;
            for (int column = 0; column < data[0].length - 1; column++)
                if (data[0][column].equals(colName)) {
                    returnValue = column;
                    break;
                }
            return returnValue;
        }

        DataSet calculateEntropy() {
            new Feature(data, data[0].length - 1).getFeatureValues().forEach(featureValue ->
                    entropy += minusPlog2((double)featureValue.getOccurences() / (data.length - 1)));
            return this;
        }

        HashMap<Feature, Double> getInfoGains() {
            return infoGains;
        }

        Feature getSplitOnFeature() {
            return splitOnFeature;
        }

        String[][] getData() {
            return data;
        }

        Double getEntropy() {
            return entropy;
        }

        String typeString() {
            return name;
        }

        double minusPlog2(double p) {
            double returnValue = 0;
            if (p != 0) returnValue = (-1) * p * Math.log(p) / Math.log(2);
            return returnValue;
        }

        DataSet createDataSet(Feature feature, FeatureValue featureValue, String[][] data) {
            int column = getColNumb(feature.getName());
            String[][] returnData = new String[featureValue.getOccurences() + 1][data[0].length];
            returnData[0] = data[0];
            int counter = 1;
            for (int row = 1; row < data.length; row++)
                if (data[row][column] == featureValue.getName()) returnData[counter++] = data[row];
            return new DataSet(feature.getName() + ": " + featureValue.getName(), deleteColumn(returnData, column));
        }

        DataSet calculateInfoGains() {
            IntStream.range(0, data[0].length - 1).forEach(column -> {
                Feature feature = new Feature(data, column);
                ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
                feature.getFeatureValues().stream().forEach(featureValue ->
                        dataSets.add(createDataSet(feature, featureValue, data)));
                double summation = 0;
                for (int i = 0; i < dataSets.size(); i++)
                    summation += ((double) (dataSets.get(i).getData().length - 1) / (data.length - 1)) * dataSets.get(i).getEntropy();
                remainder.put(feature, summation);
                infoGains.put(feature, entropy - summation);
            });
            return this;
        }

        Feature findSplitOnFeature() {
            this.calculateEntropy();
            this.calculateInfoGains();
            Iterator<Feature> iterator = infoGains.keySet().iterator();
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                splitOnFeature = feature;
                if (splitOnFeature == null || infoGains.get(splitOnFeature) <= infoGains.get(feature))
                    splitOnFeature = feature;
            }
            return splitOnFeature;
        }
    }
}