package com.example.servlet;

import com.example.model.UserProfile;
import com.example.service.AccountService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class LogInServlet extends HttpServlet {
    private AccountService accountService = new AccountService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = (String) req.getSession().getAttribute("login");
        String password = (String) req.getSession().getAttribute("password");

        if (login == null || password == null) {
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect("listing?path=D:/filemanager/" + login);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if (login.isEmpty() || password.isEmpty()) {
            resp.getWriter().write("Invalid input");
            return;
        }

        UserProfile user = accountService.getUserByLogin(login);
        if (user == null) {
            resp.getWriter().write("No such user");
            return;
        }

        if(!password.equals(user.getPassword())) {
            resp.getWriter().write("Invalid password");
            return;
        }

        req.getSession().setAttribute("login", login);
        req.getSession().setAttribute("password", password);
        resp.sendRedirect("listing?path=D:/filemanager/" + login);
    }
}
