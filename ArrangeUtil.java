package com.ir.project2.util;

import java.util.List;

/**
 * This class has custom quicksort implementation for Sorting a List of Integers.  
 * The StaticComparisonUtil is a Static class because it has utility methods that can be reused.
 */
public class ArrangeUtil {

        private List<Integer> numbers;
		private int number;
        public void arrange(List<Integer> values) {
                // check for empty or null array
                if (values ==null || values.size()==0){
                        return;
                }
                this.numbers = values;
                number = values.size();
                quicksort(0, number - 1);
        }

        private void quicksort(int low, int high) {
                int i = low, j = high;
                int pivot = numbers.get(low + (high-low)/2);

                while (i <= j) {
                        while (numbers.get(i) < pivot) {
                                i++;
                        }
                        
                        while (numbers.get(j) > pivot) {
                                j--;
                        }

                        
                        if (i <= j) {
                                exchange(i, j);
                                i++;
                                j--;
                        }
                }
                if (low < j)
                        quicksort(low, j);
                if (i < high)
                        quicksort(i, high);
        }

        public List<Integer> getNumbers() {
			return numbers;
		}

		public void setNumbers(List<Integer> numbers) {
			this.numbers = numbers;
		}

		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}

		private void exchange(int i, int j) {
                int temp = numbers.get(i);
                numbers.set(i,numbers.get(j)); 
                numbers.set(j, temp);
                
        }

}
