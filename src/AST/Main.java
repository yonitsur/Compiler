   
import java.io.*;
import java.io.PrintWriter;
import java.util.ArrayList;


import java_cup.runtime.Symbol;
import AST.*;
import IR.*;
import MIPS.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AST_DEC_LIST AST;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			AST = (AST_DEC_LIST) p.parse().value;
			
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			AST.PrintMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			AST.SemantMe(0);

			/**********************/
			/* [8] IR the AST ... */
			/**********************/
			AST.IRme();
			
			/***********************/
			/* [9] MIPS the IR ... */
			/***********************/
			IR.getInstance().MIPSme();

			/**************************************/
			/* [10] Finalize AST GRAPHIZ DOT file */
			/**************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();			

			/***************************/
			/* [11] Finalize MIPS file */
			/***************************/
			MIPSGenerator.getInstance().finalizeFile();
            
            // here we have the mips code in text file

            

            // 1. create interference graph
            ArrayList <String> lines; // lines of the mips(with temp not registers) code
            CFG cfg = new CFG();
            lines = cfg.createInterferenceGraph(outputFilename);
            
            /**************************/
			/* [12] Close output file */
			/**************************/
			file_writer.close();

            // 2. perform liveness analysis
            cfg.performLivenessAnalysis();
			
            // 3. perform register allocation
			InterferenceGraph ig = new InterferenceGraph(cfg.blocks);
            ig.performRegisterAllocation();

            // 4. perform code generation - replace temps with registers in lines
            lines = ig.performCodeGeneration(lines);
            
            // 5. write the code to the output file - write lines (mips with registers code) to output file
            ig.writeCodeToFile(outputFilename, lines);  
                    
			
    	}
			     
		catch (Exception e)
		{
            System.out.format("Unexpected error in main: %s", e.getMessage());
			e.printStackTrace();
		}
	}
}


