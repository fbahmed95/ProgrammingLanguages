// import Pair.java;
public class Map extends Sequence {

    public void add(Pair inval){
        /*The method add adds a Pair object, say p, in a Map object, 
        say mp, by comparing the key value of p with
        the key values of the Pair objects of mp, and inserts p such
        that the key values of the Pair objects are in
        ascending order. Note that a Map object may contain Pair objects
        with identical keys. In the case when
        there are multiple Pair objects with the same key, p is added 
        after these Pair objects.*/
        MapIterator iter; 
        iter = begin(); 
        MyChar otherKey = inval.key;
        Element otherData = inval.data;
        int pos = 0;
        
        if (length() == 0 ){
            super.add(inval, 0);
            return;
        } 
        
        while(!iter.equal(end())){
            MyChar currKey = iter.get().key;
            Element currData = iter.get().data;
            if(otherKey.Get() >= currKey.Get()){
                pos++;
            } 
            iter.advance();
        }
        super.add(inval, pos);
    }
        
    public MapIterator find(MyChar key){
        MapIterator iter;
        iter = begin();
        while(!iter.equal(end())){
             iter.advance();
             if(iter.get() != null){
                 Pair myPair = iter.get();  // gets Pair
                 MyChar currKey = myPair.key;   
                 if(key.Get() == currKey.Get()){
                     return iter;
                 }
             }
         }
         return iter;


    }
    
    public MapIterator begin(){
        //Method begin returns a SequenceIterator object that 
		//points to the first element of the sequence
		// 
		MapIterator iter = new MapIterator(); 
        iter.ptr = super.begin().ptr;
        return iter;

	}

	public MapIterator end(){

		MapIterator iter = new MapIterator(); 
		iter.ptr = super.end().ptr;
		return iter;
	}

}

