package research.mpl.backend.jdbc;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.sql.*;

/**
 * Created by Marilia Portela on 20/05/2017.
 */
public class FirstExample {
        // JDBC driver name and database URL
        static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
        static final String DB_URL = "jdbc:hsqldb:file:target/database/bullet-db";

        //  Database credentials
        static final String USER = "root";
        static final String PASS = "root";

        public static void main(String[] args) {

            Connection conn = null;
            Statement stmt = null;
            try{
//                File file = new File("C:\\Users\\Marilia Portela\\Desktop\\jdbcSquirrel\\jdbc-squirrel.jar");
//                URL url = file.toURI().toURL();
//
//                URLClassLoader classLoader = (URLClassLoader)ClassLoader.getSystemClassLoader();
////                ClassLoader classLoader = FirstExample.class.getClassLoader();
//                URLClassLoader child = new URLClassLoader (new URL[]{url}, classLoader);
//                Class classToLoad = Class.forName ("JdbcSquirrel", true, child);
//                Method method = classToLoad.getDeclaredMethod ("executeImmediate");
////                method.setAccessible(true);
//                Object instance = classToLoad.newInstance ();
////                Object result = method.invoke (instance);


                //STEP 2: Register JDBC driver
                Class.forName(JDBC_DRIVER);

                //STEP 3: Open a connection
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL,USER,PASS);
                System.out.println("Creating statement...");
                stmt = conn.createStatement();

                String sqlTeste = "CREATE TABLE IF NOT EXISTS public.HOJE (id_temp INT);";
                stmt.execute(sqlTeste);

                //STEP 4: Execute a query

                String sql;
                sql = "DROP PROCEDURE IF EXISTS execute_immediate";
                stmt.execute(sql);
                sql =
                " CREATE PROCEDURE execute_immediate(" +
                        "IN P1 VARCHAR(1000000),IN P2 VARCHAR(500),IN P3 VARCHAR(500),IN P4  VARCHAR (500) ) " +
                " LANGUAGE JAVA  " +
                " DETERMINISTIC MODIFIES " +
                " SQL DATA EXTERNAL NAME 'CLASSPATH:research.mpl.backend.jdbc.JdbcSquirrel.executeImmediate';";
                stmt.execute(sql);

                String immediateSql = "CREATE TABLE IF NOT EXISTS public.HOJE (id_temp INT);";
                sql = "CALL execute_immediate('" + immediateSql +"','" +
                                               DB_URL + "','" + USER + "','" + PASS + "')";
                stmt.execute(sql);

//                sql = "SELECT id, first, last, age FROM Employees";
//                ResultSet rs = stmt.executeQuery(sql);
//
//                //STEP 5: Extract data from result set
//                while(rs.next()){
//                    //Retrieve by column name
//                    int id  = rs.getInt("id");
//                    int age = rs.getInt("age");
//                    String first = rs.getString("first");
//                    String last = rs.getString("last");
//
//                    //Display values
//                    System.out.print("ID: " + id);
//                    System.out.print(", Age: " + age);
//                    System.out.print(", First: " + first);
//                    System.out.println(", Last: " + last);
//                }

                //STEP 6: Clean-up environment
//                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                //Handle errors for JDBC
                se.printStackTrace();
            }catch(Exception e){
                //Handle errors for Class.forName
                e.printStackTrace();
            }finally{
                //finally block used to close resources
                try{
                    if(stmt!=null)
                        stmt.close();
                }catch(SQLException se2){
                }// nothing we can do
                try{
                    if(conn!=null)
                        conn.close();
                }catch(SQLException se){
                    se.printStackTrace();
                }//end finally try
            }//end try
            System.out.println("Goodbye!");
        }//end main
}
