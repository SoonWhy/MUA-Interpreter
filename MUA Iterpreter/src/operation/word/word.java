package operation.word;

import datatype.dataObject.dataObject;
import mua.environment.environment;
import mua.variable.variable;

public class word {
    public dataObject word(environment variableEnvironment, String word1, String word2, boolean isFunc, variable closureVariable)throws Exception {
        if (word1.charAt(0) == ':') {
            int flag = 0;
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(word1.substring(1))) {
                    word1 = variableEnvironment.variableEnvironment.get(index).get(word1.substring(1)).getValue();
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(word1.substring(1))) {
                word1 = closureVariable.get(word1.substring(1)).getValue();
                flag = 1;
            }
            if (flag == 0) {
                throw new Exception("Error: There does NOT exist the word named " + word1.substring(1));
            }
        }
        if (word2.charAt(0) == ':') {
            int flag = 0;
            for (int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--) {
                if (variableEnvironment.variableEnvironment.get(index).has(word2.substring(1))) {
                    word2 = variableEnvironment.variableEnvironment.get(index).get(word2.substring(1)).getValue();
                    flag = 1;
                    break;
                }
            }
            if (isFunc && closureVariable.has(word2.substring(1))) {
                word2 = closureVariable.get(word2.substring(1)).getValue();
                flag = 1;
            }
            if (flag == 0) {
                throw new Exception("Error: There does NOT exist the word named " + word2.substring(1));
            }
        }
        String result = word1.concat(word2);
        return new dataObject(result);
    }
}
