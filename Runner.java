package com.ir.project2.impl;

import java.util.ArrayList;
import java.util.LinkedList;

import com.ir.project2.util.FileParserUtil.StaticFileParserUtil;
/**
 * This is the main class which begins the execution. It reads the Input File, gets the queryLines, print the postingsList for each term,
 * execute TAATAND, TAATOR, DAATAND, DAATOR for every queryLine and write it to a file.
 */
public class Runner {

	public static void main(String[] args) {
		 //Get the Path of Index
		 String path_of_index = args[0];
		//Get the Output File Path
		 String outputFilePath = args[1];
		//Get the Input File Path
		 String inputFilePath = args[2];
		 
		//Call the Util to read the input file to get the query lines
		ArrayList<String> queryLineList=StaticFileParserUtil.irFileParserUtil(inputFilePath);
		IRLuceneReader irLuceneReader =new IRLuceneReader();
		//Call and read Lucene Index. Create the inverted Index in Memory and Keep it ready.
		irLuceneReader.createIndexFromLuceneIndex(path_of_index);
		String resultToPrint="";
		String listString="";
		
		//For every query line
		for(String queryLine : queryLineList){
			String queryTerms[]=queryLine.split(" ");
			
			// Get the Query Terms
			String spaceQueryTerms="";
			for(String queryTerm : queryTerms){
				spaceQueryTerms+=queryTerm+" ";
			}
			spaceQueryTerms=spaceQueryTerms.trim();
			//Get Postings
			for(String queryTerm : queryTerms){
				resultToPrint+="GetPostings";
				resultToPrint+="\n"+queryTerm;
				resultToPrint+="\nPostings list: "+irLuceneReader.getPostingsNewIndex(queryTerm)+"\n";
			}
			//TAATAND Section
			resultToPrint+="TaatAnd";
			resultToPrint+="\n"+spaceQueryTerms;
			resultToPrint+="\nResults: ";
			LinkedList<Integer> resultList2=new LinkedList<Integer>(); 
			//Execute TAATAND
			resultList2=irLuceneReader.TAATAND(queryTerms);
			if(resultList2!=null){
				listString=resultList2.toString().replace(",", "");
				listString=listString.substring(1, listString.length()-1);
				resultToPrint+=listString;
				resultToPrint+="\nNumber of documents in results: "+resultList2.size();
			}else{
				resultToPrint+="empty";
				resultToPrint+="\nNumber of documents in results: 0";
			}
			
			resultToPrint+="\nNumber of comparisons: "+irLuceneReader.getNoOfComparisonsTaatAnd();
			
			//TAATOR Section
			resultToPrint+="\nTaatOr";		
			resultToPrint+="\n"+spaceQueryTerms;
			resultToPrint+="\nResults: ";
			
			ArrayList<Integer> resultList=new ArrayList<Integer>();
			//Execute TAATOR
			resultList=irLuceneReader.TAATOR(queryTerms);
			if(resultList!=null){
				listString="";
				listString=resultList.toString().replace(",", "");
				listString=listString.substring(1, listString.length()-1);
				resultToPrint+=listString;
				resultToPrint+="\nNumber of documents in results: "+resultList.size();
			}else{
				resultToPrint+="empty";
				resultToPrint+="\nNumber of documents in results: 0";
			}
			resultToPrint+="\nNumber of comparisons: "+irLuceneReader.getNoOfComparisonsTaatOr();
			
			//DAATAND Section
			resultToPrint+="\nDaatAnd";		
			resultToPrint+="\n"+spaceQueryTerms;
			resultToPrint+="\nResults: ";
			
			ArrayList<Integer> resultList4=new ArrayList<Integer>(); 
			//Execute DAATAND
			resultList4=irLuceneReader.DAATAND(queryTerms);
			if(resultList4!=null){
				listString="";
				listString=resultList4.toString().replace(",", "");
				listString=listString.substring(1, listString.length()-1);
				resultToPrint+=listString;
				resultToPrint+="\nNumber of documents in results: "+resultList4.size();
			}else{
				resultToPrint+="empty";
				resultToPrint+="\nNumber of documents in results: 0";
			}
			resultToPrint+="\nNumber of comparisons: "+irLuceneReader.getNoOfComparisonsDaatAnd();
			
			//DAATOR Section
			resultToPrint+="\nDaatOr";		
			resultToPrint+="\n"+spaceQueryTerms;
			resultToPrint+="\nResults: ";
			
			ArrayList<Integer> resultList3=new ArrayList<Integer>(); 
			//Execute DAATOR
			resultList3=irLuceneReader.DAATOR(queryTerms);
			if(resultList3!=null){
				listString="";
				listString=resultList3.toString().replace(",", "");
				listString=listString.substring(1, listString.length()-1);
				resultToPrint+=listString;
				resultToPrint+="\nNumber of documents in results: "+resultList3.size();
			}else{
				resultToPrint+="empty";
				resultToPrint+="\nNumber of documents in results: 0";
			}
			resultToPrint+="\nNumber of comparisons: "+irLuceneReader.getNoOfComparisonsDaatOr();
			resultToPrint+="\n";

		}
		resultToPrint=resultToPrint.trim();
		//Write the final result to file by calling the custom utility.
		StaticFileParserUtil.irStringFileWritingUtil(resultToPrint, outputFilePath);
		
        
	}

}
