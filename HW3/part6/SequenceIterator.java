public class SequenceIterator{
	public Sequence ptr;

	public SequenceIterator(){
		ptr = null;
	}
	public SequenceIterator(Sequence s){
		ptr = s;
	}
	
	public void advance(){
		ptr = ptr.next;
	}
	
	public Element get(){
		return ptr.data;
	}
	
	public boolean equal (SequenceIterator other){
		if(this.ptr == other.ptr){
			return true;
		}
		return false;
	}
}