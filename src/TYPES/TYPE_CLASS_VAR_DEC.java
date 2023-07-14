package TYPES;

public class TYPE_CLASS_VAR_DEC extends TYPE
{
	public TYPE t;
	public String strval;
	public int intval;
	public TYPE_CLASS_VAR_DEC(TYPE t,String name){
		this.t = t;
		this.name = name;
		this.strval = null;

	}
	public TYPE_CLASS_VAR_DEC(TYPE t,String name, String strval, int intval)
	{
		this.t = t;
		this.name = name;
		this.strval = strval;
		this.intval = intval;
	}
	
}
