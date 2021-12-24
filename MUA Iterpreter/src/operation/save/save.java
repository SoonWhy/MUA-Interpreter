package operation.save;

import datatype.dataObject.dataObject;
import mua.environment.environment;
import mua.variable.variable;

import java.io.File;
import java.io.FileWriter;

public class save {
    public dataObject save(environment variableEnvironment, dataObject value, boolean isFunc, variable closureVariable)throws Exception {
        if (value.getValue().charAt(0) == '"') {
            value = new dataObject(value.getValue().substring(1));
        }
        else if (value.getValue().charAt(0) == ':') {
            int flag = 0;
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(value.getValue().substring(1))) {
                    value = variableEnvironment.variableEnvironment.get(index).get(value.getValue().substring(1));
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(value.getValue().substring(1))) {
                value = closureVariable.get(value.getValue().substring(1));
                flag = 1;
            }
            if (flag == 0) {
                throw new Exception("Error: There does NOT exist the word named " + value.getValue().substring(1));
            }
        }

        File file = new File(value.getValue());
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter filewriter = new FileWriter(file.getName(), true);
        for(String key : variableEnvironment.variableEnvironment.getLast().getVariableMap().keySet()) {
            String data = "make \"".concat(key).concat(" ").concat(variableEnvironment.variableEnvironment.getLast().getVariableMap().get(key).getValue()).concat("\n");
            filewriter.write(data);
        }
        filewriter.close();

        return value;
    }
}
