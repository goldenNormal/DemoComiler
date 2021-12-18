package Global;

import javafx.scene.control.Tab;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {

    static ArrayList<String> stdout=new ArrayList<>();

    public static HashMap<String,Variable> table=new HashMap<>();
    public static void addVariable(Variable v){
        if(table.containsKey(v.getName())){
            table.replace(v.getName(),v);
        }
        else{
            table.put(v.getName(),v);
        }
    }

    public static int queryVariable(String name){
        if(table.get(name)!=null)
            return table.get(name).getValue();
        modifyVariable(name,0);
        return 0;
    }

    public static void modifyVariable(String oldName,int value){
        delVariable(oldName);
        Variable v=new Variable();
        v.setName(oldName);
        v.setValue(value);
        addVariable(v);
    }

    public static void delVariable(String name){
        table.remove(name);
    }

    public static void printAll(){
        stdout.clear();
        for(String k:table.keySet()){
            StringBuilder sb=new StringBuilder();
            sb.append("变量"+k+":\t");
            sb.append(table.get(k).value);
            stdout.add(sb.toString());
        }
        WriteFile.writeFile("./src/Semantics/symbols.txt",stdout);
    }

//    static int capacity=1024;
//    public static int[] storage=new int[capacity];
//
//
//
//    public SymbolTable(){
//        for(int i=0;i<capacity;i++){
//            storage[i]=-1;
//        }
//    }
//
//    public static int getData(int offset){
//        return storage[offset];
//    }
//
//    public static Table mainTable=new Table();
//
//    static void addVariableSymbol(String name,int value){
//        VariableLineItem vli=new VariableLineItem();
//        vli.setName(name);
//        vli.setVariable_type("int");
//        for(int i=0;i<capacity;i++){
//
//        }
//    }
//
//    static int queryVariableSymbol(String symbol_name){
//        ArrayList<TableLineItem> content=mainTable.contents;
//        for(int i=0;i<content.size();i++){
//            VariableLineItem tli=(VariableLineItem) content.get(i);
//            if(tli.getName().equals(symbol_name)){
//                //
//                return tli.variable_addr;
//            }
//        }
//        return -1;
//
//    }
//
//    static class Table{
//        TableHead head;
//        ArrayList<TableLineItem> contents;
//
//        void addItem(TableLineItem tli){
//            contents.add(tli);
//        }
//
//        void setHead(TableHead head){
//            this.head=head;
//        }
//
//    }
//
//    static class TableLineItem{
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        String name;
//    }
//
//    static class VariableLineItem extends TableLineItem{
//
//        String variable_type="int";
//        int variable_addr;
//
//        public String getVariable_type() {
//            return variable_type;
//        }
//
//        public void setVariable_type(String variable_type) {
//            this.variable_type = variable_type;
//        }
//
//        public int getVariable_addr() {
//            return variable_addr;
//        }
//
//        public void setVariable_addr(int variable_addr) {
//            this.variable_addr = variable_addr;
//        }
//
//        VariableLineItem(){};
//    }
//
//    static class FunctionLineItem extends TableLineItem {
//
//        Table func_table;
//    }
//
//    static class TableHead{
//        Table parentTable;
//    }


}
