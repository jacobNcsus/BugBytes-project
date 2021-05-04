/**
 * An item of inventory for a store. 
 *
 * @author Jacob Normington
 * @version 5/3/2021
 */
public class Item 
{
	   private String id; //database product id
	   private String category;
	   private String itemName;
	   private int itemPrice; //in USD cents 
	   private int itemQuantity;

	   /**
	    * Constructor for an item not specifying id or quantity, for use in adding an item to inventory.
	    * 
	    * @param 	category	the item's type: dairy, produce, etc.
	    * @param 	name		the name of the item
	    * @param 	price		the item's price is USD
	    */
	   public Item(String category, String name, double price)
	   { 
		   id = null;
		   this.category = category;
		   itemName = name;
		   itemPrice = (int) (price*100); //round
		   itemQuantity = 0;
	   }
	   
	   /**
	    * Full constructor of item, specifying all necessary quantities
	    * 
	    * @param 	id			a String representation of an item's unique identifier
	    * @param 	category	a String representing the type of product
	    * @param 	name		the item's name
	    * @param 	price		the unit price of the item
	    * @param 	quantity	the number of this item the customer wishes to buy
	    */
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
	    * Returns a string description of this Item.
	    */
	   public String toString()
	   {
	      return itemName + " " + itemQuantity + " @ " + getPrice();
	   }
	}