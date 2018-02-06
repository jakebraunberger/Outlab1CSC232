/*
Original code by,
Robert Lafore. 2002.
Data Structures and Algorithms in Java (2nd ed)
Sams, Indianapolis, IN, US
*/
/*
 * Modifying Authors: James Eaton
 * Date: 
 * Overview: Huffman tree
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

//////////////////////////////////////////////////////////////
class Node {
	public int iData;           // frequency
        public String Char;         // character
	public Node leftChild;      // this Node's left child
	public Node rightChild;     // this Node's right child

	public void displayNode() { // display ourself
		System.out.print('{');
                System.out.print(Char); 
		System.out.print(", ");
		System.out.print(iData); 
		System.out.print("} ");		
	}
} // end Class Node
////////////////////////////////////////////////////////////////
class Table {
    //code table class
    public String Char;     // character
    public String code;     // code
    public Table next;      // next element of table
}// end class
////////////////////////////////////////////////////////////////
class Tree {
	public Node root;                 // first Node of Tree
	public Table start;                 // first node of table
        
	public Tree() {                    // constructor
		root = null;                   // no nodes in tree yet
                start = null;                   // no table yet
	}
	
	
	public Node find(int key) {      // find node with given key
		Node current = root;         // (assumes non-empty tree)
		while (current.iData != key) {          // while no match
			if (key < current.iData) {          // go left?
				current =  current.leftChild; 
			}
			else {                              // or go right?
				current =  current.rightChild;
			}
			if(current == null)                 // if no child
			{                                   // didn't find it
				return null;              
			}			
		}
		return current;                         // found it
	}  //end find()
        
        public void insertT(String Char, String code){
            //inserts element into table
            Table Temp = new Table();
                Temp.Char = Char;
                Temp.code = code;
                
            if(start == null){
                start = Temp;
            }else{
                Table Temp2 = start;
                while(Temp2.next != null){
                    Temp2 = Temp2.next;
                }
                Temp2.next = Temp;
            }
        }//end insert
	public String findChar(String code, String key) {
            //goes through table to find code
            Table Temp = start;
            while (Temp != null){
                if(Temp.Char.endsWith(key)){
                    code = code.concat(Temp.code);
                    return code;
                }else{
                    Temp = Temp.next;
                }
            }
            return code;
        }// end find char
        
	public void insert(int id, String cd) {
		Node newNode = new Node();    // make new Node
		newNode.iData = id;           // insert data
		newNode.Char = cd;
		newNode.leftChild = null;
		newNode.rightChild = null;
		if(root == null) {            // no node in root
			root = newNode;
		}
		else {                        // root occupied
			Node current = root;      // start at root  
			Node parent;
			while (true) {            // exits internally			
				parent = current;  
				if (id < current.iData) {              // go left?
					current =  current.leftChild;
					if(current == null) {             // if the end of the line        
						parent.leftChild = newNode;   // insert on left
						return;                    
					}
				} //end if go left
				else {                                // or go right?
					current =  current.rightChild;      
					if(current == null)               // if the end of the line
					{                                 // insert on right
						parent.rightChild = newNode;
						return;                    
					}
				}
			}
		}
	} // end insert()

	
	public boolean delete(int key) {             // delete node with given key
		Node current = root;		             // (assumes non-empty list)
		Node parent = root;
		boolean isLeftChild = true;

		while (current.iData != key) {           // search for Node
			parent = current;
			if (key < current.iData) {           // go left?
				isLeftChild = true;
				current =  current.leftChild;
			}
			else {                               // or go right?
				isLeftChild = false;
				current =  current.rightChild;
			}
			if(current == null) {                // end of the line,                             
				return false;                    // didn't find it
			}			
		}
		//found the node to delete

		//if no children, simply delete it
		if (current.leftChild == null && current.rightChild == null) {
			if (current == root) {              // if root,
				root = null;                    // tree is empty
			}
			else if (isLeftChild) {
				parent.leftChild = null;        // disconnect
			}                                   // from parent
			else {
				parent.rightChild = null;
			}
		}
		//if no right child, replace with left subtree
		else if (current.rightChild == null) {  
			if (current == root) {
				root = current.leftChild;
			}
			else if (isLeftChild) {
				parent.leftChild = current.leftChild;
			}			
			else {
				parent.rightChild = current.leftChild;
			}
		}

		//if no left child, replace with right subtree
		else if (current.leftChild == null) {  
			if (current == root) {
				root = current.rightChild;
			}
			else if (isLeftChild) {
				parent.leftChild = current.rightChild;
			}			
			else {
				parent.rightChild = current.rightChild;
			}
		}

		else { // two children, so replace with inorder successor
			   // get successor of node to delete (current)
			Node successor = getSuccessor(current);

			// connect parent of current to successor instead
			if (current == root) {
				root = successor;
			}
			else if (isLeftChild) {
				parent.leftChild = successor;
			}
			else {
				parent.rightChild = successor;
			}

			//connect successor to current's left child
			successor.leftChild = current.leftChild;
		} // end else two children
		// (successor cannot have a left child)
		return true;              // success
	}// end delete()

	
	//returns node with next-highest value after delNode
	//goes right child, then right child's left descendants
	private Node getSuccessor(Node delNode) {
		Node successorParent = delNode;
		Node successor = delNode;
		Node current = delNode.rightChild;        // go to the right child
		while (current != null) {                 // until no more
			successorParent = successor;          // left children
			successor = current;
			current = current.leftChild;
		}

		if (successor != delNode.rightChild) {    // if successor not right child,
			//make connections
			successorParent.leftChild = successor.rightChild;
			successor.rightChild = delNode.rightChild;
		}
		return successor;
	}

	
	public void traverse(int traverseType) {
		switch (traverseType) {
		case 1:
			System.out.print("\nPreorder traversal: ");
			preOrder(root);
			break;
		case 2:
			System.out.print("\nInorder traversal: ");
			inOrder(root);
			break;
		case 3:
			System.out.print("\nPostorder traversal: ");
			postOrder(root,"");
			break;
		default:
			System.out.print("Invalid traversal type\n");
			break;
		}
		System.out.println();
	}

	
	private void preOrder(Node localRoot) {
		if (localRoot != null) {
			System.out.print(localRoot.iData + " ");	
			preOrder(localRoot.leftChild);
			preOrder(localRoot.rightChild);	
		}
	}

	
	private void inOrder(Node localRoot) {
		if (localRoot != null) {
			inOrder(localRoot.leftChild);
			System.out.print(localRoot.iData + " ");
			inOrder(localRoot.rightChild);		
		}
	}

	
	private void postOrder(Node localRoot, String code) {
            //modified to go through the tree and set up/print out the table
		if (localRoot != null) {
                        String codeL = code.concat("0");
                        String codeR = code.concat("1");
			postOrder(localRoot.leftChild, codeL);
			postOrder(localRoot.rightChild, codeR);
                        
                        if ((localRoot.Char != null )){
                                    System.out.print(" ");
                                    System.out.print(localRoot.Char);
                                    System.out.print(" = ");
                                    System.out.println(code);
                                    insertT(localRoot.Char, code);
                        }
		}
	}

	
	public void displayTree() {
		Stack<Node> globalStack = new Stack<Node>();
		globalStack.push(root);
		int nBlanks = 32;
		boolean isRowEmpty = false;
		System.out.println(
				".................................................................");
		while (isRowEmpty==false) {
			Stack<Node> localStack = new Stack<Node>();
			isRowEmpty = true;
			
			for (int j = 0; j < nBlanks; j++) {
				System.out.print(' ');
			}

			while (globalStack.isEmpty()==false) {
				Node temp = (Node) globalStack.pop();
				if (temp != null) {
					temp.displayNode();
					localStack.push(temp.leftChild);
					localStack.push(temp.rightChild);
					if (temp.leftChild != null ||
							temp.rightChild != null) {
						isRowEmpty = false;
					}
				}
				else {
					System.out.print("--");
					localStack.push(null);
					localStack.push(null);
				}

				for (int j = 0; j < nBlanks*2-2; j++) {
					System.out.print(' ');
				}
			} // end while globalStack not empty
			System.out.println();
			nBlanks /= 2;
			while (localStack.isEmpty()==false) {
				globalStack.push(localStack.pop());
			} // end while isRowEmpty is false
			System.out.println(
			".................................................................");
		}
	}  // end displayTree()
        public void displayCode(){
            //prints out/builds vode table
            System.out.println("................");
          //System.out.println(" A = 0101010");
            postOrder(root,"");
            System.out.println(
                               "................");
        } // end displayCode

        public String encode(Path input) {
            //uses code table to encode input
            String lineF = null;
            String code = "";
            int temp = 0;
            //reads in file
            try (BufferedReader reader = Files.newBufferedReader(input)){
                String line = null;
                while ((line = reader.readLine()) != null) {
                    lineF = lineF.concat(line);
                }
            } catch (IOException ex) {
                System.out.println("file not found");
            }
            //encodes file
            while(temp < lineF.length()){
                String key = String.valueOf(lineF.charAt(temp));
                code = code.concat(findChar("", key));
                
                temp = temp+1;
            }
                //returns code as string
                return code;
        }// end encode
        public String decode(String binary) {
            //decodes input string of 1,0's
            Node localRoot = root;
            String Linef = "";
            String code = null;
            int temp = 0;
                while(temp < binary.length()){
                    code = String.valueOf(binary.charAt(temp));
                    if(code.endsWith("0")){
                        localRoot = localRoot.leftChild;
                    }else{
                        localRoot = localRoot.rightChild;
                    }
                    if(localRoot.Char != null){
                        Linef = Linef.concat(localRoot.Char);
                        localRoot = root;
                    }
                    
                    temp = temp+1;
                }
            //returns decoded string        
            return Linef;
        }
}// end class Tree
////////////////////////////////////////////////////////////////

class TreeApp {

	public static void main(String[] args) throws IOException {
                   //set up tree for testing
		Tree theTree = new Tree();
                Path fIn = Paths.get("input/asdf");
		theTree.insert(50, null);
		theTree.insert(25, null);
		theTree.insert(75, null);
		theTree.insert(1, "a");
		theTree.insert(35, null);
		theTree.insert(65, "b");
		theTree.insert(85, "c");
		theTree.insert(30, "d");
		theTree.insert(45, null);
		theTree.insert(40, "e");
		theTree.insert(49, "f");
                
                //code needed once tree is set up (set up to be used in this order)
                    // displays tree
		theTree.displayTree();
                    // displays and sets up code tree 
                theTree.displayCode();
                    // encodes message
                String Encoded = theTree.encode(fIn); //replace quotes with path of input file
                System.out.println(Encoded);
                    // decodes message
                String Decoded = theTree.decode(Encoded);
                    // replace with output to file
                System.out.println(Decoded);
                
                
	} // end main()

	
	private static String getString() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String s = br.readLine();
		return s;
	}
	
	private static int getChar() throws IOException {
		String s = getString();
		return s.charAt(0);
	}
	
	private static int getInt() throws IOException {
		String s = getString();
		return Integer.parseInt(s);
	}	
}  // end TreeApp class
////////////////////////////////////////////////////////////////
