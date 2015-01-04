package mmboa.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author a
 *  
 */
public class DbUtil {
    public static Context initContext;

    public static Context ctx;

    public static boolean hasInited = false;

    /**
     * 取得数据库连接。
     * 
     * @return
     */
    public static Connection getConnection() {
        try {
            initContext = new InitialContext();
            ctx = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) ctx.lookup("jdbc/oa");
            Connection conn = ds.getConnection();
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConnection(String databaseName) {
        try {
            initContext = new InitialContext();
            ctx = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) ctx.lookup("jdbc/" + databaseName);
            Connection conn = ds.getConnection();
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getDirectConnection(String dbServer, String db, String dbUser, String dbPassword){
    	try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            return DriverManager.getConnection("jdbc:mysql://" + dbServer
                    + ":3306/" + db, dbUser, dbPassword);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 关闭数据库连接对象
     * @param rs ResultSet
     * @param stmt Statement
     * @param conn Connection
     */
    public static void closeConnection(ResultSet rs, Statement stmt, Connection conn) {
    	if(rs != null) {
    		try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	if(stmt != null) {
    		try {
    			stmt.close();
    			stmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	if(conn != null) {
    		try {
    			conn.close();
    			conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * 关闭数据库连接对象
     * @param rs ResultSet
     * @param stmt Statement
     * @param conn Connection
     */
    public static void closeConnection(ResultSet rs, Statement stmt, Connection conn ,boolean autoCommit) {
    	if(rs != null) {
    		try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	if(stmt != null) {
    		try {
    			stmt.close();
    			stmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	if(conn != null) {
    		try {
    			//改变事务提交方式
				conn.setAutoCommit(autoCommit);
    			conn.close();
    			conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
}
