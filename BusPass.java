package buspass;
import java.sql.*;
import java.util.Scanner;
public class BusPass {
	    private static final String url = "jdbc:mysql://localhost:3306/Buspass";
	    private static final String user = "root";
	    private static final String password= "Rootnancy";

	    public static void main(String[] args) {
	        try 
	             (Scanner sc = new Scanner(System.in)) {
	            	 
	            	 Connection conn = DriverManager.getConnection(url, user, password);
	                 System.out.println(" Connected to MySQL successfully!");
	            while (true) {
	                System.out.println("\n=== Bus Pass Management ===");
	                System.out.println("1. Issue New Bus Pass");
	                System.out.println("2. Renew Bus Pass");
	                System.out.println("3. Verify Bus Pass");
	                System.out.println("4. Deactivate Pass");
	                System.out.println("5. List All Active Passes");
	                System.out.println("6. Search Pass by Name");
	                System.out.println("7. Exit");
	                System.out.print("Choose an option: ");
	                int choice = sc.nextInt();
	                sc.nextLine(); 
	                switch (choice) {
	                    case 1 -> issuePass(conn, sc);
	                    case 2 -> renewPass(conn, sc);
	                    case 3 -> verifyPass(conn, sc);
	                    case 4 -> deactivatePass(conn, sc);
	                    case 5 -> listActivePasses(conn);
	                    case 6 -> searchByName(conn, sc);
	                    case 7 -> {
	                        System.out.println("Exiting");
	                        return;
	                    }
	                    default -> System.out.println("Invalid choice.");
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    private static void issuePass(Connection conn, Scanner sc) throws SQLException {
	        System.out.print("Enter Passenger Name: ");
	        String name = sc.nextLine();
	        System.out.print("Enter expiry date (YYYY-MM-DD): ");
	        String expiry = sc.nextLine();

	        String sql = "INSERT INTO bus_pass (name, issue_date, expiry_date) VALUES (?, CURDATE(), ?)";
	        try (PreparedStatement pst = conn.prepareStatement(sql)) {
	            pst.setString(1, name);
	            pst.setString(2, expiry);
	            int rows = pst.executeUpdate();
	            System.out.println(rows > 0 ? " Bus pass issued successfully!" : " Failed to issue pass.");
	        }
	    }

	    
	    private static void renewPass(Connection conn, Scanner sc) throws SQLException {
	        System.out.print("Enter Pass ID to renew: ");
	        int PassId = sc.nextInt();
	        sc.nextLine();
	        System.out.print("Enter new expiry date (YYYY-MM-DD): ");
	        String expiry = sc.nextLine();

	        String sql = "UPDATE bus_pass SET expiry_date = ?, Active = TRUE WHERE PassId = ?";
	        try (PreparedStatement pst = conn.prepareStatement(sql)) {
	            pst.setString(1, expiry);
	            pst.setInt(2, PassId);
	            int rows = pst.executeUpdate();
	            System.out.println(rows > 0 ? " Bus pass renewed successfully!" : "Pass not found.");
	        }
	    }

	    
	    private static void verifyPass(Connection conn, Scanner sc) throws SQLException {
	        System.out.print("Enter Pass ID to verify: ");
	        int passId = sc.nextInt();

	        String sql = "SELECT Name, expiry_date, Active FROM bus_pass WHERE PassId = ?";
	        try (PreparedStatement pst = conn.prepareStatement(sql)) {
	            pst.setInt(1, passId);
	            try (ResultSet rs = pst.executeQuery()) {
	                if (rs.next()) {
	                    String name = rs.getString("Name");
	                    Date expiry = rs.getDate("expiry_date");
	                    boolean active = rs.getBoolean("Active");

	                    System.out.println("Passenger: " + name);
	                    System.out.println("Expiry Date: " + expiry);
	                    System.out.println("Status: " + (active ? "Active" : "Inactive"));

	                    if (expiry.before(new java.util.Date())) {
	                        System.out.println(" This pass is expired!");
	                    }
	                } else {
	                    System.out.println(" Pass ID not found.");
	                }
	            }
	        }
	    }
	    private static void deactivatePass(Connection conn, Scanner sc) throws SQLException {
	        System.out.print("Enter Pass ID to deactivate: ");
	        int passId = sc.nextInt();

	        String sql = "UPDATE bus_pass SET Active = FALSE WHERE PassId = ?";
	        try (PreparedStatement pst = conn.prepareStatement(sql)) {
	            pst.setInt(1, passId);
	            int rows = pst.executeUpdate();
	            System.out.println(rows > 0 ? " Pass deactivated successfully!" : " Pass not found.");
	        }
	    }
	    private static void listActivePasses(Connection conn) throws SQLException {
	        String deactivateSQL = "UPDATE bus_pass SET Active = FALSE WHERE expiry_date < CURDATE()";
	        try (PreparedStatement pst = conn.prepareStatement(deactivateSQL)) {
	            pst.executeUpdate();
	        }

	        String sql = "SELECT PassId, Name, expiry_date FROM bus_pass WHERE Active = TRUE";
	        try (PreparedStatement pst = conn.prepareStatement(sql);
	             ResultSet rs = pst.executeQuery()) {

	            System.out.println("\n--- Active Bus Passes ---");
	            while (rs.next()) {
	                System.out.printf("ID: %d | Name: %s | Expiry: %s%n",
	                        rs.getInt("PassId"), rs.getString("Name"), rs.getDate("expiry_date"));
	            }
	        }
	    }

	     private static void searchByName(Connection conn, Scanner sc) throws SQLException {
	        System.out.print("Enter passenger name to search: ");
	        String name = sc.nextLine();

	        String sql = "SELECT PassId, Name, expiry_date, Active FROM bus_pass WHERE Name LIKE ?";
	        try (PreparedStatement pst = conn.prepareStatement(sql)) {
	            pst.setString(1, "%" + name + "%");
	            try (ResultSet rs = pst.executeQuery()) {
	                System.out.println("\n--- Search Results ---");
	                while (rs.next()) {
	                    System.out.printf("ID: %d | Name: %s | Expiry: %s | Status: %s%n",
	                            rs.getInt("PassId"),
	                            rs.getString("Name"),
	                            rs.getDate("expiry_date"),
	                            rs.getBoolean("Active") ? "Active" : "Inactive");
	                }
	            }
	        }
	        
	    }
	
}
	            
