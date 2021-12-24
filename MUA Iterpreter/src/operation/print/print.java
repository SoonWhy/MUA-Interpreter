package operation.print;

import mua.environment.environment;
import mua.variable.variable;
public class print {
    public String print(environment variableEnvironment, String data, boolean isFunc, variable closureVariable) throws Exception {
        if (data.charAt(0) == ':') {    // 变量名
            int flag = 0;
            for(int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--){
                if (variableEnvironment.variableEnvironment.get(index).has(data.substring(1))) {
                    data = variableEnvironment.variableEnvironment.get(index).get(data.substring(1)).getValue();
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(data.substring(1))) {
                data = closureVariable.get(data.substring(1)).getValue();
                flag = 1;
            }
            if (flag == 0)  {
                throw new Exception("Error: There does NOT exist the word named " + data.substring(1));
            }
        }
        else if (data.charAt(0) == '"') {
            data = data.substring(1);
        }
        if (data.charAt(0) == '['){
            System.out.println(data.substring(1, data.length()-1));
        }
        else {
            System.out.println(data);
        }
        return data;
    }
}
