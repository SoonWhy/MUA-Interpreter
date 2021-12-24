package mua.variable;

import datatype.dataObject.dataObject;

import java.util.HashMap;

public class variable {
    private HashMap<String, dataObject> variableMap;

    // @Func: 初始化
    public variable() {
        this.variableMap = new HashMap<>();
    }

    // @Func: 添加元素
    public void add(String name, dataObject item) {
        this.variableMap.put(name, item);
    }

    // @Func: 删除元素
    public void remove(String name) {
        this.variableMap.remove(name);
    }

    // @Func: 根据名字获取元素
    public dataObject get(String name) {
        return this.variableMap.get(name);
    }

    // @Func: 判断是否存在某一元素
    public boolean has(String name) {
        return this.variableMap.containsKey(name);
    }

    // @Func: 获取哈希表
    public HashMap<String, dataObject> getVariableMap() {
        return this.variableMap;
    }
}
