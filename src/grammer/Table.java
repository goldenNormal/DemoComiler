package grammer;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Table {
    int rows=14;
    int cols=22;
    int[][] table=new int[rows][cols];

    public int getValue(int i,int j){
        return table[i][j];
    }


    public Table(){
        File f = new File("./src/grammer/table");
        Scanner in = null;
        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert in!=null;
        String id;
        int value;
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                id=in.next();
                if(id.equals("sync")){
                    value=999;
                }
                else{
                    value=Integer.parseInt(id);
                }
                table[i][j]=value;
            }
        }


    }

    public static void main(String[] args) {
        ArrayList<String> s=new ArrayList<>();
        System.out.println(s);
    }

}
