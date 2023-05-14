import java.lang.reflect.Method;
import java.util.Scanner;

class calc {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter an expression: ");
        String ex = sc.nextLine();
        String res;
        if (ex.toCharArray()[0] == '/') {
            Adv adv = new Adv();
            switch (ex) {
                case "/" -> {
                    System.out.println("options:");
                    System.out.println("1. convert (c)");
                    System.out.println("2. bodies (b)");
                    System.out.println("3. rule of three (Ro3)");
                    System.out.println("4. quadratic function (q)");
                    System.out.println("5. cube (cb)");
                    System.out.println("6. prism (p)");
                    String option = sc.nextLine();
                    res = switch (option) {
                        case "1", "convert", "c" -> adv.convert();
                        case "2", "b", "bodies" -> adv.bodies();
                        case "3", "Ro3", "rule of three" -> adv.Ro3();
                        case "4", "q", "quadratic" -> adv.quadratic();
                        case "5", "cb", "cube" -> adv.cube();
                        case "6", "p", "prism" -> adv.prism();
                        default -> throw new RuntimeException("Unknown option: " + option);
                    };
                }
                case "/convert", "/unit" -> res = adv.convert();
                case "/bodies", "/body" -> res = adv.bodies();
                case "/Ro3" -> res = adv.Ro3();
                case "/quadratic", "/quadr" -> res = adv.quadratic();
                case "/cube" -> res = adv.cube();
                case "/prism" -> res = adv.prism();
                default -> throw new RuntimeException("Unknown command: " + ex);
            }
        } else {
            Expression expression = new Expression();
            res = expression.calc(ex);
        }
        System.out.println(res);
    }
}

class Expression {
    private static double eval(String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean skip(char charToSkip) {
                while (ch == ' ') nextChar();
                if (ch == charToSkip) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (skip('+')) x += parseTerm(); // addition
                    else if (skip('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (skip('*')) x *= parseFactor(); // multiplication
                    else if (skip('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (skip('+')) return parseFactor(); // unary plus
                if (skip('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (skip('(')) { // parentheses
                    x = parseExpression();
                    skip(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    x = switch (func) {
                        case "sqrt" -> Math.sqrt(x);
                        case "sin" -> Math.sin(Math.toRadians(x));
                        case "cos" -> Math.cos(Math.toRadians(x));
                        case "tan" -> Math.tan(Math.toRadians(x));
                        case "ln" -> Math.log(x);
                        case "log" -> Math.log10(x);
                        case "fact" -> factorial(x);
                        default -> throw new RuntimeException("Unknown function: " + func);
                    };
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (skip('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    private static double factorial(double x) {
        if (x < 0) throw new RuntimeException("factorial: invalid number: " + x);
        else if (x == Math.floor(x)) {
            int factorial = 1;
            for (int i = 1; i <= x; i++) factorial *= i;
            return factorial;
        } else throw new RuntimeException("factorial: invalid number: " + x);
    }

    public String calc(String ex) {
        double result = eval(ex);
        return ex + " = " + result;
    }
}

class Adv {
    public String prism() {
        Scanner sc = new Scanner(System.in);
        System.out.print("side a: ");
        double a = sc.nextDouble();
        System.out.print("side b: ");
        double b = sc.nextDouble();
        System.out.print("side c: ");
        double c = sc.nextDouble();
        double volume = a * b * c;
        double surface = 2 * (a * b + b * c + a * c);
        return ("volume: " + volume + "\nsurface: " + surface);
    }

    public String cube() {
        Scanner sc = new Scanner(System.in);
        System.out.print("side a: ");
        double a = sc.nextDouble();
        double volume = Math.pow(a, 3);
        double surface = 6 * Math.pow(a, 2);
        return ("volume: " + volume + "\nsurface: " + surface);
    }

    public String quadratic() {
        Scanner sc = new Scanner(System.in);
        System.out.print("a*x^2 + b*x + c = 0\na: ");
        double a = sc.nextDouble();
        System.out.print("b: ");
        double b = sc.nextDouble();
        System.out.print("c: ");
        double c = sc.nextDouble();
        double D = b * b - (4 * a * c);
        if (D < 0) return "no root";
        else if (D == 0) {
            double x = (-b) / (2 * a);
            return ("one root: x = " + x);
        } else {
            double D_sqrt = Math.sqrt(D);
            double x1 = (-b + D_sqrt) / (2 * a);
            double x2 = (-b - D_sqrt) / (2 * a);
            return ("two roots: x1 = " + x1 + " ; x2 = " + x2);
        }
    }

    public String Ro3() {
        Scanner sc = new Scanner(System.in);
        System.out.print("a ... b\nc ... x\na:");
        double a = sc.nextDouble();
        System.out.print("b: ");
        double b = sc.nextDouble();
        System.out.print("c: ");
        double c = sc.nextDouble();
        System.out.println("normal or inverse proportionality? (n/i)");
        char umernost = sc.next().charAt(0);
        if (umernost == 'n') {
            double x = (b * c) / a;
            return ("x = " + x + " (normal proportionality)");
        } else if (umernost == 'i') {
            double x = (a * b) / c;
            return ("x = " + x + " (inverse proportionality)");
        } else return "Err_incorrect_input";
    }

    public String convert() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Unit type: (acc angle area data length speed time)");
        String type = sc.next();
        System.out.print("number: ");
        double a = sc.nextDouble();
        double x;
        String unit;
        String to;
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
                return "%s %s = %.2f %s".formatted(a, unit, x, to);
            }
            case "area" -> {
                System.out.println("unit: (m^2 km^2 ft^2 yd^2 mi^2)");
                unit = sc.next();
                switch (unit) {
                    case "m^2" -> {
                        System.out.println("convert to: (km^2 ft^2 yd^2 mi^2)");
                        to = sc.next();
                        switch (to) {
                            case "km^2" -> x = a / 1000000;
                            case "ft^2" -> x = a / 0.0929;
                            case "yd^2" -> x = a / 0.8361;
                            case "mi^2" -> x = a / 2589988.1103;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "km^2" -> {
                        System.out.println("convert to: (m^2 ft^2 yd^2 mi^2)");
                        to = sc.next();
                        switch (to) {
                            case "m^2" -> x = a * 1000000;
                            case "ft^2" -> x = a * 10763910.4167;
                            case "yd^2" -> x = a * 1195990.0463;
                            case "mi^2" -> x = a / 2.59;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "ft^2" -> {
                        System.out.println("convert to: (m^2 km^2 yd^2 mi^2)");
                        to = sc.next();
                        switch (to) {
                            case "m^2" -> x = a / 10.7639;
                            case "km^2" -> x = a / 10763910.4167;
                            case "yd^2" -> x = a / 9;
                            case "mi^2" -> x = a / 27878400;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "yd^2" -> {
                        System.out.println("convert to: (m^2 km^2 ft^2 mi^2)");
                        to = sc.next();
                        switch (to) {
                            case "m^2" -> x = a / 1.196;
                            case "km^2" -> x = a / 1195990.0463;
                            case "ft^2" -> x = a * 9;
                            case "mi^2" -> x = a / 3097600;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "mi^2" -> {
                        System.out.println("convert to: (m^2 km^2 ft^2 yd^2)");
                        to = sc.next();
                        switch (to) {
                            case "m^2" -> x = a * 2589988.1103;
                            case "km^2" -> x = a / 0.3861;
                            case "ft^2" -> x = a * 27878400;
                            case "yd^2" -> x = a * 3097600;
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
            case "data" -> {
                System.out.println("unit: (Mb Gb MB GB)");
                unit = sc.next();
                switch (unit) {
                    case "Mb" -> {
                        System.out.println("convert to: (Gb MB GB)");
                        to = sc.next();
                        switch (to) {
                            case "Gb" -> x = a / 1000;
                            case "MB" -> x = a / 8;
                            case "GB" -> x = a / 8000;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "Gb" -> {
                        System.out.println("convert to: (Mb MB GB)");
                        to = sc.next();
                        switch (to) {
                            case "Mb" -> x = a * 1000;
                            case "MB" -> x = a * 125;
                            case "GB" -> x = a / 8;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "MB" -> {
                        System.out.println("convert to: (Mb Gb GB)");
                        to = sc.next();
                        switch (to) {
                            case "Mb" -> x = a * 8;
                            case "Gb" -> x = a / 125;
                            case "GB" -> x = a / 1000;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "GB" -> {
                        System.out.println("convert to: (Mb Gb MB)");
                        to = sc.next();
                        switch (to) {
                            case "Mb" -> x = a * 8000;
                            case "Gb" -> x = a * 8;
                            case "MB" -> x = a * 1000;
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
            case "length" -> {
                System.out.println("unit: (m km in ft mi)");
                unit = sc.next();
                switch (unit) {
                    case "m" -> {
                        System.out.println("convert to: (km in ft mi)");
                        to = sc.next();
                        switch (to) {
                            case "km" -> x = a / 1000;
                            case "in" -> x = a * 39.3701;
                            case "ft" -> x = a * 3.2808;
                            case "mi" -> x = a / 1609.344;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "km" -> {
                        System.out.println("convert to: (m in ft mi)");
                        to = sc.next();
                        switch (to) {
                            case "m" -> x = a * 1000;
                            case "in" -> x = a * 39370.0787;
                            case "ft" -> x = a * 3280.8399;
                            case "mi" -> x = a / 1.6093;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "in" -> {
                        System.out.println("convert to: (m km ft mi)");
                        to = sc.next();
                        switch (to) {
                            case "m" -> x = a / 39.3701;
                            case "km" -> x = a / 39370.0787;
                            case "ft" -> x = a / 12;
                            case "mi" -> x = a / 63360;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "ft" -> {
                        System.out.println("convert to: (m km in mi)");
                        to = sc.next();
                        switch (to) {
                            case "m" -> x = a / 3.2808;
                            case "km" -> x = a / 3280.8399;
                            case "in" -> x = a * 12;
                            case "mi" -> x = a / 5280;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "mi" -> {
                        System.out.println("convert to: (m km in ft)");
                        to = sc.next();
                        switch (to) {
                            case "m" -> x = a * 1609.344;
                            case "km" -> x = a * 1.6093;
                            case "in" -> x = a * 63360;
                            case "ft" -> x = a * 5280;
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
            case "speed" -> {
                System.out.println("unit: (m/s km/h ft/s mi/h)");
                unit = sc.next();
                switch (unit) {
                    case "m/s" -> {
                        System.out.println("convert to: (km/h ft/s mi/h)");
                        to = sc.next();
                        switch (to) {
                            case "km/h" -> x = a * 3.6;
                            case "ft/s" -> x = a * 3.2808;
                            case "mi/h" -> x = a * 2.2369;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "km/h" -> {
                        System.out.println("convert to: (m/s ft/s mi/h)");
                        to = sc.next();
                        switch (to) {
                            case "m/s" -> x = a / 3.6;
                            case "ft/s" -> x = a / 1.0973;
                            case "mi/h" -> x = a / 1.6093;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "ft/s" -> {
                        System.out.println("convert to: (m/s km/h mi/h)");
                        to = sc.next();
                        switch (to) {
                            case "m/s" -> x = a / 3.2808;
                            case "km/h" -> x = a * 1.0973;
                            case "mi/h" -> x = a / 1.4667;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "mi/h" -> {
                        System.out.println("convert to: (m/s km/h ft/s)");
                        to = sc.next();
                        switch (to) {
                            case "m/s" -> x = a / 2.2369;
                            case "km/h" -> x = a * 1.6093;
                            case "ft/s" -> x = a * 1.4667;
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
            case "time" -> {
                System.out.println("unit: (ms sec min hr)");
                unit = sc.next();
                switch (unit) {
                    case "ms" -> {
                        System.out.println("convert to: (sec min hr)");
                        to = sc.next();
                        switch (to) {
                            case "sec" -> x = a / 1000;
                            case "min" -> x = a / 60000;
                            case "hr" -> x = a / 3600000;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "sec" -> {
                        System.out.println("convert to: (ms min hr)");
                        to = sc.next();
                        switch (to) {
                            case "ms" -> x = a * 1000;
                            case "min" -> x = a / 60;
                            case "hr" -> x = a / 3600;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "min" -> {
                        System.out.println("convert to: (ms sec hr)");
                        to = sc.next();
                        switch (to) {
                            case "ms" -> x = a * 60000;
                            case "sec" -> x = a * 60;
                            case "hr" -> x = a / 60;
                            default -> {
                                return "Err_incorrect_input";
                            }
                        }
                    }
                    case "hr" -> {
                        System.out.println("convert to: (ms sec min)");
                        to = sc.next();
                        switch (to) {
                            case "ms" -> x = a * 3600000;
                            case "sec" -> x = a * 3600;
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
                return "%s %s = %.2f %s".formatted(a, unit, x, to);
            }
            default -> {
                return "Err_incorrect_input";
            }
        }
    }

    public String bodies() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Body type: (prism cube)");
        String functionName = sc.next();  // Replace with the name of the function you want to execute
        try {
            Class<?> clazz = Adv.class;  // Replace with the class name where your functions are defined
            Method method = clazz.getDeclaredMethod(functionName);
            return (String) method.invoke(clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            return String.valueOf(e);
        }
    }
}