package operation.random;

import datatype.dataObject.dataObject;
import mua.environment.environment;
import mua.variable.variable;

import java.util.regex.Pattern;

public class random {
    public dataObject random(environment variableEnvironment, dataObject value, boolean isFunc, variable closureVariable)throws Exception {
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

        String pattern = "^(\\-|\\+)?\\d+(\\.\\d+)?$";
        if (Pattern.matches(pattern, value.getValue())) {
            double number = Double.parseDouble(value.getValue());
            return new dataObject(Double.toString(Math.random() * number));
        }
        else {
            throw new Exception("Error: Illegal input for operand number.");
        }
    }
}
