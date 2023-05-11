import java.util.ArrayDeque;

import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter an expression: ");

        String ex = sc.nextLine();

        

        // Use regular expression to extract all numbers and operators

        String[] tokens = ex.split("(?<=[\\d.])(?=[^\\d.])|(?<=[^\\d.])(?=[\\d.])");

        // tokens array now contains all numbers and operators as separate strings

        

        // Stack to hold numbers and operators

        ArrayDeque<Double> nums = new ArrayDeque<>();

        ArrayDeque<Character> ops = new ArrayDeque<>();

        

        for (String token : tokens) {

            // If the token is a number, push it onto the nums stack

            if (token.matches("\\d+(\\.\\d+)?")) {

                nums.push(Double.parseDouble(token));

            }

            // If the token is an operator, push it onto the ops stack

            else if (token.matches("[+\\-*/^]")) {

                char op = token.charAt(0);

                

                // Pop numbers and perform arithmetic operations based on operator precedence

                while (!ops.isEmpty() && hasPrecedence(ops.peek(), op)) {

                    double num2 = nums.pop();

                    double num1 = nums.pop();

                    nums.push(applyOp(num1, num2, ops.pop()));

                }

                ops.push(op);

            }

            else {

                System.out.println("Invalid token: " + token);

                return;

            }

        }

        

        // Pop remaining operators and perform arithmetic operations

        while (!ops.isEmpty()) {

            double num2 = nums.pop();

            double num1 = nums.pop();

            nums.push(applyOp(num1, num2, ops.pop()));

        }

        

        // Print the result

        System.out.println(ex + " = " + nums.pop());

    }

    

    // Helper function to check if op1 has higher or equal precedence than op2

    public static boolean hasPrecedence(char op1, char op2) {

        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {

            return true;

        }

        if ((op1 == '^') && (op2 == '*' || op2 == '/' || op2 == '+' || op2 == '-')) {

            return true;

        }

        return false;

    }

    

    // Helper function to apply arithmetic operations

    public static double applyOp(double num1, double num2, char op) {

        switch (op) {

            case '+':

                return num1 + num2;

            case '-':

                return num1 - num2;

            case '*':

                return num1 * num2;

            case '/':

                if (num2 == 0) {

                    System.out.println("Cannot divide by zero.");

                    System.exit(1);

                }

                return num1 / num2;

            case '^':

                return Math.pow(num1, num2);

        }

        return 0;

    }

}
