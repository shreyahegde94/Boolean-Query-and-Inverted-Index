package com.ir.project2.util;

import java.util.List;

/**
 * This class has custom implementation for 
 * Contains (Function Overloading for List of String and Integer), 
 * Finding Minimum
 * If all elements in an List are equal 
 * The StaticComparisonUtil is a Static class because it has utility methods that can be reused.
 */
public class ComparisonUtil {
	
	public static class StaticComparisonUtil {
		/**
		 * @param List<String> List
		 * @param String matchTerm
		 * @return Checks if the matchTerm exists in the given list1 and returns TRUE or FALSE.
		 */		
		public static boolean carries(List<String> list1, String matchTerm) {
			try {
				for(String term : list1){
					if(term.equals(matchTerm)){
						return true;
					}
				}
				return false;

			} catch (Exception ex) {
				return false;
			}
		}
		/**
		 * @param List<Integer> list1
		 * @param Integer matchTerm
		 * @return Checks if the matchTerm exists in the given list1 and returns TRUE or FALSE.
		 */
		public static boolean carries(List<Integer> list1, Integer matchTerm) {
			try {
				for(Integer term : list1){
					if(term.equals(matchTerm)){
						return true;
					}
				}
				return false;

			} catch (Exception ex) {
				return false;
			}
		}
		/**
		 * @param List<Integer> list1
		 * @return Finds the smallest element of the list and returns the same.
		 */
		public static Integer least(List<Integer> list1) {
			try {
				Integer min=list1.get(0);
				for(Integer integer : list1){
					if(integer<min){
						min=integer;
					}
				}
				return min;

			} catch (Exception ex) {
				return null;
			}
		}
		/**
		 * @param List<Integer> list1
		 * @return Checks if all the elements of a list are equal and returns TRUE or FALSE
		 */
		public static boolean allAreEqual(List<Integer> list1) {
			try {
				boolean flag=true;
				for(int i=0;i<(list1.size()-1);i++){
					if(!(list1.get(i).equals(list1.get(i+1)))){
						flag=false;
						break;
					}
				}
				return flag;

			} catch (Exception ex) {
				return false;
			}
		}
	}
}
