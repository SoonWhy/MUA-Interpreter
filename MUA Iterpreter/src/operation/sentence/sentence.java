package operation.sentence;

import datatype.dataObject.dataObject;
import mua.environment.environment;
import mua.variable.variable;

public class sentence {
    public dataObject sentence(environment variableEnvironment, dataObject value1, dataObject value2, boolean isFunc, variable closureVariable)throws Exception {
        if (value1.getValue().charAt(0) == '"') {
            value1 = new dataObject(value1.getValue().substring(1));
        }
        else if (value1.getValue().charAt(0) == ':') {
            int flag = 0;
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(value1.getValue().substring(1))) {
                    value1 = variableEnvironment.variableEnvironment.get(index).get(value1.getValue().substring(1));
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(value1.getValue().substring(1))) {
                value1 = closureVariable.get(value1.getValue().substring(1));
                flag = 1;
            }
            if (flag == 0) {
                throw new Exception("Error: There does NOT exist the word named " + value1.getValue().substring(1));
            }
        }

        if (value2.getValue().charAt(0) == '"') {
            value2 = new dataObject(value2.getValue().substring(1));
        }
        else if (value2.getValue().charAt(0) == ':') {
            int flag = 0;
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(value2.getValue().substring(1))) {
                    value2 = variableEnvironment.variableEnvironment.get(index).get(value2.getValue().substring(1));
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(value2.getValue().substring(1))) {
                value2 = closureVariable.get(value2.getValue().substring(1));
                flag = 1;
            }
            if (flag == 0) {
                throw new Exception("Error: There does NOT exist the word named " + value2.getValue().substring(1));
            }
        }

        String result = "[";
        if (value1.isList()) {
            for (dataObject item : value1.getList()) {
                result = result.concat(item.getValue());
                result = result.concat(" ");
            }
        } else {
            result = result.concat(value1.getValue());
            result = result.concat(" ");
        }
        if (value2.isList()) {
            for (dataObject item : value2.getList()) {
                result = result.concat(item.getValue());
                result = result.concat(" ");
            }
            result = result.substring(0, result.length()-1);
        }else {
            result = result.concat(value2.getValue());
        }
        result = result.concat("]");

        return new dataObject(result);
    }
}
