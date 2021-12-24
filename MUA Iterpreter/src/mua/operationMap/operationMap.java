package mua.operationMap;

import java.util.HashMap;

public class operationMap {
    private HashMap<String, Integer> operationMap;

    // @Func: 构造函数
    public operationMap() {
        operationMap = new HashMap<>();
        operationMap.put("add", 2); // 加法运算
        operationMap.put("div", 2); // 除法运算
        operationMap.put("make", 2); // 赋值或新建变量
        operationMap.put("mod", 2); // 模除运算
        operationMap.put("mul", 2); // 乘法运算
        operationMap.put("print", 1); // 打印
        operationMap.put("read", 1); // 接收标准输入
        operationMap.put("sub", 2); // 减法运算
        operationMap.put("thing", 1); // 获取变量的值
        operationMap.put("run", 1); // 运行列表
        operationMap.put("erase", 1); // 清楚变量
        operationMap.put("isname", 1); // 判断是否是一个变量名
        operationMap.put("eq", 2); // 判断是否相等
        operationMap.put("gt", 2); // 判读是否更大
        operationMap.put("lt", 2); // 判断是否更小
        operationMap.put("and", 2); // 与运算
        operationMap.put("or", 2); // 或运算
        operationMap.put("not", 1); // 非运算
        operationMap.put("if", 3); // 条件分支
        operationMap.put("isnumber", 1); // 是否是数字
        operationMap.put("isword", 1); // 是否是字
        operationMap.put("isbool", 1); // 是否是布尔量
        operationMap.put("islist", 1); // 是否是表
        operationMap.put("isempty", 1); // 是否为空字或空列表
        operationMap.put("return", 1);  // 函数专属，规定返回值
        operationMap.put("export", 1);  // 函数专属，局部变量扩展到全局变量
        operationMap.put("readlist", 0); // 从标准输入读取一行形成列表
        operationMap.put("word", 2);    // 合并两个word
        operationMap.put("sentence", 2);    // 合并两个value为list，若value为list展开一层
        operationMap.put("list", 2);    // 合并两个value为list，不展开list
        operationMap.put("join", 2);    // 将value添加到一个list的末尾
        operationMap.put("first", 1);    // 返回word的第一个字符，或list的第一个元素
        operationMap.put("last", 1);    // 返回word的最后一个字符，list的最后一个元素
        operationMap.put("butfirst", 1);    // 返回除第一个元素外剩下的表，或除第一个字符外剩下的字
        operationMap.put("butlast", 1);    // 返回除最后一个元素外剩下的表，或除最后一个字符外剩下的字
        operationMap.put("random", 1);    // 返回[0,number)的一个随机数
        operationMap.put("int", 1);    // floor the int
        operationMap.put("sqrt", 1);    // 返回number的平方根
        operationMap.put("save", 1);    // 在名为word的文件中，以源码形式保存当前命名空间内的名字及其对应的值（即将形如 make <key> <value> 的代码写入文件），返回文件名
        operationMap.put("load", 1);    // 执行名为word的文件中所有代码，返回true
        operationMap.put("erall", 0);    // 清除当前命名空间的全部内容，返回true
    }

    // @Func: 检测指令是否存在
    public boolean ifExist(String key) {
        return operationMap.containsKey(key);
    }

    // @Func: 获取操作数
    public Integer getOperandNum(String key) {
        return operationMap.get(key);
    }
}
