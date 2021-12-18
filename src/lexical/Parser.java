package lexical;

import java.io.*;
import java.util.*;

public class Parser {
    int state;
    Quad quad=new Quad();
    StringBuffer buffer=new StringBuffer();
    HashSet<String> keyPatterns=new HashSet<>();
    ArrayList<Quad> quads=new ArrayList<>();


    ArrayList<String> stderr=new ArrayList<>();
    ArrayList<String> stdout=new ArrayList<>();
    int col=14;
    int row=16;
    String[][] dfa=new String[row][col];

    Parser(){
        state=0;
        keyPatterns.add("int");
        keyPatterns.add("real");
        keyPatterns.add("if");
        keyPatterns.add("then");
        keyPatterns.add("else");
        keyPatterns.add("while");
        keyPatterns.add("INT");
        keyPatterns.add("REAL");
        keyPatterns.add("IF");
        keyPatterns.add("THEN");
        keyPatterns.add("ELSE");
        keyPatterns.add("WHILE");

        File f = new File("./src/lexical/DFA");
        Scanner in = null;
        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(int i=0;i<row;i++){
            for (int j=0;j<col;j++){
                String str=in.next();
                dfa[i][j]=str;
            }
        }
    }

    public void setPosition(int line,int offset){
        quad.setLines(line);
        quad.setOffset(offset);
        buffer.delete(0,buffer.length());
    }

    int mapTerminal(int ch){
        if(Utils.isDiv(ch))return 0;
        if(Utils.isScienceE(ch))return 10;
        if(Utils.isLetter(ch))return 1;
        if(Utils.isDigital(ch))return 2;
        if(Utils.isAddMinus(ch))return 3;
        if(Utils.isCompare(ch))return 4;
        if(Utils.isExclam(ch))return 5;
        if(Utils.isEqual(ch))return 6;
        if(Utils.isDelimit(ch))return 7;
        if(Utils.isNewLine(ch))return 9;
        if(Utils.isBlank(ch))return 8;

        if(Utils.isNumPoint(ch))return 11;
        if(Utils.isMutiply(ch))return 12;
        return 13;
    }

    void  move(int ch){
//        if(!Utils.isNewLine(ch)&&!Utils.isBlank(ch))
//        System.out.println("当前读入的字符是"+(char)ch);
//        else if(Utils.isNewLine(ch))
//            System.out.println("当前读入的是新行");;
        int old_state=state;
        String action=dfa[state][mapTerminal(ch)];
        if(action.startsWith("p")||action.startsWith("P")){
            printQuad();
            state=Integer.parseInt(action.split(";")[1]);
        }else if(action.startsWith("e")||action.startsWith("E")){
            error();
            state=Integer.parseInt(action.split(";")[1]);
        }
        else{
            state=Integer.parseInt(action);
        }
        if(old_state==0&&state!=0){
            //新的token
            setPosition(LexStart.lines, LexStart.offsets);
        }
        if(!Utils.isBlank(ch)&&!Utils.isNewLine(ch)){
            buffer.append((char)ch);
        }


//          if(Utils.isDiv(ch)){
//              switch (state){
//                  case 0:
//                      state=1;
//                      break;
//                  case 1: case 2:
//                      state=2;
//                      break;
//                  case 3: case 4: case 5: case 6:
//                  case 7: case 8: case 9:
//                  case 10: case 13:
//                      printQuad();
//                      state=1;
//                      break;
//                  case 11: case 12: case 14: case 15:
//                      error();
//                      break;
//              }
//          }
//          else if(Utils.isLetter(ch)){
//              if(Utils.isScienceE(ch)&&(state==9||state==10)){
//                    state=11;
//              }else{
//                  switch (state){
//                      case 0: case 3:
//                          state=3;break;
//                      case 2:
//                          state=2;break;
//                      case 1: case 5: case 6: case 7: case 8: case 13:
//                          printQuad();
//                          state=3;break;
//                      case 4:
//                          state=4;break;
//                      case 10: case 11: case 12: case 14: case 15:
//                          state=14;break;
//                      case 9:
//                          printQuad();
//                          state=4;
//                  }
//              }
//          }
//          else if(Utils.isDigital(ch)){
//              switch (state){
//                  case 0: case 9:
//                      state=9;break;
//                  case 1: case 5: case 6: case 7: case 8: case 13:
//                      printQuad();
//                      state=9;break;
//                  case 3: case 4:
//                      state=4;break;
//                  case 10: case 15:
//                      state=10;break;
//                  case 11: case 12:
//                      state=12;break;
//              }
//          }
//          else if(Utils.isUnaryOp(ch)){
//              switch (state){
//                  case 0:
//                      state=5;break;
//                  case 1: case 3: case 4: case 5: case 6: case 7:
//                  case 8: case 9: case 10:
//                      printQuad();
//                      state=5;break;
//                  case 12: case 14: case 15:
//                      state=14;break;
//                  case 11:
//                      if(ch=='+'||ch=='-'){
//                          state=12;
//                      }else{
//                          state=14;
//                      }
//                          break;
//              }
//        }
//          else if(Utils.isCompare(ch)){
//              switch (state){
//                  case 0:
//                      state=6;
//                      break;
//                  case 11: case 12: case 15:
//                      state=14;break;
//                  case 1: case 13: case 3: case 4:
//                  case 5: case 6: case 7: case 8: case 9:
//                  case 10:
//                      printQuad();state=6;break;
//              }
//          }
//          else if(Utils.isExclam(ch)){
//              switch (state){
//                  case 0:state=7;break;
//                  case 11: case 12: case 15:
//                      state=14;break;
//                  case 1: case 3: case 4:
//                  case 5: case 6:
//                  case 7: case 8: case 9: case 10:
//                  case 13:
//
//              }
//          }




//        switch (state) {
//            case 0:
//                if (Utils.isDiv(ch)) {
//                    state = 1;
//                } else if (Utils.isLetter(ch)) {
//                    state = 3;
//                } else if (Utils.isDigital(ch)) {
//                    state = 9;
//                } else if (Utils.isUnaryOp(ch)) {
//                    state = 5;
//                } else if (Utils.isCompare(ch) || Utils.isEqual(ch)) {
//                    state = 6;
//                } else if (Utils.isExclam(ch)) {
//                    state = 7;
//                } else if (Utils.isDelimit(ch)) {
//                    state = 13;
//                } else if (Utils.isBlank(ch) || Utils.isNewLine(ch)) {
//                    state = 0;
//                } else if (Utils.isNumPoint(ch)) {
//                    state = 14;
//                } else {
//                    state = 14;
//                }
//
//                break;
//            case 1:
//                if (Utils.isDiv(ch)) {
//                    state = 2;
//                } else if (Utils.isLetter(ch)) {
//                    printQuad();
//                    state = 3;
//                } else if (Utils.isDigital(ch)) {
//                    printQuad();
//                    state = 9;
//                } else if (Utils.isUnaryOp(ch)) {
//                    printQuad();
//                    state = 5;
//                } else if (Utils.isCompare(ch) || Utils.isEqual(ch)) {
//                    printQuad();
//                    state = 6;
//                } else if (Utils.isExclam(ch)) {
//                    printQuad();
//                    state = 7;
//                } else if (Utils.isDelimit(ch)) {
//                    printQuad();
//                    state = 13;
//                } else if (Utils.isBlank(ch) || Utils.isNewLine(ch)) {
//                    printQuad();
//                    state = 0;
//                } else if (Utils.isNumPoint(ch)) {
//                    printQuad();
//                    state = 14;
//                } else {
//                    state = 14;
//
//                }
//                break;
//            case 2:
//                if (Utils.isNewLine(ch)){
//                    state=0;
//                } else {
//                    state = 2;
//                }
//                break;
//            case 3:
//                if (Utils.isDiv(ch)) {
//                    printQuad();
//                    state = 1;
//                } else if (Utils.isLetter(ch)) {
//
//                    state = 3;
//                } else if (Utils.isDigital(ch)) {
//
//                    state = 4;
//                } else if (Utils.isUnaryOp(ch)) {
//                    printQuad();
//                    state = 5;
//                } else if (Utils.isCompare(ch) || Utils.isEqual(ch)) {
//                    printQuad();
//                    state = 6;
//                } else if (Utils.isExclam(ch)) {
//                    printQuad();
//                    state = 7;
//                } else if (Utils.isDelimit(ch)) {
//                    printQuad();
//                    state = 13;
//                } else if (Utils.isBlank(ch) || Utils.isNewLine(ch)) {
//                    printQuad();
//                    state = 0;
//                } else if (Utils.isNumPoint(ch)) {
//                    state = 14;
//                } else {
//                    state = 14;
//                }
//                break;
//            case 4:
//                if(Utils.isDiv(ch)){
//                    printQuad();
//                    state=1;
//                }else if(Utils.isLetter(ch)||Utils.isDigital(ch)){
//                    state=4;
//                }else if(Utils.isUnaryOp(ch)){
//                    printQuad();
//                    state=5;
//                }else if(Utils.isCompare(ch)||Utils.isEqual(ch)){
//                    printQuad();
//                    state=6;
//                }else if(Utils.isExclam(ch)){
//                    printQuad();
//                    state=7;
//                }else if(Utils.isDelimit(ch)){
//                    printQuad();
//                    state=13;
//                }else if(Utils.isBlank(ch)||Utils.isNewLine(ch)){
//                    printQuad();
//                    state=0;
//                }else {
//                    state=14;
//                }
//                break;
//            case 5:
//                if(Utils.isDiv(ch)){
//                    printQuad();
//                    state=1;
//                }else if(Utils.isLetter(ch)){
//                    printQuad();
//                    state=3;
//                }else if(Utils.isDigital(ch)){
//                    printQuad();
//                    state=9;
//                }else if(Utils.isUnaryOp(ch)){
//                    printQuad();
//                    state=5;
//                }else if(Utils.isCompare(ch)){
//                    printQuad();
//                    state=6;
//                }else if(Utils.isExclam(ch)){
//                    printQuad();
//                    state=7;
//                }else if(Utils.isEx)
//                break;
//            case 6:
//                break;
//            case 7:
//                break;
//            case 8:
//                break;
//            case 9:
//                break;
//            case 10:
//                break;
//            case 11:
//                break;
//            case 12:
//                break;
//            case 13:
//                break;
//            case 14:
//                break;
//            case 15:
//                break;
//
//        }
    }

    void STDOUT(){
        writeFile("./src/lexical/stdout.txt",stdout);
    }

    void printCode(){
        ArrayList<String> code=new ArrayList<>();
        for(int i=0;i<quads.size();i++){
            code.add(quads.get(i).token);
        }
        Global.WriteFile.writeFileInLine("./src/lexical/code.txt",code);

    }

    void writeFile(String path,ArrayList<String> content){
        File writename = new File(path); // 相对路径，如果没有则要建立一个新的output。txt文件
        BufferedWriter out;
        try {
            writename.createNewFile(); // 创建新文件
            out = new BufferedWriter(new FileWriter(writename));
            for(int i=0;i<content.size();i++){
                out.write(content.get(i)+"\n");
            }
            out.flush(); // 把缓存区内容压入文件
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void STDERR(){
//        for(int i=0;i<stderr.size();i++){
//            System.out.println(stderr.get(i));
//        }
        writeFile("./src/lexical/stderr.txt",stderr);
    }

    void error(){
        stderr.add("ERROR"+LineInfo()+":"+buffer.toString());
        buffer.delete(0,buffer.length());
    }

    void printQuad(){
        switch (state){
            case 1:
                quad.setToken("/");
                quad.setValue("_");
                break;
            case 4:
                quad.setToken("ID");
                quad.setValue(buffer.toString());
                break;

            case 5: case 6: case 7: case 8:case 13:
                quad.setToken(buffer.toString());
                quad.setValue("_");
                break;
            case 9:case 10:case 11:case 12:
                quad.setToken("NUM");
                quad.setValue(buffer.toString());
                break;
            case 3:
                if(keyPatterns.contains(buffer.toString())){
                    //是关键词
                    quad.setToken(buffer.toString());
                    quad.setValue("_");
                }
                else{
                    quad.setToken("ID");
                    quad.setValue(buffer.toString());
                }

                break;
        }
        stdout.add(quad.toString());
        quads.add(new Quad(quad));
        buffer.delete(0,buffer.length());
        setPosition(LexStart.lines, LexStart.offsets);
    }

    String LineInfo(){
        return "("+quad.lines+","+quad.offset+")";
    }

    public static void main(String[] args) {
        StringBuilder buffer=new StringBuilder();
        buffer.append("23");
        buffer.delete(0,buffer.length());
        System.out.println(buffer);
    }


}
