//Brian Duong
//cs1468

package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinearList<E extends Comparable<E>> implements LinearListADT<E>{

	@SuppressWarnings("hiding")
	class Node<E>{
		E data;
		Node <E> next;	//pointer
		Node <E> prev;
		public Node(E obj) {
			data = obj;
			next = prev = null;
		}
	}
	
	private Node<E> head;
	private Node<E> tail;
	private int currentSize;
	protected long modificationCounter;
	
	public LinearList() {
		head = tail = null;
		currentSize = 0;
		modificationCounter = 0;
	}
	
	@Override
	public boolean addFirst(E obj) {
		Node <E> newNode = new Node<E>(obj);
		if(!isEmpty()) {
			newNode.next = head;
			head.prev = newNode;
			head = newNode;
			currentSize++;
			modificationCounter++;
		}
		else {
			newNode.next = head;
			head = tail = newNode;
			currentSize++;
			modificationCounter++;
			return true;
		}
		return false;
	}

	@Override
	public boolean addLast(E obj) {
		Node <E> newNode = new Node<E>(obj);
		if(isEmpty()) {
			addFirst(obj);		
		}
		else {
			tail.next = newNode;
			newNode.prev = tail;
			tail = newNode;
			currentSize++;	
			modificationCounter++;
			return true;
		}
		return false;
	}

	@Override
	public E removeFirst() {
		if(isEmpty()) {
			return null;
		}
		E temp = head.data;
		if(head == tail) {
			head = tail = null;
		}
		else {
			head = head.next;
			head.prev = null;
		}
		currentSize--;
		modificationCounter++;
		return temp;
	}

	@Override
	public E removeLast() {
		if(isEmpty()) {
			return null;
		}
		E temp = head.data;
		if(head == tail) {				//one node
			return removeFirst();
		}
		else {
			tail = tail.prev;
			tail.next = null;
		}
		currentSize--;
		modificationCounter++;
		return temp;
	}

	@Override
	public E remove(E obj) {
		Node <E> current = head;
		
		while(current != null) {
			if(((Comparable<E>)current.data).compareTo(obj) == 0) {
				if(current == head) {		//if obj is at front
					return removeFirst();
				}
				if(current == tail) {		//if obj is at rear
					return removeLast();
				}
				currentSize--;
				current.prev.next = current.next;		//changes the current pointer to the next
				current.next.prev = current.prev;		//changes the current pointer to the previous
				modificationCounter++;
				return current.data;			
			}
			current = current.next;	
		}
		return null;
	}

	@Override
	public E peekFirst() {
		if(isEmpty()) {
			return null;
		}
		return head.data;
	}

	@Override
	public E peekLast() {
		if(isEmpty()) {
			return null;
		}
		return tail.data;
	}

	@Override
	public boolean contains(E obj) {
		Node <E> current = head;
		while(current != null) {
			if(((Comparable<E>)current.data).compareTo(obj) == 0) {
				return true;
			}
			current = current.next;
		}
		return false;
	}

	@Override
	public E find(E obj) {
		Node <E> current = head;
		while(current != null) {
			if(((Comparable<E>)current.data).compareTo(obj) == 0) {
				return current.data;
			}
			current = current.next;
		}
		return null;
	}

	@Override
	public void clear() {
		currentSize = 0;
		head = tail = null;
		modificationCounter++;
	}

	@Override
	public boolean isEmpty() {
		return (head == null);
	}

	@Override
	public boolean isFull() {
		return false;
	}

	@Override
	public int size() {
		return currentSize;
	}

	@Override
	public Iterator<E> iterator() {
		return new IteratorHelper();
	}
	
	private class IteratorHelper implements Iterator<E>
	{
		Node <E> current;
		long stateCheck;
		
		public IteratorHelper() {
			current = head;
			stateCheck = modificationCounter;
		}
	
		public boolean hasNext() {
			if(stateCheck != modificationCounter) {
				throw new ConcurrentModificationException();
			}
			return current != null;
		}
		
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
		    E temp = current.data;
		    current = current.next;
		    return temp;
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
