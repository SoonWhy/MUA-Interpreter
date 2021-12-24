package operation.ifOperation;

import datatype.dataObject.dataObject;
import mua.environment.environment;
import mua.variable.variable;
import operation.run.run;

public class ifOperation {
    public String ifOperation(environment variableEnvironment, String a, String b, String c, boolean isFunc, variable closureVariable) throws Exception {
        String result = "[]";
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
        if (c.charAt(0) == ':') {
            int flag = 0;
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(c.substring(1))) {
                    c = variableEnvironment.variableEnvironment.get(index).get(c.substring(1)).getValue();
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(c.substring(1))) {
                c = closureVariable.get(c.substring(1)).getValue();
                flag = 1;
            }
            if (flag == 0) {
                throw new Exception("Error: There does NOT exist the word named " + c.substring(1));
            }
        }

        if (a.charAt(0) == '"') {
            a = a.substring(1);
        }

        dataObject bObj = new dataObject(b);
        dataObject cObj = new dataObject(c);

        if (a.equals("true") || a.equals("false")) {
            run runObj = new run();
            variableEnvironment.variableEnvironment.add(new variable());
            dataObject runList;
            if (a.equals("true")) {
                runList = bObj;
            }
            else {
                runList = cObj;
            }
            try {
                if (isFunc) {
                    for (String key : closureVariable.getVariableMap().keySet()) {
                        variableEnvironment.variableEnvironment.getLast().add(key, closureVariable.get(key));
                    }
                }
                result = runObj.run(runList, variableEnvironment, false, null).getValue();
                variableEnvironment.variableEnvironment.removeLast();
                return result;
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
        else {
            throw new Exception("Illegal Value for Operands");
        }
        return result;
    }
}
