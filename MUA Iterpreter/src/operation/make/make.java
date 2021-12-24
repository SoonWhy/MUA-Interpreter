package operation.make;

import mua.environment.environment;
import mua.variable.variable;
import datatype.dataObject.dataObject;

public class make {
    public dataObject make(environment variableEnvironment, String name, dataObject value, boolean isFunc, variable closureVariable)throws Exception {
        variable variables = variableEnvironment.variableEnvironment.getLast(); // make 只能对最近一层环境修改
        if (name.charAt(0) == ':') {
            int flag = 0;
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(name.substring(1))) {
                    name = variableEnvironment.variableEnvironment.get(index).get(name.substring(1)).getValue();
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(name.substring(1))) {
                name = closureVariable.get(name.substring(1)).getValue();
                flag = 1;
            }
            if (flag == 0) {
                throw new Exception("Error: There does NOT exist the word named " + name.substring(1));
            }
        }
        name = name.substring(1);   // 去掉“
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

        if (variables.has(name)) {  // 已经存在，更新这个值
            variables.remove(name);
            variables.add(name, value);
        }
        else {
            variables.add(name, value);
        }

        return value;
    }
}
