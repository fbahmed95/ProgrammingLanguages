// public class, subclass of Element class

public class MyInteger extends Element{
    
    int content;
    
    public MyInteger(){
        content = 0;
    }
    
    public int Get(){
        return content;
    }
    
    public void Set(int value){
        content = value;
    }
    
 
}