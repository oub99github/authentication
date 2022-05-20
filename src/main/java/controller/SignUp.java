package controller;

import model.Results;
import model.User;
import service.DbService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SignUp extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("register.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String phoneNumber = req.getParameter("phoneNumber");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String prePassword = req.getParameter("prePassword");

        if (password.equals(prePassword)){
            DbService dbService = new DbService();
            User user = new User();
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setPhoneNumber(phoneNumber);
            user.setUsername(username);
            user.setPassword(password);
            Results results = dbService.registerUser(user);
            if (results.isSuccess()){
                PrintWriter writer = resp.getWriter();
                writer.write(results.getMessage());
            }else {
                PrintWriter writer = resp.getWriter();
                writer.write(results.getMessage());
            }
        }else {
            PrintWriter writer = resp.getWriter();
            writer.write("Return Password");
        }

    }
}
