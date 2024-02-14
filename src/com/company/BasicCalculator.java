package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides a graphical user interface for a basic calculator program.
 *
 * Author: Christian Gabriel A. Bayquen
 * Course Code: CS122
 * Course Number: 9386
 */
public class BasicCalculator {

    // Colors used for the look and feel of calculator program
    private static final Color BACKGROUND_COLOR = new Color(43,45,47);
    private static final Color BUTTON_NUMBER_COLOR = new Color(95,96,98);
    private static final Color BUTTON_OPERATIONS_COLOR = new Color(255,159,12);
    private static final Color BUTTON_MISC_COLOR = new Color(63,65,67);
    private static final String BY_NAME = "By: Christian Gabriel A. Bayquen";

    // Variables used as placeholders in order to compute for the answer required by the user.
    private static double firstNum = 0;
    private static double secondNum = 0;
    private static double answer = 0;
    private static String operation = "";

    // Boolean variables used to keep track of buttons pressed by user.
    private static boolean firstNumFilled = false;
    private static boolean pointPressed = false;
    private static boolean operationLastPressed = false;
    private static boolean numberLastPressed = false;
    private static boolean equalsLastPressed = false;

    private JLabel historyField;
    private JLabel ioField;
    private JFrame frame;

    /**
     * Instantiates and displays the main window of the calculator program and
     * adds all of the necessary components.
     * Algorithm
     * 1. Declare JFrame variable
     * 2. Set default close operation
     * 3. Add the components to JFrame variable
     * 4. Set JFrame to be visible
     */
    public BasicCalculator(){
        frame = new JFrame("Calculator");
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToMainFrame(frame);
        frame.setMinimumSize(new Dimension(450,605));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Adds all essential components to the main frame.
     * Algorithm
     * 1. Create mainPanel, displayPanel, and buttonsPanel
     * 2. populate displayPanel and buttonsPanel
     * 3. add displayPanel and buttonsPanel to mainPanel
     * 4. add mainPanel to mainFrame.
     */
    private void addComponentsToMainFrame(JFrame frame){
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel mainPanel = new JPanel(new GridBagLayout());
        JPanel displayPanel = new JPanel(new GridBagLayout());
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        JPanel[] panels = {displayPanel,buttonsPanel};

        displayPanel.setBackground(BACKGROUND_COLOR);
        buttonsPanel.setBackground(BACKGROUND_COLOR);

        populateDisplayPanel(displayPanel);
        populateButtonsPanel(buttonsPanel);

        for(byte i = 0; i < 2; i++){
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = gbc.weightx = 1;
            gbc.gridx = 0;
            gbc.gridy = i;
            mainPanel.add(panels[i],gbc);
        }

        frame.add(mainPanel);
    }

    /**
     * Adds historyField and ioField to the displayPanel.
     * @param panel
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
     * Adds all buttons with their proper properties to the buttons panel
     */
    private void populateButtonsPanel(JPanel panel){
        JButton button;
        GridBagConstraints c = new GridBagConstraints();
        String[][] textOfButtons = {{"PC","+/-","CE","÷"}
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
        if(button.getText().equals("CE") || button.getText().equals("+/-") || button.getText().equals("PC") ){
            button.setBackground(BUTTON_MISC_COLOR);
        }else if(button.getText().equals("÷") || button.getText().equals("×") || button.getText().equals("-") || button.getText().equals("+") || button.getText().equals("=")){
            button.setBackground(BUTTON_OPERATIONS_COLOR);
        }else{
            button.setBackground(BUTTON_NUMBER_COLOR);
        }
    }

    /**
     * Determines what to do if a button within the GUI is pressed.
     * Switch e.getActionCommand //The text that the button holds
     */
    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String button = e.getActionCommand();
            if (button.equals("CE")) { // pressed CLEAR
                clearPressed();
            } else if (button.equals("=")) { // pressed EQUALS
                equalsPressed();
            } else if (isOperation(button)) { // pressed OPERATION
                operationPressed(button);
            }else if(button.equals("PC")){
                frame.dispose();
                PrecedenceCalculator pc = new PrecedenceCalculator();
            }else { // PRESSED ANYTHING OTHER THAN ABOVE ( NUMBERS, PERIOD, NEGATIVE  )
                elsePressed(button);
            }
        }

        /**
         * Determines whether the string representation of a number should include a
         * decimal point and adds commas to the whole part.
         */
        private String format(double num){
            String stringEquivalent = String.valueOf(num);
            String wholePart = getWholePart(stringEquivalent);
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
        private String getWholePart(String number){
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
         * Removes the commas within the digits of a given string of numbers.
         */
        private String removeCommas(String string){
            String[] stringArray = string.split("");
            String noCommas = "";
            for (String s : stringArray) {
                if (!s.equals(","))
                    noCommas += s;
            }
            return noCommas;
        }

        /**
         * Adds commas to the digits of a string of numbers. Inputted number must be a whole number
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

        /**
         * Checks whether the button pressed is an operation button.
         */
        private boolean isOperation(String string) {
            return string.equals("+") || string.equals("-") || string.equals("÷") || string.equals("×");
        }

        /**
         * Returns the result of an arithmetic operation between two numbers.
         */
        private double calculate(double x, double y, String operation)  {
                switch (operation) {
                    case "+":
                        return x + y;
                    case "-":
                        return x - y;
                    case "×":
                        return x * y;
                    case "÷":
                        if(y == 0){
                            JOptionPane.showMessageDialog(null,"Math error: Division by zero");
                            return 0;
                        }
                        return x / y;
                    default:
                        return 0;
                }
        }

        /**
         * Handles what to do if the clear button is pressed. Sets all of the
         * variables into their default values.
         */
        private void clearPressed(){
            firstNum = 0;
            secondNum = 0;
            pointPressed = false;
            firstNumFilled = false;
            equalsLastPressed = false;
            operationLastPressed = false;
            numberLastPressed = false;
            operation = "";
            historyField.setText("--- ");
            ioField.setText("0");
        }

        /**
         * Handles what to do if the equals button is pressed.
         */
        private void equalsPressed(){
            if(operation.equals("")){
                return;
            }
            if(equalsLastPressed){
                answer = calculate(firstNum,secondNum,operation);
                historyField.setText(format(firstNum) + " " + operation + " " + format(secondNum) + " =" );
                ioField.setText(format(answer));
                firstNum = answer;
                return;
            }
            secondNum = Double.parseDouble(removeCommas(ioField.getText()));
            answer = calculate(firstNum,secondNum,operation);
            ioField.setText(format(answer));
            historyField.setText(format(firstNum) + " " + operation + " " + format(secondNum) + " =" );
            firstNum = answer;
            pointPressed = false;
            equalsLastPressed = true;
            operationLastPressed = false;
            numberLastPressed = false;
        }

        /**
         * Handles what to do if an operation button is pressed.
         */
        private void operationPressed(String button){
            if(historyField.getText().equals("--- ") || historyField.getText().equals(BY_NAME)){
                historyField.setText("");
            }
            if(equalsLastPressed){
                operation = button;
                historyField.setText(format(firstNum) + " " + operation);
            }else if(operationLastPressed) {
                operation = button;
                historyField.setText(format(firstNum) + " " + operation);
            } else if(numberLastPressed && !firstNumFilled){
                operation = button;
                firstNum = Double.parseDouble(removeCommas(ioField.getText()));
                historyField.setText(historyField.getText() + " " + format(firstNum) + " " + operation);
                firstNumFilled = true;
            }else if(numberLastPressed && firstNumFilled){
                secondNum = Double.parseDouble(removeCommas(ioField.getText()));
                firstNum = calculate(firstNum,secondNum,operation);
                ioField.setText(format(firstNum));
                operation = button;
                historyField.setText(historyField.getText() + " " + format(secondNum) + " " +operation);
            }
            pointPressed = false;
            operationLastPressed = true;
            equalsLastPressed = false;
            numberLastPressed = false;
        }

        /**
         * Handles what to do if neither of the operation buttons, clear button, or
         * equals button is pressed.
         */
        private void elsePressed(String button){
            if(equalsLastPressed){
                historyField.setText("--- ");
                ioField.setText("0");
            }
            if(operationLastPressed){
                ioField.setText("0");
            }
            numberLastPressed = true;
            operationLastPressed = false;
            equalsLastPressed = false;
            if(button.equals("+/-")){
                ioField.setText("-");
                return;
            }
            if(button.equals("0") && ioField.getText().equals("0")){
                ioField.setText("0");
                return;
            }
            if(ioField.getText().equals("0") && button.equals(".")){
                pointPressed = true;
                ioField.setText("0.");
                return;
            }else if(ioField.getText().equals("0.")){
                ioField.setText(ioField.getText() + button);
                return;
            }else if(ioField.getText().equals("0")){
                ioField.setText(button);
                return;
            }else if(button.equals(".")){
                if(!pointPressed){
                    pointPressed = true;
                    updateioField(button,ioField);
                }
                return;
            }
            if(operationLastPressed){
                ioField.setText(button);
                operationLastPressed = false;
            }else{
                updateioField(button,ioField);
            }
        }// end of if else block

        /**
         * Adds commas to the output field of the calculator as digits are being
         * inputted.
         */
        private void updateioField(String button, JLabel ioField){
            try{
                ioField.setText(addCommas(removeCommas(getWholePart(ioField.getText() + button))) + getDecimalPart(ioField.getText() + button));
            }catch (StringIndexOutOfBoundsException e){ //Checks whether the existing value in the output field has a decimal point
                ioField.setText(addCommas(removeCommas(getWholePart(ioField.getText() + button))));
            }
        }

    }//end of BasicCalculatorButtonHandler class
}// end of CalculatorGraphicalUserInterface class
