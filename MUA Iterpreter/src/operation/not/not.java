package operation.not;

import mua.environment.environment;
import mua.variable.variable;

public class not {
    public String not(environment variableEnvironment, String a, boolean isFunc, variable closureVariable) throws Exception {
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
        if (a.charAt(0) == '"') {
            a = a.substring(1);
        }

        if (a.equals("true") || a.equals("false")) {
            boolean boolA = a.equals("true");
            return String.valueOf(!boolA);
        }
        else {
            throw new Exception("Illegal Value for Operands");
        }
    }
}
