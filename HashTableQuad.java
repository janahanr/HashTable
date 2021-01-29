//Divya Thakkar
//thakkd2
//400210186
//L02
//COMPENG 2SI4 Lab 4 (HashTableQuad class)

public class HashTableQuad {
	private Integer [] table; //hashTable values held in an array
	private int tableSize; //size of the entire hashTable
	private int keysCount; //number of keys stored in the hashTable at any given time
	private double load; //max desired load factor (we should not exceed this value)
	//load must be less than 1
	
	// primeNums = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199];
		
	//1. constructor creating an empty hash table
	public HashTableQuad(int maxNum, double load) throws IllegalArgumentException { 
		
		if(load > 1) { //first ensures load is a valid input
			throw new IllegalArgumentException("Entered load factor must be less than or equal to 1");
		}
		
		this.keysCount = 0; //empty hashTable so number of keys is 0
		this.load = load; 
		this.tableSize = (int) Math.ceil(maxNum/load); //rounded up as more hash spots are neeeded
		 
		while(isPrime(this.tableSize) == false) { //iterate up until smallest prime number is found
			this.tableSize++;
		}
				
		this.table = new Integer [this.tableSize]; //appropriate sized hash Table made
	}
	
	
	//2. Constructor for an array of integer inputs
	
	//use Integer class instead of int, allows more parsing on the object itself	
	public HashTableQuad(Integer[] input, double load) throws IllegalArgumentException {
		if(load > 1) { // //first ensures load is a valid input
			throw new IllegalArgumentException("Entered load factor must be less than or equal to 1");
		}
		
		this.load = load;
		this.tableSize = (int) Math.ceil(input.length/load); //rounded up once again
		
		while(isPrime(this.tableSize) == false) { //ensure size of the Table is a prime number
			this.tableSize++;
		}
				
		this.table = new Integer[this.tableSize]; //appropriate sized HashTable created
		
		for (int i = 0; i < input.length; i++) { //take values from the input array
			insert(input[i]); //enter them into the HashTable using insert function defined below
		}
		
	}
	
	//3. isIn method checks if an integer is in the HashTable
    public boolean isIn(int n) { 
        for (int i = n % tableSize; i < tableSize; i++) { //first check occurs at index of hash function
            if (table[i] == null) { //if this spot is empty, the number should've existed at this spot since no number takes its place
            	return false; //null at that index means this value isn't in the table at all, return False since number is not in the table
            }
            if (table[i] == n) { //if number is found, congrats, true
            	return true;
            }
        }
        //exiting this for loop means the number isn't found yet, need to go back to beginning of array
        //iterate up by one until end of the list if collisions have occurred and value has been pushed to another location
        
        
        //from beginning of array until hashFunction value index is iterated here
        for (int i = 0; i < n % tableSize; i++) { 
            if (table[i] == null) { //same logic above
            	return false;
            }
            if (table[i] == n) { //same logic above
            	return true;
            }
        }

        return false; //if we iterate through the whole list and reach this point, value not found
    }
    
    //4. Method to insert a value into the hashTable
	public void insert(int n) {
		//FIX THIS STATEMENT BELOW WRONG!!!!!!!!!!!!!!!
		if ((keysCount+1)/tableSize > load) { //calculate actual load at current time and compare to desired Max load
			rehash(); //rehash if this insertion will cause the load to exceed the maxLoad value
		}
		
		if(isIn(n) == true) { //don't need to insert anything if the value is already in the hashTable
			return; //exit the insert function, don;t need to do anything
		}
		
		int i = 0;

		while(true) {
		if(table[(n + i*i)%tableSize] == null) { //quadratic probing, add a value of i^n to the index, where n increases linearly
			table[(n + i*i)%tableSize] = n; //insert at the same point if we find a null/empty index
			keysCount++; //if a value is inserted, update number of keys in the array
			return;
            }
		else {
			i++; //increment probing constant (n)
            }
		}
	}
	
	
	
    //5. Method to insert a value into the hashTable while counting the number of probes taken to insert the value
	public int insertCount(int n) {
		int iterations = 1; //initialize the number of probes taken to start at 1
		//FIX THIS STATEMENT BELOW WRONG!!!!!
		if ((keysCount+1)/tableSize > load) { //calculate actual load at current time and compare to desired Max load
			rehash(); //rehash if this insertion will cause the load to exceed the maxLoad value
			//rehashing will not effect probed iterations
		}
		
		if(isIn(n) == true) { //don't need to insert anything if the value is already in the hashTable
			return 0; //exit the insert function, don;t need to do anything
			//if the number is already in the hashTable, return 0 number of probed iterations required
			//FIX THIS WRONG!!!!!!!!!!!!
		}
		
		int i = 0;

		while(true) {
		if(table[(n + i*i)%tableSize] == null) { //quadratic probing, add a value of i^n to the index, where n increases linearly
			table[(n + i*i)%tableSize] = n; //insert at the same point if we find a null/empty index
			keysCount++; //if a value is inserted, update number of keys in the array
			break; //exit the function as our initalized value of 1 for number of iterations is correct
			//we have inserted the number in our first attempt
            }
		else {
			i++; //increment probing constant (n)
			iterations++; //every time we have to probe an additional amount, increment number of iterations
            }
		}
		return iterations; //final return
	}
		
	public void rehash() { //6. Method to rehash the table with an according size if the load factor is exceeded
		HashTableQuad temporary = new HashTableQuad(keysCount,load); //create a temporary HashTable to be able to transfer values
		//initalize this hashTable with same number of keys as before since only size of Table changes but number of keys remains same
		//initalize this hashTable with same max load factor as before since it stays the same as initially inputted
		int sizeVar=2*temporary.tableSize; //double the size
		while (!isPrime(sizeVar)) { //iterate through and call primeChecker function 
			sizeVar++; //increment until prime number for the size is found 
		}
//		System.out.println(tableSize); //not needed
//		System.out.println(sizeVar); //not needed
		temporary.table = new Integer[sizeVar]; //make the size of the hashTable with appropriately found size
		temporary.tableSize=sizeVar; //update instance field
		for (int i=0; i<tableSize;i++) { //iterate through the old table and transfer all old values
			if (table[i]!=null) { //insert at null positions
				temporary.insert(table[i]); //call old function
			}
		}
		table=temporary.table; //update parameters/instance fields
		tableSize=temporary.tableSize; //update parameters/instance fields
	}
	
	
    public void printKeys() { //7. Method to print the keys that exist in the hashTable
        int enteredKeys = 0; //variable that tracks number of keys that have been printed to screen

        if(keysCount == 0) { //easy case where no keys need to printed, take advantage of instance field that we made
        	System.out.println("This hash table is empty!");
        }
        
        
        //if hashTable isn't empty, we need to iterate through the table itself
        for (int i = 0; i < tableSize; i++){ 
            if (table[i] != null) { //indicates a key is present at this index
                System.out.print(table[i]); //print that key
                enteredKeys++; //update number of keys printed to screen

                if (enteredKeys != keysCount){ //if we have not printed all keys to the screen, add a comma and continue
                    System.out.print(", "); //will not print a comma for the last element
                }
            }

            if (enteredKeys == keysCount) { //once all keys are displayed to screen, we can break function and finish, if not, continue printing
            	break;
            }
        }
    }
	
    public void printKeysAndIndexes() { //8. Method to print the keys that exist in the hashTable along with the indexes they are stored at
        int enteredKeys = 0; //variable that tracks number of keys that have been printed to screen
        
        if(keysCount == 0) { //easy case where no keys need to printed, take advantage of instance field that we made
        	System.out.println("This hash table is empty!");
        }

      //if hashTable isn't empty, we need to iterate through the table itself
        for (int i = 0; i < tableSize; i++){ 
            if (table[i] != null){ //indicates a key is present at this index
                System.out.print("The index is " + i + "and the value is " + table[i]); //print that key along with its index
                enteredKeys++; //update number of keys printed to screen

                if (enteredKeys != keysCount){ //if we have not printed all keys to the screen, add a comma and continue
                    System.out.print(", "); //will not print a comma for the last element
                }
            }

            if (enteredKeys == keysCount) { //once all keys are displayed to screen, we can break function and finish, if not, continue printing
            	break;
            }
        }
    }
	
    
    //Simple function. Algorithm checks whether a value is prime or not.
    public boolean isPrime(double x) { //double type instead of int gets rid of some errors
		for (int n=2;n<x;n++) { //start at 2 and check all factors up to the number (not including the number itself or not including 1)
			//do not include 1 or the number itself as all numbers are divisible by 1 and n(number itself) even if they are prime
			if (n%x==0) { //if divisible by anything else, this is not prime
				return false;
			}
		}
		return true; //if we are able to iterate through all values and find no divisible factors, our number is prime!!!!!!!
	}
    	
	//Class accessors
	public int getTableSize() { //accessor for the size of our hashTable
		return this.tableSize;
	}
	
	public int getKeysCount() { //accessor for the number of keys currently Stored
		return this.keysCount;
	}
	
	public double getLoad() { //accessor for the max allowable load factor
		return this.load;
	}
	
	public Integer[] getTable() { //accessor for the Table of values itself 
		return this.table;
		//cannot print this directly, need to run a for loop and print each index itself
	}
	
} //end HashTableQuad class definition