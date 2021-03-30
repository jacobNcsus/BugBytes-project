import java.util.Scanner;
/**
 * Test code for shopping cart
 *
 * @author Jacob Normington
 * @version 2/10/2020
 */
public class ShoppingCartPrinter
{
   public static void main(String args[])
   {
      Item item1 = new Item();
      Scanner input = new Scanner(System.in);
      System.out.println("Item 1");
      System.out.println("Enter the item name:");
      item1.setName(input.next());
      System.out.println("Enter the item price:");
      item1.setPrice(input.nextInt());
      System.out.println("Enter the item quantity:");
      item1.setQuantity(input.nextInt());

      
      Item item2 = new Item();
      System.out.println();
      System.out.println("Item 2");
      System.out.println("Enter the item name:");
      item2.setName(input.next());
      System.out.println("Enter the item price:");
      item2.setPrice(input.nextInt());
      System.out.println("Enter the item quantity:");
      item2.setQuantity(input.nextInt());
      
      System.out.println();
      System.out.println("TOTAL COST");
      item1.printItemCost();
      item2.printItemCost();
      System.out.println();
      System.out.println("Total: $" + item1.getTotalCost() + item2.getTotalCost() );
      
      input.close();
   }
}
