
package com.luv2code.web.jdbc;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import javax.sql.DataSource;

@WebServlet(name = "StudentControllerServlet", urlPatterns = {"/StudentControllerServlet"})
public class StudentControllerServlet extends HttpServlet {
    
    private StudentDbUtil studentDbUtil;
    
    @Resource(name="jdbc/web_student_tracker")
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        super.init();
        
        // create our student db util... and pass in the conn pool /datasource
        try{
            studentDbUtil = new StudentDbUtil(dataSource);
        }
        catch(Exception e){
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            
        try{
            // read the "command" parameter
            String theCommand = request.getParameter("command");
            
            // if the command is missing, then default to listing students
            if(theCommand == null){
                theCommand = "LIST";
            }
            
            // route to the appropriate method
            switch(theCommand){
                case "LIST":
                    listStudents(request, response);
                    break;
                    
                case "LOAD":
                    loadStudent(request, response);
                    break;
                    
                case "DELETE":
                    deleteStudent(request, response);
                    break;
                
                default:
                    listStudents(request, response);
            }
            // list the students.. in MVC fashion
            listStudents(request, response);
        }
        catch(Exception e){
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
        try{
            // read the "command" parameter
            String theCommand = request.getParameter("command");

            // route to the appropriate method
            switch(theCommand){
                case "ADD":
                    addStudent(request, response);
                    break;
                    
                case "UPDATE":
                    updateStudent(request, response);
                    break;
                                   
                default:
                    listStudents(request, response);
            }

        }
        catch(Exception e){
            throw new ServletException(e);
        }
    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        // get students from db util
        List<Student> students = studentDbUtil.getStudents();
        
        // add students to the request
        request.setAttribute("STUDENT_LIST", students);
        
        // send to JSP page (view)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
        dispatcher.forward(request, response);
    }
    
    private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        // read student info from form data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        
        // create a new student object
        Student theStudent = new Student(firstName, lastName, email);
        
        // add the student to the database
        studentDbUtil.addStudent(theStudent);
        
        // send back to main page (the student list)
        response.sendRedirect(request.getContextPath() + "/StudentControllerServlet");
    }

    private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        // read student id from form data
        String theStudentId = request.getParameter("studentId");
        
        // get student from database (db util)
        Student theStudent = studentDbUtil.getStudents(theStudentId);
        
        // place student in the request attribute
        request.setAttribute("THE_STUDENT", theStudent);
  
        // send to jsp page: update-student-form.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
        dispatcher.forward(request, response);
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        // read student info from form data
        int id = Integer.parseInt(request.getParameter("studentId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        
        // create a new student object
        Student theStudent = new Student(id, firstName, lastName, email);
        
        // perform update on database
        studentDbUtil.updateStudent(theStudent);
        
        // send them back to the "list students" page
        listStudents(request, response);
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        // read student id from form data
        String theStudentId = request.getParameter("studentId");
        
        // delete student from database
        studentDbUtil.deleteStudent(theStudentId);
        
        // send them back to "list students" page
        listStudents(request, response);
    }

}
