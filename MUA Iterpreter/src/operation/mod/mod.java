package operation.mod;

import mua.environment.environment;
import mua.variable.variable;

import java.util.regex.Pattern;

public class mod {
    public String mod(environment variableEnvironment, String a, String b, boolean isFunc, variable closureVariable) throws Exception {
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
        String pattern = "^(\\-|\\+)?\\d+(\\.\\d+)?$";

        if (Pattern.matches(pattern, a)) {
            if (Pattern.matches(pattern, b)) {
                return String.valueOf((int)Double.parseDouble(a) -  (int)Double.parseDouble(a) / (int)Double.parseDouble(b) * (int)Double.parseDouble(b));
            }
            else if (b.equals("true")) {
                return "0";
            }
            else {
                throw new Exception("Illegal Value for Operands B");
            }
        }
        else if (a.equals("true") || a.equals("false") ){
            if (a.equals("false")) {
                return "0";
            }
            else {
                if (Pattern.matches(pattern, b)) {
                    return String.valueOf(1 - 1 / (int)Double.parseDouble(b) * (int)Double.parseDouble(b));
                }
                else if (b.equals("true")) {
                    return "0";
                }
                else {
                    throw new Exception("Illegal Value for Operands B");
                }
            }

        }
        else {
            throw new Exception("Illegal Value for Operands A");
        }
    }
}
