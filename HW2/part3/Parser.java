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
        //printC("#include <stdio.h>\n");
        //printC("int main() {");
        block();
    }

    private void block(){
        table.increaseScope();
    	declaration_list();
    	statement_list();
    	table.decreaseScope();
    	//printC("}");
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
    	mustbe(TK.DECLARE);
    	//printC("int ");
    	if(is(TK.ID)){
    	  //printC("x_" + tok.string );
    	   table.addSymbol(tok.string);
    	}
    	mustbe(TK.ID);
    	
    	while( is(TK.COMMA) ) {
    	    scan();
    	    if(is(TK.ID)){
    	        //printC(", " + tok.string);
    	        
    	        table.addSymbol(tok.string);
    	    }
    	    mustbe(TK.ID);
    	}
    //	printC(";");
    }
    
    private void statement_list() {
        while( is(TK.TILDE) || is(TK.ID) || 
                is(TK.PRINT) || is(TK.DO) || is(TK.IF)){
                    statement();
                }
    }
    
    private void statement() {
        if(is(TK.TILDE) || is(TK.ID)){
            assignment();
        }
        else if(is(TK.PRINT)){
            print(); 
        }
        else if(is(TK.DO)){
            doParser();
        }
        else if(is(TK.IF)){
            ifParser();
        }
    } 
    
    
    private void assignment(){
        ref_id(); 
        mustbe(TK.ASSIGN); 
        //printC(" = ");
        expr();
    }
    
    
    private void ref_id(){
        int number = -1;
        if(is(TK.TILDE)){
            scan();
            if(is(TK.NUM)){
                String num = new String(tok.string);
                number = Integer.parseInt(num);
                
                mustbe(TK.NUM);

            } 
            if(is(TK.ID)){
                if(number != -1){   // if we read the number ~1x
                    if(!table.checkScope(tok.string, number)){
                        if(number==0){
                            System.err.println("no such variable ~" + tok.string + " on line "
                                    + tok.lineNumber);
                            System.exit(1); 
                        }
                        else{
                            System.err.println("no such variable ~" + number + tok.string + " on line "
                                + tok.lineNumber);
                                System.exit(1); 
                        }
        
                    }
                }
                else{ // if 
                    if(!table.checkScope(tok.string, -1)) {
                        System.err.println("no such variable ~" + tok.string + " on line "
                                    + tok.lineNumber);
                                    System.exit(1); 
                
                        
                    }
                }
            } 
        }

        else if(is(TK.ID)){
            if(!table.checkTable(tok.string)){
                    System.err.println(tok.string + " is an undeclared variable on line "
                        + tok.lineNumber);
                    System.exit(1); 
            }
        }
       // printC("x_" + tok.string);
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
           // printC("(");
            expr(); 
            mustbe(TK.RPAREN);
        //    printC(")");
        }
        else if(is(TK.TILDE) || is(TK.ID)){
            ref_id();
        }
        else if(is(TK.NUM)){
          //  printC(tok.string + ";");
            mustbe(TK.NUM);
        }
    }
    
    private void addop(){
        if(is(TK.PLUS)){
            mustbe(TK.PLUS);
        } 
        else if(is(TK.MINUS)){
            mustbe(TK.MINUS);
        }
    }
    
    private void multop(){
         if(is(TK.TIMES)){
            mustbe(TK.TIMES);
        } 
        else if(is(TK.DIVIDE)){
            mustbe(TK.DIVIDE);
        }      
    }
    
    private void print(){
        mustbe(TK.PRINT);
        expr();
    }
    
    private void doParser(){
        mustbe(TK.DO);
        guarded_command();
        mustbe(TK.ENDDO);
    }
    
    private void guarded_command(){
        expr();
        mustbe(TK.THEN);
        block();
    }
    
    private void ifParser(){
        mustbe(TK.IF);
        guarded_command();
        while(is(TK.ELSEIF)){
            scan();
            guarded_command();
        }
        
        if(is(TK.ELSE)){
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
