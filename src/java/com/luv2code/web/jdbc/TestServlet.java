package com.luv2code.web.jdbc;

import java.sql.*;
import jakarta.annotation.Resource;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import javax.sql.DataSource;

@WebServlet(name = "TestServlet", urlPatterns = {"/TestServlet"})
public class TestServlet extends HttpServlet {
    
    // Define datasource/connection pool for Resource Injection
    @Resource(name="jdbc/web_student_tracker")
    private DataSource dataSource;
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Step 1: Set up the printwriter
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        
        // Step 2: Get a connection to the database
        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;
        
        try{
            conn = dataSource.getConnection();
            
        // Step 3: Create a SQL statements
        String sql = "SELECT * FROM student";
        s = conn.createStatement();
        
        // Step 4: Execute SQL Query
        rs = s.executeQuery(sql);
        
        // Step 5: Process the result set
        while (rs.next()){
            String email = rs.getString("email");
            out.println(email);
          }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
