package io.zipcoder;

import org.apache.commons.io.IOUtils;

import java.util.ArrayList;


public class Main {

    public String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        ItemParser jerkSon = new ItemParser();
        StringBuilder builder = new StringBuilder();
        ArrayList<String> text = jerkSon.parseRawDataIntoStringArray(result);
        ArrayList<ArrayList<String>> keyValuePairs = new ArrayList<>();
        for(String s : text){
            keyValuePairs.add(jerkSon.findKeyValuePairsInRawItemData(s));
        }
        for(ArrayList<String> items : keyValuePairs){
            for(String s : items){
                try {
                    Item temp = jerkSon.parseStringIntoItem(s);
                    System.out.println(temp.toString());
                }
                catch(ItemParseException ipe){

                }
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) throws Exception{
        String output = (new Main()).readRawDataToString();
        System.out.println(output);
        // TODO: parse the data in output into items, and display to console.
    }
}
