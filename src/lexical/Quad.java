package lexical;

public class Quad {
    String token;
    String value;
    Integer lines;
    Integer offset;


    public void setToken(String token) {
        this.token = token;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Quad(){
    }

    public Quad(Quad q){
        this.token=q.token;
        this.value=q.value;
        this.lines=q.lines;
        this.offset=q.offset;
    }

    public void setLines(Integer lines) {
        this.lines = lines;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

    public Integer getLines() {
        return lines;
    }

    public Integer getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return String.format("%s\t\t%s\t\t%s\t\t%s\t\t",token,value,lines,offset);

//        return String.format("%s%s%s%s",FormatString(20,token),FormatString(20,value),
//                FormatString(15,String.valueOf(lines)),FormatString(15,String.valueOf(offset)));
    }

    String FormatString(int len,String str){
        int n=str.length();
        StringBuilder sb=new StringBuilder(str);
        if(n<len){
            for(int i=0;i<len-n;i++){
                sb.append(" ");
            }
        }
        return sb.toString();
    }

}
