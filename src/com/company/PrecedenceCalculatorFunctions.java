package com.company;

import javax.swing.*;
import java.util.Stack;

/**
 * Contains the more complicated functions that the precedence calculator needs to process.
 *
 * Author: Christian Gabriel A. Bayquen
 * Course Code: CS122
 * Course Number: 9386
 */
public class PrecedenceCalculatorFunctions {

    /**
     *  Implements the concept of the shunting-yard algorithm in order to convert a mathematical expression
     *  expressed in infix notation to postfix notation.
     *  Algorithm:
     *  1. Create String postfix where postfix will be stored.
     *  2. Create a tokenArray which contains all tokens inputted in infix notation by parsing by " " white space.
     *  3. For every token in token Array:
     *      3.1 If token is opening parenthesis "(", push token.
     *          else,
     *      3.2 If token is an operator,
     *          3.2.1 Check ff stack is empty or token is higher precedence to stack.peek or stack.peek is "(", then push token
     *                else,
     *                concatenate value of stack.pop to postfix until stack is empty or stack.peek is of higher precedence to token or stack.peek is "(".
     *          3.2.2 push token
     *      3.3 If token is closing parenthesis ")",
     *          3.3.1 Concatenate value of stack.pop to postfix until stack.peek = "("
     *          3.3.2 pop the stack to remove "("
     *      3.4 If token is numerical value,
     *          3.4.1 concatenate token into postfix
     *  4. Concatenate value of stack.pop to postfix until stack is empty.
     *  5. Return postfix.
     */
    public static String toPostfix(String infix){
        String postFix = "";
        String[] tokenArray = infix.split(" ");
        Stack<String> s = new Stack<>();
        String token = "";

        for (String value : tokenArray) {
            token = value;
            if (token.equals("(")) {
                s.push(token);
            } else if (isOperator(token)) {
                if (s.isEmpty()) {
                    s.push(token);
                } else if (tokenIsHigherPrecedence(token, s.peek()) || s.peek().equals("(")) {
                    s.push(token);
                } else {
                    while (!s.isEmpty() && !tokenIsHigherPrecedence(token, s.peek())) {
                        if (!s.peek().equals("(")) {
                            postFix += s.pop() + " ";
                        } else break;
                    }
                    s.push(token);
                }
            } else if (token.equals(")")) {
                while (!s.peek().equals("(")) {
                    postFix += s.pop() + " ";
                }
                s.pop();
            } else {
                postFix += token + " ";
            }
        }
        while (!s.isEmpty()){
            postFix += s.pop() + " ";
        }
        return postFix;
    } // end of toPostFix method

    /**
     *  Evaluates a mathematical expression expressed in postfix notation.
     *  Algorithm:
     *  1. Simplify the tokens given by calling simplifyTokens() method.
     *  2. Create tokenArray with contents returned by process 1.
     *  3. Create a Stack.
     *  4. For every token in tokenArray:
     *      4.1 If token is number, push it in stack
     *      4.2 If token is not a number:
     *          4.2.1 Pop stack two times
     *          4.2.2 Perform arithmetic operation among two stacked numbers, use token as operation.
     *          4.2.3 Push returned value of 4.2.2 in stack.
     *  5. Return stack.pop(), last value inputted into stack which is the answer.
     */
    public static double evaluatePostFix(String postfix) throws Exception{
        double answer = 0;
        String[] tokenArray = simplifyTokens(postfix);
        Stack<Double> s = new Stack<>();
        double x,y;
        for(int i = 0; i < tokenArray.length; i++){
            try{
                s.push(Double.parseDouble(tokenArray[i]));
            }catch (NumberFormatException e){
                y = s.pop();
                x = s.pop();
                answer = calculate(x,y,tokenArray[i]);
                s.push(answer);
            }
        }
        return s.pop();
    }

    /**
     * Simplifies a list of tokens. Tokens that contain functions such as trigonometric or
     * logarithmic functions will already be simplified and returned in a String array.
     * Algorithm:
     * 1. Create tokens array containing all unsimplified tokens.
     * 2. Create simplifiedTokens array with length tokens.array.
     * 3. For every token in tokens array:
     *      3.1 Check contents of token.
     *      3.2 Simplify token according to its contents.
     *      3.3 Add simplified token to simplifiedTokens array.
     * 4. Return simplifiedTokens
     */
    private static String[] simplifyTokens(String string) throws Exception{
        String[] tokenArray = string.split(" ");
        String[] simplified = new String[tokenArray.length];
        String token;
        double raw;
        for(int i = 0; i < tokenArray.length; i++){
            token = tokenArray[i];
            try{
                Double.parseDouble(token);
                simplified[i] = tokenArray[i];
            }catch (NumberFormatException e){
                if(token.equals("(") || token.equals(")") || isOperator(token)){ // If token is a parenthesis
                    simplified[i] = token;
                }else if(tokenIsTrigoOrLog(token)){ // If token is a follows the format xxx(<number>)
                    simplified[i] = processTrigOrLogToken(token);
                }else if(token.charAt(token.length()-1) == '²'){ // If token needs to be squared
                    raw = Double.parseDouble(token.substring(0,token.length()-1));
                    simplified[i] = String.valueOf(raw*raw);
                }else if(token.charAt(0) == '√'){ // finds the square root of token if needed
                    raw = Double.parseDouble(token.substring(1));
                    simplified[i] = String.valueOf(Math.sqrt(raw));
                }else if(token.contains("^")){ // If token needs to be raised to a number
                    String[] parts = token.split("\\^");
                    simplified[i] = String.valueOf(Math.pow(Double.parseDouble(parts[0]),Double.parseDouble(parts[1])));
                }else if(token.contains("-")){ // If token needs to be multiplied by -1
                    raw = Double.parseDouble(token.substring(3));
                    simplified[i] = String.valueOf(raw*-1);
                }
            }
        }
        return simplified;
    } // end of simplifyTokens method

    /**
     * Determines whether a token contains a trigonometric or logarithmic function as well the absolute value function.
     * These functions are inputted into the calculator as xxx(<number>).
     */
    private static boolean tokenIsTrigoOrLog(String token){
        return token.contains("sin") || token.contains("cos") || token.contains("tan") ||
                token.contains("cot") || token.contains("csc") || token.contains("sec") ||
                token.contains("log") || token.contains("ln") || token.contains("abs");
    }

    /**
     * Simplifies a token that passes the tokenIsTrigoOrLog method.
     * Algorithm:
     * 1. Extract numerical value out of token.
     * 2. Determine what mathematical function should be operated on returned value of process 1.
     * 3. Return String value of returned value of process 2.
     */
    private static String processTrigOrLogToken(String token){
        String simplifiedToken = token;
        double value = 0;
        if(token.contains("ln")){
            simplifiedToken = token.substring(3,token.length()-1);
        }else{
            simplifiedToken = token.substring(4,token.length()-1);
        }
        value = evaluateExpression(simplifiedToken);
        if(token.contains("sin")){
            value = Math.sin(value);
        }else if(token.contains("cos")){
            value = Math.cos(value);
        }else if(token.contains("tan")){
            value = Math.tan(value);
        }else if(token.contains("cot")){
            value = Math.pow(Math.tan(value),-1);
        }else if(token.contains("sec")){
            value = Math.pow(Math.cos(value),-1);
        }else if(token.contains("csc")){
            value = Math.pow(Math.sin(value),-1);
        }else if(token.contains("log")){
            value = Math.log10(value);
        }else if(token.contains("ln")){
            value = Math.log(value);
        }else if(token.contains("abs")){
            value = Math.abs(value);
        }
        return String.valueOf(value);
    } // end of processTrigOrLogToken method

    /**
     * Tries to evaluate an expression located within a mathematical function.
     */
    private static Double evaluateExpression(String expression) {
        try {
            return evaluatePostFix(toPostfix(expression));
        } catch (Exception mathException) {
            mathException.printStackTrace();
            JOptionPane.showMessageDialog(null, "Syntax Error");
        }
        return 0.0;
    }

    /**
     *  Returns the value of the value produced by an arithmetic operation.
     */
    private static double calculate(double x, double y, String operation) {
        switch (operation) {
            case "+":
                return x + y;
            case "-":
                return x - y;
            case "×":
                return x * y;
            case "÷":
                return x / y;
            default:
                return 0;
        }
    }

    /**
     * Determines whether a string is an operation.
     */
    private static boolean isOperator(String x){
        return x.equals("×") || x.equals("+") || x.equals("-") || x.equals("÷");
    }

    /**
     *  Determines whether an operation is of higher precedence of another. Follows
     *  the MDAS ( Multiplication, Division, Addition, Subtraction ) precedence of
     *  operations.
     */
    private static boolean tokenIsHigherPrecedence(String token, String top){
        if((token.equals("+") || token.equals("-")) && (top.equals("×") || top.equals("÷") )){
            return false;
        }else if((top.equals("+") || top.equals("-")) && (token.equals("×") || token.equals("÷") )) {
            return true;
        }else if((token.equals("+") || token.equals("-")) && (top.equals("-") || top.equals("+"))){
            return false;
        }else if((top.equals("×") || top.equals("÷")) && (token.equals("×") || token.equals("÷") )){
            return false;
        }else if(isOperator(token) && top.equals("(")){
            return false;
        }
        return false;
    }

}// end of PrecedenceCalculatorFunctions class
