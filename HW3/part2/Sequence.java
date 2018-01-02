public class Sequence extends Element{
	Sequence next;
	Element data;

	public Sequence(Element data, Sequence next){
		this.data = data;
		this.next = next; 
	}

	public Sequence(){
		next = null;
		data = null;
	}

	public Element first(){
		return data;
	}

	public Sequence rest(){
		return next;
	}

	public int length(){
		int count = 0;
		Sequence iterator = next;
		while(iterator != null){
			count++;
			iterator = iterator.next; 
		}
		return count;
	}





	public void add(Element elm, int pos){
	    if(pos > length()){
	        System.err.println("POS: out of bound");
	        System.exit(0);
	    }
	    
	    if(pos == 0){
			//copy current head
			Sequence temp = new Sequence(this.data, this.next);
			this.data = elm;	//swap head element
			this.next = temp;	//change link to copied head
			
			return; 	//done
		}

	    Sequence newPtr = new Sequence();   // newPtr contains new elem
        Sequence ptr = this;        // current node 
        
        for (int i = 0; i < pos && ptr.next != null; i++) {
            newPtr = ptr;
            ptr = ptr.next;
        }
		newPtr.next = new Sequence(elm, ptr);
		
	
	}

	public void delete(int pos){
	    if(pos == 0){
			data = next.data;           // current data becomes the next data
			next = next.next;           // this next becomes next's next pointer
			return;
		}
		
		Sequence ptr = this;
		for(int i=0; ptr != null && i < pos-1; i++){
			ptr = ptr.next; 
		}
		Sequence nextPtr = ptr.next.next;
		ptr.next = nextPtr;

	}


	public void Print() {	
		System.out.print("[");
		Sequence ptr = this; 
		Element data = ptr.data;
		while(ptr != null && ptr.data != null){
			System.out.print(" "); 
			data.Print();
			ptr = ptr.next;
			data = ptr.data;
		}
		System.out.print(" ]");
	}
	
	public Element index(int pos){
		if(pos > this.length() || pos < 0){
			System.err.println("Can't access. Invalid position.");
			System.exit(0);
		}
		
		Sequence ptr = this;
		for(int i=0; i<pos; i++){
			ptr = ptr.next;
		}
		return ptr.data;
	}
	
}