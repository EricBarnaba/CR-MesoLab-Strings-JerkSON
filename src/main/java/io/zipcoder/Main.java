package io.zipcoder;

import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class Main {
    ItemParser jerkSon;
    ArrayList<String> itemsSeen;

    public Main(){
        jerkSon = new ItemParser();
        itemsSeen = new ArrayList<>();
    }


    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));

        //StringBuilder builder = new StringBuilder();
        ArrayList<String> text = jerkSon.parseRawDataIntoStringArray(result);
        ArrayList<ArrayList<String>> keyValuePairs = new ArrayList<>();
        for(String s : text){
            keyValuePairs.add(jerkSon.findKeyValuePairsInRawItemData(s));
        }

        for(ArrayList<String> item : keyValuePairs){
                try {
                    Item temp = jerkSon.parseStringIntoItem(item.toString());
                    //builder.append(temp.toString() + "\n");
                    if(!itemsSeen.contains(temp.getName())) itemsSeen.add(temp.getName());
                }
                catch(ItemParseException ipe){
                }
            }
        return makeOutputText();
    }

    private String makeOutputText(){
        StringBuilder builder = new StringBuilder();
        for(String itemType : itemsSeen){
            builder.append(String.format("Name:%10s\t",itemType));
            int timesSeen = jerkSon.getNamesAndPrices().get(itemType).size();
            builder.append(String.format("Seen:%4d times\n", timesSeen));
            builder.append("$$$$$$$$$$$$$$$\t$$$$$$$$$$$$$$$\n");
            appendPriceOccurrences(itemType,builder);
            builder.append("\n");
        }
        builder.append(String.format("Errors         \t"));
        builder.append(String.format("Seen:%4d times", jerkSon.getExceptions()));
        return builder.toString();
    }

    private void appendPriceOccurrences(String key, StringBuilder builder){
        HashMap<Double,Integer> priceMap = jerkSon.getPriceOccurrences(key);
        Iterator<Double> priceIterator = priceMap.keySet().iterator();
        while(priceIterator.hasNext()){
            Double price = priceIterator.next();
            builder.append(String.format("Price:%9.2f\t", price));
            builder.append(String.format("Seen:%4d times\n", priceMap.get(price)));
            builder.append("===============\t===============\n");
        }
    }

    public static void main(String[] args) throws Exception{
        String output = (new Main()).readRawDataToString();
        System.out.println(output);
        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/ericbarnaba/Dev/Labs/HurtLocker/myOutput.txt"));
        writer.write(output);
        writer.close();
        // TODO: parse the data in output into items, and display to console.
    }
}
