package BankingManagementSystem;
import java.sql.*;
import java.util.Scanner;
import java.util.scanner;
public class BankingApp {

	public class hotelmanagement{
		private static final String url="jdbc:mysql://localhost:3306/banking_system";
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
				Connection connection = DriverManager.getConnection(url,username,password);
				Scanner scanner = new Scanner(System.in);
				User user = new User(connection,scanner);
				Accounts accounts= new Accounts(connection,scanner);
				AccountManager accountManager = new AccountManager(connection,scanner);
				String email;
				long account_number;
				while(true)
				{
					
					System.out.println("     WELCOME TO BANKING SYSTEM    ");
					System.out.println("*----------------*---------------*");
					System.out.println();
					System.out.println("    1. Register");
					System.out.println("    2. Login");
					System.out.println("    3. Exit");
					System.out.println("   enter your choice");
					int choice1 =scanner.nextInt();
					switch(choice1)
					{
					case 1:
						user.register();
						break;
					case 2:
						email =user.login();
						if(email!=null)
						{
							System.out.println();
							System.out.println("user Logged in");
							if(!accounts.account_exist(email))
							{
								System.out.println();
								System.out.println("1 open a new bank account");
								System.out.println("2 exist");
								if (scanner.nextInt() ==1)
								{
									account_number = accounts.open_account(email);
									System.out.println("account created successfully");
									System.out.println( "your account number is:" + account_number);
								}
								else {
									break
								}
							}
							account_number = accounts.getAccount_number(email);
							int choice2= 0;
							while(choice2 != 5)
							{
								System.out.println();
								System.out.println("1. debit money");
								System.out.println("2. credit Money");
								System.out.println("3. Transfor Money");
								System.out.println("4. check Balance");
								System.out.println("5. log out");
								System.out.println("enter your choice");
								choice2 = scanner.nextInt();
								switch (choice2)
								{
								case 1:
									accountManager.debit_money(account_number);
								
								break;
								case 2:
									accountManager.credit_money(account_number);
								
								break;
								
								case 3:
									accountManager.transfer_money(account_number);
								
								break;
								
								case 4:
									accountManager.getBalance(account_number);
								
								break;
								case 5:
									break;
									
									default:
										System.out.println("Invalid choice. try again");
								}
							}
							
						}
						
}
