/**
 * ItemToPurchase is a class containing information about an item to be used in a virtual storefront. 
 *
 * @author Jacob Normington
 * @version 2/10/2020
 */
public class ItemToPurchase
{
   private String itemName;
   private String itemDescription;
   private int itemPrice;
   private int itemQuantity;

   /**
    * Default constructor for objects of ItemToPurchase class, 
    * sets all fields to 0 or equivalent.
    */
   public ItemToPurchase()
   {
      new ItemToPurchase("none", "none", 0, 0);
   }
   
   public ItemToPurchase(String name, String description, int price, int quantity)
   {
      itemName = name;
      itemDescription = description;
      itemPrice = price;
      itemQuantity = quantity;
   }
   
   /**
    * An example of a method - replace this comment with your own
    */
   public static void main(String args[])
   {
        
   }
    
   /**
    * An example of a method - replace this comment with your own
    *
    * @param      none
    * @return     name of the item
    */
   public String getName()
   {
      return itemName;
   }
   public void setName(String name)
   {
      itemName = name;
   }
   
   public String getDescription()
   {
      return itemDescription;
   }
   public void setDescription(String description)
   {
      itemDescription = description;
   }

   public int getPrice()
   {
      return itemPrice;
   }
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
    * Outputs the item name followed by the quantity, price, and subtotal
    */
   public void printItemCost()
   {
      System.out.println(itemName + " " + itemQuantity 
         + " @ $" + itemPrice + " = $" + (itemQuantity*itemPrice));
   }
   
   /**
    * Outputs the item name and description
    */
   public void printItemDescription()
   {
      System.out.println(itemName + ": " + itemDescription);
   }
}
