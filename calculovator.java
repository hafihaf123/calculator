//package com.calculovator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.UnaryOperator;
import java.math.RoundingMode;

public class calculovator {

  public static void main(String[] args)
  {
     Scanner sc = new Scanner(System.in);
     String input = "";
     
     while (true) {
     input = sc.nextLine();
     
     if(input.equals("exit")) break;
     
     String result = Expression.parse(input);
     
     System.out.println(result);
     }
     
     System.out.println("bye");
     
     sc.close();
  }
}

class Expression {
  
  public static String parse(String expression) {
    
    String result = "error";
    
    List<String> numbers = new ArrayList<>();
    List<String> characters = new ArrayList<>();
    
    char[] arr = expression.toCharArray();
    int len = arr.length;
    boolean isFirstNumber = true;
    
    int i, numi = 0, chari = 0;
    
    for(i = 0 ; i < len ; i++) {
      if (Utility.isDigit(arr[i])) {
        if (i == 0) {
          isFirstNumber = true;
          numbers.add(arr[i] + "");
        }
        else {
          if (Utility.isDigit(arr[i-1])){
            String numberInList = numbers.get(numi);
            numberInList += arr[i];
            numbers.set(numi, numberInList);
          }
          else {
            numbers.add(arr[i] + "");
            numi++;
          } 
        }
        
      }
      else {
        if (i == 0) {
          isFirstNumber = false;
          characters.add(arr[i] + "");
        }
        else {
          if (Utility.isDigit(arr[i-1])){
            if (!characters.isEmpty()) 
              chari++;
            characters.add(arr[i] + ""); 
          }
          else {
            String characterInList = characters.get(chari);
            characterInList += arr[i];
            characters.set(chari, characterInList);
          } 
        }
        
      }
    }
    
    if (!isFirstNumber) {
      String firstCh = characters.get(0);
      
      if (firstCh.contains("+")) {
        firstCh.replaceAll("\\+", "");
        characters.set(0, firstCh);
        System.out.println(characters.get(0));
      }
      if (firstCh.contains("-")) {
        numbers.add(0,"0");
      }
      else if (firstCh.equals("(")) {
        numbers.add(0, "0");
        characters.set(0, "+(");
      }
      else if (firstCh.contains("(")){
        numbers.add(0, "0");
        //characters.set(0, "+" + firstCh);
      }
    }
    
    List<Integer> braceStartI = new ArrayList<>();
    List<Integer> braceEndI = new ArrayList<>();
      
    for (i=0 ; i < characters.size() ; i++) {
      String char_i = characters.get(i);
      if (char_i.contains("(") ) {
        braceStartI.add(i);
      }
      if (char_i.contains(")") ) {
        braceEndI.add(i);
      }
    }
      
    if (!braceStartI.isEmpty()) {
      int braceIndex = 0;
      for (int item: braceStartI) {
        int start = item + 1 - braceIndex;
        int end = braceEndI.get(braceIndex) - braceIndex;
          
        List<String> braceNumList = new ArrayList<>(numbers.subList( start, end));
        braceNumList.add(numbers.get(end));
        List<String> braceCharList = new ArrayList<>(characters.subList( start, end));
        System.out.println(braceNumList);
        System.out.println(braceCharList);
        result = eval(braceNumList, braceCharList);
        Utility.replaceRange(numbers, start, end, result);
        
        String braceStartCh = characters.get(start-1);
        braceStartCh = braceStartCh.replaceFirst("\\(", "");
        String braceEndCh = characters.get(end);
        braceEndCh = braceEndCh.replaceFirst("\\)", "");
        
        if (braceStartCh.equals("")) {
          characters.remove(start-1);
        }
        else {
          characters.set(start-1, braceStartCh);
        }
        if (braceEndCh.equals("")) {
          characters.remove(end);
        }
        else {
          characters.set(end, braceEndCh);
        }
        
        for (int ii=start;ii<end;ii++){
          characters.remove(start);
        }
        
        braceIndex++;
      }
    }
    
    return eval(numbers, characters);
    
  }
  
  public static String eval(List<String> numbers, List<String> characters) {
    
    int lenC = characters.size();
    
    System.out.println(characters);
    System.out.println(numbers);
    
    while (Utility. listContains(characters, "*x/÷^")) {
      for (int i = 0 ; i < lenC ; i++) {
        switch(characters.get(i)) {
          case "*":
          case "x":
            BigDecimal m1 = new BigDecimal(numbers.get(i));
            BigDecimal m2 = new BigDecimal(numbers.get(i+1));
            BigDecimal multipRes = m1.multiply(m2);
            Utility.replaceRange(numbers, i, i+1, multipRes + "");
            characters.remove(i);
            lenC = characters.size();
            break;
          case "/":
          case "÷":
            /*BigDecimal d1 = new BigDecimal(numbers.get(i));
            BigDecimal d2 = new BigDecimal(numbers.get(i+1));
            int scale = 10;
            BigDecimal divRes = d1.divide(d2, scale, RoundingMode.HALF_UP);
            Utility.replaceRange(numbers, i, i+1, divRes + "");
            characters.remove(i);*/
            Utility.priorityOperation(numbers, characters, i, Operations.DIVIDE);
            lenC = characters.size();
            break;
          case "^":
          case "**":
            Utility.priorityOperation(numbers, characters, i, Operations.POWER);
            lenC = characters.size();
            
        } 
      }
    }
    
    BigDecimal result = new BigDecimal(numbers.get(0));
    
    for (int i = 0 ; i < lenC ; i++) {
      switch(characters.get(i)) {
        case "+":
          result = result.add(new BigDecimal(numbers.get(i+1))) ;
          break;
        case "-":
          result = result.subtract(new BigDecimal(numbers.get(i+1))) ;
          break;
      }
    }
    
    return result + "";
  }
  
}

class Utility {
  
  public static void replaceRange(List<String> list, int from, int to, final String toReplace) {
    list.subList(from, to+1).replaceAll(new UnaryOperator<String>() {
        @Override
        public String apply(String e) {
            return toReplace;
        }
    });
    for (int i = to ; i > from ; i--){
      list.remove(i);
    }
  }
  
  public static boolean listContains(List<String> list, String x)
  {
    int len = list.size();
    String listI;
    char[] arr = x.toCharArray();
    
    for(int i = 0 ; i < len ; i++) {
      listI = list. get(i);
      for(int ii=0;ii<arr.length;ii++) {
        if (listI.contains(arr[ii]+"")) return true; 
      }
    }
    return false;
  }
  
  public static void addToStartAtI(List<String> list, int index, String toAdd) {
    String original = list.get(index);
    String newS = toAdd + original;
    list.set(index, newS);
  }
  
  public static boolean isDigit(char c) {
    if (Character.isDigit(c)) return true;
    if (c == '.' || c == ',') return true;
    return false;
  }

  public static void priorityOperation(List<String> numbers, List<String> characters, int i, Operations op) {
    BigDecimal e1 = new BigDecimal(numbers.get(i));
    BigDecimal e2 = new BigDecimal(numbers.get(i+1));
    switch //TODO
    BigDecimal expRes = e1.pow(e2.intValue());
    Utility.replaceRange(numbers, i, i+1, expRes + "");
    characters.remove(i);
    //lenC = characters.size();
  }
  
}

enum Operations {
  ADD,
  SUBTRACT,
  MULTIPLY,
  DIVIDE,
  POWER;
}
