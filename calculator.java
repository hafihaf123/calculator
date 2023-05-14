import java.util.ArrayDeque;
import java.util.Scanner;

// Compiler version JDK 11.0.2

 class calc 
 {
   public static void main(String[] args)
   { 
       Scanner sc = new Scanner(System.in);
       System.out.print("Enter an expression: ");
       String ex = sc.nextLine();
       String res;
       if (ex.toCharArray()[0] == '/') {
         //System.out.println("work in progress");
         Adv adv = new Adv();
           switch (ex) {
               case "/" -> {
                   System.out.println("options:");
                   System.out.println("1. prism (p)");
                   System.out.println("2. quadratic function (q)");
                   System.out.println("3. trojclenka - rule of three (3)");
                   System.out.println("4. convert (c)");
                   String option = sc.nextLine();
                   res = switch (option) {
                       case "1", "p", "prism" -> adv.prism();
                       case "2", "q", "quadratic" -> adv.quadratic();
                       case "3", "trojclenka", "Ro3" -> adv.trojclenka();
                       case "4", "convert", "c" -> adv.convert();
                       default -> "Error_incorrect_input";
                   };
               }
               case "/prism" -> res = adv.prism();
               case "/quadratic", "/quadr" -> res = adv.quadratic();
               case "/trojclenka", "/Ro3" -> res = adv.trojclenka();
               case "/convert", "/unit" -> res = adv.convert();
               default -> res = "Error";
           }
       }
       else {
         Expression expression = new Expression();
         res = expression.calc(ex);
       }
       sc.close();
       System.out.println(res);
   }
 }
 
 class Expression
 {
   public String calc(String ex) {
        String[] tokens = ex.split("(?<=[\\d.])(?=[^\\d.()])|(?<=[^\\d.()])(?=[\\d.()])");
        ArrayDeque<Double> nums = new ArrayDeque<>();
        ArrayDeque<Character> ops = new ArrayDeque<>();
        
        for (String token : tokens) {
            if (token.matches("\\d+(\\.\\d+)?")) {
                nums.push(Double.parseDouble(token));
            }
            else if (token.matches("[+\\-*/^]")) {
                char op = token.charAt(0);
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
        while (!ops.isEmpty()) {
            double num2 = nums.pop();
            double num1 = nums.pop();
            nums.push(applyOp(num1, num2, ops.pop()));
        }
        return (ex + " = " + nums.pop());
    }
    
    private static boolean hasPrecedence(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return true;
        }
        return (op1 == '^') && (op2 == '*' || op2 == '/' || op2 == '+' || op2 == '-');
    }
    
    private static double applyOp(double num1, double num2, char op) {
        switch (op) {
            case '+' -> {
                return num1 + num2;
            }
            case '-' -> {
                return num1 - num2;
            }
            case '*' -> {
                return num1 * num2;
            }
            case '/' -> {
                if (num2 == 0) {
                    System.out.println("Cannot divide by zero.");
                    System.exit(1);
                }
                return num1 / num2;
            }
            case '^' -> {
                return Math.pow(num1, num2);
            }
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
       sc.close();
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
       sc.close();
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
   public String trojclenka()
     {
       Scanner sc = new Scanner(System.in);
       System.out.print("a ... b\nc ... x\na:");
       double a = sc.nextDouble();
       System.out.print("b: ");
       double b = sc.nextDouble();
       System.out.print("c: ");
       double c = sc.nextDouble();
       System.out.println("priama alebo nepriama umernost? (p/n)");
       char umernost = sc.next().charAt(0);
       sc.close();
       if (umernost == 'p') {
         double x = (b*c)/a;
         return ("x = " + x + " (priama umernost)");
       }
       else if (umernost == 'n') {
         double x = (a*b)/c;
         return ("x = " + x + " (nepriama umernost)");
       }
       else return "Err_incorrect_input";
     }
   public String convert()
     {
       Scanner sc = new Scanner(System.in);
       System.out.print("number: ");
       double a = sc.nextDouble();
       System.out.println("Unit type: (acc angle)");
       String type = sc.next();
       double x; String unit; String to;
         switch (type) {
             case "acc" -> {
                 System.out.println("unit: (m/s^2 ft/s^2 g cm/s^2)");
                 unit = sc.next();
                 switch (unit) {
                     case "m/s^2" -> {
                         System.out.println("convert to: (ft/s^2 g cm/s^2)");
                         to = sc.next();
                         switch (to) {
                             case "ft/s^2" -> x = a / 0.3048;
                             case "g" -> x = a / 9.8066;
                             case "cm/s^2" -> x = a / 0.01;
                             default -> {
                                 return "Err_incorrect_input";
                             }
                         }
                     }
                     case "ft/s^2" -> {
                         System.out.println("convert to: (m/s^2 g cm/s^2)");
                         to = sc.next();
                         switch (to) {
                             case "m/s^2" -> x = a / 3.2808;
                             case "g" -> x = a / 32.174;
                             case "cm/s^2" -> x = a / 0.0328;
                             default -> {
                                 return "Err_incorrect_input";
                             }
                         }
                     }
                     case "g" -> {
                         System.out.println("convert to: (m/s^2 ft/s^2 cm/s^2)");
                         to = sc.next();
                         switch (to) {
                             case "m/s^2" -> x = a * 9.0866;
                             case "ft/s^2" -> x = a * 32.174;
                             case "cm/s^2" -> x = a * 980.665;
                             default -> {
                                 return "Err_incorrect_input";
                             }
                         }
                     }
                     case "cm/s^2" -> {
                         System.out.println("convert to: (m/s^2 ft/s^2 g)");
                         to = sc.next();
                         switch (to) {
                             case "m/s^2" -> x = a / 100;
                             case "ft/s^2" -> x = a / 30.48;
                             case "g" -> x = a / 980.665;
                             default -> {
                                 return "Err_incorrect_input";
                             }
                         }
                     }
                     default -> {
                         return "Err_incorrect_input";
                     }
                 }
                 return "%s %s = %.2f %s".formatted(a, unit, x, to);
             }
             case "angle" -> {
                 System.out.println("unit: (rad deg min sec)");
                 unit = sc.next();
                 switch (unit) {
                     case "rad" -> {
                         System.out.println("convert to: (deg min sec)");
                         to = sc.next();
                         switch (to) {
                             case "deg" -> x = a * 57.2958;
                             case "min" -> x = a * 3437.7468;
                             case "sec" -> x = a * 206264.8062;
                             default -> {
                                 return "Err_incorrect_input";
                             }
                         }
                     }
                     case "deg" -> {
                         System.out.println("convert to: (rad min sec)");
                         to = sc.next();
                         switch (to) {
                             case "rad" -> x = a / 57.2958;
                             case "min" -> x = a * 60;
                             case "sec" -> x = a * 3600;
                             default -> {
                                 return "Err_incorrect_input";
                             }
                         }
                     }
                     case "min" -> {
                         System.out.println("convert to: (rad deg sec)");
                         to = sc.next();
                         switch (to) {
                             case "rad" -> x = a / 3437.7468;
                             case "deg" -> x = a / 60;
                             case "sec" -> x = a * 60;
                             default -> {
                                 return "Err_incorrect_input";
                             }
                         }
                     }
                     case "sec" -> {
                         System.out.println("convert to: (rad deg min)");
                         to = sc.next();
                         switch (to) {
                             case "rad" -> x = a / 206264.8062;
                             case "deg" -> x = a * 3600;
                             case "min" -> x = a * 60;
                             default -> {
                                 return "Err_incorrect_input";
                             }
                         }
                     }
                     default -> {
                         return "Err_incorrect_input";
                     }
                 }
                 return "%f %s = %.4f %s".formatted(a, unit, x, to);
             }
             default -> {
                 return "Err_incorrect_input";
             }
         }
     }
 }
