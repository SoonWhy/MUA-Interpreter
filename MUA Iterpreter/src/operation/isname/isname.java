package operation.isname;

import mua.environment.environment;
import mua.variable.variable;

public class isname {
    public String isname(environment variableEnvironment, String name, boolean isFunc, variable closureVariable) throws Exception {
        name = name.trim().substring(1);   // 去掉:
        int flag = 0;
        for(int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--){
            if (variableEnvironment.variableEnvironment.get(index).has(name)) {
                flag = 1;
                break;
            }
        }
        if (isFunc && closureVariable.has(name)) {
            flag = 1;
        }
        if (flag == 0)  {
            return "false";
        }
        else {
            return "true";
        }
    }
}
