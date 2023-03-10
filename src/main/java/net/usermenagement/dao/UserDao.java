package net.usermenagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.usermenagement.model.User;

//This Dao class provides CRUD database operations for the table users in the database
public class UserDao {
	private String jdbcURL = "jdbc:mysql://localhost:3306/mysql_database";
	private String jdbcUsername = "root";
	private String jdbcPassword = "blknfg182";
	
	private static final String INSERT_USER_SQL = "INSERT INTO users" + "(name, email, country) VALUES" + "(?,?,?);";
	
	private static final String SELECT_USER_BY_ID = "SELECT id, name, email, country from users where id = ?;";
	
	private static final String SELECT_ALL_USERS = "SELECT * from users";
	
	private static final String DELETE_USERS_SQL = "DELETE from users where id = ?;";
	
	private static final String UPDATE_USERS_SQL = "UPDATE users set name = ?, email = ?, country = ? where id = ?;";
	
	
	//create connection
	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername,jdbcPassword);
		}catch (SQLException e) {
			e.getStackTrace();
			// TODO: handle exception
		}catch (ClassNotFoundException e) {
			e.getStackTrace();
		}
		return connection;
	}
	
	public void insertUser(User user) throws SQLException {
		try(Connection connection = getConnection()){ 
			
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.executeUpdate();
			
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}
		
	
		public boolean updateUser(User user) throws SQLException {
			boolean rowUpdated = false;
			try(Connection connection = getConnection()){ 
				
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);
				preparedStatement.setString(1, user.getName());
				preparedStatement.setString(2, user.getEmail());
				preparedStatement.setString(3, user.getCountry());
				preparedStatement.setInt(4, user.getId());
				
				rowUpdated = preparedStatement.executeUpdate() > 0;
				
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			
			return rowUpdated;
			
		}
		
		public User selectUser(int id) throws SQLException {
			
			User user = null;
			
			try(Connection connection = getConnection()){ 
				
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
				preparedStatement.setInt(1, id);
				System.out.println(preparedStatement);
				
				ResultSet rs = preparedStatement.executeQuery();
				
				while(rs.next()) {
					String name = rs.getString("name");
					String email = rs.getString("email");
					String country = rs.getString("country");
					user = new User(id, name, email, country);
				}
				
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			
			return user;
		}
		
		
		public List<User> selectAllUser() throws SQLException {
			List<User> users = new ArrayList<>();
			User user = null;
			
			try(Connection connection = getConnection()){ 
				
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
				
				System.out.println(preparedStatement);
				
				ResultSet rs = preparedStatement.executeQuery();
				
				while(rs.next()) {
					Integer id = rs.getInt("id");
					String name = rs.getString("name");
					String email = rs.getString("email");
					String country = rs.getString("country");
					users.add(new User(id, name, email, country));
				}
				
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			
			return users;
		}
		
		
		public boolean deleteUser(int id) throws SQLException {
			boolean rowDeleted = false;
			try(Connection connection = getConnection()){ 
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);
				preparedStatement.setInt(1, id);
				
				rowDeleted = preparedStatement.executeUpdate() > 0;
				
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			
			return rowDeleted;
			
		}
		
		private void printSQLException(SQLException ex) {
			for (Throwable e : ex) {
				if (e instanceof SQLException) {
					e.printStackTrace(System.err);
					System.err.println("SQLState: " + ((SQLException) e).getSQLState());
					System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
					System.err.println("Message: " + e.getMessage());
					Throwable t = ex.getCause();
					while (t != null) {
						System.out.println("Cause: " + t);
						t = t.getCause();
					}
				}
			}
		}
		
	
	
	
	
	
	}

