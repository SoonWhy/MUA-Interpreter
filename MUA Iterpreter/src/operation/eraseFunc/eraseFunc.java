package operation.eraseFunc;
import mua.environment.environment;
import mua.variable.variable;

public class eraseFunc {
    public String eraseFunc(environment variableEnvironment, variable closureVariable, String name) throws Exception {
        name = name.trim().substring(1);   // 去掉:
        if (variableEnvironment.variableEnvironment.getLast().has(name)){
            String result = variableEnvironment.variableEnvironment.getLast().get(name).getValue();
            variableEnvironment.variableEnvironment.getLast().remove(name); // 清除元素
            return result;
        }
        else if (closureVariable.has(name)) {
            String result = closureVariable.get(name).getValue();
            closureVariable.remove(name); // 清除元素
            return result;
        }
        else {
            throw new Exception("Error: There does NOT exist the word named " + name);
        }
    }
}
