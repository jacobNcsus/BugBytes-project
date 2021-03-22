/**
 * ItemToPurchase is a class containing information about an item to be used in a virtual storefront. 
 *
 * @author Jacob Normington
 * @version 3/22/2021
 */
public class ItemToPurchase extends Item
{
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
      super(name, description, price);
      itemQuantity = quantity; 
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
      return getPrice()*itemQuantity;
   } 
   
   /**
    * Outputs the item name followed by the quantity, price, and subtotal
    */
   public void printItemCost()
   {
      System.out.println(getName() + " " + itemQuantity 
         + " @ $" + getPrice() + " = $" + (itemQuantity*getPrice()));
   }
   
   /**
    * Returns a 
    */
   public String toString()
   {
      return super.toString() + ""; 
   }
}
