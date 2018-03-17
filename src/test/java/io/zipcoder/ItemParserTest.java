package io.zipcoder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ItemParserTest {

    private String rawSingleItem =    "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##";

    private String weirdSingleItem = "naMe:Co0KIes%price:3.23^type:Food*expiration:1/25/2016##";

    private String weirdSingleItem2 = "naMe:Milk%price:3.23!type:Food@expiration:1/25/2016##";

    private String rawSingleItemIrregularSeperatorSample = "naMe:MiLK;price:3.23;type:Food^expiration:1/11/2016##";

    private String rawBrokenSingleItem =    "naMe:Milk;price:;type:Food;expiration:1/25/2016##";

    private String rawMultipleItems = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##"
                                      +"naME:BreaD%price:1.23^type:Food*expiration:1/02/2016##"
                                      +"NAMe:BrEAD;price:1.23!type:Food@expiration:2/25/2016##";
    private ItemParser itemParser;

    @Before
    public void setUp(){
        itemParser = new ItemParser();
    }

    @Test
    public void addToNamesAndPricesTest() throws ItemParseException{
        //Given
        ArrayList<Double> expectedMilkPrice = new ArrayList<>();
        ArrayList<Double> expectedBreadPrice = new ArrayList<>();
        expectedMilkPrice.add(3.23);
        expectedBreadPrice.add(1.23);
        expectedBreadPrice.add(1.23);

        //When
        itemParser.parseStringIntoItem("naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##");
        itemParser.parseStringIntoItem("naME:BreaD%price:1.23^type:Food*expiration:1/02/2016##");
        itemParser.parseStringIntoItem("NAMe:BrEAD;price:1.23!type:Food@expiration:2/25/2016##");
        ArrayList<Double> actualMilkPrice = itemParser.getNamesAndPrices().get("Milk");
        ArrayList<Double> actualBreadPrice = itemParser.getNamesAndPrices().get("Bread");

        //Then
        Assert.assertEquals(expectedBreadPrice, actualBreadPrice);
        Assert.assertEquals(expectedMilkPrice,actualMilkPrice);
    }

    @Test
    public void getPriceOccurrencesTest() throws ItemParseException{
        //Given
        itemParser.parseStringIntoItem("naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##");
        itemParser.parseStringIntoItem("naMe:Milk;price:3.25;type:Food;expiration:1/25/2016##");
        itemParser.parseStringIntoItem("naMe:Milk;price:3.25;type:Food;expiration:1/25/2016##");
        itemParser.parseStringIntoItem("naME:BreaD%price:1.23^type:Food*expiration:1/02/2016##");
        itemParser.parseStringIntoItem("NAMe:BrEAD;price:1.23!type:Food@expiration:2/25/2016##");

        //When
        int expectedBread = 2;
        int expectedMilk1 = 1;
        int expectedMilk2 = 2;
        int actualBread = itemParser.getPriceOccurrences("Bread").get(1.23);
        int actualMilk1 = itemParser.getPriceOccurrences("Milk").get(3.23);
        int actualMilk2 = itemParser.getPriceOccurrences("Milk").get(3.25);

        //Then
        Assert.assertEquals(expectedBread,actualBread);
        Assert.assertEquals(expectedMilk1,actualMilk1);
        Assert.assertEquals(expectedMilk2,actualMilk2);
    }

    @Test
    public void parseRawDataIntoStringArrayTest(){
        Integer expectedArraySize = 3;
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(rawMultipleItems);
        Integer actualArraySize = items.size();
        assertEquals(expectedArraySize, actualArraySize);
    }

    @Test
    public void parseStringIntoItemTest() throws ItemParseException{
        Item expected = new Item("Milk", 3.23, "Food","1/25/2016");
        Item actual = itemParser.parseStringIntoItem(rawSingleItem);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void parseWeirdStringIntoItemTest() throws ItemParseException{
        Item expected = new Item("Cookies", 3.23, "Food","1/25/2016");
        Item actual = itemParser.parseStringIntoItem(weirdSingleItem);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = ItemParseException.class)
    public void parseBrokenStringIntoItemTest() throws ItemParseException{
        itemParser.parseStringIntoItem(rawBrokenSingleItem);
    }

    @Test
    public void findKeyValuePairsInRawItemDataTest(){
        Integer expected = 4;
        Integer actual = itemParser.findKeyValuePairsInRawItemData(rawSingleItem).size();
        assertEquals(expected, actual);
    }

    @Test
    public void findKeyValuePairsInWeirdItemDataTest(){
        Integer expected = 4;
        Integer actual = itemParser.findKeyValuePairsInRawItemData(weirdSingleItem).size();
        assertEquals(expected, actual);
    }

    @Test
    public void findKeyValuePairsInWeirdItemData2Test(){
        Integer expected = 4;
        Integer actual = itemParser.findKeyValuePairsInRawItemData(weirdSingleItem2).size();
        assertEquals(expected, actual);
    }

    @Test
    public void findKeyValuePairsInRawItemDataTestIrregular(){
        Integer expected = 4;
        Integer actual = itemParser.findKeyValuePairsInRawItemData(rawSingleItemIrregularSeperatorSample).size();
        assertEquals(expected, actual);
    }
}
