package operation.export;

import datatype.dataObject.dataObject;
import mua.environment.environment;
import mua.variable.variable;

public class export {
    public String export(environment variableEnvironment, String name, boolean isFunc, variable closureVariable) throws Exception {
        variable variables = variableEnvironment.variableEnvironment.getFirst(); // 全局
        String dataValue = null;
        if (name.charAt(0) == ':') {
            int flag = 0;
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(name.substring(1))) {
                    name = variableEnvironment.variableEnvironment.get(index).get(name.substring(1)).getValue();
                    name = name.substring(1);   // 去掉“
                    dataValue = variableEnvironment.variableEnvironment.getLast().get(name).getValue();
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(name.substring(1))) {
                name = closureVariable.get(name.substring(1)).getValue();
                name = name.substring(1);   // 去掉“
                dataValue = closureVariable.get(name).getValue();
                flag = 1;
            }
            if (flag == 0) {
                throw new Exception("Error: There does NOT exist the word named " + name.substring(1));
            }
        }
        else {
            name = name.substring(1);
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(name)) {
                    dataValue = variableEnvironment.variableEnvironment.getLast().get(name).getValue();
                    break;
                }
            }
            if (isFunc && closureVariable.has(name)) {
                dataValue = closureVariable.get(name).getValue();
            }
        }
        if (variables.has(name)) {  // 已经存在，更新这个值
            variables.get(name).setValue(dataValue);
        }
        else {
            variables.add(name, new dataObject(dataValue));
        }

        return dataValue;
    }
}
