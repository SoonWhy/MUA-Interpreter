package operation.erall;

import datatype.dataObject.dataObject;
import mua.environment.environment;
import mua.variable.variable;

public class erall {
    public dataObject erall(environment variableEnvironment, boolean isFunc, variable closureVariable)throws Exception {
        variableEnvironment.variableEnvironment.removeLast();
        variableEnvironment.variableEnvironment.add(new variable());
        if (isFunc) {
            closureVariable = new variable();
        }
        return new dataObject("true");
    }
}
