package grammer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Mapping {
    //对终结符，非终结符，以及产生式进行映射
    public ArrayList<String> Terminals=new ArrayList<>();
    public ArrayList<String> NonTerminals=new ArrayList<>();
    public Map<String,Integer> map=new HashMap<>();
    public ArrayList<Rule> rules=new ArrayList<>();
    public HashSet<String> Tsets=new HashSet<>();
    public boolean isTerminal(String ch){

        return Tsets.contains(ch);
    }

    public Mapping(){
        readRules();
        readTerminals();
        readNonTerminals();
    }

    public int mapping(String Symbol){
//        System.out.println(Symbol);
        return map.get(Symbol);
    }

    public String getTerminal(int i){
        return Terminals.get(i);
    }

    public String getNonTerminal(int i){
        return NonTerminals.get(i);
    }

    public Rule getRule(int i){
        return rules.get(i);
    }


    void readTerminals(){
        File f = new File("./src/grammer/Terminator");
        Scanner in = null;
        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true){
            assert in != null;
            String nex;
            if(in.hasNext())
                nex=in.next();
            else break;
            map.put(nex,Terminals.size());
            Terminals.add(nex);
            Tsets.add(nex);
        }
    }

    void readNonTerminals(){
        File f = new File("./src/grammer/NonTerminator");
        Scanner in = null;
        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true){
            assert in != null;
            String nex;
            if(in.hasNext())
                nex=in.next();
            else break;
            map.put(nex,NonTerminals.size());
            NonTerminals.add(nex);
        }
    }

    void readRules(){
        File f = new File("./src/grammer/Product_Rules");
        Scanner in = null;
        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert in!=null;
        while(in.hasNext()){
            ArrayList<String> temp=new ArrayList<>(Arrays.asList(in.nextLine().split(" ")));
            Rule rule=new Rule();
            rule.setHead(temp.get(0));
            ArrayList<String> content=new ArrayList<>();
            for(int i=2;i<temp.size();i++){
                if(!temp.get(i).equals("NULL"))
                content.add(temp.get(i));
            }
            rule.setContent(content);
            rules.add(rule);
        }
    }



}
