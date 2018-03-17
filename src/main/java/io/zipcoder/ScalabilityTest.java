package io.zipcoder;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class ScalabilityTest {

    public static void main(String[] args) throws Exception {
        String output = (new Main()).readRawDataToString("scalabilityTestData.txt");
        System.out.println(output);
//        BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/ericbarnaba/Dev/Labs/HurtLocker/scalabilityTestOutput.txt"));
//        writer.write(output);
//        writer.close();
    }
}
