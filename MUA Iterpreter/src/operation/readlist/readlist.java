package operation.readlist;

import datatype.dataObject.dataObject;
import mua.environment.environment;

import java.util.ArrayList;
import java.util.Scanner;

public class readlist {
    public dataObject readlist(environment variableEnvironment) {
        String[] inputArray;
        String input;
        Scanner scan = new Scanner(System.in);
        String result = "[";
        input = scan.nextLine();
        inputArray = input.split(" ");
        for(String item : inputArray) {
            result = result.concat(item);
            result = result.concat(" ");
        }
        result = result.substring(0, result.length()-1);
        result = result.concat("]");
        return new dataObject(result);
    }
}
