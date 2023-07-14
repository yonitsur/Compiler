package TYPES;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST params;
	public TYPE_CLASS owner;
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType,String name,TYPE_LIST params, TYPE_CLASS owner)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.owner = owner;
	}
	
	public boolean isFunction(){ return true;}
}
