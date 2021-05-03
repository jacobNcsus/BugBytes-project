/**
 * An item of inventory for a store. 
 *
 * @author Jacob Normington
 * @version 3/22/2021
 */
public class Item 
{
	   private String id; //database product id
	   private String category;
	   private String itemName;
	   private int itemPrice; //in USD cents 
	   private int itemQuantity;

	   /**
	    * Default constructor for objects of ItemToPurchase class, 
	    * sets all fields to 0 or equivalent.
	    */
	   public Item()
	   {
		   itemName = null;
		   itemPrice = 0;
		   itemQuantity = 0;
	   }
	   
	   /**
	    * Constructor for an item not specifying id or quantity, for use in adding an item to inventory.
	    * 
	    * @param 	category	the item's type: dairy, produce, etc.
	    * @param 	name		the name of the item
	    * @param 	price		the item's price is USD
	    */
	   public Item(String category, String name, double price)
	   { 
	      this.category = category;
	      itemName = name;
	      itemPrice = (int) (price*100); //round
	      itemQuantity = 0;
	   }
	   
	   public Item(String id, String category, String name, double price, int quantity)
	   {
	      this.id = id; 
	      this.category = category;
	      itemName = name;
	      itemPrice = (int) (price*100); //round
	      itemQuantity = quantity;
	   }
	   
	   public String getProductId()
	   {
		   return id; 
	   }

	   public String getName()
	   {
	      return itemName;
	   }
	   public void setName(String name)
	   {
	      itemName = name;
	   }
	   
	   public String getCategory()
	   {
	      return category;
	   }
	   public void setCategory(String category)
	   {
	      this.category = category;
	   }

	   /**
	    * Returns the item's price in USD. 
	    */
	   public double getPrice()
	   {
	      return itemPrice/100.0;
	   }
	   /**
	    * Changes the price of the item.  
	    * 
	    * @param	price	the item's value in USD cents. 
	    */
	   public void setPrice(int price)
	   {
	      itemPrice = price;
	   }
	   
	   public int getQuantity()
	   {
	      return itemQuantity;
	   }
	   public void setQuantity(int quantity)
	   {
	      itemQuantity = quantity;
	   }
	   
	   /**
	    * Returns the item price times quantity. 
	    */
	   public double getTotalCost()
	   {
	      return itemQuantity/100.0*itemPrice;
	   } 
	   
	   /**
	    * Outputs the item name followed by the quantity, price, and subtotal
	    */
	   public void printItemCost()
	   {
	      System.out.println(getName() + " " + itemQuantity 
	         + " @ $" + itemPrice + " = $" + getTotalCost());
	   }
	   
	   /**
	    * Returns a string description of this Item.
	    */
	   public String toString()
	   {
	      return itemQuantity + " " + itemName + " @ " + itemQuantity;
	   }
	}