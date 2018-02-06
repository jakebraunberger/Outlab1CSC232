/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package huffman;

import java.util.Comparator;

/**
 *
 * @author x13r229
 */
public class FrequencyTable {
    private int ascii;
    private int frequency;
    private FrequencyTable next;
    private FrequencyTable prev;
    private Node node;
    
    public FrequencyTable(int a, int f)
    {
        ascii = a;
        frequency = f;
        next = null;
        prev = null;
        node = new Node();
        node.Char = Character.toString((char)a);
        node.iData = f;
        node.leftChild = null;
        node.rightChild = null;
    }
    
    public FrequencyTable(Node n)
    {
        node = n;
        frequency = n.iData;
        next = null;
        prev = null;
        if (node.Char != null)
            ascii = (int)(node.Char.charAt(0));
    }
    
    public void append(FrequencyTable f)
    {
        if (f.frequency <= this.frequency)
        {
            // not
            f.prev = this.prev;
            prev.next = f;
            this.prev = f;
            f.next = this;
        }
        if (f.frequency > this.frequency)
        {
            // continue down the queue if possible
            if (next == null)
            {
                next = f;
                f.prev = this;
            }
            
            // if possible, tell the next one to append
            else
            {
                next.append(f);
            }
        }
    }
    
    public void print()
    {
        System.out.println("ascii: " + (char)ascii + "\t freq: " + frequency);
        
        if (next == null)
            return;
        else
            next.print();
    }
    
    public FrequencyTable getNext()
    {
        return next;
    }
    
    public FrequencyTable remove()
    {
        FrequencyTable hold = next;
        if (prev != null)
        {
            prev.next = next;
        }
        if (next != null)
        {
            next.prev = prev;
        }
        prev = null;
        next = null;
        return hold;
    }
    
    public Node getNode()
    {
        return node;
    }
    
    public int getFrequency()
    {
        return node.iData;
    }
    
    public int getLength(int i)
    {
        if (next == null)
            return ++i;
        else
            return next.getLength(++i);
    }
    
}
