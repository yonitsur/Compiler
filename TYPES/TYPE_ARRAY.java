package TYPES;

public class TYPE_ARRAY extends TYPE{
    /***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE type;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_ARRAY(TYPE type,String name)
	{
		this.name = name;
		this.type = type;
	
	}
    
	public boolean isArray(){ return true;}
    
}
