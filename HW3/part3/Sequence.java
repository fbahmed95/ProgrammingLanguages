public class Sequence extends Element{
	Sequence next;
	Element data;

public Sequence(){
		next = null;
		data = null;
	}
	
	public Sequence(Element data, Sequence next){
		this.data = data;
		this.next = next; 
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

	public Element index(int pos){
        Sequence ptr = this;        // current node 
		if(pos < 0 || pos > length()){
			System.err.println("POS: out of bound"); 
			System.exit(0); 
		} 
		else if (pos == 0){
			return first();
		} 
		
		for (int i=0; i < pos; i++){
			ptr = ptr.next; 
		}
		return ptr.data;
		
	}
	
	public Sequence flatten(){
		Sequence newSeq = new Sequence(); 
		flattenHelper(newSeq, this); 
		return newSeq;
	}
	
	public void flattenHelper(Sequence newSeq, Sequence sequenceToFlatten){
		while(sequenceToFlatten != null){
			if(sequenceToFlatten.data instanceof MyChar ||
			sequenceToFlatten.data instanceof MyInteger){
				newSeq.add(sequenceToFlatten.data, newSeq.length());
			} else {
				flattenHelper(newSeq, (Sequence)sequenceToFlatten.data);
			}
			sequenceToFlatten = sequenceToFlatten.next;
		}
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
	
	public Sequence copy(){
		Sequence copySeq = new Sequence(); 
		copyHelper(copySeq, this); 
		return copySeq;
	}

	public void copyHelper(Sequence copySeq, Sequence originalSequence){
		while(originalSequence != null){
			// don't really need these nested if/else statements..can combine.
			// todo: make cleaner
			if(originalSequence.data instanceof MyChar ||
			originalSequence.data instanceof MyInteger){
				MyChar newchar = new MyChar();
				MyInteger newInteger = new MyInteger(); 
				if(originalSequence.data instanceof MyChar){
					newchar.Set(((MyChar)originalSequence.data).Get());
					copySeq.add(newchar, copySeq.length());
				} else {
					newInteger.Set(((MyInteger)originalSequence.data).Get());
					copySeq.add(newInteger, copySeq.length());
				} 
			} 
			else {
				Sequence nestedSeq = new Sequence();
				copyHelper(nestedSeq, (Sequence)originalSequence.data);
				if (nestedSeq.data != null){
					copySeq.add(nestedSeq, copySeq.length());
				}
			}
			originalSequence = originalSequence.next;
		}
	}
}