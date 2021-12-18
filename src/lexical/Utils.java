package lexical;

public class Utils {
    public static boolean isDigital(int ch){
        return Character.isDigit(ch);
    }
    public static boolean isLetter(int ch){
        return Character.isLetter(ch);
    }
    public static boolean isDiv(int ch){
        return ch=='/';
    }
    public static boolean isEqual(int ch){
        return ch=='=';
    }

    public static boolean isAddMinus(int ch){

        return ch=='+'||ch=='-';
    }

    public static boolean isMutiply(int ch){
        return ch=='*';
    }

    public static boolean isExclam(int ch){
        return ch=='!';
    }

    public static boolean isDelimit(int ch){
        return ch=='{'||ch=='}'||ch=='('||ch==')'||ch==';'||ch==',';
    }

    public static boolean isBlank(int ch){
        return Character.isWhitespace(ch);
    }

    public static boolean isCompare(int ch){
        return ch=='>'||ch=='<';
    }

    public static boolean isNewLine(int ch){

        return ch=='\n';
    }

    public static boolean isScienceE(int ch){
        return ch=='E'||ch=='e';
    }

    public static boolean isNumPoint(int ch){
        return ch=='.';
    }

    public static void main(String[] args) {
        String str="p;0";
        System.out.println(str.split(";")[1]);
    }
}
