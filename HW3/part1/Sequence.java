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

}