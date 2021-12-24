package operation.join;

import datatype.dataObject.dataObject;
import mua.environment.environment;
import mua.variable.variable;

public class join {
    public dataObject join (environment variableEnvironment, dataObject listName, dataObject value, boolean isFunc, variable closureVariable)throws Exception {
        if (listName.getValue().charAt(0) == '"') {
            listName = new dataObject(listName.getValue().substring(1));
        }
        else if (listName.getValue().charAt(0) == ':') {
            int flag = 0;
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(listName.getValue().substring(1))) {
                    listName = variableEnvironment.variableEnvironment.get(index).get(listName.getValue().substring(1));
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(listName.getValue().substring(1))) {
                listName = closureVariable.get(listName.getValue().substring(1));
                flag = 1;
            }
            if (flag == 0) {
                throw new Exception("Error: There does NOT exist the word named " + listName.getValue().substring(1));
            }
        }
        if (!listName.isList()) {
            throw new Exception("Error: The variable input " + listName.getValue().substring(1) + " is NOT a list.");
        }

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
        listName.addListAtTail(value);

        return listName;
    }
}
