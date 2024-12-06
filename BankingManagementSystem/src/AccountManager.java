import java.util.Scanner;
import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;

class AccountManager {
    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

 

    public void credit_money(long account_number) throws SQLException {
    	scanner.nextLine();
        System.out.println("Enter the amount to credit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("enter security pin:");
        String security_pin = scanner.nextLine();
        try {
        	connection.setAutoCommit(false);
        	if(account_number != 0)
        	{
        		 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE security_pin = ?");
        		 preparedStatement.setString(1,security_pin);
        		 ResultSet resultSet = preparedStatement.executeQuery();
        		 
        		 if(resultset.next())
        		 {
        			 String credit_query ="UPDATE Accounts SET balance = balance + ?WHERE account_number = ?");
        			 preparedStatement preparedStatement1 = connection.preparedStatement(credit_query);
        			 preparedStatement1.setDouble(1, amount);
        			 preparedStatement1.setLong(2, account_number);
        			 int rowsAffected = preparedStatement1.executeUpdate();
        			 if(rowsAffected>0)
        			 {
        				 System.out.println("rs."+amount +"credited successfully");
        				 connection.commit();
        				 connection.setAutoCommit(true);
        				 return;
        			 }
        			 else {
        				 System.out.println("Transaction Failed");
        				 connection.rollback();
        				 connection.setAutoCommit(true);
        			 }
        		 }else {
        			 System.out.println("Invalid security pin!");
        		 }
        		 
        	}
        	catch (SQLException e) {
                e.printStackTrace();
            }
        	connection.setAutoCommit(true);
        
    }
    public void debit_money(long account_number) throws SQLException {
    	scanner.nextLine();
        System.out.println("Enter the amount to debit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("enter security pin:");
        String security_pin = scanner.nextLine();
        try {
        	connection.setAutoCommit(false);
        	if(account_number !=0)
        	{
        		PreparedStatement preparedStatement = connection.preparedStatement("SELECT * FROM Accounts WHERE security_pin =?");
        		preparedStatement.setString(1,security_pin);
        		ResultSet resultSet = preparedStatement.executeQuery();
        		if(resultSet.next()) {
        			double current_balance = resultSet.getDouble("balance");
        			if(amount<=current_balance) {
        				String credit_query ="UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
        				PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
        				preparedStatement1.setDouble(1,amount);
        				preparedStatement1.setLong(2,account_number);
        				int rowsAffected =  preparedStatement1.executeUpdate();
        				if(rowsAffected>0)
        				{
        					System.out.println("rs."+amount +"debited successfully");
        					 connection.commit();
            				 connection.setAutoCommit(true);
            				 return;
            			 }
            			 else {
            				 System.out.println("Transaction Failed");
            				 connection.rollback();
            				 connection.setAutoCommit(true);
            			 }
        				
        					
        				}
        			else {
           			 System.out.println("Insufficient Balance");
           		 }
        			} else {
        				System.out.println("Invalid security pin!");
        			}
        	}
        	}
        catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
        }
        
     

    public void transfer_money(long sender_account_number) throws SQLException {
    	scanner.nextLine();
    
        System.out.println("Enter the account number to transfer to: ");
        long receiver_account_number = scanner.nextLong();
        System.out.println("Enter the amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("enter security pin:");
        String security_pin = scanner.nextLine();
        try {
        	connection.setAutoCommit(false);
        	if(sender_account_number != 0 && receiver_account_number !=0)
        	{
        		PreparedStatement preparedStatement = connection.preparedStatement("SELECT * FROM Accounts WHERE  account_number = ? AND security_pin =?");
        		preparedStatement.setLong(1,sender_account_number);
        		preparedStatement.setString(2,security_pin);
        		ResultSet resultSet = preparedStatement.executeQuery();
        		if(resultSet.next()) {
        			double current_balance = resultSet.getDouble("balance");
        			if(amount<=current_balance)
        			{
        				string debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        				string credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        				PreparedStatement creditpreparedStatement = connection.preparedStatement(credit_query);
        				PreparedStatement debitpreparedStatement = connection.preparedStatement(debit_query);
        				creditpreparedStatement.setDouble(1,amount);
        				creditpreparedStatement.setLong(2,receiver_account_number);
        				debitpreparedStatement.setDouble(1,amount);
        				debitpreparedStatement.setLong(2,sender_account_number);
        				int rowAffected1 = debitpreparedStatement.executeUpdate();
        				int rowAffected2 = creditpreparedStatement.executeUpdate();
        				if(rowAffected1 > 0 && rowAffected2 >0)
        				{
        				system.out.println("Transaction successfull");
        				connection.commit();
           				 connection.setAutoCommit(true);
           				 return;
        				}
        				else {
                  			 System.out.println("Transaction Failed");
                  			 connection.rollback();
                  			 connection.setAutoCommit(true);
                  		 }
        			}else {
        				System.out.println("Insufficient Balance");
        			}
        		} else {
        			System.out.println("Invalid security pin");
        		}
        	}
        	}
            catch (SQLException e) {
                e.printStackTrace();
            }
            connection.setAutoCommit(true);
        }
        
        
        


    public void getBalance(long account_number)  {
    	scanner.nextLine();
    	System.out.print("enter security pin:");
        String security_pin = scanner.nextLine();
        try {
        	 PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ?");
        	 preparedStatement.setLong(1,account_number);
        	 preparedStatement.setString(2, security_pin);
        	 ResultSet resultSet = preparedStatement.executeQuery();
        	 if(resultSet.next())
        	 {
        		 double balance = resultSet.getDouble("balance");
        		 System.out.println("Balance:" +balance);
        	 }
        	 else {
     			System.out.println("Invalid security pin");
     		}
        	 
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
       