package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {

    private int exceptions;
   // private Pattern namePattern = Pattern.compile("([Mm][Ii][Ll][Kk]) | ([Bb][Rr][Ee][Aa][Dd]) | ([Cc][Oo][Oo][Kk][Ii][Ee][Ss]) | ([Aa][Pp][Pp][Ll][Ee][Ss])");
    private Pattern wordPattern = Pattern.compile("(:[a-zA-Z|\\d]+)[^./@;!%*^]");
    private Pattern pricePattern = Pattern.compile("(\\d+\\.\\d{2})");
    //private Pattern typePattern = Pattern.compile("([Ff][Oo][Oo][Dd])");
    //private Pattern typePattern = Pattern.compile("\\w+");
    private Pattern expirationPattern = Pattern.compile("(\\d+/\\d{2}/\\d{4})");

    public ItemParser(){
        exceptions = 0;
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
        if(!words.find()) throw new ItemParseException();
        name = words.group().substring(1);
        name = name.toLowerCase();
        name = name.substring(0,1).toUpperCase() + name.substring(1);
        if(name.contains("0"))name =  name.replace('0','o');
        if(!words.find()) throw new ItemParseException();
        type = words.group().substring(1);


        Double price ;
        Matcher dollars = pricePattern.matcher(rawItem);
        if(!dollars.find()) throw new ItemParseException();
        price = Double.valueOf(dollars.group());

        String expiration;
        Matcher date = expirationPattern.matcher(rawItem);
        if(!date.find()) throw new ItemParseException();
        expiration = date.group();

        return new Item(name,price,type,expiration);
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[;^%@!*]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawItem);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString){
        return new ArrayList<String>(Arrays.asList(inputString.split(stringPattern)));
    }



}
