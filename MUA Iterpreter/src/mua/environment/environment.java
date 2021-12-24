package mua.environment;

import mua.variable.variable;

import java.util.LinkedList;

public class environment {
    public LinkedList<variable> variableEnvironment = new LinkedList<variable>();

    public environment(variable globalVariable) {
        this.variableEnvironment.add(globalVariable);   // 第一个节点应为全局变量
    }



}
