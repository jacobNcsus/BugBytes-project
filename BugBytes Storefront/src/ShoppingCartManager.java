import java.util.Scanner;
/**
 * Implements a menu to manipulate a shopping cart via console input
 *
 * @author Jacob Normington
 * @version 2/13/2020
 */
public class ShoppingCartManager
{
   /**
    * Begins the manager to create your shopping cart by system input
    */
   public static void main(String args[])
   {
        Scanner in = new Scanner(System.in);
        System.out.println("Shopping Cart Manager");
        System.out.println("...");
        System.out.println("Please enter your name.");
        String name = in.nextLine();
        System.out.println("Customer Name: " + name);
        System.out.println("...");
        System.out.println("Please enter today's date.");
        String date = in.nextLine();
        System.out.println("Current Date: " + date);
        System.out.println("...");
        ShoppingCart cart = new ShoppingCart(name, date);
        printMenu(cart);
        
        in.close();
   }
    
   /**
    * Outputs a menu of options that can be used to manipulate a
    * shopping cart
    *
    * @param   cart  the shopping cart to be modified
    * @return        none
    */
   public static void printMenu(ShoppingCart cart)
   {
      Scanner in = new Scanner(System.in);
      System.out.println();
      System.out.println("MENU");
      System.out.println("a - Add item to cart");
      System.out.println("d - Remove item from cart");
      System.out.println("c - Change item quantity");
      System.out.println("i - Output item's descriptions");
      System.out.println("o - Output shopping cart");
      System.out.println("q - Quit");
      System.out.println("");
      System.out.println("Choose an option: ");
      String next = in.nextLine();
      
      if(!next.equals( "q")) //quit
      {
         if(next.equals("a")) //add an item to cart
         {
            System.out.println("ADD ITEM TO CART");
            System.out.println("Enter the item name:");
            String name = in.nextLine();
            System.out.println("Enter the item description:");
            String description = in.nextLine();
            System.out.println("Enter the item price:");
            int price = java.lang.Integer.parseInt(in.next());
            System.out.println("Enter the item quantity:");
            int quantity = java.lang.Integer.parseInt(in.next());
            cart.addItem(new ItemToPurchase(name, description, price, quantity));
         }
         if(next.equals("d")) //remove an item from cart
         {
            System.out.println("REMOVE ITEM FROM CART");
            System.out.println("Enter name of item to remove:");
            cart.removeItem(in.nextLine());
         }
         if(next.equals("c")) //change item quantity
         {
            System.out.println("CHANGE ITEM QUANTITY");
            ItemToPurchase item = new ItemToPurchase();
            System.out.println("Enter the item name:");
            item.setName(in.nextLine());
            System.out.println("Enter the new quantity:");
            item.setQuantity(in.nextInt());
            cart.modifyItem(item);
         }
         if(next.equals("i")) //outputs item's descriptions
         {
            System.out.println("OUTPUT ITEM'S DESCRIPTIONS");
            cart.printDescription();
         }
         if(next.equals("o")) //outputs shopping cart
         {
            System.out.println("OUTPUT SHOPPING CART");
            System.out.println();
            cart.printTotal();
         }
         printMenu(cart);
      }
      in.close();
   }
}
