/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

/**
 *
 * @author x13r229
 */
public class Huffman {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String input = "go go gophers";
        int [] nCount = new int[128];
        FrequencyTable ft = new FrequencyTable(-1,-1);
        FrequencyTable ftHold1, ftHold2;
        int currentAscii;
        
        
        // frequency count
        for (int j = 0; j < input.length(); j++)
        {
            nCount[(int)input.charAt(j)]++;
        }
        
        // implement a priority queue
        for (int j = 0; j < 128; j++)
        {
            currentAscii = j;
            // append it to the Frequency Table
            if (nCount[currentAscii] > 0)
            {
                ft.append(new FrequencyTable(currentAscii, nCount[currentAscii]));
            }
        }
        
        // iterate to get rid of the first thing
        ft = ft.remove();
        ft.print();
        
        while (ft.getLength(0) > 2)
        {
            // remove two lowest frequency items from the table
            ftHold1 = ft;
            ft = ft.remove();
            ftHold2 = ft;
            ft = ft.remove();
            
            // create a new node and let this be the children
            Node n = new Node();
            n.Char = null;
            n.leftChild = ftHold1.getNode();
            n.rightChild = ftHold2.getNode();
            n.iData = ftHold1.getFrequency() + ftHold2.getFrequency();
            
            ftHold1 = null;
            ftHold2 = null;
            
            ft.append(new FrequencyTable(n));
            System.out.println("\n*********Iteration************\n");
            ft.print();
        }
        
        Node root = new Node();
        root.leftChild = ft.getNode();
        ft = ft.remove();
        root.rightChild = ft.getNode();
        root.iData = root.leftChild.iData + root.rightChild.iData;
        Tree theTree = new Tree(root);
        
        
    }
    
}
