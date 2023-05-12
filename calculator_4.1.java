import java.util.*;

 // Compiler version JDK 11.0.2

 class calculator
 {
   public static void main(String args[])
   { 
       Scanner sc = new Scanner(System.in);
       System.out.print("Enter an expression: ");
       String ex = sc.nextLine();
       String res = "";
       if (ex.toCharArray()[0] == '/') {
         //System.out.println("work in progress");
         Adv adv = new Adv();
         switch (ex) {
           case "/prism":
             res = adv.prism();
             break;
           case "/quadratic":
             res = adv.quadratic();
             break;
           case "/quadr":
             res = adv.quadratic();
             break; 
           default:
             res = "Error";
         }
       }
       else {
         Expression expression = new Expression();
         res = expression.calc(ex);
       }
       System.out.println(res);
   }
 }
 
 class Expression
 {
   public String calc(String ex) {
        /*Scanner sc = new Scanner(System.in);
        System.out.print("Enter an expression: ");
        String ex = sc.nextLine();*/
        
        // Use regular expression to extract all numbers and operators
        //String[] tokens = ex.split("(?<=[\\d.])(?=[^\\d.])|(?<=[^\\d.])(?=[\\d.])");
        // tokens array now contains all numbers and operators as separate strings
        
        String[] tokens = ex.split("(?<=[\\d.])(?=[^\\d.()])|(?<=[^\\d.()])(?=[\\d.()])");
       
        
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
                return ("Invalid token: " + token);
            }
        }
        
        // Pop remaining operators and perform arithmetic operations
        while (!ops.isEmpty()) {
            double num2 = nums.pop();
            double num1 = nums.pop();
            nums.push(applyOp(num1, num2, ops.pop()));
        }
        
        // Print the result
        return (ex + " = " + nums.pop());
    }
    
    // Helper function to check if op1 has higher or equal precedence than op2
    private static boolean hasPrecedence(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return true;
        }
        if ((op1 == '^') && (op2 == '*' || op2 == '/' || op2 == '+' || op2 == '-')) {
            return true;
        }
        return false;
    }
    
    // Helper function to apply arithmetic operations
    private static double applyOp(double num1, double num2, char op) {
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

 class Adv
 {
   public String prism()
     {
       Scanner sc = new Scanner(System.in);
       System.out.print("side a: ");
       double a = sc.nextDouble();
       System.out.print("side b: ");
       double b = sc.nextDouble();
       System.out.print("side c: ");
       double c = sc.nextDouble();
       double volume = a*b*c;
       double surface = 2*(a*b + b*c + a*c);
       return ("volume: " + volume + "\nsurface: " + surface);    
     }
   public String quadratic()
     {
       Scanner sc = new Scanner(System.in);
       System.out.print("a*x^2 + b*x + c = 0\na: ");
       double a = sc.nextDouble();
       System.out.print("b: ");
       double b = sc.nextDouble();
       System.out.print("c: ");
       double c = sc.nextDouble();
       double D = b*b - (4*a*c);
       if (D<0) return "no real solution";
       else if (D == 0) {
         double x = (-b)/(2*a);
         return ("one real solution: x = " + x);
       }
       else {
         double D_sqrt = Math.sqrt(D);
         double x1 = (-b + D_sqrt)/(2*a);
         double x2 = (-b - D_sqrt)/(2*a);
         return ("two real solutions: x1 = " + x1 + " ; x2 = " + x2);
       }
     }
 }