class Matrix extends Sequence{
    public int row;
    public int col;
    
    public Matrix(int rowsize, int colsize){
        this.row = rowsize;
        this.col = colsize;
        for(int i=0; i < row * col; i++){
            //for(int j=0; j < colsize; j++){
                MyInteger integer = new MyInteger();
                this.add(integer, this.length());
            //}
        }
        
    }
    
    public void Set(int rowsize, int colsize, int value){
        Sequence ptr = this;
        int pos = (col * rowsize) + colsize; 
        SequenceIterator it = new SequenceIterator();
        for(int i = 0; i < pos; i++){
            ptr = ptr.next;
            //System.out.println(i);
        }
        ((MyInteger)ptr.data).Set(value);

    }

    
    public int Get(int rowsize, int colsize){
        Sequence ptr = this;
        int pos = (col * rowsize) + colsize;
        SequenceIterator it = new SequenceIterator();
        for(int i = 0; i < pos; i++){
            ptr = ptr.next;
        }
        return ((MyInteger)ptr.data).Get();
    }
    
    public Matrix Sum(Matrix mat){
        Matrix newMatrix = new Matrix(this.row, this.col);
        
        for(int i=0; i < row; i++){
            for(int j=0; j < col; j++){
                int newVal = this.Get(i, j) + mat.Get(i, j);
                newMatrix.Set(i, j, newVal);
            }
        }
        
        return newMatrix;
    }
    
    public Matrix Product(Matrix mat){
        if(this.col != mat.row ){//|| this.col != mat.col){
            System.out.println("Matrix dimensions incompatible for Product");
            System.exit(0);
        }
        Matrix newMatrix = new Matrix(this.row, mat.col);
        
        
        for(int i=0; i < this.row; i++){
            for(int j=0; j < mat.col; j++){
                int newVal = 0;
                for(int k=0; k < this.col; k++){
                newVal += this.Get(i, k) * mat.Get(k, j);
                }
            newMatrix.Set(i, j, newVal);
            }
        }
        
        return newMatrix;
    }
    
    public void Print(){
        for(int i=0; i < row; i++){
            System.out.print("[ ");
            for(int j=0; j < col; j++){
                System.out.print(this.Get(i, j) + " ");
            }
        System.out.println("]");
        }
    }
    
}