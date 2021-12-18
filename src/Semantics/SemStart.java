package Semantics;

import Global.SymbolTable;
import Global.Variable;
import Global.WriteFile;
import grammer.*;
import lexical.Quad;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class SemStart {

    //读取Quad
    //构造树
    //错误处理
    static ArrayList<String> stderr=new ArrayList<>();

    static ArrayList<Quad> TokenList=new ArrayList<>();
    static GrammerTree tree;
    static String stackToken;

    public static void main(String[] args) {
        stderr.clear();
        TokenList.clear();

        WriteFile.clearFile("./src/Semantics/stderr.txt");
        WriteFile.clearFile("./src/Semantics/symbols.txt");
        WriteFile.clearFile("./src/grammer/output.txt");
        TokenList=readQuad();
        Quad q=new Quad();
        q.setToken("$");
        q.setValue("_");
        if(TokenList.size()>0){
            Quad last=TokenList.get(TokenList.size()-1);
            q.setLines(last.getLines());
            q.setOffset(last.getOffset());
        }
        else{
            q.setLines(1);
            q.setOffset(1);
        }
        TokenList.add(q);

        Table table=new Table();
        Mapping mapping=new Mapping();
        Stack<String> stack=new Stack<>();

        stack.add(mapping.getTerminal(21));//加入$符号
        stack.add(mapping.getNonTerminal(0));//加入program
        tree=new GrammerTree(mapping.getNonTerminal(0));

        Quad inputQuad;
        int pInput=0;
        int i,j,op;
        boolean error=false;
        while (!stack.empty()){
            if(pInput==TokenList.size()){
                error=true;
                break;
            }
            inputQuad=TokenList.get(pInput);
            stackToken=stack.peek();

            if(inputQuad.getToken().equalsIgnoreCase("ID")){
                //存入符号表中
                Variable v=new Variable();
                v.setName(inputQuad.getValue());
                SymbolTable.addVariable(v);
            }


//            System.out.printf("栈顶%s和输入%s\n",stackToken,inputQuad.getToken());
            if(mapping.isTerminal(stackToken)){
                if (stackToken.equalsIgnoreCase(inputQuad.getToken())) {
                    if(!inputQuad.getValue().equals("_")){
                        if(inputQuad.getToken().equalsIgnoreCase("ID")){

                            tree.addAttr("name",inputQuad.getValue());
                        }else if(inputQuad.getToken().equalsIgnoreCase("NUM")){

                            tree.addAttr("value",inputQuad.getValue());

                        }
                    }
//                    System.out.println(" 正确匹配");
                    pInput++;
                    stack.pop();
                    if(!stackToken.equals("$")){
                        tree.moveToNext();
                    }
                }else{
                    error=true;
                    appendError(0,inputQuad);
//                    System.out.println("栈中终结符与输入的终结符不匹配");
                    pInput++;
                    if(pInput==TokenList.size()){
                        error=true;
                        appendError(1,inputQuad);
//                        System.out.println("输入串已经空，但stack未空");
                        break;
                    }
                }
                continue;
            }
            i=mapping.mapping(stackToken);
            j=mapping.mapping(inputQuad.getToken());

            op=table.getValue(i,j)-1;

            switch (op){
                case -2:
                    error=true;
                    appendError(2,inputQuad);
                    pInput++;
                    if(pInput==TokenList.size()){
                        appendError(1,inputQuad);
                        break;
                    }
                    break;
                case 998:
                    stack.pop();
                    error=true;
                    appendError(3,inputQuad);
                    tree.moveToNext();
                    break;
                default:
                    Rule rule =mapping.getRule(op);
                    if(!rule.getHead().equals(stackToken)){
                        error=true;
                        appendError(4,inputQuad);
                    }
                    else{
                        //使用产生式
                        ArrayList<String> content=rule.getContent();
                        stack.pop();

                        if(content.size()!=0){
                            for(int index=content.size()-1;index>=0;index--){
                                stack.push(content.get(index));
                            }
                            for(int index=0;index<content.size();index++){
                                //设置节点的值，从quad里
                                tree.appendSon(new  GrammerTree.Node(content.get(index)));
                            }
                        }

                        tree.enterSon();
                    }

                    break;
            }

        }
        if(pInput!=TokenList.size()){
            error=true;
            appendError(5,null);
        }
        if(!error){
            tree.traverse();
            tree.calItself();
            SymbolTable.printAll();
        }
        else {

            WriteFile.writeFile("./src/Semantics/stderr.txt",stderr);
        }


    }

    public static ArrayList<Quad> readQuad(){
//        ArrayList<String> temp=new ArrayList<>();
        File f = new File("./src/lexical/stdout.txt");
        Scanner in = null;

        ArrayList<Quad>res=new ArrayList<>();

        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Quad quad=new Quad();
        assert in != null;


        while (in.hasNext()){
            String[] temp=in.nextLine().split("\t\t");

            quad.setToken(temp[0]);
            quad.setValue(temp[1]);
            quad.setLines(Integer.parseInt(temp[2]));
            quad.setOffset(Integer.parseInt(temp[3]));
            res.add(new Quad(quad));
        }

        return res;
    }

    static void appendError(int error_code,Quad quad){
        StringBuilder sb=new StringBuilder();
        sb.append("ERROR("+quad.getLines()+","+quad.getOffset()+"):");

        switch (error_code){
            case 0:
                sb.append("期待输入 \'"+stackToken+"\' ,得到的输入 \'"+quad.getToken()+"\' ");
                stderr.add(sb.toString());

            case 1:
                sb.append("输入串已无，但stack非空");
                stderr.add(sb.toString());
                break;
            case 3:
                sb.append("错误的输入 \'"+quad.getToken()+"\' ");
                stderr.add(sb.toString());
                break;
            case 2:
                sb.append("错误的输入字符 \'"+quad.getToken()+"\' ");
                stderr.add(sb.toString());
                break;
            case 4:
                sb.append("编译器代码书写有误");
                stderr.add(sb.toString());
                break;
            case 5:
                sb.append("栈空....但是输入带中还有");
                stderr.add(sb.toString());
                break;
        }
    }
}
