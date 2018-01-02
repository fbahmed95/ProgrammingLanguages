 class MapIterator extends SequenceIterator{

// 	public SequenceIterator(){
// 		ptr = null;
// 	}
// 	public SequenceIterator(Sequence s){
// 		ptr = s;
// 	}
	
// 	public void advance(){
// 		ptr = ptr.next;
// 	}
	
	public Pair get(){
		return (Pair)ptr.data;
	}
	
// 	public boolean equal (MapIterator other){
// 		if(this.ptr == other.ptr){
// 			return true;
// 		}
// 		return false;
// 	}
}