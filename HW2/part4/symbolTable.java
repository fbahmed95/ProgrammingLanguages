/* *** This file is a separate class to handle the symbol table. *** */
import java.util.*; 

/* Handles the following:
** check symbol --> check if the symbol is already in the table
** add symbol --> calls on check symbol, adds if check symbol returns false
** increase scope --> for each new block, add a new arrayList to the table
** decrease scope --> delete from table
*/
public class symbolTable {
    
  private ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();;

  // check for symbol in current scope level
  public boolean checkSymbol(String symbol) {
  int length = table.size();
  ArrayList<String> currBlock = table.get(length-1);
  if(currBlock.contains(symbol)){ 
      return true;
    } else return false;
}

  // check the whole table 
  public int checkTable(String symbol) {
      int length = table.size();
      for(int x = length -1; x >= 0; x--) {
        ArrayList<String> currBlock = table.get(x);
       if(currBlock.contains(symbol)) {
          return x;
        }
      }

    return -1;

  }
  

    // WITH SCOPING HANDLER
  public boolean checkScope(String symbol, int scope) {
    if (scope >= table.size()){
      return false;
    }
    if(scope == -1){
      ArrayList<String> currBlock = table.get(0);
      if(currBlock.contains(symbol)) {
        return true;
      }
    }
    else{
      ArrayList<String> currBlock = table.get(table.size() - scope - 1);
      if(currBlock.contains(symbol)) {
        return true;
      }
    }
    return false;    
  }

  public void addSymbol(String symbol) {
    // check if symbol has already been defined previously
    if(checkSymbol(symbol)) {
      System.err.println("redeclaration of variable " + symbol);
    }

    // add symbol to current scope
    int length = table.size();
    ArrayList<String> currBlock = table.get(length-1);
    currBlock.add(symbol);
  }


  public void increaseScope() {
    //add a new block
    ArrayList<String> newBlock = new ArrayList<String>();
    table.add(newBlock);
  }

  public void decreaseScope() {
    // remove the last element
    table.remove(table.size()-1); 
  }

  public int getLevel(){
    return table.size() -1; 
  }
}
