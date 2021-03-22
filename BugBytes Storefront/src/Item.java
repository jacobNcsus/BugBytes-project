/**
 * A generic object of an item for sale. 
 *
 * @author Jacob Normington
 * @version 3/22/2021
 */
public abstract class Item 
{
	   private int id; //are we going to actually use this?
	   private String itemName;
	   private String category; 
	   private String itemDescription;
	   private int itemPrice; //in USD cents 

	   /**
	    * Default constructor for objects of ItemToPurchase class, 
	    * sets all fields to 0 or equivalent.
	    */
	   public Item()
	   {
	      new ItemToPurchase("none", "none", 0, 0);
	   }
	   
	   public Item(String name, String description, int price)
	   {
	      itemName = name;
	      itemDescription = description;
	      itemPrice = price;
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
	   
	   public String getDescription()
	   {
	      return itemDescription;
	   }
	   public void setDescription(String description)
	   {
	      itemDescription = description;
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
	   
	   /**
	    * Outputs the item name and description
	    */
	   public String toString()
	   {
	      return itemName + ": " + itemDescription;
	   }
	   
	   /**
	    * Prints to console the item name and description
	    */
	   public void printItemDescription()
	   {
	      System.out.println(toString()); 
	   }
	}