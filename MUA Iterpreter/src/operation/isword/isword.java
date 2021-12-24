package operation.isword;

import mua.environment.environment;
import mua.variable.variable;

import java.util.regex.Pattern;

public class isword {
    public String isword(environment variableEnvironment, String a, boolean isFunc, variable closureVariable) throws Exception {
        if (a.charAt(0) == ':') {
            int flag = 0;
            for(int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--){
                if (variableEnvironment.variableEnvironment.get(index).has(a.substring(1))) {
                    a = variableEnvironment.variableEnvironment.get(index).get(a.substring(1)).getValue();
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(a.substring(1))) {
                a = closureVariable.get(a.substring(1)).getValue();
                flag = 1;
            }
            if (flag == 0)  {
                throw new Exception("Error: There does NOT exist the word named " + a.substring(1));
            }
        }
        String pattern = "^(\\-|\\+)?\\d+(\\.\\d+)?$";
        if (a.charAt(0) == '[' && a.charAt(a.length() - 1) == ']') {
            return "false";
        }
        else if (Pattern.matches(pattern, a)) {
            return "false";
        }
        else {
            return "true";
        }
    }
}
