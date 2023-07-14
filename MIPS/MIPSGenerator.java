/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class MIPSGenerator
{
	private int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.print("li $v0,10\n");
		fileWriter.print("syscall\n");
		fileWriter.close();
	}
	public void field_access(TEMP dst, int offset, TEMP varAddress)
	{
		int varAddressidx =varAddress.getSerialNumber();
		fileWriter.format("\tbeq Temp_%d,$zero,invalid_ptr_dref\n",varAddressidx);
		load(dst, offset, varAddress);
	}

	public void field_set(TEMP class_address,int offset, TEMP src)
	{
		int class_addressidx =class_address.getSerialNumber();
		fileWriter.format("\tbeq Temp_%d,$zero,invalid_ptr_dref\n",class_addressidx);
		if (src == null)
		{
			fileWriter.format("\tsw $zero,%d(Temp_%d)\n",offset,class_addressidx);
		}
		else{
			int srcidx =src.getSerialNumber();
			fileWriter.format("\tsw Temp_%d,%d(Temp_%d)\n",srcidx,offset,class_addressidx);
		}
		
	}
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}

	public void print_string(TEMP t)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,4\n");
		fileWriter.format("\tsyscall\n");
	}

	// set return value in $v0
	public void return_value(TEMP t, String epilogueLabel)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tmove $v0,Temp_%d\n",idx);
		fileWriter.format("\tj %s\n",epilogueLabel);
	}

	// set return value in $v0 to be null
	public void return_null(String epilogueLabel)
	{
		fileWriter.format("\tmove $v0,$zero\n");
		fileWriter.format("\tj %s\n",epilogueLabel);
	}
	
	public void vtable_entry(String class_name,String method_name)
	{
		fileWriter.format("\t.word f_%s_%s\n",class_name,method_name);
	}
	public void allocate_int(String var_name, int val)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word %d\n",var_name, val);
		fileWriter.format(".text\n");
	}
	public void allocate_string(String var_name, String val)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .asciiz %s\n",var_name,val);
		fileWriter.format(".text\n");
	}
	public void data(){
		fileWriter.format(".data\n");
	}
	public void text(){
		fileWriter.format(".text\n");
	}
	public void virtual_call(TEMP var_address,String funcName, int offset, String class_name)
	{
		int var_addressidx =var_address.getSerialNumber();
		TEMP temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		int tidx =temp.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,0(Temp_%d)\n", tidx,var_addressidx);
		fileWriter.format("\tlw Temp_%d,%d(Temp_%d)\n",tidx,offset,tidx);
		fileWriter.format("\tjalr Temp_%d\n",tidx);
		

	}
	public void prologue(int sp_offset){
		fileWriter.format("\tsub $sp,$sp,4\n");
		fileWriter.format("\tsw $ra,0($sp)\n");
		fileWriter.format("\tsub $sp,$sp,4\n");
		fileWriter.format("\tsw $fp,0($sp)\n");
		fileWriter.format("\tmove $fp,$sp\n");
		for (int i=0;i<10;++i)
		{
			fileWriter.format("\tsub $sp,$sp,4\n");
			fileWriter.format("\tsw $t%d,0($sp)\n",i);
		}
		fileWriter.format("\tsub $sp,$sp,%d\n",sp_offset);
	}
	public void epilogue(int sp_offset){
		fileWriter.format("\tmove $sp,$fp\n");
		for (int i=0;i<10;++i)
		{
			int offset = (i+1)*4;
			fileWriter.format("\tlw $t%d,-%d($sp)\n",i,offset);
		}
		fileWriter.format("\tlw $fp,0($sp)\n");
		fileWriter.format("\tlw $ra,4($sp)\n");
		fileWriter.format("\tadd $sp,$sp,%d\n",sp_offset);
		fileWriter.format("\tjr $ra\n");
		
	}
	public void array_access(TEMP dst,TEMP array,TEMP index)
	{
		TEMP temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		int idxdst=dst.getSerialNumber();
		int idxarray=array.getSerialNumber();
		int idxindex=index.getSerialNumber();
		int idxtmp=temp.getSerialNumber();
		fileWriter.format("\tblt Temp_%d,$zero,access_violation\n",idxindex);
		fileWriter.format("\tlw Temp_%d,0(Temp_%d)\n",idxtmp,idxarray);
		fileWriter.format("\tbge Temp_%d,Temp_%d,access_violation\n",idxindex,idxtmp);
		fileWriter.format("\tmove Temp_%d,Temp_%d\n",idxtmp,idxindex);
		fileWriter.format("\taddi Temp_%d,Temp_%d,1\n",idxtmp,idxtmp);
		fileWriter.format("\tmul Temp_%d,Temp_%d,4\n",idxtmp,idxtmp);
		fileWriter.format("\taddu Temp_%d,Temp_%d,Temp_%d\n", idxtmp,idxarray,idxtmp);
		fileWriter.format("\tlw Temp_%d,0(Temp_%d)\n",idxdst,idxtmp);
	}

	public void array_store(TEMP array,TEMP index,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		int idxarray=array.getSerialNumber();
		int idxindex=index.getSerialNumber();
		TEMP temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		int idxtmp=temp.getSerialNumber();
		fileWriter.format("\tblt Temp_%d,$zero,access_violation\n",idxindex);
		fileWriter.format("\tlw Temp_%d,0(Temp_%d)\n",idxtmp,idxarray);
		fileWriter.format("\tbge Temp_%d,Temp_%d,access_violation\n",idxindex,idxtmp);
		fileWriter.format("\tmove Temp_%d,Temp_%d\n",idxtmp,idxindex);
		fileWriter.format("\taddi Temp_%d,Temp_%d,1\n",idxtmp,idxtmp);
		fileWriter.format("\tmul Temp_%d,Temp_%d,4\n",idxtmp,idxtmp);
		fileWriter.format("\taddu Temp_%d,Temp_%d,Temp_%d\n", idxtmp,idxarray,idxtmp);
		fileWriter.format("\tsw Temp_%d,0(Temp_%d)\n",idxsrc,idxtmp);
	}
	public void load_this(TEMP dst)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,8($fp)\n",idxdst);
	}
	public void load_global(TEMP dst,String var_name)
	{
        int idxdst=dst.getSerialNumber();
	    fileWriter.format("\tla Temp_%d,global_%s\n",idxdst,var_name);
	}
	public void store_field(TEMP src, int offset) // store field from inside a class method
	{
		TEMP temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		int idxsrc=src.getSerialNumber();
		int idxtmp=temp.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,8($fp)\n",idxtmp,idxsrc);
		fileWriter.format("\tsw Temp_%d,%d(Temp_%d)\n",idxsrc,offset,idxtmp);
	}
	public void load_field(TEMP dst, int offset)
	{
		TEMP temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		int idxtmp=temp.getSerialNumber();
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,8($fp)\n",idxtmp);
		fileWriter.format("\tlw Temp_%d,%d(Temp_%d)\n",idxdst,offset,idxtmp);
	}
	public void load(TEMP dst,int offset)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,%d($fp)\n",idxdst,offset);
	}
	public void load(TEMP dst,int offset,TEMP src )
	{
		int idxdst=dst.getSerialNumber();
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,%d(Temp_%d)\n",idxdst,offset,idxsrc);
	}

    public void load_string(TEMP dst, String str, String label)
    {
        int idxdst = dst.getSerialNumber();
        fileWriter.format(".data\n");
        fileWriter.format("\t%s: .asciiz %s\n", label, str);
        fileWriter.format(".text\n");
        fileWriter.format("\tla Temp_%d,%s\n", idxdst, label);
    }
	public void load_int(TEMP dst, int val)
    {
        int idxdst = dst.getSerialNumber();
        fileWriter.format("\tli Temp_%d,%d\n", idxdst, val);
    }
	public void load_nil(TEMP dst)
	{
		int dstidx =dst.getSerialNumber();
		fileWriter.format("\tmove Temp_%d,$zero\n",dstidx);
	}
	public void store_field_value(TEMP dst,int val)
	{
		int idxdst=dst.getSerialNumber();
		
		fileWriter.format("\tsw Temp_%d,%d\n",idxdst,val);
	}
	public void store_global(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);		
	}
	public void store_local(TEMP src, int offset)
	{
		int srcidx = src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,%d($fp)\n", srcidx, offset);
	}
	public void sw(TEMP dst, int offset, TEMP src)
	{
		int dstidx = dst.getSerialNumber();
		int srcidx = src.getSerialNumber();

		fileWriter.format("\tsw Temp_%d,%d(Temp_%d)\n", dstidx, offset, srcidx);
	}
	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
	}
	public void li(String t,int value)
	{
		
		fileWriter.format("\tli %s,%d\n",t,value);
	}
	public void lb(TEMP dst, int offset, TEMP src)
	{
		int di=dst.getSerialNumber();
		int si=src.getSerialNumber();
		fileWriter.format("\tlb Temp_%d,%d(Temp_%d)\n",di,offset,si);
	}

	public void move(TEMP dst,TEMP src)
	{
		int di=dst.getSerialNumber();
		int si=src.getSerialNumber();
		fileWriter.format("\tmove Temp_%d,Temp_%d\n",di,si);
	}
	public void move(TEMP dst, String si)
	{
		int di=dst.getSerialNumber();
		fileWriter.format("\tmove Temp_%d,%s\n",di,si);
	}
	public void arithmetic_op(String op,TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\t%s Temp_%d,Temp_%d,Temp_%d\n",op,dstidx,i1,i2);
	}
	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		arithmetic_op("add",dst,oprnd1,oprnd2);
	}
	public void addi(TEMP dst,TEMP oprnd1,int val)
	{
		int i1 =oprnd1.getSerialNumber();
		int dstidx=dst.getSerialNumber();
		fileWriter.format("\taddi Temp_%d,Temp_%d,%d\n",dstidx,i1,val);
	}
	
	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		arithmetic_op("sub",dst,oprnd1,oprnd2);
	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		arithmetic_op("mul",dst,oprnd1,oprnd2);
	}
	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		// check div by zero:
		int i2 =oprnd2.getSerialNumber();
		fileWriter.format("\tbeqz Temp_%d,div_by_zero\n",i2);
		arithmetic_op("div",dst,oprnd1,oprnd2);
	}

	public void addu(TEMP dst,TEMP oprnd1, int num)
	{
		int i1 =oprnd1.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\taddu Temp_%d,Temp_%d,%d\n",dstidx,i1,num);
	}
	public void label(String inlabel)
	{	
		
		// if (inlabel.equals("main"))
		// {
		// 	fileWriter.format(".text\n");
		// 	fileWriter.format("%s:\n",inlabel);
		// }
		// else
		// {
			fileWriter.format("%s:\n",inlabel);
		//}
	}	
	
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}
	public void branch(String op,TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();

		fileWriter.format("\t%s Temp_%d,Temp_%d,%s\n",op,i1,i2,label);
	}
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		branch("blt",oprnd1,oprnd2,label);
	}
	public void bgt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		branch("bgt",oprnd1,oprnd2,label);
	}

	public void ble(TEMP oprnd1,TEMP oprnd2,String label){
		branch("ble",oprnd1,oprnd2,label);
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		branch("bge",oprnd1,oprnd2,label);
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		branch("bne",oprnd1,oprnd2,label);
	}
	
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		branch("beq",oprnd1,oprnd2,label);
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
	}
	public void jal(String label)
	{
		fileWriter.format("\tjal %s\n",label);
	}
	public void popParams(int num)
	{
		fileWriter.format("\taddi $sp,$sp,%d\n",num*4);
	}
	public void push(TEMP t)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\taddi $sp,$sp,-4\n");
		fileWriter.format("\tsw Temp_%d,0($sp)\n",idx);
	}

	private void syscall(int type){
		fileWriter.format("\tli $v0,%d\n",type);
		fileWriter.format("\tsyscall\n");
	}

	public void new_array(TEMP dst, TEMP size)
	{
		int dstidx =dst.getSerialNumber();
		int sizeidx =size.getSerialNumber();

		fileWriter.format("\tmove $a0,Temp_%d\n",sizeidx);
		fileWriter.format("\tadd $a0,$a0,1\n");
		fileWriter.format("\tmul $a0,$a0,4\n");
		this.syscall(9);
		fileWriter.format("\tmove Temp_%d,$v0\n",dstidx);
		fileWriter.format("\tsw Temp_%d,0(Temp_%d)\n",sizeidx, dstidx);
	}
	public void malloci(int size){
		fileWriter.format("\tli $a0,%d\n",size);
		this.syscall(9);
	}
	public void malloc(TEMP dst, TEMP size){
		int dstidx =dst.getSerialNumber();
		int sizeidx =size.getSerialNumber();
		fileWriter.format("\tmove $a0,Temp_%d\n",sizeidx);
		this.syscall(9);
		fileWriter.format("\tmove Temp_%d,$v0\n",dstidx);
	}
	public void get_length(TEMP length, TEMP string, String label)
	{
		int lengthidx =length.getSerialNumber();
		int stringidx =string.getSerialNumber();

		fileWriter.format("\tmove Temp_%d,$zero\n",lengthidx);
		fileWriter.format("\tmove $a0,Temp_%d\n",stringidx);
		label(label);
		fileWriter.format("\tlb $a1,0($a0)\n");
		fileWriter.format("\taddi Temp_%d,Temp_%d,1\n",lengthidx,lengthidx);
		fileWriter.format("\taddi $a0,$a0,1\n");
		fileWriter.format("\tbne $a1,$zero,%s\n",label);
		// remove the last 1
		fileWriter.format("\taddi Temp_%d,Temp_%d,-1\n",lengthidx,lengthidx);
	}
	public void add_string(String label, String label2)
	{
		fileWriter.format("\tlb $a2,0($a0)\n");
		fileWriter.format("\tbeq $a2,$zero,%s\n",label2);
		fileWriter.format("\tsb $a2,0($a1)\n");
		fileWriter.format("\taddi $a0,$a0,1\n");
		fileWriter.format("\taddi $a1,$a1,1\n");
		fileWriter.format("\tj %s\n",label);
	}
	public void concat_string(TEMP dst, TEMP string1, TEMP string2,String label, String label2, String label3, String label4)
	{
		int dstidx =dst.getSerialNumber();
		int string1idx =string1.getSerialNumber();
		int string2idx =string2.getSerialNumber();

		fileWriter.format("\tmove $a1,Temp_%d\n",dstidx);
		fileWriter.format("\tmove $a0,Temp_%d\n",string1idx);
		label(label);		
		add_string(label,label2);
		label(label2);		
		fileWriter.format("\tmove $a0,Temp_%d\n",string2idx);
		label(label3);		
		add_string(label3,label4);
		label(label4);
		fileWriter.format("\tsb $zero,0($a1)\n");
			
	}
	
	public void new_object(TEMP dst, String class_name, int size)
	{
		TEMP temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		int dstidx =dst.getSerialNumber();
		int idxtmp =temp.getSerialNumber();
		this.malloci(size);
		fileWriter.format("\tmove Temp_%d,$v0\n",dstidx);
		fileWriter.format("\tla Temp_%d,vt_%s\n",idxtmp,class_name);
		fileWriter.format("\tsw Temp_%d,0(Temp_%d)\n",idxtmp,dstidx);
		
				
	}
	public void printErrorComment(String comment)
	{
		instance.fileWriter.format("\tla $a0,%s\n", comment);
		instance.syscall(4);
			//exit
		instance.syscall(10);
	}
	public void ErrorPointerDerf(TEMP obj){
		int objidx =obj.getSerialNumber();
		// check if pointer is null
		instance.fileWriter.format("\tbeq Temp_%d,$zero,invalid_ptr_dref\n",objidx);
		
	}
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();
			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
			instance.fileWriter.print(".text\n");
			//dev by zero label
			instance.label("div_by_zero");
			instance.printErrorComment("string_illegal_div_by_0");
			// invalid ptr dref label
			instance.label("invalid_ptr_dref");
			instance.printErrorComment("string_invalid_ptr_dref");
			instance.label("access_violation");
			instance.printErrorComment("string_access_violation");
		}
		return instance;
	}
}
