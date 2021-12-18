package grammer;

import Global.SymbolTable;
import Global.WriteFile;
import lexical.Quad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class GrammerTree {



    public Node root;
    public Stack<Node> nodes=new Stack<>();

    ArrayList<String> stdout=new ArrayList<>();

    public GrammerTree(String v){
        root=new Node(v);
        nodes.push(root);
    }

    public void appendSon(Node node){
        Node topNode=nodes.peek();
        topNode.addSon(node);
    }

    public void  moveToNext(){
        nodes.pop();
    }


    public boolean hasNextNode(){
        return nodes.peek()!=null;
    }

    public void enterSon(){
        if(nodes.peek().sons!=null){
            ArrayList<Node>sons=nodes.peek().sons;
            nodes.pop();
            for(int i=sons.size()-1;i>=0;i--){
                nodes.push(sons.get(i));
            }
        }
       else moveToNext();
    }

    public void addAttr(String key,String v){
        nodes.peek().addAttr(key,v);
    }

    public void calItself(){
        //需要和符号表互动，最后将符号表的内容输出到文件上，涉及到控制流跳转
        cal(root);
    }

    void cal(Node node){
        Node expr;
        Node expr1,expr2;
        Node id,num;
        Node boolop;
        String op;
        Node stmt1,stmt2,boolexpr;
        boolean boolres;
        int res;
        switch (node.getName()){
            case "assgstmt":
                //标识符节点，表达式节点
                id=node.getSon(0);
                expr=node.getSon(2);
                cal(expr);
                //id.getName()获取到的的是ID，  id.getAttr("name")才是真正的变量名
                System.out.println("对"+id.getAttr("name")+"进行了赋值"+expr.getAttr("value"));
                id.addAttr("value",expr.getAttr("value"));
                //对符号表进行修改
                SymbolTable.modifyVariable(id.getAttr("name"),Integer.parseInt(id.getAttr("value")));
//                System.out.println("对符号表中进行了修改");
                node.addAttr("value","0");//零表示正常，无问题
                break;
            case "boolop":
                boolop=node.getSon(0);
                node.addAttr("value",boolop.getName());
                break;
            case "boolexpr":{
                expr1=node.getSon(0);
                boolop=node.getSon(1);
                expr2=node.getSon(2);
                for(int i=0;i<3;i++){
                    cal(node.getSon(i));
                }
                op=boolop.getAttr("value");
                switch (op){
                    case ">":
                        boolres=Integer.parseInt(expr1.getAttr("value"))>Integer.parseInt(expr2.getAttr("value"));
                        break;
                    case "<":
                        boolres=Integer.parseInt(expr1.getAttr("value"))<Integer.parseInt(expr2.getAttr("value"));
                        break;
                    case "==":
                        boolres=Integer.parseInt(expr1.getAttr("value"))==Integer.parseInt(expr2.getAttr("value"));
                        break;
                    case ">=":
                        boolres=Integer.parseInt(expr1.getAttr("value"))>=Integer.parseInt(expr2.getAttr("value"));
                        break;
                    case "<=":
                        boolres=Integer.parseInt(expr1.getAttr("value"))<=Integer.parseInt(expr2.getAttr("value"));
                        break;
                    case "!=":
                        boolres=Integer.parseInt(expr1.getAttr("value"))!=Integer.parseInt(expr2.getAttr("value"));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + op);
                }
                node.addAttr("value",String.valueOf(boolres));
                break;
            }

            case "ifstmt":
                boolexpr=node.getSon(2);
                stmt1=node.getSon(5);
                stmt2=node.getSon(7);
                cal(boolexpr);
                if(Boolean.parseBoolean(boolexpr.getAttr("value"))){
                    //为真，执行stmt1
                    cal(stmt1);
                }
                else{
                    cal(stmt2);
                }
                node.addAttr("value","0");//零表示正常，无问题
                break;
            case "whilestmt":
                boolexpr=node.getSon(2);
                stmt1=node.getSon(4);
                cal(boolexpr);
                while (boolexpr.getAttr("value").equalsIgnoreCase("true")){
                    //为真，执行stmt1
//                    System.out.println("here");
                    cal(stmt1);
                    cal(boolexpr);
                }
                node.addAttr("value","0");//零表示正常，无问题
                break;
            case "simpleexpr":
                if(node.getSons().size()==3){
                    expr=node.getSon(1);
                    cal(expr);
                    node.addAttr("value",expr.getAttr("value"));
                }else{
                    if(node.getSon(0).getName().equalsIgnoreCase("ID")){
                        id=node.getSon(0);
                        System.out.println("查询到符号表中的"+id.getAttr("name")+"的变量值"+String.valueOf(SymbolTable.queryVariable(id.getAttr("name"))));
                        id.addAttr("value",String.valueOf(SymbolTable.queryVariable(id.getAttr("name"))));
                        node.addAttr("value",id.getAttr("value"));
                    }else if(node.getSon(0).getName().equalsIgnoreCase("NUM")){
                        num=node.getSon(0);
                        node.addAttr("value",num.getAttr("value"));
                    }
                }
                break;
            case "arithexpr":
                expr1=node.getSon(0);
                expr2=node.getSon(1);
                cal(expr1);
                cal(expr2);
                if(expr2.getSons()==null||expr2.getSons().size()==0){
                    node.addAttr("value",expr1.getAttr("value"));
                }
                else if(expr2.getSon(0).getName().equalsIgnoreCase("+")){
//                    System.out.println("做加法前"+node.getAttr("value"));
//                    System.out.println("expr1_value:\t"+expr1.getAttr("value"));
//                    System.out.println("expr2_value:\t"+expr2.getAttr("value"));
                    res=Integer.parseInt(expr1.getAttr("value"))+Integer.parseInt(expr2.getAttr("value"));
//                    System.out.println("res:"+res);
                    node.addAttr("value",String.valueOf(res));
//                    System.out.println("做加法后"+node.getAttr("value"));
                }else if(expr2.getSon(0).getName().equalsIgnoreCase("-")){
//                    System.out.println("做减法前"+node.getAttr("value"));
                    res=Integer.parseInt(expr1.getAttr("value"))-Integer.parseInt(expr2.getAttr("value"));
                    node.addAttr("value",String.valueOf(res));
//                    System.out.println("做减法后"+node.getAttr("value"));
                }
                break;
            case "arithexprprime":
                if(node.sons!=null&&node.getSons().size()==3){
                    expr1=node.getSon(1);
                    expr2=node.getSon(2);
                    cal(expr1);
                    cal(expr2);
                    if(expr2.getSons()==null||expr2.getSons().size()==0){
                        node.addAttr("value",expr1.getAttr("value"));
                    }
                    else if(expr2.getSon(0).getName().equalsIgnoreCase("+")){
                        res=Integer.parseInt(expr1.getAttr("value"))+Integer.parseInt(expr2.getAttr("value"));
                        node.addAttr("value",String.valueOf(res));
                    }else if(expr2.getSon(0).getName().equalsIgnoreCase("-")){
                        res=Integer.parseInt(expr1.getAttr("value"))-Integer.parseInt(expr2.getAttr("value"));
                        node.addAttr("value",String.valueOf(res));
                    }
                }
                break;
            case "multexpr":
                expr1=node.getSon(0);
                expr2=node.getSon(1);
                cal(expr1);
                cal(expr2);
                if(expr2.getSons()==null||expr2.getSons().size()==0){
                    node.addAttr("value",expr1.getAttr("value"));
                }
                else if(expr2.getSon(0).getName().equalsIgnoreCase("*")){
//                    System.out.println("做加法前"+node.getAttr("value"));
//                    System.out.println("expr1_value:\t"+expr1.getAttr("value"));
//                    System.out.println("expr2_value:\t"+expr2.getAttr("value"));
                    res=Integer.parseInt(expr1.getAttr("value"))*Integer.parseInt(expr2.getAttr("value"));
//                    System.out.println("res:"+res);
                    node.addAttr("value",String.valueOf(res));
//                    System.out.println("做加法后"+node.getAttr("value"));
                }else if(expr2.getSon(0).getName().equalsIgnoreCase("/")){
//                    System.out.println("做减法前"+node.getAttr("value"));
                    res=Integer.parseInt(expr1.getAttr("value"))/Integer.parseInt(expr2.getAttr("value"));
                    node.addAttr("value",String.valueOf(res));
//                    System.out.println("做减法后"+node.getAttr("value"));
                }
                break;
            case "multexprprime":
                if(node.sons!=null&&node.getSons().size()==3){
                    expr1=node.getSon(1);
                    expr2=node.getSon(2);
                    cal(expr1);
                    cal(expr2);
                    if(expr2.getSons()==null||expr2.getSons().size()==0){
                        node.addAttr("value",expr1.getAttr("value"));
                    }
                    else if(expr2.getSon(0).getName().equalsIgnoreCase("*")){
                        res=Integer.parseInt(expr1.getAttr("value"))*Integer.parseInt(expr2.getAttr("value"));
                        node.addAttr("value",String.valueOf(res));
                    }else if(expr2.getSon(0).getName().equalsIgnoreCase("/")){
                        res=Integer.parseInt(expr1.getAttr("value"))/Integer.parseInt(expr2.getAttr("value"));
                        node.addAttr("value",String.valueOf(res));
                    }
                }
                break;
            default:
                if(node.getSons()!=null&&node.getSons().size()!=0){
                    for(int i=0;i<node.getSons().size();i++){
                        cal(node.getSon(i));
                    }
                }
                break;
        }
    }

    public static class Node{
        String name;
        HashMap<String,String>attr=new HashMap<>();
        ArrayList<Node> sons=new ArrayList<>();

        public Node(String v){
            name=v;
            attr=new HashMap<>();
            sons=null;
        }


        public ArrayList<Node> getSons() {
            return sons;
        }

        public void addSon(Node node) {
            if(this.sons==null) {
                this.sons = new ArrayList<>();
            }
            sons.add(node);
        }



        public Node getSon(int i){

            return sons.get(i);
        }



        public String getName() {
            return name;
        }

        public void setName(String value) {
            this.name = value;
        }



        public void addAttr(String key,String value){
            attr.put(key,value);
        }

        public String getAttr(String k){
            return attr.get(k);
        }

    }

    public void traverse(){
        Node p=root;
        if (p!=null){
            stdout.add(p.getName());
            for(int i=0;i<p.sons.size();i++){
                traverse(p.sons.get(i),1);
            }
        }
        WriteFile.writeFile("./src/grammer/output.txt",stdout);

    }

    void traverse(Node node,int layer){
        Node p=node;
        StringBuilder sb=new StringBuilder();
        if(p!=null){
            sb.delete(0,sb.length());
            for(int i=0;i<layer;i++){
                sb.append("   ");
            }

            sb.append(p.getName());
            stdout.add(sb.toString());

            if(p.sons!=null){
                for(int i=0;i<p.sons.size();i++){
                    traverse(p.sons.get(i),layer+1);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Float.parseFloat("12E+1"));
    }


}
