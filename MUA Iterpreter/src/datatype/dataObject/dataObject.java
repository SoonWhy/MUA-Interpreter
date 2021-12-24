package datatype.dataObject;

import mua.operationMap.operationMap;
import mua.variable.variable;

import java.util.ArrayList;
import java.util.List;

public class dataObject {
    private String value;
    private int type;   // 0 for word/number/bool, 1 for list
    private int func;   // 0 for not a function, 1 for is a function
    private int paramsNum;  // 函数参数数量
    private List<dataObject> list;
    private variable closureVariable;



    public dataObject(String input) {
        this.setValue(input);
    }

    // @Func: 判断当前的列表变量是否是一个函数
    private boolean judgeFunction() {
        if (type == 0) {
            return false;
        }
        else {
            if (list.size() == 2 && list.get(0).type == 1 && list.get(1).type == 1) {     // 列表中应该刚好有两项列表
                List<dataObject> variableList = (List<dataObject>) list.get(0).list;
                operationMap operationMap = new operationMap();
                this.paramsNum = 0;
                for (dataObject item : variableList) {
                    if (item.type == 1 || operationMap.ifExist(item.value)) {   // 变量列表中不应当出现列表、也不应当出现操作符关键词
                        this.paramsNum = 0;
                        return false;
                    }
                    this.paramsNum = this.paramsNum + 1;
                }
                return true;
            }
            else {
                return false;
            }
        }
    }

    // @Func: 返回是否是函数
    public boolean isFunc() {
        return this.func == 1;
    }

    // @Func: 返回是否是列表
    public boolean isList() {
        return this.type == 1;
    }

    // @Func: 获取字符串形式数值
    public String getValue() {
        return this.value;
    }

    // @Func: 获取列表的大小
    public int getListSize() {
        return list.size();
    }

    // @Func: 获取列表
    public List<dataObject> getList() {
        return list;
    }

    // @Func: 获取列表中某个元素
    public dataObject getListByIndex(int index) {
        return list.get(index);
    }

    // @Func: 向列表末尾添加一个元素
    public void addListAtTail(dataObject item) {
        this.list.add(item);
        this.value = this.value.substring(0, this.value.length()-1).concat(" ").concat(item.getValue()).concat("]");
    }

    // @Func: 重新设置数值
    public String setValue(String newValue) {
        this.value = newValue.trim();
        if (this.value.charAt(0) == '[') {
            this.type = 1;
            buildList(); // 转化为list
            if (judgeFunction()) {  // 判断如果是函数
                func = 1;
                closureVariable = new variable();
            }
            else {
                this.func = 0;
            }
        }
        else {
            this.type = 0;
            this.func = 0;
        }
        return newValue;
    }

    // @Func: 根据字符串构建表
    public void buildList() {
        this.list = new ArrayList<dataObject>();
        String content = this.value.substring(1, this.value.length() - 1).trim();     // 去掉最外层的括号
        for (int i = 0; i < content.length(); ) {
            int end = 0;
            if (content.charAt(i) == ' ') {
                i++;
                continue;
            }
            if (content.charAt(i) == '[') {
                int count = 1;
                for (int j = i + 1; j < content.length(); j++) {    // 寻找结尾
                    if (content.charAt(j) == '[') count++;
                    if (content.charAt(j) == ']') {
                        count--;
                        if (count == 0) {
                            end = j;
                            break;
                        }
                    }
                }
                this.list.add(new dataObject(content.substring(i, end) + "]"));  // 进入list
            } else {
                int flag = 0;
                for (int j = i + 1; j < content.length(); j++) {
                    if (content.charAt(j) == ' ') {
                        end = j;
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) end = content.length();
                this.list.add(new dataObject(content.substring(i, end)));     // 进入list
            }
            i = end + 1;
        }
    }

    // @Func: 添加元素
    public void add(String name, dataObject item) {
        this.closureVariable.add(name, item);
    }

    // @Func: 删除元素
    public void remove(String name) {
        this.closureVariable.remove(name);
    }

    // @Func: 根据名字获取元素
    public dataObject get(String name) {
        return this.closureVariable.get(name);
    }

    // @Func: 判断是否存在某一元素
    public boolean has(String name) {
        return this.closureVariable.has(name);
    }

    // @Func: 获取整个variable
    public variable getClosureVariable() {
        return this.closureVariable;
    }

    // @Func: 清空整个variable
    public void clearClosureVariable() {
        this.closureVariable = new variable();
    }
}
