package com.company;

/**
 * An executable class used to test the functionalities of BasicCalculator.java
 *
 * Author: Christian Gabriel A. Bayquen
 * Course Code: CS122
 * Course Number: 9386
 */
public class CalculatorTester {

    public static void main(String[] args) {
        try{
            CalculatorTester myProgram = new CalculatorTester();
            myProgram.run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void run(){
        BasicCalculator userInterface = new BasicCalculator();
    }

}// end of CalculatorTester class
