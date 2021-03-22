import java.util.ArrayList;
/**
 * A ShoppingCart is an ArrayList of ItemToPurchase objects, with 
 * functionality similar to that used in virtual storefronts.
 *
 * @author Jacob Normington
 * @version 3/22/2021
 */
public class ShoppingCart
{
   private String customerName;
   private String currentDate;
   private ArrayList<ItemToPurchase> cartItems;

   /**
    * Default constructor for ShoppingCart objects
    */
   public ShoppingCart()
   {
      customerName = "none";
      currentDate = "January 1, 2020";
      cartItems = new ArrayList<>();
   }
   
   public ShoppingCart(String name, String date)
   {
      customerName = name;
      currentDate = date;
      cartItems = new ArrayList<>();
   }
   
   public String getCustomerName()
   {
      return customerName;
   }
   
   public String getDate()
   {
      return currentDate;
   }
   
   /**
    * Adds an item to cartItems list
    *
    * @param  item   an item to be added to the cart
    */
   public void addItem(ItemToPurchase item)
   {
      cartItems.add(item);
   }
   
   /**
    * Removes an item from the cart
    *
    * @param  name   the name of the item to be removed
    * @return        the item removed from the list, or null if not found
    */
   public ItemToPurchase removeItem(String name)
   {
      for (ItemToPurchase item : cartItems)
      {
         if (item.getName().equals(name))
         {
            cartItems.remove(item);
            return item;
         }
      }
      System.out.println("Item not found in cart. Nothing removed.");
      return null; 
   }
   
   /**
    * Adds an item to the shopping cart from the database
    */
   public void addToCart()
   {
      //I don't know how to do that
   }
   
   /**
    * Modifies an item's description, price, and/or quantity
    *
    * @param   item  the item to be modified
    * @return        none
    */
   public void modifyItem(ItemToPurchase item)
   {
      for (ItemToPurchase itemX : cartItems)
      {
         if (itemX.getName().equals(item.getName()))
         {
            if(item.getDescription().equals("none"))
               itemX.setDescription(item.getDescription());
            
            if(!(item.getQuantity() == 0))
               itemX.setQuantity(item.getQuantity());
            
            if(!(item.getPrice() == 0))
               itemX.setPrice((int) (item.getPrice()*100));
            
            return;
         }
      }
      System.out.println("Item not found in cart. Nothing modified.");
   }
   
   /**
    * Returns the number of items in the cart
    *
    * @param     none
    * @return    total number of items in cart
    */
   public int getCartSize()
   {
      int itemCount = 0;
      for (ItemToPurchase item : cartItems)
      {
         itemCount += item.getQuantity();
      }
      return itemCount;
   }
   
   /**
    * Determines and returns the total cost of items in cart
    *
    * @return    total check out price of the cart
    */
   public int getSubtotal()
   {
      int cartPrice = 0;
      for (ItemToPurchase item : cartItems)
      {
         cartPrice += item.getTotalCost();
      }
      return cartPrice;
   }
   
   /**
    * Prints out the cart with item prices
    */
   public void printTotal()
   {
      if(cartItems.size() == 0)
      {
         System.out.println("SHOPPING CART IS EMPTY");
         return;
      }
      System.out.println(customerName + "'s Shopping Cart -" + currentDate);
      System.out.println();
      System.out.println("Number of Items: " + getCartSize());
      System.out.println();
      int total = 0;
      for (ItemToPurchase item : cartItems)
      {
         item.printItemCost();
         total += item.getQuantity()*item.getPrice();
      }
      System.out.println("Total: $" + total);
   }
   
   /**
    * Outputs each item's description
    */
   public void printDescription()
   {
      if(cartItems.size() == 0)
      {
         System.out.println("SHOPPING CART IS EMPTY");
         return;
      }
      System.out.println(customerName + "'s Shopping Cart -" + currentDate);
      System.out.println();
      System.out.println("Item Descriptions");
      System.out.println();
      for (ItemToPurchase item : cartItems)
      {
         item.printItemDescription();
      }
   }
}
