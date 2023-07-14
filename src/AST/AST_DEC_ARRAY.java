package AST;
import TEMP.*;
import IR.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_ARRAY extends AST_DEC{
    
    public String type;
    public String name;

    public AST_DEC_ARRAY(String type,String name, int line_number)
   
	{
		super(line_number);

		System.out.format("===================== arrayDec -> %s:%s  \n",name,type);
       
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		this.name = name;
		this.type = type;
	}
    public void PrintMe()
	{
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		System.out.format("AST NODE DEC ARRAY\n%s\t Array:%s\n",name,type);
	
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST NODE DEC ARRAY\n%s\t Array:%s\n",name,type));
			
	}
    public TYPE SemantMe()
	{	
        TYPE arrayType = null;
        arrayType = SYMBOL_TABLE.getInstance().find(type);
        if (arrayType == null)
		{
			String errorString = String.format(">> ERROR [%d:%d] non existing type %s\n",46,76,arrayType);
			throwError(errorString, line_number);			
		}
        // check if arrayType is not void
        if (arrayType == TYPE_VOID.getInstance())
        {
            String errorString = String.format(">> ERROR [%d:%d] array type cannot be void\n",66,16);	
			throwError(errorString, line_number);			
        }

        if (SYMBOL_TABLE.getInstance().find(name) != null)
		{
			String errorString = String.format(">> ERROR [%d:%d] variable %s already exists in scope\n",42,20,name);
			throwError(errorString, line_number);			
		}


        // if type is not "string" or "int" it must be a class or array
        if (!(type.equals("string") || type.equals("int"))){
            if (!arrayType.isClass() && !arrayType.isArray()){
                String errorString = String.format(">> ERROR [%d:%d] array type must be class or array if not class\n",7,17);
                throwError(errorString, line_number);
            }
        }


        TYPE_ARRAY ret_type = new TYPE_ARRAY(arrayType,name);
        SYMBOL_TABLE.getInstance().enter(name, ret_type);
		return ret_type;	
	}

	// public TEMP IRme()
	// {
	// 	TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
	// 	if (this.e == null){
	// 		//TODO: New Object...
	// 	} else {
	// 		TEMP size = this.e.IRme();
	// 		IR.
	// 		getInstance().
	// 		Add_IRcommand(new IRcommand_NewArray(dst, size));
	// 	}
	// 	return dst;
	// }

}
