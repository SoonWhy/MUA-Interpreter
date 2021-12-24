package operation.load;

import datatype.dataObject.dataObject;
import mua.environment.environment;
import mua.variable.variable;
import operation.run.run;

import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class load {
    public dataObject load(environment variableEnvironment, dataObject value, boolean isFunc, variable closureVariable)throws Exception {
        if (value.getValue().charAt(0) == '"') {
            value = new dataObject(value.getValue().substring(1));
        }
        else if (value.getValue().charAt(0) == ':') {
            int flag = 0;
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(value.getValue().substring(1))) {
                    value = variableEnvironment.variableEnvironment.get(index).get(value.getValue().substring(1));
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(value.getValue().substring(1))) {
                value = closureVariable.get(value.getValue().substring(1));
                flag = 1;
            }
            if (flag == 0) {
                throw new Exception("Error: There does NOT exist the word named " + value.getValue().substring(1));
            }
        }

        File file = new File(value.getValue());
        if(!file.exists()){
            throw new Exception("Error: There dose NOT exist the file named " + value.getValue());
        }
        Scanner scan = new Scanner(file);
        String data = "test";
        String inputData = "[";
        try {
            while ((data = scan.next()) != null) {
                inputData = inputData.concat(data).concat(" ");
            }
        }
        catch (Exception e ){}
        inputData = inputData.substring(0, inputData.length()-1).concat("]");

        run runObj = new run();
        dataObject runList = new dataObject(inputData);
        try {
            runObj.run(runList, variableEnvironment, false, null);
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return new dataObject("true");
    }
}
