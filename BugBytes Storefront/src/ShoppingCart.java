/**
 * A ShoppingCart is an List of Item objects, with 
 * functionality similar to that used in virtual storefronts.
 *
 * @author Jacob Normington
 * @version 3/22/2021
 */
public class ShoppingCart
{
   private String customerName;
   private String currentDate; //is this important?
   private int id; //user id
   private Connector c; 
   
   private CartNode head; //beginning of list
   private CartNode tail; //end of list
   private int size; //the total number of items in the cart

   /**
    * Default constructor for ShoppingCart objects
    */
   public ShoppingCart()
   {
      customerName = "none";
      currentDate = "January 1, 2020";
      c=Connector.getCon();
      
      head = null;  
      tail = head; 
      size = 0; 
   }
   
   public ShoppingCart(String name, String date)
   {
      customerName = name;
      currentDate = date;
      head = null;  
      tail = head; 
      size = 0;
   }
   
   public String getCustomerName()
   {
      return customerName;
   }
   
   public String getDate()
   {
      return currentDate;
   }
   
   public int getCustomerId()
   {
	   return id; 
   }
   
   /**
    * Returns the number of items in the cart
    *
    * @return    total number of items in cart
    */
   public int getCartSize()
   {
      return size;
   }
   
   /**
    * Adds a new item to the shopping cart at the end of the list
    * Not reflected in database. 
    *
    * @param  item   an item to be added to the cart
    */
   private void addItem(Item item)
   {
      if (head == null)
      {
    	  head = new CartNode(item);
    	  tail = head; 
      }
      else
      {
    	  tail.add(item);
    	  tail = tail.getNext(); 
      }
      size += item.getQuantity();
   }
   
   /**
    * Removes an item from the cart.
    * Not reflected in database.
    *
    * @param  id   	a string representing the item in the cart
    * @return       the item removed from the list, or null if not found
    */
   private Item removeItem(String id)
   {
	   if (size > 0)
	   {
		   CartNode node = head; 
		   while(node.hasNext()) //does not include last element
		   {
			   if (node.getValue().getProductId().equals(id))
		       { 
				   if(node == head)
				   {
					   head = null; 
					   node.getNext().setPrevious(null); //remove link from next node
				   }
				   else
					   node.remove();
				   size -= node.getValue().getQuantity(); 
		           return node.getValue();
		       }
			   node = node.getNext(); 
		   }
		   if (tail.getValue().getProductId().equals(id))
		   {
			   tail = null; 
			   node.getPrevious().setNext(null); //remove link from previous node
			   size -= node.getValue().getQuantity(); 
	           return node.getValue(); 
		   }
	   }
	   System.out.println("Item not found in cart. Nothing removed.");
	   return null;
   }
   
   /**
    * Alters the quantity of an item in the cart. 
    * Not reflected in database.
    * 
    * @param	id		a string representing the item in the database
    * 			amount		the number of item user wants to buy
    */
   private void changeQuantity(String id, int amount)
   {
	   if (size > 0)
	   {
		   CartNode node = head; 
		   while(node.hasNext()) //does not include last element
		   {
			   if (node.getValue().getProductId().equals(id))
		       { 
				   node.getValue().setQuantity(amount); 
		       }
			   node = node.getNext(); 
		   }
		   if (tail.getValue().getProductId().equals(id))
		   {
			   tail.getValue().setQuantity(amount); 
		   }
	   }
	   System.out.println("Item not found in cart. No change.");
   }
   
   /**
    * Modifies an item's description, price, and/or quantity.
    * Not reflected in database.
    *
    * @param   item  the item to be modified
    * @return        none
    */
   private void modifyItem(Item item) //this is probably not useful 
   {
	   CartNode node = head; 
	   while(node.hasNext())
	   {
		   if(node.getValue().getName().equals(item.getName()))
		   {
			   if(item.getDescription().equals("none"))
				   node.getValue().setDescription(item.getDescription());
	            
	            if(!(item.getQuantity() == 0))
	            	node.getValue().setQuantity(item.getQuantity());
	            
	            if(!(item.getPrice() == 0))
	            	node.getValue().setPrice((int) (item.getPrice()*100));
	            
	            return;
		   }
		   node = node.getNext(); 
	   }
	   if(tail.getValue().getName().equals(item.getName()))
	   {
		   if(item.getDescription().equals("none"))
			   tail.getValue().setDescription(item.getDescription());
            
            if(!(item.getQuantity() == 0))
            	tail.getValue().setQuantity(item.getQuantity());
            
            if(!(item.getPrice() == 0))
            	tail.getValue().setPrice((int) (item.getPrice()*100));
            
            return;
	   }
	   System.out.println("Item not found in cart. Nothing modified.");
   }
   
   /**
    * Adds an item to the shopping cart from the database. 
    * 
    * @param   	prodID		a string representing the item in the database
    * 			quantity	the quantity of that item to be purchased
    * @return        		none
    */
   public void addToCart(String prodID, int quantity)
   {
	   //find item
	   String category = c.readItem(prodID, "PRODUCT_TYPE");
	   String name = c.readItem(prodID, "PRODUCT_NAME");
	   double price = Double.parseDouble(c.readItem(prodID, "PRICE")); 
	   
	   
	   addItem(new Item(prodID, category, name, price, quantity)); //update cart
	   
	   c.addToCart(id, prodID, quantity); //update database
   }
   
   /**
    * Removes an item from the shopping cart. 
    * 
    * @param   	prodID		a string representing the item in the database
    * @return        		none
    */
   public void removeFromCart(String prodID)
   {
      removeItem(prodID); //update cart
      
      c.removeFromCart(id, prodID); //update database
   }
   
   /**
    * Changes the quantity of an item in the cart. 
    */
   public void changeCartQuantity(String prodID, int quantity)
   {
	   	changeQuantity(prodID, quantity);
	   	
	   	c.removeFromCart(id, prodID);
   }
   
   /**
    * Searches through the database for changes which need to be made to the cart
    */
   public void update()
   {
      //I don't know how to do that
   }
   
   /**
    * Determines and returns the total cost of items in cart
    *
    * @return    total check out price of the cart
    */
   public int getSubtotal()
   {
      int cartPrice = 0;
      if (size > 0)
      {
		   CartNode node = head; 
		   while(node.hasNext()) //does not include last element
		   {
			   cartPrice += node.getValue().getTotalCost(); 
			   node = node.getNext(); 
		   }
		   cartPrice += tail.getValue().getTotalCost(); 
      }
	  return cartPrice; 
   }
   
   /**
    * Prints out the cart with item prices
    */
   public void printTotal()
   {
      if(size == 0)
      {
         System.out.println("SHOPPING CART IS EMPTY");
      }
      else
      {
    	  System.out.println(customerName + "'s Shopping Cart -" + currentDate);
          System.out.println();
          System.out.println("Number of Items: " + getCartSize());
          System.out.println();
          CartNode node = head; 
          int total = 0;
          while(node.hasNext())
          {
        	  node.getValue().printItemCost(); 
        	  total += node.getValue().getTotalCost(); 
        	  node = node.getNext(); 
          }
          tail.getValue().printItemCost();
          total += tail.getValue().getTotalCost(); 
          System.out.println("Total: $" + total);
      }
   }
   
   /**
    * Outputs each item's description
    */
   public void printDescription()
   {
      if(size == 0)
      {
         System.out.println("SHOPPING CART IS EMPTY");
      }
      else
      {
    	  System.out.println(customerName + "'s Shopping Cart -" + currentDate);
    	  System.out.println();
          System.out.println("Item Descriptions");
          System.out.println();
          CartNode node = head; 
          while(node.hasNext())
          {
        	  node.getValue().printItemDescription(); 
        	  node = node.getNext(); 
          }
          tail.getValue().printItemDescription();
      }
   }
}

/**
 * Node in a doubly-linked list of Item objects
 */
class CartNode
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
