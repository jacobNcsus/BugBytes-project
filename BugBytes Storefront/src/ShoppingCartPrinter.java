import java.util.Scanner;
/**
 * Write a description of class Template here.
 *
 * @author Jacob Normington
 * @version 2/10/2020
 */
public class ShoppingCartPrinter
{
     /**
    * An example of a method - replace this comment with your own
    */
   public static void main(String args[])
   {
      ItemToPurchase item1 = new ItemToPurchase();
      Scanner input = new Scanner(System.in);
      System.out.println("Item 1");
      System.out.println("Enter the item name:");
      item1.setName(input.next());
      System.out.println("Enter the item price:");
      item1.setPrice(input.nextInt());
      System.out.println("Enter the item quantity:");
      item1.setQuantity(input.nextInt());

      
      ItemToPurchase item2 = new ItemToPurchase();
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
      System.out.println("Total: $" + ((item1.getQuantity()*item1.getPrice())
          + (item2.getQuantity()*item2.getPrice())));
      
      input.close();
   }
}
