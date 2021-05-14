package main;

/**
 * 	Node in a doubly-linked list of Item objects
 *  *
 * 	@author Jacob Normington
 * 	@version 5/11/2021
 */
	public class CartNode
	{
		private CartNode previous; 
		private Item value;
		private CartNode next; 
		
		public CartNode(Item val)
		{
			value = val; 
			previous = null; 
			next = null; 
		}
		
		public CartNode(Item val, CartNode previous)
		{
			value = val; 
			this.previous = previous; 
		}
		
		public void add(Item next)
		{	
			this.next = new CartNode(next, this); 
		}
		
		/**
		 * Removes a node from the list
		 *
		 * @throws	IllegalArgumentException
		 */
		public void remove()
		{
			if (previous != null && next != null) //not the first element
			{
				previous.next = next; //link previous to next
				next.previous = previous; //link next back to previous
			}
			else
				throw new IllegalArgumentException("Cannot remove head or tail of list"); 
		}

		public CartNode getPrevious() {
			return previous;
		}
		public void setPrevious(CartNode previous) {
			this.previous = previous;
		}

		public Item getValue() {
			return value;
		}
		public void setValue(Item value) {
			this.value = value;
		}

		public boolean hasNext() {
			return next != null; 
		}
		public CartNode getNext() {
			return next;
		}
		public void setNext(CartNode next) {
			this.next = next;
		}

	}


