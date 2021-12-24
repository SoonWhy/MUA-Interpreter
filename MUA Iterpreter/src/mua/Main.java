package mua;

import datatype.dataObject.dataObject;
import mua.operationMap.operationMap;
import mua.variable.variable;
import mua.environment.environment;
import operation.add.add;
import operation.and.and;
import operation.butfirst.butfirst;
import operation.butlast.butlast;
import operation.div.div;
import operation.eq.eq;
import operation.erall.erall;
import operation.erase.erase;
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
import operation.run.run;
import operation.save.save;
import operation.sentence.sentence;
import operation.sqrt.sqrt;
import operation.sub.sub;
import operation.thing.thing;
import operation.word.word;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    private static variable variables; // 储存变量
    private static operationMap operationMap; // 储存指令
    private static environment variableEnvironment; // 记录变量环境
    private static List<String> input;
    private static Stack<dataObject> valueStack;
    private static Stack<String> operationStack;

    public static void main(String[] args) {
        variables = new variable();
        operationMap = new operationMap();
        variableEnvironment = new environment(variables);
        input = new ArrayList<String>();
        valueStack = new Stack<dataObject>();
        operationStack = new Stack<String>();
        input = readInput("in");
        int index = 0;
        int operandsNum = 0;
        int listParse = 0;
        variableEnvironment.variableEnvironment.getLast().add("pi", new dataObject("3.14159"));
        Stack<Integer> formerStackItemNum = new Stack<>(); // 用于记载在这条指令入栈前，数值栈已经有多少元素了。这些元素是这条指令不能使用的。
        formerStackItemNum.push(0); // 用来防止空栈错误
        while (index < input.size()) {
            String item = input.get(index);
            if (item.charAt(0) == '[') {    // 列表
                String nextItem;
                for (int parseIndex = 0; parseIndex < item.length(); parseIndex++) {
                    if (item.charAt(parseIndex) == '[') {
                        listParse++;
                    }
                    else {
                        break;
                    }
                }
                for (int parseIndex = item.length() - 1; parseIndex >= 0 ; parseIndex--) {
                    if (item.charAt(parseIndex) == ']') {
                        listParse--;
                    }
                    else {
                        break;
                    }
                }
                while(listParse > 0) {
                    index++;
                    nextItem = input.get(index);
                    for (int parseIndex = 0; parseIndex < nextItem.length(); parseIndex++) {
                        if (nextItem.charAt(parseIndex) == '[') {
                            listParse++;
                        }
                        else {
                            break;
                        }
                    }
                    for (int parseIndex = nextItem.length() - 1; parseIndex >= 0 ; parseIndex--) {
                        if (nextItem.charAt(parseIndex) == ']') {
                            listParse--;
                        }
                        else {
                            break;
                        }
                    }
                    item = item + " " + nextItem;
                }
            }
            if (operationMap.ifExist(item)) {       // 如果是指令
                operationStack.push(item);
                formerStackItemNum.push(valueStack.size());
                operandsNum = operationMap.getOperandNum(operationStack.peek());
            }
            else if (variables.has(item) && variables.get(item).isFunc()) {    // 如果是函数
                operationStack.push(item);
                formerStackItemNum.push(valueStack.size());
                operandsNum = variables.get(item).getListByIndex(0).getListSize();  // 函数参数个数
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
                if (!operationStack.empty()) {      // 更好的做法是维护一个栈
                    String inst = operationStack.peek();
                    if (operationMap.ifExist(inst)) {
                        operandsNum = operationMap.getOperandNum(operationStack.peek());
                    }
                    else {
                        for(int environIndex = variableEnvironment.variableEnvironment.size() - 1; environIndex >= 0; environIndex--){
                            if (variableEnvironment.variableEnvironment.get(environIndex).has(inst) && variableEnvironment.variableEnvironment.get(environIndex).get(inst).isFunc()) {
                                operandsNum = variableEnvironment.variableEnvironment.get(environIndex).get(inst).getListByIndex(0).getListSize();
                                break;
                            }
                        }
                    }
                }
            }
            index++;
        }
    }

    // @Func: 读入输入
    public static List<String> readInput(String fileName) {
        List<String> inputData = new ArrayList<String>();
        try {
            // System.out.println(fileName);
            Scanner scan = new Scanner(new File(fileName));
            String data = "test";
            while ((data = scan.next()) != null) {
                inputData.add(data);
            }
        }
        catch (Exception e) {

        }

        // System.out.println(inputData);
        return inputData;
    }

    // @Func: 解码并且执行指令
    public static void decoder() {
        String op1, op2, op3;
        String inst = operationStack.pop();
        switch (inst) {
            case "add":
                add addObj = new add();
                op2 = valueStack.pop().getValue();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(addObj.add(variableEnvironment, op1, op2, false, null)));
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
                    valueStack.push(new dataObject(andObj.and(variableEnvironment, op1, op2, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "butfirst":
                butfirst butfirstObj = new butfirst();
                dataObject butfirstOp = valueStack.pop();
                try {
                    valueStack.push(butfirstObj.butfirst(variableEnvironment, butfirstOp, false, null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "butlast":
                butlast butlastObj = new butlast();
                dataObject butlastOp = valueStack.pop();
                try {
                    valueStack.push(butlastObj.butlast(variableEnvironment, butlastOp, false, null));
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
                    valueStack.push(new dataObject(divObj.div(variableEnvironment, op1, op2, false, null)));
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
                    valueStack.push(new dataObject(eqObj.eq(variableEnvironment, op1, op2, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "erall":
                erall erallObj = new erall();
                try {
                    valueStack.push(erallObj.erall(variableEnvironment, false, null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "erase":
                erase eraseObj = new erase();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(eraseObj.erase(variableEnvironment, op1)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "first":
                first firstObj = new first();
                dataObject firstOp = valueStack.pop();
                try {
                    valueStack.push(firstObj.first(variableEnvironment, firstOp, false, null));
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
                    valueStack.push(new dataObject(gtObj.gt(variableEnvironment, op1, op2, false, null)));
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
                    valueStack.push(new dataObject(ifObj.ifOperation(variableEnvironment, op1, op2, op3, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "int":
                intOperation intObj = new intOperation();
                dataObject intOp = valueStack.pop();
                try {
                    valueStack.push(intObj.intOperation(variableEnvironment, intOp, false, null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "isbool":
                isbool isboolObj = new isbool();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(isboolObj.isbool(variableEnvironment, op1, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "isempty":
                isempty isemptyObj = new isempty();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(isemptyObj.isempty(variableEnvironment, op1, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "islist":
                islist islistObj = new islist();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(islistObj.islist(variableEnvironment, op1, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "isname":
                isname isnameObj = new isname();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(isnameObj.isname(variableEnvironment, op1, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "isnumber":
                isnumber isnumberObj = new isnumber();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(isnumberObj.isnumber(variableEnvironment, op1, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "isword":
                isword iswordObj = new isword();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(iswordObj.isword(variableEnvironment, op1, false, null)));
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
                    valueStack.push(joinObj.join(variableEnvironment, joinOp1, joinOp2, false, null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "last":
                last lastObj = new last();
                dataObject lastOp = valueStack.pop();
                try {
                    valueStack.push(lastObj.last(variableEnvironment, lastOp, false, null));
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
                    valueStack.push(listObj.list(variableEnvironment, listOp1, listOp2, false, null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "load":
                load loadObj = new load();
                dataObject loadOp = valueStack.pop();
                try {
                    valueStack.push(loadObj.load(variableEnvironment, loadOp, false, null));
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
                    valueStack.push(new dataObject(ltObj.lt(variableEnvironment, op1, op2, false, null)));
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
                    dataObject makeData = makeObj.make(variableEnvironment, op1, makeop2, false, null);
                    valueStack.push(makeData);
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
                    valueStack.push(new dataObject(modObj.mod(variableEnvironment, op1, op2, false, null)));
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
                    valueStack.push(new dataObject(mulObj.mul(variableEnvironment, op1, op2, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "not":
                not notObj = new not();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(notObj.not(variableEnvironment, op1, false, null)));
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
                    valueStack.push(new dataObject(orObj.or(variableEnvironment, op1, op2, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "print":
                print printObj = new print();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(printObj.print(variableEnvironment,op1, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "random":
                random randomObj = new random();
                dataObject randomOp = valueStack.pop();
                try {
                    valueStack.push(randomObj.random(variableEnvironment, randomOp, false, null));
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
            case "readlist":
                readlist readlistObj = new readlist();
                try {
                    valueStack.push(readlistObj.readlist(variableEnvironment));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "run":
                run runObj = new run();
                String listValue = valueStack.pop().getValue();
                dataObject runList = new dataObject("[]");
                if (listValue.charAt(0) == '[') {
                    runList = new dataObject(listValue);
                }
                else if (listValue.charAt(0) == ':' && variables.has(listValue.substring(1))) {
                    runList = variables.get(listValue.substring(1));
                }
                // variableEnvironment.variableEnvironment.add(new variable());
                try {
                    valueStack.push(runObj.run(runList, variableEnvironment, false, null));
                    // variableEnvironment.variableEnvironment.removeLast();
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "save":
                save saveObj = new save();
                dataObject saveOp = valueStack.pop();
                try {
                    valueStack.push(saveObj.save(variableEnvironment, saveOp, false, null));
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
                    valueStack.push(sentenceObj.sentence(variableEnvironment, sentenceOp1, sentenceOp2, false, null));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "sqrt":
                sqrt sqrtObj = new sqrt();
                dataObject sqrtOp = valueStack.pop();
                try {
                    valueStack.push(sqrtObj.sqrt(variableEnvironment, sqrtOp, false, null));
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
                    valueStack.push(new dataObject(subObj.sub(variableEnvironment, op1, op2, false, null)));
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                break;
            case "thing":
                thing thingObj = new thing();
                op1 = valueStack.pop().getValue();
                try {
                    valueStack.push(new dataObject(thingObj.thing(variableEnvironment,op1, false, null)));
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
                    valueStack.push(wordObj.word(variableEnvironment, op1, op2, false, null));
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
                for(int index = variableEnvironment.variableEnvironment.size() - 1; index >= 0; index--){
                    if (variableEnvironment.variableEnvironment.get(index).has(inst) && variableEnvironment.variableEnvironment.get(index).get(inst).isFunc()) {
                        runFuncList = variableEnvironment.variableEnvironment.get(index).get(inst);
                        funcParamList = runFuncList.getListByIndex(0);
                        funcRunList = runFuncList.getListByIndex(1);
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0)  {
                    System.out.println("Decoder Error!");
                }
                else {
                    run funRunObj = new run();
                    variable funcVariable = new variable();
                    for(int i = funcParamList.getListSize() - 1; i >= 0; i--) {
                        dataObject value = valueStack.pop();
                        if (value.getValue().charAt(0) == ':') {
                            value = variables.get(value.getValue().substring(1));
                        }
                        funcVariable.add(funcParamList.getListByIndex(i).getValue(), value);
                    }
                    variableEnvironment.variableEnvironment.add(funcVariable);
                    try {
                        valueStack.push(funRunObj.run(funcRunList, variableEnvironment, true, runFuncList));
                        variableEnvironment.variableEnvironment.removeLast();
                    }
                    catch (Exception e) {
                        System.out.println("Error: " + e);
                    }
                    break;
                }
                break;
        }
        if (operationStack.empty()) {
            valueStack.clear();
        }
    }
}
