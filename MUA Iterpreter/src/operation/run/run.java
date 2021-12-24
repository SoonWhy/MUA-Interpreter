package operation.run;

import datatype.dataObject.dataObject;
import mua.environment.environment;
import mua.operationMap.operationMap;
import mua.variable.variable;
import operation.add.add;
import operation.and.and;
import operation.butfirst.butfirst;
import operation.butlast.butlast;
import operation.div.div;
import operation.eq.eq;
import operation.erall.erall;
import operation.erase.erase;
import operation.eraseFunc.eraseFunc;
import operation.export.export;
import operation.first.first;
import operation.gt.gt;
import operation.ifOperation.ifOperation;
import operation.intOperation.intOperation;
import operation.isbool.isbool;
import operation.isempty.isempty;
import operation.islist.islist;
import operation.isname.isname;
import operation.isnumber.isnumber;
import operation.isword.isword;
import operation.join.join;
import operation.last.last;
import operation.list.list;
import operation.load.load;
import operation.lt.lt;
import operation.make.make;
import operation.mod.mod;
import operation.mul.mul;
import operation.not.not;
import operation.or.or;
import operation.print.print;
import operation.random.random;
import operation.read.read;
import operation.readlist.readlist;
import operation.save.save;
import operation.sentence.sentence;
import operation.sqrt.sqrt;
import operation.sub.sub;
import operation.thing.thing;
import operation.word.word;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class run {
    private variable variables; // 储存变量
    private operationMap operationMap; // 储存指令
    private Stack<dataObject> valueStack;
    private Stack<String> operationStack;
    private environment runVariableEnvironment;
    private int returnFlag;
    private boolean isFunc;
    private dataObject runList;

    // @Func: 初始化
    public run () {
        runVariableEnvironment = null;
        variables = new variable();
        operationMap = new operationMap();
        valueStack = new Stack<dataObject>();
        operationStack = new Stack<String>();
        returnFlag = 0;
        isFunc = false;
        runList = new dataObject("[]");
    }

    public dataObject run(dataObject runningList, environment variableEnvironment, boolean runFunc, dataObject runFuncList) throws Exception {
        runVariableEnvironment = variableEnvironment;
        variables = variableEnvironment.variableEnvironment.getLast();
        operationMap = new operationMap();
        valueStack = new Stack<dataObject>();
        operationStack = new Stack<String>();
        returnFlag = 0;
        isFunc = runFunc;
        runList = runFuncList;

        int index = 0;
        int operandsNum = 0;
        int firstOpFlag = 1;
        int listParse = 0;
        int funcFlag = 0;
        int functionParamsNum = 0;

        dataObject result = new dataObject("null");
        String resultString = "[]";
        Stack<Integer> formerStackItemNum = new Stack<>(); // 用于记载在这条指令入栈前，数值栈已经有多少元素了。这些元素是这条指令不能使用的。
        formerStackItemNum.push(0); // 用来防止空栈错误
        while (index < runningList.getListSize()) {
            dataObject listItem = runningList.getListByIndex(index);
            String item = listItem.getValue();
            if (item.charAt(0) == '[') {    // 列表
                String nextItem;
                listParse++;
                if (item.charAt(item.length() - 1) == ']') {
                    listParse--;
                }
                while(listParse > 0) {
                    index++;
                    nextItem = runningList.getListByIndex(index).getValue();
                    if(nextItem.charAt(0) == '[') {
                        listParse++;
                    }
                    if(nextItem.charAt(nextItem.length() - 1) == ']') {
                        listParse--;
                    }
                    item = item + " " + nextItem;
                }
            }
            for(int environIndex = variableEnvironment.variableEnvironment.size() - 1; environIndex >= 0; environIndex--) {  // 在变量链表中判断是不是函数（其实这里如果是run内调用的run，可以直接看runninglist的func属性）
                if (variableEnvironment.variableEnvironment.get(environIndex).has(item) && variableEnvironment.variableEnvironment.get(environIndex).get(item).isFunc()) {
                    funcFlag = 1;
                    functionParamsNum = variableEnvironment.variableEnvironment.get(environIndex).get(item).getListByIndex(0).getListSize();
                    break;
                }
                if(isFunc && runFuncList.has(item) && runFuncList.get(item).isFunc()) {
                    funcFlag = 1;
                    functionParamsNum = runFuncList.get(item).getListByIndex(0).getListSize();
                    break;
                }
            }
            if (item.equals("return")) {    // 函数特别指令
                valueStack.clear();
                returnFlag = 1;
                firstOpFlag = 1;
            }
            if (operationMap.ifExist(item)) {       // 如果是指令
                operationStack.push(item);
                formerStackItemNum.push(valueStack.size());
                operandsNum = operationMap.getOperandNum(operationStack.peek());
            }
            else if (funcFlag == 1) {    // 如果是函数
                operationStack.push(item);
                formerStackItemNum.push(valueStack.size());
                operandsNum = functionParamsNum;
                funcFlag = 0;
            }
            else if(item.charAt(0) == ':') {
                valueStack.push(new dataObject(item));   // store as variable name
            }
            else {
                valueStack.push(new dataObject(item));
            }
            while ( operandsNum + formerStackItemNum.peek() <= valueStack.size() && operationStack.size() > 0) {
                decoder();
                formerStackItemNum.pop();
                if (!operationStack.empty()) {      // （更好的做法是维护一个栈），判断是否可以因为新结果的产生而继续操作
                    String inst = operationStack.peek();
                    if (operationMap.ifExist(inst)) {
                        operandsNum = operationMap.getOperandNum(operationStack.peek());
                    }
                    else {
                        for(int environIndex = variableEnvironment.variableEnvironment.size() - 1; environIndex >= 0; environIndex--){ // 判断是否是函数名
                            if (variableEnvironment.variableEnvironment.get(environIndex).has(inst) && variableEnvironment.variableEnvironment.get(environIndex).get(inst).isFunc()) {
                                operandsNum = variableEnvironment.variableEnvironment.get(environIndex).get(inst).getListByIndex(0).getListSize();
                                break;
                            }
                        }
                        if (isFunc && runFuncList.has(inst)) {   // 捕获的变量里面的函数名
                            operandsNum = runFuncList.get(inst).getListByIndex(0).getListSize();
                        }
                    }
                }
                else if (firstOpFlag == 1 && valueStack.size() == 1) {  // run 指令返回值为第一个op的结果
                    resultString = valueStack.peek().getValue();
                    if (resultString.charAt(0) == ':') {  // 获取具体的值，如果是变量或者字面量要解析
                        int flag = 0;
                        for(int resultIndex = variableEnvironment.variableEnvironment.size() - 1; resultIndex >= 0; resultIndex--){
                            if (variableEnvironment.variableEnvironment.get(resultIndex).has(resultString.substring(1))) {
                                result = new dataObject(variableEnvironment.variableEnvironment.get(resultIndex).get(resultString.substring(1)).getValue());
                                if (result.isFunc()) { // 如果是函数
                                    HashMap<String, dataObject> closureVariableMap = variableEnvironment.variableEnvironment.get(resultIndex).get(resultString.substring(1)).getClosureVariable().getVariableMap();
                                    for(String key : closureVariableMap.keySet()) {
                                        result.add(key, closureVariableMap.get(key));
                                    }
                                }
                                flag = 1;
                                break;
                            }
                            if (resultIndex == variableEnvironment.variableEnvironment.size() - 1 && isFunc && runFuncList.has(resultString.substring(1))) {   // 捕获的变量里面的变量名
                                result = new dataObject(runFuncList.get(resultString.substring(1)).getValue());
                                if (result.isFunc()) { // 如果是函数
                                    HashMap<String, dataObject> closureVariableMap = runFuncList.get(resultString.substring(1)).getClosureVariable().getVariableMap();
                                    for(String key : closureVariableMap.keySet()) {
                                        result.add(key, closureVariableMap.get(key));
                                    }
                                }
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0)  {
                            throw new Exception("Error: There does NOT exist the word named " + resultString.substring(1));
                        }
                    }
                    else if (resultString.charAt(0) == '"') {
                        result = new dataObject(resultString.substring(1));
                    }
                    else {
                        result = valueStack.pop();
                        if(isFunc && result.isFunc()) {
                            for (String key : runVariableEnvironment.variableEnvironment.getLast().getVariableMap().keySet()) { // local内寻找
                                boolean existFlag = false;
                                for (dataObject variableItem : result.getListByIndex(0).getList()) {
                                    if (variableItem.getValue().equals(key)) {
                                        existFlag = true;
                                        break;
                                    }
                                }
                                if (!existFlag) {
                                    result.add(key, runVariableEnvironment.variableEnvironment.getLast().get(key));
                                }
                            }
                            for (String key : runList.getClosureVariable().getVariableMap().keySet()) { // 函数闭包区域内寻找
                                boolean existFlag = false;
                                for (dataObject variableItem : result.getListByIndex(0).getList()) {
                                    if (variableItem.getValue().equals(key)) {
                                        existFlag = true;
                                        break;
                                    }
                                }
                                if (!existFlag) {
                                    result.add(key, runList.getClosureVariable().get(key));
                                }
                            }
                        }
                    }
                    firstOpFlag = 0;
                    if (returnFlag == 1) {  // 如果是return命令，应当立即返回
                        return result;
                    }
                }
            }
            index++;
        }
        if (firstOpFlag == 1 && !valueStack.isEmpty()) {
            resultString = valueStack.peek().getValue();
            if (resultString.charAt(0) == ':') {  // 获取具体的值，如果是变量或者字面量要解析
                int flag = 0;
                for(int resultIndex = variableEnvironment.variableEnvironment.size() - 1; resultIndex >= 0; resultIndex--){
                    if (variableEnvironment.variableEnvironment.get(resultIndex).has(resultString.substring(1))) {
                        result = new dataObject(variableEnvironment.variableEnvironment.get(resultIndex).get(resultString.substring(1)).getValue());
                        if (result.isFunc()) { // 如果是函数
                            HashMap<String, dataObject> closureVariableMap = variableEnvironment.variableEnvironment.get(resultIndex).get(resultString.substring(1)).getClosureVariable().getVariableMap();
                            for(String key : closureVariableMap.keySet()) {
                                result.add(key, closureVariableMap.get(key));
                            }
                        }
                        flag = 1;
                        break;
                    }
                    if (resultIndex == variableEnvironment.variableEnvironment.size() - 1 && isFunc && runFuncList.has(resultString.substring(1))) {   // 捕获的变量里面的变量名
                        result = new dataObject(runFuncList.get(resultString.substring(1)).getValue());
                        if (result.isFunc()) { // 如果是函数
                            HashMap<String, dataObject> closureVariableMap = runFuncList.get(resultString.substring(1)).getClosureVariable().getVariableMap();
                            for(String key : closureVariableMap.keySet()) {
                                result.add(key, closureVariableMap.get(key));
                            }
                        }
                        flag = 1;
                        break;
                    }
                }

                if (flag == 0)  {
                    throw new Exception("Error: There does NOT exist the word named " + resultString.substring(1));
                }
            }
            else if (resultString.charAt(0) == '"') {
                result = new dataObject(resultString.substring(1));
            }
        }
        String pattern = "^(\\-|\\+)?\\d+(\\.\\d+)?$";
        if (Pattern.matches(pattern, resultString)) {
            result = new dataObject(String.valueOf(Double.valueOf(resultString)));
        }
        return result;
    }
    // @Func: 解码并且执行指令
    public void decoder() throws Exception {
        String op1, op2, op3;
        String inst = operationStack.pop();
        switch (inst) {
            case "add":
                add addObj = new add();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(addObj.add(runVariableEnvironment, op1, op2, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "and":
                and andObj = new and();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(andObj.and(runVariableEnvironment, op1, op2, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "butfirst":
                butfirst butfirstObj = new butfirst();
                dataObject butfirstOp = valueStack.pop();
                try {
                    valueStack.push(butfirstObj.butfirst(runVariableEnvironment, butfirstOp, isFunc, isFunc ? runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "butlast":
                butlast butlastObj = new butlast();
                dataObject butlastOp = valueStack.pop();
                try {
                    valueStack.push(butlastObj.butlast(runVariableEnvironment, butlastOp, isFunc, isFunc ? runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "div":
                div divObj = new div();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(divObj.div(runVariableEnvironment, op1, op2, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "eq":
                eq eqObj = new eq();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(eqObj.eq(runVariableEnvironment, op1, op2, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "erall":
                erall erallObj = new erall();
                try {
                    valueStack.push(erallObj.erall(runVariableEnvironment, isFunc, isFunc ? runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "erase":
                erase eraseObj = new erase();
                eraseFunc eraseFuncObj = new eraseFunc();
                op1 = valueStack.pop().getValue();
                try {
                    if (isFunc) {
                        valueStack.push(new dataObject(eraseFuncObj.eraseFunc(runVariableEnvironment, runList.getClosureVariable(), op1)));
                    }
                    else {
                        valueStack.push(new dataObject(eraseObj.erase(runVariableEnvironment, op1)));
                    }
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "export":
                export exportObj = new export();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(exportObj.export(runVariableEnvironment, op1, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "first":
                first firstObj = new first();
                dataObject firstOp = valueStack.pop();
                try {
                    valueStack.push(firstObj.first(runVariableEnvironment, firstOp, isFunc, isFunc ? runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "gt":
                gt gtObj = new gt();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(gtObj.gt(runVariableEnvironment, op1, op2, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "if":
                ifOperation ifObj = new ifOperation();
                op3 = valueStack.pop().getValue();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(ifObj.ifOperation(runVariableEnvironment, op1, op2, op3, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "int":
                intOperation intObj = new intOperation();
                dataObject intOp = valueStack.pop();
                try {
                    valueStack.push(intObj.intOperation(runVariableEnvironment, intOp, isFunc, isFunc ? runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "isbool":
                isbool isboolObj = new isbool();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(isboolObj.isbool(runVariableEnvironment, op1, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "isempty":
                isempty isemptyObj = new isempty();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(isemptyObj.isempty(runVariableEnvironment, op1, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "islist":
                islist islistObj = new islist();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(islistObj.islist(runVariableEnvironment, op1, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "isname":
                isname isnameObj = new isname();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(isnameObj.isname(runVariableEnvironment, op1, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "isnumber":
                isnumber isnumberObj = new isnumber();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(isnumberObj.isnumber(runVariableEnvironment, op1, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "isword":
                isword iswordObj = new isword();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(iswordObj.isword(runVariableEnvironment, op1, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "join":
                join joinObj = new join();
                dataObject joinOp2 = valueStack.pop();
                dataObject joinOp1 = valueStack.pop();
                try {
                    valueStack.push(joinObj.join(runVariableEnvironment, joinOp1, joinOp2, isFunc, isFunc ? runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "last":
                last lastObj = new last();
                dataObject lastOp = valueStack.pop();
                try {
                    valueStack.push(lastObj.last(runVariableEnvironment, lastOp, isFunc, isFunc ? runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "load":
                load loadObj = new load();
                dataObject loadOp = valueStack.pop();
                try {
                    valueStack.push(loadObj.load(runVariableEnvironment, loadOp, isFunc, isFunc ? runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "list":
                list listObj = new list();
                dataObject listOp2 = valueStack.pop();
                dataObject listOp1 = valueStack.pop();
                try {
                    valueStack.push(listObj.list(runVariableEnvironment, listOp1, listOp2, isFunc, isFunc ? runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "lt":
                lt ltObj = new lt();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(ltObj.lt(runVariableEnvironment, op1, op2, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "make":
                make makeObj = new make();
                dataObject makeop2 = valueStack.pop();
                op1 = valueStack.pop().getValue();
                try {
                    dataObject makeData = makeObj.make(runVariableEnvironment, op1, makeop2, isFunc, isFunc ? runList.getClosureVariable() : null);
                    valueStack.push(makeData);
                    if (isFunc && makeData.isFunc()) {   // 属于函数内定义的函数
                        List<dataObject> funcParamList = makeData.getListByIndex(0).getList();
                        for (String key : runVariableEnvironment.variableEnvironment.getLast().getVariableMap().keySet()) { // local内寻找
                            if (key.equals(op1.substring(1))) {
                                continue;
                            }
                            boolean existFlag = false;
                            for (dataObject item : funcParamList) {
                                if (item.getValue().equals(key)) {
                                    existFlag = true;
                                    break;
                                }
                            }
                            if (!existFlag) {
                                makeData.add(key, runVariableEnvironment.variableEnvironment.getLast().get(key));
                            }
                        }
                        for (String key : runList.getClosureVariable().getVariableMap().keySet()) { // 函数闭包区域内寻找
                            if (key.equals(op1.substring(1))) {
                                continue;
                            }
                            boolean existFlag = false;
                            for (dataObject item : funcParamList) {
                                if (item.getValue().equals(key)) {
                                    existFlag = true;
                                    break;
                                }
                            }
                            if (!existFlag) {
                                makeData.add(key, runList.getClosureVariable().get(key));
                            }
                        }

                    }
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "mod":
                mod modObj = new mod();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(modObj.mod(runVariableEnvironment, op1, op2, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "mul":
                mul mulObj = new mul();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(mulObj.mul(runVariableEnvironment, op1, op2, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "not":
                not notObj = new not();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(notObj.not(runVariableEnvironment, op1, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "or":
                or orObj = new or();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(orObj.or(runVariableEnvironment, op1, op2, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "print":
                print printObj = new print();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(printObj.print(runVariableEnvironment,op1, isFunc, isFunc ? runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "random":
                random randomObj = new random();
                dataObject randomOp = valueStack.pop();
                try {
                    valueStack.push(randomObj.random(runVariableEnvironment, randomOp, isFunc, isFunc ? runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "read":
                read readObj = new read();
                op1 = valueStack.pop().getValue();
                valueStack.push(new dataObject(readObj.read(op1)));
                break;
            case "return":
                break;
            case "run":
                run runObj = new run();
                String listValue = valueStack.pop().getValue();
                dataObject runList = new dataObject("[]");
                if (listValue.charAt(0) == '[') {
                    runList = new dataObject(listValue);
                }
                else if (listValue.charAt(0) == ':') {
                    int flag = 0;
                    for(int index = runVariableEnvironment.variableEnvironment.size() - 1; index >= 0; index--){
                        if (runVariableEnvironment.variableEnvironment.get(index).has(listValue.substring(1))) {
                            runList = runVariableEnvironment.variableEnvironment.get(index).get(listValue.substring(1));        // P3 感觉这里好像不用改了
                            flag = 1;
                            break;
                        }
                        if (index == runVariableEnvironment.variableEnvironment.size() - 1 && isFunc && runList.has(listValue.substring(1))) {   // 捕获的变量里面的变量名
                            runList = runList.get(listValue.substring(1));
                            flag = 1;
                            break;
                        }
                    }
                    if (flag == 0)  {
                        throw new Exception("Error: There does NOT exist the word named " + listValue.substring(1));
                    }
                }
                runVariableEnvironment.variableEnvironment.add(new variable());
                try {
                    valueStack.push(runObj.run(runList, runVariableEnvironment, false, null));   // 还没深入思考
                    runVariableEnvironment.variableEnvironment.removeLast();
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "readlist":
                readlist readlistObj = new readlist();
                try {
                    valueStack.push(readlistObj.readlist(runVariableEnvironment));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "save":
                save saveObj = new save();
                dataObject saveOp = valueStack.pop();
                try {
                    valueStack.push(saveObj.save(runVariableEnvironment, saveOp, isFunc, isFunc ? this.runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "sentence":
                sentence sentenceObj = new sentence();
                dataObject sentenceOp2 = valueStack.pop();
                dataObject sentenceOp1 = valueStack.pop();
                try {
                    valueStack.push(sentenceObj.sentence(runVariableEnvironment, sentenceOp1, sentenceOp2, isFunc, isFunc ? this.runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "sqrt":
                sqrt sqrtObj = new sqrt();
                dataObject sqrtOp = valueStack.pop();
                try {
                    valueStack.push(sqrtObj.sqrt(runVariableEnvironment, sqrtOp, isFunc, isFunc ? this.runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "sub":
                sub subObj = new sub();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(subObj.sub(runVariableEnvironment, op1, op2, isFunc, isFunc ? this.runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "thing":
                thing thingObj = new thing();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(thingObj.thing(runVariableEnvironment, op1, isFunc, isFunc ? this.runList.getClosureVariable() : null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "word":
                word wordObj = new word();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(wordObj.word(runVariableEnvironment, op1, op2, isFunc, isFunc ? this.runList.getClosureVariable() : null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            default:
                int flag = 0;
                dataObject funcRunList = new dataObject("[]");
                dataObject funcParamList = new dataObject("[]");
                dataObject runFuncList = new dataObject("[]");
                for(int index = runVariableEnvironment.variableEnvironment.size() - 1; index >= 0; index--){
                    if (runVariableEnvironment.variableEnvironment.get(index).has(inst) && runVariableEnvironment.variableEnvironment.get(index).get(inst).isFunc()) {
                        runFuncList = runVariableEnvironment.variableEnvironment.get(index).get(inst);
                        funcParamList = runFuncList.getListByIndex(0);
                        funcRunList = runFuncList.getListByIndex(1);
                        flag = 1;
                        break;
                    }
                }
                if (isFunc && this.runList.has(inst) && this.runList.get(inst).isFunc()) {
                    runFuncList = this.runList.get(inst);
                    funcParamList = runFuncList.getListByIndex(0);
                    funcRunList = runFuncList.getListByIndex(1);
                    flag = 1;
                }
                if (flag == 0)  {
                    System.out.println("Decoder Error!" + inst);
                }
                else {
                    run funRunObj = new run();
                    variable funcVariable = new variable();
                    for(int i = funcParamList.getListSize() - 1; i >= 0; i--) {
                        String value = valueStack.pop().getValue();
                        dataObject paramObj = new dataObject(value);
                        if (value.charAt(0) == ':') {
                            for(int index = runVariableEnvironment.variableEnvironment.size() - 1; index >= 0; index--){
                                if (runVariableEnvironment.variableEnvironment.get(index).has(value.substring(1))) {
                                    paramObj = runVariableEnvironment.variableEnvironment.get(index).get(value.substring(1));
                                    break;
                                }
                            }
                            if (isFunc && this.runList.has(value.substring(1))) {
                                paramObj = this.runList.get(value.substring(1));
                            }
                        }
                        funcVariable.add(funcParamList.getListByIndex(i).getValue(), paramObj);
                    }
                    runVariableEnvironment.variableEnvironment.add(funcVariable);
                    try {
                        valueStack.push(funRunObj.run(funcRunList, runVariableEnvironment, true, runFuncList));
                        runVariableEnvironment.variableEnvironment.removeLast();
                    }
                    catch (Exception e) {
                        System.out.println("Error: " + e);
                    }
                    break;
                }
                break;
        }
//        if (operationStack.empty() && returnFlag == 0) {
//            valueStack.clear();
//        }
    }
}
