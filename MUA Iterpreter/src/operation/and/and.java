package operation.and;

import mua.environment.environment;
import mua.variable.variable;

import java.util.regex.Pattern;


public class and {
    public String and(environment variableEnvironment, String a, String b, boolean isFunc, variable closureVariable) throws Exception {
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
        if (b.charAt(0) == ':') {
            int flag = 0;
            for(int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--){
                if (variableEnvironment.variableEnvironment.get(index).has(b.substring(1))) {
                    b = variableEnvironment.variableEnvironment.get(index).get(b.substring(1)).getValue();
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(b.substring(1))) {
                b = closureVariable.get(b.substring(1)).getValue();
                flag = 1;
            }
            if (flag == 0)  {
                throw new Exception("Error: There does NOT exist the word named " + b.substring(1));
            }
        }
        if (a.charAt(0) == '"') {
            a = a.substring(1);
        }
        if (b.charAt(0) == '"') {
            b = b.substring(1);
        }

        if ((a.equals("true") || a.equals("false")) && (b.equals("true") || b.equals("false"))) {
            boolean boolA = a.equals("true");
            boolean boolB = b.equals("true");
            return String.valueOf(boolA && boolB);
        }
        else {
            throw new Exception("Illegal Value for Operands");
        }
    }
}
