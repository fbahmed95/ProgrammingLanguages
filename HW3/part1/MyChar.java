// public class, subclass of Element class

public class MyChar extends Element{
    char content;
    
    public MyChar(){
        content = '0';
    }
    
    public char Get(){
        return content;
    }
    
    public void Set(char value){
        content = value;
    }
    
}