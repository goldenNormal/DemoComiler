package Global;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteFile {
    public static void clearFile(String path){
        File writename = new File(path); // 相对路径，如果没有则要建立一个新的output。txt文件
        BufferedWriter out;
        try {
            writename.createNewFile(); // 创建新文件
            out = new BufferedWriter(new FileWriter(writename));
            out.write("");
            out.flush(); // 把缓存区内容压入文件
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeFile(String path, ArrayList<String> content){
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

    public static void writeFileInLine(String path, ArrayList<String> content){
        File writename = new File(path); // 相对路径，如果没有则要建立一个新的output。txt文件
        BufferedWriter out;
        try {
            writename.createNewFile(); // 创建新文件
            out = new BufferedWriter(new FileWriter(writename));
            for(int i=0;i<content.size();i++){
                out.write(content.get(i)+" ");
            }
            out.flush(); // 把缓存区内容压入文件
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
