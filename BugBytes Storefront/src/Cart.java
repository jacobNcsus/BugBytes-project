//Youser Alalusi
package Cart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class Cart {
    
    private Connection myConn;
    private String url = "jdbc:mysql://localhost:3306/shop_test";
    private String username = "root";
    private String password = "root"; 

    
    public Cart() {
        try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                myConn = DriverManager.getConnection(url, username, password);
        } catch (Exception e){
                e.printStackTrace();
        }
    }
    
    public static void main (String[] args) {
        Cart cart = new Cart();
        
        cart.viewAisle("Alcohol");
//        cart.addToCart(1, "ALC01", 2);
//        cart.removeFromCart(1, "ALC01");
//        cart.getCartItems(1);
//        cart.CONFIRM_ORDER(1);
        
    }
    
    public void close()	{
            try {
                myConn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
	}
	
    public void viewAisle(String prodType) {
        try {
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from products WHERE PRODUCT_TYPE='"+prodType+"'");
            while(myRs.next()) {
                System.out.println(myRs.getString("PRODUCT_ID") + ", " + myRs.getString("PRODUCT_TYPE") + ", " + myRs.getString("NAME") + ", " + myRs.getString("PRICE") + ", " + myRs.getString("QUANTITY") + ", " + myRs.getString("REORDER"));
            }
            myStmt.close(); 
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    public void addToCart(int custID, String prodID, int quantity) {
        // Retrieve product details
        String name = "";
        Double price = 0.0;
        try {
            Statement myStmt = myConn.createStatement();
            String statementText = "SELECT NAME, PRICE, QUANTITY FROM products WHERE PRODUCT_ID=\"" + prodID + "\"";
            ResultSet myRs = myStmt.executeQuery(statementText);
            myRs.next();
            name = myRs.getString("NAME");
            price = myRs.getDouble("PRICE");       
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Add to cart
        try {
            Statement myStmt = myConn.createStatement();
            String statementText = "INSERT INTO cart(CUSTOMER_ID_CARD, PRODUCT_ID, NAME, QUANTITY, TOTAL_COST) VALUES(" + custID + ", \"" + prodID + "\", \"" + name + "\", " + quantity + ", \"$" + (price * quantity) + "\")";
            myStmt.executeUpdate(statementText);
            myStmt.close(); 
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
    
    public void removeFromCart(int custID, String prodID) {
        try {
            Statement myStmt = myConn.createStatement();
            String statementText = "DELETE FROM cart WHERE PRODUCT_ID=\"" + prodID + "\" AND CUSTOMER_ID_CARD=\"" + custID + "\""; 
            myStmt.executeUpdate(statementText);
            myStmt.close(); 
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
    
    public void getCartItems(int custID) {
        try {
            Statement myStmt = myConn.createStatement();
            ResultSet myRs = myStmt.executeQuery("SELECT * FROM cart WHERE CUSTOMER_ID_CARD=\"" + custID + "\"");
            while(myRs.next()) {
                System.out.println(myRs.getString("CUSTOMER_ID_CARD") + ", " + myRs.getString("PRODUCT_ID") + ", " + myRs.getString("NAME") + ", " + myRs.getString("QUANTITY") + ", " + myRs.getString("TOTAL_COST"));
            }
            myStmt.close(); 
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
    public void CONFIRM_ORDER(int custID) {
        
        try {
            Statement myStmt = myConn.createStatement();
            String statementText = "SELECT c.PRODUCT_ID, p.NAME, c.QUANTITY, p.QUANTITY AS stockRemaining FROM cart c LEFT JOIN products p ON c.PRODUCT_ID = p.PRODUCT_ID WHERE CUSTOMER_ID_CARD=\"" + custID+ "\"";
            ResultSet myRs = myStmt.executeQuery(statementText);
            while(myRs.next()) {
                System.out.println();
                if (myRs.getInt("stockRemaining") < myRs.getInt("QUANTITY")) {
                    System.out.println("Insufficient inventory");
                } else {
                    System.out.println("Order placed");
                    // call order.placeOrder(custID) method
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}