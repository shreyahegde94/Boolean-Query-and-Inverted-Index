package com.ir.project2.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import com.ir.project2.util.ArrangeUtil;
import com.ir.project2.util.ComparisonUtil.StaticComparisonUtil;

/**
 * The Class is used to create Inverted Index and Contains the following methods -
 * createIndexFromLuceneIndex - Created an inverted index map and set it in invertedIndexMap
 * getPostingsNewIndex - Get Postings List for printing in the Output file
 * getMasterPostingsList - Get a List of all the PostingsList. Reused by a number of methods
 * TAATOR - Implement the TAATOR and return the result List
 * TAATAND - Implement the TAATAND and return the result List
 * DAATOR - Implement the DAATOR and return the result List
 * DAATAND - Implement the DAATAND and return the result List
 */
public class IRLuceneReader {

	Map<String,LinkedList<Integer>> invertedIndexMap=new HashMap<String,LinkedList<Integer>>();
	private int noOfComparisonsTaatAnd=0;
	private int noOfComparisonsTaatOr=0;
	private int noOfComparisonsDaatAnd=0;
	private int noOfComparisonsDaatOr=0;

	
	public int getNoOfComparisonsTaatAnd() {
		return noOfComparisonsTaatAnd;
	}

	public void setNoOfComparisonsTaatAnd(int noOfComparisonsTaatAnd) {
		this.noOfComparisonsTaatAnd = noOfComparisonsTaatAnd;
	}

	public int getNoOfComparisonsTaatOr() {
		return noOfComparisonsTaatOr;
	}

	public void setNoOfComparisonsTaatOr(int noOfComparisonsTaatOr) {
		this.noOfComparisonsTaatOr = noOfComparisonsTaatOr;
	}

	public int getNoOfComparisonsDaatAnd() {
		return noOfComparisonsDaatAnd;
	}

	public void setNoOfComparisonsDaatAnd(int noOfComparisonsDaatAnd) {
		this.noOfComparisonsDaatAnd = noOfComparisonsDaatAnd;
	}

	public int getNoOfComparisonsDaatOr() {
		return noOfComparisonsDaatOr;
	}

	public void setNoOfComparisonsDaatOr(int noOfComparisonsDaatOr) {
		this.noOfComparisonsDaatOr = noOfComparisonsDaatOr;
	}

		public void createIndexFromLuceneIndex(String directoryPath){
			
			//Reading the directory
			Path path = Paths.get(directoryPath);
			Directory directory;
	
			try {
				System.out.println("Indexing Path -"+directoryPath);
				directory = FSDirectory.open(path);
				// Open Directory
				IndexReader reader = DirectoryReader.open(directory);
				
				// Instantiate fieldArray
				List<String> fieldArray=new ArrayList<String>();
				fieldArray.add("text_da");
				fieldArray.add("text_de");
				fieldArray.add("text_es");
				fieldArray.add("text_fr");
				fieldArray.add("text_it");
				fieldArray.add("text_ja");
				fieldArray.add("text_nl");
				fieldArray.add("text_no");
				fieldArray.add("text_pt");
				fieldArray.add("text_ru");
				fieldArray.add("text_sv");
				
				Fields fields = MultiFields.getFields(reader);
				//Creating an Inverted Index and assign it to the GlobalField - invertedIndexMap
				for (String field : fields) {
					if(StaticComparisonUtil.carries(fieldArray, field)){
			            Terms terms = fields.terms(field);
			            TermsEnum termsEnum = terms.iterator();
			            BytesRef term=termsEnum.next();
			            while (term!= null) {
			            	if(term!=null){
				            	String termString = term.utf8ToString();
				            	    PostingsEnum postingsEnum = MultiFields.getTermDocsEnum(reader, field, term);
					            	LinkedList<Integer> docLinkList=new LinkedList<Integer>();
				            	    while((postingsEnum).nextDoc()!=PostingsEnum.NO_MORE_DOCS){
				            	    	docLinkList.add(postingsEnum.docID());
					            	}
				            	    invertedIndexMap.put(termString, docLinkList);
				                term=termsEnum.next();
			                }
			            }
					}
		        }
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * @param queryTerm
		 * @return Postings List for a given query term separated by a space
		 */
		public String getPostingsNewIndex(String queryTerm){
			
			LinkedList<Integer> posList=new LinkedList<Integer>();
			String resultString="";
			posList=invertedIndexMap.get(queryTerm);
			for(Integer docId : posList){
				resultString+=docId.toString()+" ";
			}
		resultString=resultString.trim();
		return resultString;
		}
		/**
		 * @param queryTerms
		 * @return List of Postings List in the increasing order of the size. To be reused by other functions.
		 */
		public LinkedList<LinkedList<Integer>> getMasterPostingsList(String[] queryTerms){
			LinkedList<LinkedList<Integer>> documentsList=new LinkedList<LinkedList<Integer>>();
			for (String queryTerm : queryTerms) {
				LinkedList<Integer> tmpDocList=new LinkedList<Integer>();
				tmpDocList.addAll(invertedIndexMap.get(queryTerm));
				if(!documentsList.isEmpty()){
					int docsListSize=documentsList.size();
					for(int i=0;i<docsListSize;i++){
						if((tmpDocList.size())<=(documentsList.get(i).size())){
							documentsList.add(i, tmpDocList);
							break;
						}else{
							if(i+1==docsListSize){
								documentsList.add(tmpDocList);
								break;
							}
						}
						
					}
				}else{
					documentsList.add(tmpDocList);
				}
			}
			return documentsList;
		}
		
		/**
		 * @param queryTerms
		 * @return Performs the TAATOR operations one term at a time and returns the resultList finally.
		 */
		public ArrayList<Integer> TAATOR(String[] queryTerms){
			
				ArrayList<Integer> resultList=new ArrayList<Integer>();
				int noOfComparisons=0;
				//Iterating through all the query terms
				for (String queryTerm : queryTerms) {
					LinkedList<Integer> tempDocList;
					tempDocList=invertedIndexMap.get(queryTerm);
					//Iterating through all the docIds of each queryTerm Postings list and adding to the result after the exists check.
					for(Integer docId : tempDocList){
						noOfComparisons++;
						//Custom coded CarriesFunction Call
						if(!StaticComparisonUtil.carries(resultList, docId)){
							resultList.add(docId);
						}
					}
			}
			setNoOfComparisonsTaatOr(noOfComparisons);
			//Sort the output list in ascending order.
			ArrangeUtil au=new ArrangeUtil();
			au.arrange(resultList);
			resultList=(ArrayList<Integer>) au.getNumbers();
			au=null;
			return resultList;
		}
		/**
		 * @param queryTerms
		 * @return Performs the TAATAND operations one term at a time and returns the resultList finally.
		 */
		public LinkedList<Integer> TAATAND(String[] queryTerms){
			
			LinkedList<Integer> resultList=new LinkedList<Integer>();
			LinkedList<LinkedList<Integer>> docsList=new LinkedList<LinkedList<Integer>>();
			int noOfComparisons=0;
			//Handling a special condition when there is only one query term.
			if(queryTerms.length==1){
				resultList=invertedIndexMap.get(queryTerms[0]);
			}else{
				docsList=getMasterPostingsList(queryTerms);
				// Implementing TAATAND Logic after forming the Lists
				// Take one term's Posting list at a time and perform the AND logic
				// Need an intermediateList to hold the temporary results and when the last term loops, the intermediate result becomes the final result.
				LinkedList<Integer> intermediateList=new LinkedList<Integer>();
				int masterDocsListSize=docsList.size();
				for(int i=0;i<(masterDocsListSize-1);i++){
					LinkedList<Integer> compareList1;
					if(intermediateList.isEmpty()){
						compareList1=docsList.get(i);	
					}else{
						compareList1=intermediateList;
						intermediateList=new LinkedList<Integer>();
					}
					LinkedList<Integer> compareList2=docsList.get(i+1);
					
					for(Integer element1 : compareList1){
						for(Integer element2 : compareList2){
							noOfComparisons++;							
							if(element1.equals(element2)){
								    //Add the element to the intermediate list.
									intermediateList.add(element1);
									continue;
							}
						}
					}
					if(i+2==masterDocsListSize){
						// when the last term loops, the intermediate result becomes the final result.
						resultList=intermediateList;
					}
				}
			}
			
		//Sort the output list in ascending order.
		ArrangeUtil au=new ArrangeUtil();
		au.arrange(resultList);
		resultList=(LinkedList<Integer>) au.getNumbers();
		au=null;
		setNoOfComparisonsTaatAnd(noOfComparisons);
		return resultList;
	}
	/**
	 * @param queryTerms
	 * @return Performs the DAATOR operations of all posting lists at a time and returns the resultList finally.
	 */	
	public ArrayList<Integer> DAATOR(String[] queryTerms){
			
		ArrayList<Integer> resultList=new ArrayList<Integer>();
		LinkedList<LinkedList<Integer>> documentsList=new LinkedList<LinkedList<Integer>>();
		int noOfComparisons=0;
		documentsList=getMasterPostingsList(queryTerms);
		
		// Implementing DAAT OR Logic - Processing all the Postings list in Parallel.
							//Continue the logic till All the PostingsList of all the terms are exhausted.
							while(documentsList.size()!=0){
								ArrayList<Integer> docIdParallelList=new ArrayList<Integer>();
								for(int j=0;j<documentsList.size();j++){
									docIdParallelList.add(documentsList.get(j).get(0));
									if(docIdParallelList.size()==documentsList.size()){
										boolean match = StaticComparisonUtil.allAreEqual(docIdParallelList);
										if(match){
											noOfComparisons++;
											//If all elements are equal, add to the element to the resultList and remove it from the corresponding posting list(s).
											resultList.add(docIdParallelList.get(0));
											for(int m=0;m<(documentsList.size());m++){
												documentsList.get(m).remove(docIdParallelList.get(0));
												int tempListSize=documentsList.get(m).size();
												if(tempListSize==0){
													documentsList.remove(m);
													m--;
												}
											}
										}else{
											//If all elements are not equal, find the minimum and add it to the resultList. Then remove the minimum elements.
											noOfComparisons++;
											Integer min = StaticComparisonUtil.least(docIdParallelList);
											resultList.add(min);
											for(int k=0;k<(documentsList.size());k++){
												documentsList.get(k).remove(min);
												int tempListSize=documentsList.get(k).size();
												if(tempListSize==0){
													documentsList.remove(k);
													k--;
												}
											}
											
										}
									}
							}
						}
				//Sort the output list in ascending order.
				ArrangeUtil au=new ArrangeUtil();
				au.arrange(resultList);
				resultList=(ArrayList<Integer>) au.getNumbers();
				au=null;
				setNoOfComparisonsDaatOr(noOfComparisons);
				return resultList;
	}
/**
 * @param queryTerms
 * @return Performs the DAAAND operations of all posting lists at a time and returns the resultList finally.
 */
public ArrayList<Integer> DAATAND(String[] queryTerms){
	ArrayList<Integer> resultList=new ArrayList<Integer>();
	LinkedList<LinkedList<Integer>> documentsList=new LinkedList<LinkedList<Integer>>();
	int noOfComparisons=0;
	documentsList=getMasterPostingsList(queryTerms);
	
	// Implementing AND Logic after forming the Master List that contains the Postings List
				
				//int smallestListSize=documentsList.get(0).size();
				int masterListSize=documentsList.size();
					boolean stopFlag=true;
					while(stopFlag){
						ArrayList<Integer> docIdParallelList=new ArrayList<Integer>();
						for(int j=0;j<(masterListSize);j++){
							docIdParallelList.add(documentsList.get(j).get(0));
							if(docIdParallelList.size()==masterListSize){
								boolean match = StaticComparisonUtil.allAreEqual(docIdParallelList);
								if(match){
									//If all elements are equal, add to the element to result list and remove it from the corresponding posting list(s).
									resultList.add(docIdParallelList.get(0));
									noOfComparisons++;
									for(int m=0;m<(masterListSize);m++){
										documentsList.get(m).remove(docIdParallelList.get(0));
										int tempListSize=documentsList.get(m).size();
										if(tempListSize==0){
											stopFlag=false;
											break;
										}
									}
									if(!stopFlag){
										break;
									}
								}else{
									//If all elements not equal, remove the minimum from the corresponding posting list(s).
									Integer min = StaticComparisonUtil.least(docIdParallelList);
									noOfComparisons++;
									for(int k=0;k<(masterListSize);k++){
										documentsList.get(k).remove(min);
										int tempListSize=documentsList.get(k).size();
										//End if one of the list gets exhausted in comparison, since it's AND logic.
										if(tempListSize==0){
											stopFlag=false;
											break;
										}
									}
									if(!stopFlag){
										break;
									}
								}
							}
						}
					}
			//Sort the output list in ascending order.
			ArrangeUtil au=new ArrangeUtil();
			au.arrange(resultList);
			resultList=(ArrayList<Integer>) au.getNumbers();
			au=null;
			setNoOfComparisonsDaatAnd(noOfComparisons);
			return resultList;

}		
}
