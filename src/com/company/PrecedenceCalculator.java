package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A calculator that takes into consideration the precedence of operations in evaluating inputted
 * mathematical expressions. Contains a few scientific functions.
 *
 * NOTE: Expressions inputted within functions cannot be evaluated by the calculator.
 *       White spaces within the function causes problems in converting the expression into
 *       postfix notation.
 *
 * Author: Christian Gabriel A. Bayquen
 * Course Code: CS122
 * Course Number: 9386
 */
public class PrecedenceCalculator {

    // Colors used for the look and feel of calculator program
    private static final Color BACKGROUND_COLOR = new Color(43,45,47);
    private static final Color UPPER_BUTTONS_COLOR = new Color(51, 54, 55);
    private static final Color BUTTON_NUMBER_COLOR = new Color(95,96,98);
    private static final Color BUTTON_OPERATIONS_COLOR = new Color(255,159,12);
    private static final Color BUTTON_MISC_COLOR = new Color(63,65,67);

    /**
     * Used as the main input and output field for the program. Upon pressing the "equals" button,
     * the contents of ioField will be set to the answer returned by evaluating the expression
     * previously in the ioField.
     */
    private JLabel ioField;

    /**
     * This is where the expression inputted in the ioField will be transferred upon pressing the "equals"
     * button.
     */
    private JLabel historyField;

    // Boolean variables used to keep track of buttons pressed by user.
    private boolean equalsLastPressed = false;
    private boolean parenthesisLastPressed = false;
    private boolean operationLastPressed = false;
    private boolean firstCharToBePressed = true;
    private boolean trigoLastPressed = false;

    // String variables used as placeholders for the expression and answer to be shown to user.
    private String infix;
    private String postfix;
    private String answer;

    //Main frame of the calculator program.
    JFrame frame;

    private static final String BY_NAME = "By: Christian Gabriel A. Bayquen";

    /**
     * Creates an instance of the PrecedenceCalculator previewing its frame.
     * Algorithm:
     * 1. Instantiate frame.
     * 2. Add components to frame.
     * 3. Set frame's properties.
     * 4. Set frame visible.
     */
    PrecedenceCalculator(){
        frame = new JFrame("Precedence Calculator");
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToFrame(frame);
        frame.setMinimumSize(new Dimension(500,657));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Adds all the main panels to the frame with their respective settings.
     */
    private void addComponentsToFrame(JFrame frame){
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel mainPanel = new JPanel(new GridBagLayout());
        JPanel displayPanel = new JPanel(new GridBagLayout());
        JPanel sciButtonPanel = new JPanel(new GridBagLayout());
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        JPanel[] panels = {displayPanel,sciButtonPanel,buttonsPanel};

        for(JPanel panel : panels)
            panel.setBackground(BACKGROUND_COLOR);

        populateDisplayPanel(displayPanel);
        populateAdditionalButtonsPanel(sciButtonPanel);
        populateButtonsPanel(buttonsPanel);

        for(byte i = 0; i < panels.length; i++ ){
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = gbc.weightx = 1;
            gbc.gridx = 0;
            gbc.gridy = i;
            mainPanel.add(panels[i],gbc);
        }

        // Add the mainPanel to the mainFrame
        frame.add(mainPanel);
    }

    /**
     * Adds the appropriate components to the display panel.
     */
    private void populateDisplayPanel(JPanel panel){
        GridBagConstraints c = new GridBagConstraints();
        historyField = new JLabel(BY_NAME);
        historyField.setEnabled(false);
        historyField.setBorder(null);
        historyField.setBackground(BACKGROUND_COLOR);
        historyField.setHorizontalAlignment(SwingConstants.RIGHT);
        historyField.setVerticalAlignment(SwingConstants.BOTTOM);
        historyField.setFont(new Font("Roboto",Font.PLAIN,15));
        historyField.setForeground(Color.WHITE);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = .25;
        c.ipady = 13;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(1,1,1,1);
        panel.add(historyField, c);

        ioField = new JLabel();
        ioField.setBorder(null);
        ioField.setBackground(BACKGROUND_COLOR);
        ioField.setFont(new Font("Roboto",Font.PLAIN,50));
        ioField.setForeground(Color.WHITE);
        ioField.setHorizontalAlignment(SwingConstants.RIGHT);
        ioField.setText("0");
        c.weightx = 1;
        c.weighty = .25;
        c.ipady = 25;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        panel.add(ioField, c);
    }

    /**
     * Adds the appropriate buttons to the AdditionalButtonsPanel which contains some scientific  computations.
     */
    private void populateAdditionalButtonsPanel(JPanel panel){
        GridBagConstraints c = new GridBagConstraints();
        String[] buttonText = {"BC", "√n", "n²", "(", ")","sin","cos","tan","x^n","log","cot","sec","csc","|x|","ln"};
        JButton button;
        int row = 1;
        int column = 1;
        for(int i = 0; i < buttonText.length; i++){
            button = new JButton(buttonText[i]);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Roboto",Font.PLAIN,15));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addActionListener(new ButtonHandler());
            button.setBackground(UPPER_BUTTONS_COLOR);
            c.fill = GridBagConstraints.BOTH;
            c.ipadx = 30;
            c.ipady = 25;
            c.weightx = 1;
            c.weighty = 1;
            c.gridx = column-1;
            c.gridy = row-1;
            if(column == 5){
                column = 1;
                row++;
            }else{
                column++;
            }
            c.insets = new Insets(1,1,1,1);
            panel.add(button,c);
        }
    }

    /**
     * Adds the required buttons to the buttonsPanel
     */
    private void populateButtonsPanel(JPanel panel){
        JButton button;
        GridBagConstraints c = new GridBagConstraints();
        String[][] textOfButtons = {{"+/-","CE","⌫","÷"}
                ,{"7","8","9","×",}
                ,{"4","5","6","-",}
                ,{"1","2","3","+"}};

        for(int i = 0, col = 0, row = 0; i < 16; i++){
            button = new JButton(textOfButtons[row][col]);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Roboto",Font.PLAIN,20));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addActionListener(new ButtonHandler());
            determineButtonColor(button);
            c.fill = GridBagConstraints.BOTH;
            c.ipadx = 30;
            c.ipady = 50;
            c.weightx = 1;
            c.weighty = 1;
            c.gridx = col;
            c.gridy = row;
            c.insets = new Insets(1,1,1,1);
            panel.add(button,c);
            col++;
            if(col == 4){
                col = 0;
                row++;
            }
        }
        addLastRow(panel);

    }

    /**
     * Adds the last three buttons to the last row of the buttonsPanel.
     */
    private void addLastRow(JPanel panel){
        JButton button;
        String[] buttonTitles = {"0",".","="};
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 30;
        c.ipady = 50;
        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 2;
        c.gridy = 4;
        c.gridx = 0;
        c.insets = new Insets(1,1,1,1);
        for(int i = 0; i < 3; i++){
            button = new JButton(buttonTitles[i]);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Roboto",Font.PLAIN,20));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.addActionListener(new ButtonHandler());
            determineButtonColor(button);
            if(i > 0) {
                c.gridwidth = 1;
                c.gridx = i + 1;
            }
            panel.add(button,c);
        }

    }

    /**
     * Sets the button's background color depending on its text.
     */
    private static void determineButtonColor(JButton button){
        if(button.getText().equals("CE") || button.getText().equals("+/-") || button.getText().equals("⌫") ){
            button.setBackground(BUTTON_MISC_COLOR);
        }else if(button.getText().equals("÷") || button.getText().equals("×") || button.getText().equals("-") || button.getText().equals("+") || button.getText().equals("=")){
            button.setBackground(BUTTON_OPERATIONS_COLOR);
        }else{
            button.setBackground(BUTTON_NUMBER_COLOR);
        }
    }

    /**
     * Handles what to do when a butten is pressed.
     */
    private class ButtonHandler implements ActionListener {

        /**
         * Upon button press, this method will determine how the calculator program should behvave
         * depending on which button is pressed.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String button = e.getActionCommand();
            if(ioField.getText().equals("0") || equalsLastPressed)  // Sets the text of ioField to empty whenever equals is last pressed
                ioField.setText("");
            if(button.equals("CE") || equalsLastPressed){ // Resets calculator's properties to default values
                ioField.setText("0");
                historyField.setText("--- ");
                equalsLastPressed = false;
                parenthesisLastPressed = false;
                operationLastPressed = false;
                firstCharToBePressed = true;
            }else if(button.equals("BC")){ // Goes back to basic calculator
                frame.dispose();
                BasicCalculator bc = new BasicCalculator();
            }else if(button.equals("=")){ // Evaluates the expression inputted by user
                infix = ioField.getText();
                historyField.setText(infix);
                try {
                    postfix = PrecedenceCalculatorFunctions.toPostfix(infix); // Convert expression to postFix notation
                    answer = format(PrecedenceCalculatorFunctions.evaluatePostFix(postfix)); // Evaluates the postfix notation returning the answer
                    ioField.setText(answer); // Set ioField to answer
                }catch (Exception mathException){
                    mathException.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Syntax Error");
                }finally {
                    equalsLastPressed = true;
                    parenthesisLastPressed = false;
                    operationLastPressed = false;
                }
            }else if(button.equals("⌫")) { // removes the most recent inputted character
                if (!ioField.getText().equals("")) {
                    ioField.setText(removeLastChar(ioField.getText()));
                }
                if(ioField.getText().equals("0") || ioField.getText().equals("")){
                    ioField.setText("0");
                }
                operationLastPressed = false;
                parenthesisLastPressed = false;
            }else if(button.equals("√n")) { // formats the square root input
                ioField.setText(ioField.getText() + "√");
                parenthesisLastPressed = false;
                operationLastPressed = false;
                firstCharToBePressed = false;
            }else if(button.equals("x^n")){ // formats the "raised to" input
                ioField.setText(ioField.getText() + "^");
                parenthesisLastPressed = false;
                operationLastPressed = false;
                firstCharToBePressed = false;
            }else if(button.equals("|x|")){ // formats the absolute value input
                ioField.setText(ioField.getText() + "abs(");
                parenthesisLastPressed = false;
                operationLastPressed = false;
                firstCharToBePressed = false;
                trigoLastPressed = true;
            }else if(button.equals("+/-")){ // formats the "negative" input
                ioField.setText(ioField.getText() + "-");
                parenthesisLastPressed = false;
                operationLastPressed = false;
                firstCharToBePressed = false;
            }else if(button.equals("n²")){ // formats the "squared" input
                ioField.setText(ioField.getText() + "²");
                parenthesisLastPressed = false;
                operationLastPressed = false;
                firstCharToBePressed = false;
            }else if(isOperation(button)) { // determines what to do when an operation is pressed
                if(!parenthesisLastPressed && !firstCharToBePressed) {
                    ioField.setText(ioField.getText() + " " + button + " ");
                }else{
                    ioField.setText(ioField.getText() + button + " ");
                }
                parenthesisLastPressed = false;
                operationLastPressed = true;
                firstCharToBePressed = false;
            }else if(button.equals("(") || button.equals(")")){
                if(button.equals(")")){
                    if(parenthesisLastPressed || trigoLastPressed) {
                        ioField.setText(ioField.getText() + button + " ");
                        trigoLastPressed = false;
                    }else{
                        ioField.setText(ioField.getText() + " " + button + " ");
                    }
                }else if(!operationLastPressed && !firstCharToBePressed){
                    ioField.setText(ioField.getText() + " " +button + " ");
                }else{
                    ioField.setText(ioField.getText() +button + " ");
                }
                parenthesisLastPressed = true;
                firstCharToBePressed = false;
            }else if(isTrigoInput(button)){ // determines what to do when a trigonometric or logarithmic button is pressed
                processTrigoInput(button);
            }else{ // formats other buttons upon input. Generally for numbers inputted.
                ioField.setText(ioField.getText() + button);
                parenthesisLastPressed = false;
                operationLastPressed = false;
                firstCharToBePressed = false;
            }
        }

        /**
         * Determines whether the button pressed is referring to a trigonometric or logarithmic
         * function.
         */
        private boolean isTrigoInput(String button){
            return button.equals("sin") || button.equals("cos") || button.equals("tan") ||
                    button.equals("cot") || button.equals("sec") || button.equals("csc") ||
                    button.equals("log") || button.equals("ln");
        }

        /**
         * Handles what to do when a trigonometric or logarithmic button is pressed.
         */
        private void processTrigoInput(String button){
            ioField.setText(ioField.getText() + button + "(");
            trigoLastPressed = true;
            parenthesisLastPressed = true;
            operationLastPressed = false;
            firstCharToBePressed = false;
        }

    }// end of ButtonHandler class

    /**
     * Checks whether the button pressed is an operation button.
     */
    private boolean isOperation(String string) {
        return string.equals("+") || string.equals("-") || string.equals("÷") || string.equals("×");
    }

    /**
     * Removes the last character of a string.
     */
    private String removeLastChar(String string){
        String toReturn = "";
        try {
            toReturn = string.substring(0, string.length() - 1);
            return toReturn;
        }catch (StringIndexOutOfBoundsException e){ }
        return toReturn;
    }

    /**
     * Determines whether the string representation of a number should include a
     * decimal point and adds commas to the whole part.
     */
    private String format(double num){
        String stringEquivalent = String.valueOf(num);
        String wholePart = getWholepart(stringEquivalent);
        String decimalPart = getDecimalPart(stringEquivalent);
        if(num % 1 == 0) {
            return addCommas(String.valueOf((int)num));
        }else{
            return addCommas(wholePart) + decimalPart;
        }
    }

    /**
     * Returns the whole part of a string of numbers(Digits preceding the decimal point.)
     */
    private String getWholepart(String number){
        try {
            return number.substring(0, number.indexOf("."));
        }catch (StringIndexOutOfBoundsException e){
            return number;
        }
    }

    /**
     * Returns the decimal part of a string of numbers(Digits after the decimal point).
     */
    private String getDecimalPart(String number){
        return number.substring(number.indexOf("."));
    }

    /**
     * Adds commas to the digits of a string of numbers. Inputted number must be a whole number.
     * Algorithm
     * 1. Determine whether number is negative.
     * 1.1 If negative, remove negative sign
     * 1.2 If not, leave number as it is.
     * 2. Create a String array where each digit of the given number is stored in an index.
     * 3. From right to left, concatenate each digit of the number into a new string while
     *     adding a comma (",") every after three digits. Do not add a comma after the last
     *     digit.
     * 4. Add a negative sign ("-") to the end of the newly created string if number had a negative value
     * 5. Get the reversed order of the newly created string with commas.
     * 6. Return reversed order string.
     */
    private String addCommas(String wholeNum){
        String[] digits;
        String formattedString = "";
        boolean isNegative = false;
        if(wholeNum.charAt(0) == '-'){
            digits = wholeNum.substring(1).split("");
            isNegative = true;
        }else{
            digits = wholeNum.split("");
        }
        for(int i = digits.length-1, j = 0; i > -1; i--, j++ ){
            formattedString += digits[i];
            if( j == 2 && i != 0){
                formattedString += ",";
                j = -1;
            }
        }
        if(isNegative){
            formattedString += "-";
            return reverseString(formattedString);
        }else{
            return reverseString(formattedString);
        }
    }

    /**
     * Returns a string in reverse order.
     */
    private String reverseString(String string){
        String reversedString = "";
        String[] digitArray = string.split("");
        for(int i = digitArray.length-1; i > -1; i--){
            reversedString += digitArray[i];
        }
        return reversedString;
    }

}// end of PrecedenceCalculator class
