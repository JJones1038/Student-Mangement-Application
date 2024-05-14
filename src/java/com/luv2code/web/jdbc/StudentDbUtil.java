package com.luv2code.web.jdbc;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import java.sql.*;

public class StudentDbUtil {
    
    private DataSource dataSource;
    
    public StudentDbUtil(DataSource theDataSource){
        dataSource = theDataSource;
    }
    
    public List<Student> getStudents() throws Exception{
        
        List<Student> students = new ArrayList<>();
        
        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;
        
        try{
            // get a connection
            conn = dataSource.getConnection();
            
            // create a sql statement
            String sql = "SELECT * FROM student ORDER BY last_name";
            s = conn.createStatement();
            
            // execute query
            rs = s.executeQuery(sql);
            
            // process result set
            while(rs.next()){
                
                // retrieve data from result set row
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                
                // create new student object
                Student tempStudent = new Student(id, firstName, lastName, email);
                
                // add it to the list of students
                students.add(tempStudent);
            }
        
        }
        finally{
            // close JDBC objects
            close(conn, s, rs);
        }
        
        return students;
    }

    private void close(Connection conn, Statement s, ResultSet rs) {
        
        try{
            if(rs != null){
                rs.close();
            }
            
            if(s != null){
                s.close();
            }
            
            if(conn != null){
                conn.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    void addStudent(Student theStudent) throws Exception{
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try{
            // get db connection
            conn = dataSource.getConnection();
            
            // create sql for insert
            String sql = "INSERT INTO student (first_name, last_name, email) values (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            
            // set the param values for the student
            ps.setString(1, theStudent.getFirstName());
            ps.setString(2, theStudent.getLastName());
            ps.setString(3, theStudent.getEmail());

            // execute sql insert
            ps.execute();
            
        }
        finally{
            // clean up JDBC objects
            close(conn, ps, null);
        }
    }

    Student getStudents(String theStudentId) throws Exception {
        Student theStudent = null;
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        int studentId;
        
        try{
            // convert student id to int
            studentId = Integer.parseInt(theStudentId);
            
            // get connection to database
            conn = dataSource.getConnection();
            
            // create sql to get selected student
            String sql = "SELECT * FROM student WHERE id = ?";
            
            // create prepared statement
            ps = conn.prepareStatement(sql);
            
            //set params
            ps.setInt(1, studentId);
            
            // execute statement
            rs = ps.executeQuery();
            
            // retrieve data from result set row
            if(rs.next()){
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                
                // use the studentId during construction
                theStudent = new Student(studentId, firstName, lastName, email);
            } else{
                throw new Exception("Could not find student id: " + studentId);
            }
            
            return theStudent;
            
        }finally{
            close(conn, ps, rs);
        }
        
    }

    void updateStudent(Student theStudent) throws Exception {
         
        Connection conn = null;
        PreparedStatement ps = null;
        
        try{
            //get db connection
            conn = dataSource.getConnection();

            // create sql update statement
            String sql = "UPDATE student SET first_name = ?, last_name = ?, email = ? WHERE id = ?";

            // prepare statement
            ps = conn.prepareStatement(sql);

            // set params
            ps.setString(1, theStudent.getFirstName());
            ps.setString(2, theStudent.getLastName());
            ps.setString(3, theStudent.getEmail());
            ps.setInt(4, theStudent.getId());

            // execute sql statement
            ps.execute();
        }
        finally{
            close(conn, ps, null);
        }
    }   

    void deleteStudent(String theStudentId) throws Exception{
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try{
            // convert student id to int
            int studentId = Integer.parseInt(theStudentId);
            
            // get connection to database
            conn = dataSource.getConnection();
            
            // create sql to delete student
            String sql = "DELETE FROM student WHERE id = ?";
            
            // prepare statement
            ps = conn.prepareStatement(sql);
            
            // set params
            ps.setInt(1, studentId);
            
            // execute sql statement
            ps.execute();
            
        }
        finally{
            // clean up JDBC
            close(conn, ps, null);
        }
    }
}
