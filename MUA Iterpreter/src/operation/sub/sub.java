package operation.sub;

import mua.environment.environment;
import mua.variable.variable;

import java.util.regex.Pattern;

public class sub {
    public String sub(environment variableEnvironment, String a, String b, boolean isFunc, variable closureVariable) throws Exception {
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
                return String.valueOf(Double.parseDouble(a) - Double.parseDouble(b));
            }
            else if (b.equals("true") || b.equals("false")) {
                double doubleB = (b.equals("true")) ? 1 : 0;
                return String.valueOf(Double.parseDouble(a) - doubleB);
            }
            else {
                throw new Exception("Illegal Value for Operands B");
            }
        }
        else if (a.equals("true") || a.equals("false")){
            double doubleA = (a.equals("true")) ? 1 : 0;
            if (Pattern.matches(pattern, b)) {
                return String.valueOf(doubleA - Double.parseDouble(b));
            }
            else if (b.equals("true") || b.equals("false")) {
                double doubleB = (b.equals("true")) ? 1 : 0;
                return String.valueOf(doubleA - doubleB);
            }
            else {
                throw new Exception("Illegal Value for Operands B");
            }
        }
        else {
            throw new Exception("Illegal Value for Operands A");
        }
    }
}
