package TYPES;
import AST.*;
public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}
	public boolean isFunction(){ return false;}
	public boolean isList(){ return false;}
    public boolean isNil(){ return false;}

}
