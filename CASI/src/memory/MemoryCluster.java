/*
 * Author:	Aidan Evans
 * Date:	3/2/2019
 */


package memory;


import java.util.ArrayList;
import java.util.Arrays;


public class MemoryCluster {
	
	
	private ArrayList<Memory> memory = new ArrayList<Memory>();
	
	
	
	//adds new memory
	//pre-conditions:	array of strings as key words and string representing the content
	public void add(String[] key_words, String info) {
		
		Memory mem = new Memory();
		
		mem.identifiers = key_words;
		mem.content = info;
		
		memory.add(mem);
		
	}
	
	
	
	//clears current memory at specific index
	public void forget(int index) {
		
		memory.remove(index);
		
	}
	
	
	
	//clears current memory
	public void forget_all() {
		
		memory.clear();
		
	}
	
	
	
	//retrieves specific memory
	//pre-conditions:	accepts a string representing what is being searched for
	//post-conditions:	returns content of specific memory as string array
	public String[] pull(String statement) {
		
		if (memory.size() != 0 ) {
			
			//creates and initialize variables
			int i, j;
			String[] words = statement.split(" ");
			int[] count = new int[memory.size()];
			
			for (i = 0; i < memory.size(); i++)
				count[i] = 0;
			
			
			//transverses memory and counts how many words in statement match identifiers in each Memory
			for (i = 0; i < memory.size(); i++) {
				for (j = 0; j < words.length; j++) {
					
					if (Arrays.asList(memory.get(i).identifiers).contains(words[j])) {
						count[i]++;
					}
					
				}
			}
			
			
			//determines index, indices, of highest value in count
			int max = count[0];
			
			//determines max
			for (i = 1; i < count.length; i++) {
				if (max <= count[i]) {
					max = count[i];
				}
			}
			
			//creates array list of indices max value found
			ArrayList<Integer> max_index_arr = new ArrayList<>();
			
			for (i = 0; i < count.length; i++) {
				if (max == count[i]) {
					max_index_arr.add(i);
				}
			}
			
			
			//returns content of memory with most identifier matches
			//if each index of count equals 0 returns null
			ArrayList<String> content_list = new ArrayList<>();
			
			for (Integer index : max_index_arr) {
				content_list.add(memory.get(index).content);
			}
			
			String[] return_list = content_list.toArray(new String[0]);
			
			return return_list;
			
		}
		
		return null;
		
	}
	
	
	
	//returns entire memory ArrayList
	public Memory[] pull_all() {
		
		Memory[] mem_arr = memory.toArray(new Memory[0]);
		
		return mem_arr;
		
	}
	
	public static void main(String[] args) {
		MemoryCluster mem = new MemoryCluster();
		String[] key_list_1 = {"location", "latitude"};
		String[] key_list_2 = {"location", "longitude"};
		mem.add(key_list_1, "lat");
		mem.add(key_list_2, "long");
		
		String[] test = mem.pull("location");
		for (String s : test)
			System.out.println(s);
	}
	
	
	
}
