import java.util.Scanner;
import java.sql.*;
class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }
    

   

    public long open_account(String email) {
    	if(!account_exist(email))
    	{
    		String open_account_query ="INSERT INTO Accounts(account_number, full_name,email, balance, security_pin) VALUES(?,?,?,?,?)";
    		scanner.nextLine();
    		System.out.println();
    		System.out.println("Full name:");
            String full_name = scanner.nextLine();
            System.out.println("Enter initial Amount:");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("enter security pin :");
            String security_pin = scanner.nextLine();
            try {
            	long account_number = generateAccountNumber();
            	PreparedStatement preparedStatement = connection.preparedStatement(open_account_query);
            	preparedStatement.setLong(1,account_number);
            	preparedStatement.setString(2,full_name);
            	preparedStatement.setString(3,email);
            	preparedStatement.detDouble(4,balance);
            	preparedStatement.setString(5,security_pin);
            	int rowsAffected = preparedStatement.executeUpdate();
            	if(rowsAffected>0)
            	{
            		return account_number;
            	}
            	else
            	{
            		throw new RuntimeException("Account Creation failed");
            	}
            	
            }
            catch(SQLException e) {
            	e.printStackTrace();
            }
            
            
    	}
    	public long getAccount_number(String email) {
    		String query="SELECT account_number from Accounts WHERE email = ?";
    		try {
    			PreparedStatement preparedStatement = connection.preparedStatement(query);
    			preparedStatement.setString(1,email);
    			ResultSet resultSet = preparedStatement.executeQuery();
    			if(resultSet.next())
    			{
    				return resultSet.getLong("account_number");
    			}
    		}
    		catch(SQLException e) {
            	e.printStackTrace();
            }
    		throw new RuntimeException("Account Number Doesnot Exist");
    		}
    	private long generateAccountNumber() {
    		
    		try {
    			Statement statement = connection.createStatement();
    			ResultSet resultSet = statement.executeQuery("SELET account_number from Accounts ORDER BY account_number DESC LIMIT 1");
    			if(resultSet.next()) {
    				long last_account_number = resultSet.getLong("account_number");
    				return last_account_number+1;
    			}
    			else {
    				return 10000100;
    			}
    		}
    		catch(SQLException e) {
            	e.printStackTrace();
            }
    		return 10000100;
    		
    	}
    	public boolean account_exist(String email)
    	{
    		String query= "SELECT account_from Accounts WHERE email = ?";
    		try {
    			PreparedStatement preparedStatement = connection.createStatement(query);
    			preparedStatement.setString(1,email);
    			ResultSet resultSet = preparedStatement.executeQuery();
    			if(resultSet.next()) {
    				return true;
    			}
    			else {
    				return false;
    			}
    			catch(SQLException e) {
                	e.printStackTrace();
                }
    			return false;
    	}
    		}