package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {

    private int exceptions;

    private Pattern wordPattern = Pattern.compile("(:[a-zA-Z|\\d|\\s]+)[^./@;!%*^,]");
    private Pattern pricePattern = Pattern.compile("(\\d+\\.\\d{2})");
    private Pattern expirationPattern = Pattern.compile("(\\d{1,2}/\\d{2}/\\d{4})");

    private HashMap<String,ArrayList<Double>> namesAndPrices;

    public ItemParser(){
        exceptions = 0;
        namesAndPrices = new HashMap<>();
    }


    public ArrayList<String> parseRawDataIntoStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawData);
        return response;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException{
        String name;
        String type;
        Matcher words = wordPattern.matcher(rawItem);
        if(!words.find()) incrementExceptionsAndThrowIPE();
        name = words.group().substring(1,2).toUpperCase() + words.group().substring(2).toLowerCase();
        if(name.contains("0"))name =  name.replace('0','o');

        if(!words.find()) incrementExceptionsAndThrowIPE();
        type = words.group().substring(1);

        Double price ;
        Matcher dollars = pricePattern.matcher(rawItem);
        if(!dollars.find()) incrementExceptionsAndThrowIPE();
        price = Double.valueOf(dollars.group());

        String expiration;
        Matcher date = expirationPattern.matcher(rawItem);
        if(!date.find()) incrementExceptionsAndThrowIPE();
        expiration = date.group();

        addToNamesAndPrices(name,price);
        return new Item(name,price,type,expiration);
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[;^%@!*]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawItem);
        return response;
    }

    public int getExceptions(){return this.exceptions;}


    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString){
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }

    private void incrementExceptionsAndThrowIPE() throws ItemParseException{
        exceptions++;
        throw new ItemParseException();
    }

    private void addToNamesAndPrices(String name, Double price){
        if(!namesAndPrices.containsKey(name)){
            namesAndPrices.put(name,new ArrayList<>());
            namesAndPrices.get(name).add(price);
        }
        else namesAndPrices.get(name).add(price);
    }

    public HashMap<Double, Integer> getPriceOccurrences(String key){
        HashMap<Double,Integer> output = new HashMap<>();
        ArrayList<Double> priceList = namesAndPrices.get(key);
        for(Double price : priceList){
            if(!output.containsKey(price)){
                output.put(price,1);
            }
            else{
                output.put(price,output.get(price)+1);
            }
        }
        return output;
    }

    public HashMap<String,ArrayList<Double>> getNamesAndPrices(){return this.namesAndPrices;}

}
