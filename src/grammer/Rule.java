package grammer;

import java.util.ArrayList;

public class Rule {
    String head;
    ArrayList<String> content=new ArrayList<>();



    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Rule:\t" +
                 head + " -> " +
                 content;
    }
}
