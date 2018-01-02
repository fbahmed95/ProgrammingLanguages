/* *** This file is given as part of the programming assignment. *** */
import java.util.*; 
public class Parser {

    symbolTable table = new symbolTable();
    // tok is global to all these parsing methods;
    // scan just calls the scanner's scan method and saves the result in tok.
    private Token tok; // the current token
    private void scan() {
	    tok = scanner.scan();
    }

    private Scan scanner;
    Parser(Scan scanner) {
    	this.scanner = scanner;
    	scan();
    	program();
    	if( tok.kind != TK.EOF )
    	    parse_error("junk after logical end of program");
    }
    private void printC(String s){
        System.out.print(s);
    }
    
    private void program(){
        printC("#include <stdio.h>\n");
        printC("int main() {");
        block();
    }

    private void block(){
        table.increaseScope(); // ADD A NEW ARRAYLIS IN THE TABLE   
    	declaration_list();  
    	statement_list();
    	table.decreaseScope();  // delete the array list
    	printC("}");
    }

    private void declaration_list() {
    	// below checks whether tok is in first set of declaration.
    	// here, that's easy since there's only one token kind in the set.
    	// in other places, though, there might be more.
    	// so, you might want to write a general function to handle that.
    	while( is(TK.DECLARE) ) {
    	    declaration();
    	}
    }

    private void declaration() {
        boolean flag = false;  // when flag = true, don't print the comma 
    	mustbe(TK.DECLARE);
        int currScope = table.getLevel();  // current scope we are in
    	if(is(TK.ID)){
    	   if(!table.checkSymbol(tok.string)){   // symbol not in current scope
        	   printC("int x_" + currScope + tok.string);
        	   
    	   }
    	   else{    // symbol is in current scope
    	       flag = true; // don't print the comma for the next symbol 
    	   }
    	   
    	   table.addSymbol(tok.string);
    	   
    	}
    	String temp = tok.string;
    	mustbe(TK.ID);
    	
    	while( is(TK.COMMA) ) {
    	    scan();
    	    if(is(TK.ID)){
    	        if(!table.checkSymbol(tok.string)){  // if not in the table
    	            if(flag){ 
    	                printC(" int x_"+ currScope + tok.string); 
    	            }
    	            else{
    	                printC(", x_" + currScope + tok.string);
    	            }
    	        } else flag = true;
    	       table.addSymbol(tok.string);
    	    }
    	    mustbe(TK.ID);
    	}
	printC(";");
    }
    
    private void statement_list() {
        while( is(TK.TILDE) || is(TK.ID) || 
                is(TK.PRINT) || is(TK.DO) || is(TK.IF) || is(TK.FOR)){
                    statement();
                }
    }
    
    private void statement() {
        if(is(TK.TILDE) || is(TK.ID)){
            assignment();  // go to '='
        }
        else if(is(TK.PRINT)){
            print(); // go to '!'
        }
        else if(is(TK.DO)){
            doParser(); // go to 'do'
        }
        else if(is(TK.IF)){
            ifParser(); // go to 'IF'
        }
        else if(is(TK.FOR)){
            forParser(); // go to 'for'
        }
    } 
    
    
    private void assignment(){
        ref_id(); 
        mustbe(TK.ASSIGN); 
        printC(" = ");
        expr();
        printC(";");
    }
    
    
    private void ref_id(){
        int variableScope = 0;  // what number we will append to the symbol 
        int number = -1;
        if(is(TK.TILDE)){
            scan();
            if(is(TK.NUM)){
                String num = new String(tok.string);
                number = Integer.parseInt(num); // scope given with the tilde
                mustbe(TK.NUM);
            } 
            if(is(TK.ID)){
                if(number != -1){   // if we read the symbol ~NUMx
                    if(!table.checkScope(tok.string, number)){      // check the scope given
                        if(number==0){      // if we dont need to print num with tilde in the error
                            System.err.println("no such variable ~" + tok.string + " on line "
                                    + tok.lineNumber);
                            System.exit(1); 

                        }
                        else{   // if we DO need to print num with tilde in the error
                            System.err.println("no such variable ~" + number + tok.string + " on line "
                                + tok.lineNumber);
                                System.exit(1); 
                        }
        
                    }
                    // if number = 0, variableScope = table.getLevel();
                    // if number = 1, variableScope = table.getLevel() - 1; 
                    // in general, if number >= 0, variableScope = table.getLevel() - number;  
                    variableScope = table.getLevel() - number;
                }
                else{ // we read the symbol ~x, the variable is declared globally, so variableScope = 0 
                    if(!table.checkScope(tok.string, -1)) {
                        System.err.println("no such variable ~" + tok.string + " on line "
                                    + tok.lineNumber);
                                    System.exit(1); 
                    }
                    variableScope = 0;
                }
            } 
        }

        else if(is(TK.ID)){  // read a symbol without the tilde
            int scopeFound = table.checkTable(tok.string); 
            if(scopeFound == -1){
                    System.err.println(tok.string + " is an undeclared variable on line "
                        + tok.lineNumber);
                    System.exit(1); 
            }
            variableScope = scopeFound;         
        }
        printC("x_"+ variableScope + tok.string);
        mustbe(TK.ID);
    }
    
    private void expr(){
        term();
        while(is(TK.PLUS) || is(TK.MINUS)){
            addop();
            term();
        }
    }
    
    private void term(){
        factor();
        while(is(TK.TIMES) || is(TK.DIVIDE)){
            multop();
            factor();
        }
    }
    
    private void factor(){
        if(is(TK.LPAREN)){
            mustbe(TK.LPAREN);
           printC("(");
            expr(); 
            mustbe(TK.RPAREN);
            printC(")");
        }
        else if(is(TK.TILDE) || is(TK.ID)){
            ref_id();
        }
        else if(is(TK.NUM)){
            printC(tok.string);
            mustbe(TK.NUM);
        }
    }
    
    private void addop(){
        if(is(TK.PLUS)){
            mustbe(TK.PLUS);
            printC(" + ");
        } 
        else if(is(TK.MINUS)){
            mustbe(TK.MINUS);
            printC(" - ");
        }
    }
    
    private void multop(){
         if(is(TK.TIMES)){
            mustbe(TK.TIMES);
            printC(" * ");
        } 
        else if(is(TK.DIVIDE)){
            mustbe(TK.DIVIDE);
            printC(" / ");
        }      
    }
    
    private void print(){
        printC("printf(\"%d" + "\\" + "n\", " );
        mustbe(TK.PRINT);
        expr();
        printC(");");
    }
    
    private void doParser(){
        printC("while(");
        mustbe(TK.DO);

        guarded_command();
        mustbe(TK.ENDDO);
        //printC("}");
    }
    private void forParser(){
        printC("for(int i=0; i<");
        mustbe(TK.FOR);
        
        expr();
        printC("; i++){");
        
        block();
        mustbe(TK.ENDFOR);
        
    }
    
    private void guarded_command(){
        expr();
        printC("<=0){");
        mustbe(TK.THEN);
        block();
    }
    
    private void ifParser(){
        mustbe(TK.IF);
        printC("if(");
        guarded_command();
        while(is(TK.ELSEIF)){
            printC("else if(");
            scan();
            guarded_command();
        }
        
        if(is(TK.ELSE)){
            printC("else{");
            scan();
            block();
        }
        mustbe(TK.ENDIF);
    }

    // is current token what we want?
    private boolean is(TK tk) {
        return tk == tok.kind;
    }

    // ensure current token is tk and skip over it.
    private void mustbe(TK tk) {
	if( tok.kind != tk ) {
	    System.err.println( "mustbe: want " + tk + ", got " +
				    tok);
	    parse_error( "missing token (mustbe)" );
	}
	scan();
    }

    private void parse_error(String msg) {
	System.err.println( "can't parse: line "
			    + tok.lineNumber + " " + msg );
	System.exit(1);
    }
}
