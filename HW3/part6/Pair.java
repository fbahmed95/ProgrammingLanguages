public class Pair extends Element {
    public MyChar key; 
    public Element data; 
    
    public Pair(){
        key = null;
        data = null;
    }

    public Pair(MyChar key, Element data){
        this.key = key;
        this.data = data;
    }
    
    public void Print(){
        System.out.print("(");
        key.Print();
        System.out.print(" ");
        data.Print(); 
        System.out.print(")");
    }
    
    
}