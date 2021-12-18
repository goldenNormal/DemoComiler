package GUI;

import Semantics.SemStart;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import lexical.LexStart;
import lexical.Quad;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.SplittableRandom;

public class FXMLController {
    @FXML private Text actiontarget;
    @FXML public TextArea inputArea;
    @FXML public TextArea outputArea;

    Scanner readFile(String path){
        File f = new File(path);
        Scanner in = null;
        try {
            in = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return in;
    }

    public FXMLController() throws FileNotFoundException {
    }

    @FXML protected void handleLexicalButtonAction(ActionEvent event) throws IOException {
        File f=new File("input.txt");
        FileOutputStream fos1=new FileOutputStream(f);
        OutputStreamWriter dos1=new OutputStreamWriter(fos1);
        dos1.write(inputArea.getText());
        dos1.close();

        System.out.println("词法分析");
        LexStart.main(null);
        //词法分析结束
        ArrayList<Quad> quads= SemStart.readQuad();
        StringBuilder sb=new StringBuilder();
        //写入错误流
        Scanner in=readFile("./src/lexical/stderr.txt");
        while (in.hasNext()){
            sb.append(in.nextLine()).append("\n\n");
        }
        for(int i=0;i<3;i++){
            sb.append("--------------");
        }
        //写入输出流
        sb.append("\n\n");
        for(int i=0;i<quads.size();i++){
            Quad q=quads.get(i);
            String str="(  "+q.getToken()+"  ,"+q.getValue()+",  "+q.getLines()+","+q.getOffset()+")";
            sb.append(str);
            sb.append("\n\n");
        }

        outputArea.setText(sb.toString());

    }
    @FXML protected void handleParseButtonAction(ActionEvent event) {
        System.out.println("语法分析");
        SemStart.main(null);
        StringBuilder sb=new StringBuilder();
        sb.append("完成！\n\n");
        Scanner in=readFile("./src/Semantics/stderr.txt");

        for(int i=0;i<3;i++){
            sb.append("--------------");
        }
        sb.append("\n\n");
        while (in.hasNext()){
            sb.append(in.nextLine()).append("\n\n");
        }

        outputArea.setText(sb.toString());

    }
    @FXML protected void handleGrammerTreeButtonAction(ActionEvent event) {
        System.out.println("语法树构建");
        Scanner in=readFile("./src/grammer/output.txt");
        StringBuilder sb=new StringBuilder();
        while (in.hasNext()){
            sb.append(in.nextLine()).append("\n");
        }
        outputArea.setText(sb.toString());
    }
    @FXML protected void handleCodeGenarateButtonAction(ActionEvent event) {
        System.out.println("打印符号表");
        Scanner in=readFile("./src/Semantics/symbols.txt");
        StringBuilder sb=new StringBuilder();
        while (in.hasNext()){
            sb.append(in.nextLine()).append("\n\n");
        }
        outputArea.setText(sb.toString());
    }
    @FXML protected void handleUploadFileButtonAction(ActionEvent event) {
        System.out.println("浏览文件");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
//        fileChooser.showOpenDialog(primaryStage);
    }

}
