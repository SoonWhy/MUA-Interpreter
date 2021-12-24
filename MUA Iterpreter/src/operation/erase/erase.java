package operation.erase;
import mua.environment.environment;

public class erase {
    public String erase(environment variableEnvironment, String name) throws Exception {
        name = name.trim().substring(1);   // 去掉:
        if (variableEnvironment.variableEnvironment.getLast().has(name)){
            String result = variableEnvironment.variableEnvironment.getLast().get(name).getValue();
            variableEnvironment.variableEnvironment.getLast().remove(name); // 清除元素
            return result;
        }
        else {
            throw new Exception("Error: There does NOT exist the word named " + name);
        }
    }
}
