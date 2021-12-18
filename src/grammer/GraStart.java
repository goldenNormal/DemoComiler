package grammer;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class GraStart {

    static ArrayList<String> stderr=new ArrayList<>();
    static ArrayList<String> stdout=new ArrayList<>();
    static GrammerTree tree;


    static void appendError(){
        stderr.add("Error");
        System.out.println("ERROR");
    }

    static void appendOutput(){
        stdout.add("Output");
    }

    static ArrayList<String> readFile(String path){
        ArrayList<String> code=new ArrayList<>();
        File f = new File("./src/grammerAnalysis/input.txt");
        Scanner in = null;


        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (in.hasNext()){
            String temp=in.next();
            code.add(temp);
        }
        code.add("$");
        return code;
    }

    public static void main(String[] args) {
        //初始化工作，生成table，mapping，栈以及读取代码
        Table table=new Table();
        Mapping mapping=new Mapping();
        Stack<String> stack=new Stack<>();
        ArrayList<String> code=readFile("./src/grammer/input.txt");

        System.out.println(code);
        //开始进行语法分
        /*
        非终结符列表
       参考NonTerminator文件

       终结符列表
       参考Terminator文件

       产生式列表
       参考Product_Rules文件
        */
        stack.add(mapping.getTerminal(21));//加入$符号
        stack.add(mapping.getNonTerminal(0));//加入program
        tree=new GrammerTree(mapping.getNonTerminal(0));//相应地去构造语法树

        String inputToken;
        String stackToken;
        int pInput=0;//输入读取指针
        int i,j,op;//i，j是表格的行和列的位置，op是表格单元中的数值
        while (!stack.empty()){

            inputToken=code.get(pInput);
            stackToken=stack.peek();

            System.out.printf("栈首：%s\t输入: %s\n",stackToken,inputToken);

            if(mapping.isTerminal(stackToken)){
                if(Utils.equal(stackToken,inputToken)){
                    //正确匹配
                    System.out.println("正确匹配");
                    pInput++;
                    stack.pop();
                    if(!stackToken.equals("$"))
                        tree.moveToNext();
                }
                else{
                    //出错，忽视inputToken
                    //输入串的终结符和栈中的终结符不相等
                    appendError();
                    System.out.println("栈中终结符与输入的终结符不符");
                    pInput++;

                    if(pInput==code.size()){
                        //输入串已无，但stack非空
                        appendError();
                        System.out.println("输入串已无，但stack非空");
                        break;
                    }
                }
                continue;
            }

            //栈首为非终结符，先将终结符和非终结符映射为行和列的位置，使用table进行判断要使用哪一条产生式
            i=mapping.mapping(stackToken);
            j=mapping.mapping(inputToken);

            op=table.getValue(i,j)-1;

            System.out.printf("栈顶为非终结符，此时对于行%d,列%d,对应的表格中的产生式编号%d\n",i,j,op);


            switch (op){
                case -1:
                    appendError();
                    System.out.println("没有对应的产生式，并且不是sync单元");
                    pInput++;
                    //出错，直接忽视inputToken

                    if(pInput==code.size()){
                        //输入串已无，但stack非空
                        appendError();
                        System.out.println("输入串已无，但stack非空");

                        break;
                    }
                    break;
                case 999:
                    stack.pop();
                    //出错，sync单元，忽视stackToken
                    appendError();
                    System.out.println("sync单元");
                    tree.moveToNext();
                    break;
                default:
                    Rule rule=mapping.getRule(op);

                    if(!rule.getHead().equals(stackToken)){
                        System.out.println("这说明我代码写错了");
                        System.out.println("rule 的 head："+rule.getHead()+"\t栈首："+stackToken);
                    }
                    else{
                        //使用产生式进行替换，更新语法树和栈
                        //栈顶出栈，content从后往前，元素进栈
                        ArrayList<String>content=rule.getContent();
                        System.out.println("使用下列产生式");
                        System.out.println(rule);

                        stack.pop();
                        if(content.size()!=0){
                            for(int index=content.size()-1;index>=0;index--){
                                stack.push(content.get(index));
                            }
                            for(int index=0;index<content.size();index++){
                                tree.appendSon(new GrammerTree.Node(content.get(index)));
                            }

                        }
                        tree.enterSon();
                        appendOutput();

                    }
                    //正确匹配
                    break;
            }
        }
        if(pInput!=code.size()){
            appendError();
            System.out.println("end....但是输入带中还有");
            //code中还有剩余的部分，出错

        }

        tree.traverse();
    }
}
