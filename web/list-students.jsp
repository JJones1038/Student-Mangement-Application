<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Student Management App</title>
        <link rel="stylesheet" href="css/style.css"/>
    </head>
          
    <body>
        
        <div id="wrapper">
            <div id="header">
                <h2>Company Name</h2>
            </div>
        </div>
        
        <div id="container">
            <div id="content">
                <input type="button" value="Add Student"
                       onclick="window.location.href='add-student-form.jsp'; return false;"
                       class="add-student-button">
                <table>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Action</th>
                    </tr>
                    <c:forEach var="tempStudent" items="${STUDENT_LIST}">
                        
                        <!-- set up link for each student -->
                        <c:url var="tempLink" value="StudentControllerServlet">
                            <c:param name="command" value="LOAD"/>
                            <c:param name="studentId" value="${tempStudent.id}"/>
                        </c:url>
                        
                        <!-- set up link to delete student -->
                        <c:url var="deleteLink" value="StudentControllerServlet">
                            <c:param name="command" value="DELETE"/>
                            <c:param name="studentId" value="${tempStudent.id}"/>
                        </c:url>
                        <tr>
                            <td>${tempStudent.firstName}</td>
                            <td>${tempStudent.lastName}</td> 
                            <td>${tempStudent.email}</td>
                            <td>
                                <a href="${tempLink}">Update</a>
                                 | 
                                <a href="${deleteLink}"
                                   onclick="if(!(confirm('Are you sure you want to delete this student?'))) return false">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
        
    </body>
</html>
