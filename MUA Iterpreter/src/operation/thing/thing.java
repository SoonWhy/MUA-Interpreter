package operation.thing;

import mua.environment.environment;
import mua.variable.variable;

public class thing {
    public String thing(environment variableEnvironment, String name, boolean isFunc, variable closureVariable) throws Exception{
        name = name.trim().substring(1);   // 去掉:
        int flag = 0;
        for(int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--){
            if (variableEnvironment.variableEnvironment.get(index).has(name)) {
                name = variableEnvironment.variableEnvironment.get(index).get(name).getValue();
                flag = 1;
                break;
            }
        }
        if (isFunc && closureVariable.has(name)) {
            name = closureVariable.get(name).getValue();
            flag = 1;
        }
        if (flag == 0)  {
            throw new Exception("Error: There does NOT exist the word named " + name);
        }
        return name;
    }
}
