package TYPES;
import java.util.*;

import AST.AST_CFIELD_LIST;
public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_CLASS_VAR_DEC_LIST data_members;
	public HashMap<String,Integer> vtable;
	public HashMap<String,Integer> field_map ;
	public AST_CFIELD_LIST cfields;
	//public HashMap<String, String> only_son_funcs = new HashMap<String, String>();
	public ArrayList<String> methods = new ArrayList<String>();
	public ArrayList<String> methods_classes = new ArrayList<String>();
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_CLASS_VAR_DEC_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
		this.vtable = new HashMap<String,Integer>();
		this.field_map = new HashMap<String,Integer>();
		// only_son_funcs = new HashMap<String, String>();
		methods = new ArrayList<String>();
		methods_classes = new ArrayList<String>();
		this.cfields = null;
	}
	public boolean isClass(){ return true;}
	public boolean isSubClassOf(String other_class_name){
		if (name.equals(other_class_name)) return true;
		if (father == null) return false;
		return father.isSubClassOf(other_class_name);
	}
}
