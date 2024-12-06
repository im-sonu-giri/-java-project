package hotelmanagementsystem;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;

public class hotelmanagement{
	private static final String url="jdbc:mysql://localhost:3306/Hotel_db";
	private static final String username="root";
	private static final String password="sonu+1209";
	
	public static void main(String[] args)throws ClassNotFoundException, SQLException{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e)
		{
			System.out.println(e.getMessage());
		}
		try {
			Connection connection= DriverManager.getConnection(url,username,password);
			while(true)
			{
				System.out.println();
				System.out.println("       HOTEL MANAGEMENT SYSTEM     ");
				System.out.println("*----------------*---------------*");
				Scanner scanner= new Scanner(System.in);
				System.out.println("    1. Reserve a room");
				System.out.println("    2. view Reservation");
				System.out.println("    3. Get roomnumber");
				System.out.println("    4. Update Reservation");
				System.out.println("    5. Delete Reservation");
				System.out.println("    0. EXit");
				System.out.println("    choose an option:");
				int choice =scanner.nextInt();
				switch(choice)
				{
				case 1:
					reserveRoom(connection,scanner);
					break;
				case 2:
					viewReservations(connection);
					break;
				case 3:
					getRoomNumber(connection,scanner);
					break;
				case 4:
					updatereservation(connection,scanner);
					break;
				case 5:
					deleteReservation(connection,scanner);
					break;
				case 0:
					exit(scanner);
					scanner.close();
					return;
					default:
						System.out.println("Invalid choice. try again");
				}
				
			}
		}catch(SQLException e)
			{
				System.out.println(e.getMessage());
			}
			catch(InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
		private static void getRoomNumber(Connection connection, Scanner scanner) {
		try {
			System.out.print("Enter reservation Id:");
			int reservationId =scanner.nextInt();
			System.out.print("Enter guest name:");
			String guestName =scanner.next();
			
			String sql = "SELECT room_number FROM reservations " +
		             "WHERE reservation_id = " + reservationId +
		             " AND guest_name = '" + guestName + "'";
			
			try(Statement statement =connection.createStatement();
					ResultSet resultSet=statement.executeQuery(sql)){
					if (resultSet.next()) {
	                    int roomNumber = resultSet.getInt("room_number");
	                    System.out.println("Room number for reservation ID " + reservationId+
	                            " and Guest " + guestName + " is: " + roomNumber);
				}
				else {
					System.out.println("reservation not found for the given ID and guestname");
				}
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}
		private static void reserveRoom(Connection connection,Scanner scanner) {
			try {
				System.out.print("Enter guest name:");
				String guestName= scanner.next();
				scanner.nextLine();
				System.out.print("Enter room number:");
				int roomNumber =scanner.nextInt();
				System.out.print("Enter contact number:");
				String contactNumber =scanner.next();
				String sql="INSERT INTO reservations(guest_name,room_number,contact_number)" + 
				"VALUES('"+guestName + "' , " + roomNumber + ",' " + contactNumber +"')";
				try(Statement statement =connection.createStatement())
				{
					int affectedRows = statement.executeUpdate(sql);
					if (affectedRows > 0)
					{
						System.out.println("Reservation successfull");
					}
					else {
						System.out.println("Reservation failed.");
					}
				}
			}
				
				catch(SQLException e)
				{
					e.printStackTrace();
					
				}
			}
			private static void viewReservations(Connection connection) throws SQLException
			{
				String sql="SELECT reservation_id, guest_name, room_number, contact_number,reservation_date FROM reservations";
				try(Statement statement = connection.createStatement(); 
						ResultSet  resultset =statement.executeQuery(sql))
				{
					System.out.println("Current Reservations:");
					System.out.println("+---------------+-------------------+----------------+----------------+-----------------------+");
					System.out.println("| Reservation ID  | Guest            |Room Number     |contact Number  |    Reservation Date  ");
					System.out.println("+---------------+-------------------+----------------+----------------+------------------------+");
					while(resultset.next())
					{
						int reservationId = resultset.getInt("reservation_id");
						String guestName = resultset.getString("guest_name");
						int roomNumber = resultset.getInt("room_number");
						String contactNumber = resultset.getString("contact_number");
						String reservationDate = resultset.getTimestamp("reservation_date").toString();
						//format and display the reservation data in a table-like format
						System.out.printf("|%-14d |%-15s |%-13d| %-20s |%-19s |\n" ,
						reservationId ,guestName,roomNumber,contactNumber,reservationDate);
					}
					System.out.println("+---------------+-------------------+----------------+----------------+------------------------+");
				}
			}
				
				private static  void updatereservation(Connection connection ,Scanner scanner){
					try {
						System.out.print("enter reservation id to update");
						int reservationId = scanner.nextInt();
						scanner.nextLine();
						if(!reservationExists(connection,reservationId))
						{
							System.out.println("Reservation not found for the given ID");
							return;
						}
						System.out.println("enter new guest name:");
						String newGuestName = scanner.nextLine();
						System.out.println("Enter new room number:");
						String newRoomNumber = scanner.nextLine();
						System.out.println("enter a new contact number:");
						String newContactNumber =scanner.next();
						String sql ="UPDATE reservations SET guest_name= '" + newGuestName +"' ," +
						"room_number ="+ newRoomNumber +", "+
								 "contact_number = '"+newContactNumber +"' "+
						"WHERE reservation_id ="+ reservationId;
						
						 try(Statement statement = connection.createStatement())
						 {
							 int affectedRows = statement.executeUpdate(sql);
							 if(affectedRows > 0)
							 {
								 System.out.print("Reservation updated successfully!");
								 
							 }
							 else {
								 System.out.print("Reservation update failed.");
							 }
						 }
						
						
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
				}
				private static void deleteReservation(Connection connection,Scanner scanner)
				{
					try {
						System.out.print("enter reservation ID to delete:");
						int reservationID = scanner.nextInt();
						
						if(!reservationExists(connection,reservationID))
						{
							System.out.println("reservation not found for the given ID.");
							return;
						}
						String sql="DELETE FROM reservations WHERE reservation_id = "+reservationID;
						try(Statement statement = connection.createStatement())
						{
							int affectedRows = statement.executeUpdate(sql);
							if(affectedRows >0)
							{
								System.out.print("reservation deleted successfully!");
								
							}
							else {
								System.out.println("reservation deletion failed");
							}
						}
					}
					catch(SQLException e)
					{
						e.printStackTrace();
						
					}
				}
				private static boolean reservationExists(Connection connection,int reservationID)
				{
					try {
						String sql ="SELECT reservation_id FROM reservations WHERE reservation_id = "+reservationID;
						try(Statement statement = connection.createStatement();
								ResultSet resultset = statement.executeQuery(sql))
						{
							return resultset.next();
						}
					}
					catch(SQLException e)
					{
						e.printStackTrace();
						return false;
					}
				}
				public static void exit(Scanner scanner) throws InterruptedException
				{
					System.out.print("Exiting System");
					int i =5;
					while(i!=0) {
						System.out.print(".");
						Thread.sleep(450);
						i--;
						
				}
					System.out.println();
					System.out.println("thankyou for using HOTEL MANAGEMENT SYSTEM !!!");
				
	}
}
