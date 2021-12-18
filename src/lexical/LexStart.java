package lexical;

import Global.WriteFile;

import java.io.*;

public class LexStart {
    static int lines=1;
    static int offsets=1;



    public static void main(String[] args) {
        lines=1;
        offsets=1;
        WriteFile.clearFile("./src/lexical/stderr.txt");
        WriteFile.clearFile("./src/lexical/stdout.txt");
        boolean word_reading=false;
        try {
            BufferedReader in = new BufferedReader(new FileReader("./input.txt"));
            int ch;

            Parser parser=new Parser();

            //四元组（token，value，lines，offset）
            while ((ch = in.read()) != -1){
                if(ch==13){
                    //10和13是换行和回车的asc
                }else{
                    if(!Utils.isBlank(ch)&&!Utils.isNewLine(ch)){
                        //ch不是空格或制表符也不是换行
                        if(!word_reading){
                            word_reading=true;
                            parser.setPosition(lines,offsets);
                        }
                    }else{
                        if(Utils.isNewLine(ch)){
                            offsets=0;
                            lines++;
                        }
                        word_reading=false;
                    }
                    parser.move(ch);
                    offsets++;
                }
            }
            parser.move('\n');


            parser.STDOUT();
            System.out.println("-----------------------------");
            parser.STDERR();
            parser.printCode();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
