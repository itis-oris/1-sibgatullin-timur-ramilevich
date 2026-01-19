package ru.freelib.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.freelib.model.Book;
import ru.freelib.model.Genre;
import ru.freelib.model.User;
import ru.freelib.service.BookService;
import ru.freelib.service.GenreService;
import ru.freelib.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private GenreService genreService;
    private BookService bookService;
    private UserService userService;

    @Override
    public void init() {
        genreService = (GenreService) getServletContext().getAttribute("genreService");
        bookService = (BookService) getServletContext().getAttribute("bookService");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("currentContext", request.getContextPath());
        HttpSession session = request.getSession(true);
        if (session == null || session.getAttribute("user") == null) {
            session = request.getSession(true);
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("userId".equals(cookie.getName())) {
                        try {
                            long userId = Integer.parseInt(cookie.getValue());
                            User user = userService.findById(userId);
                            if (user != null) {
                                session.setAttribute("user", user);
                                session.setAttribute("role", user.getRole());
                                session.setAttribute("nickname", user.getNickname());
                                response.sendRedirect(request.getContextPath() + "/home");
                                return;
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }
        List<Genre> genres = genreService.findAll();
        List<Book> books = bookService.findAll();
        request.setAttribute("genres", genres);
        request.setAttribute("books", books);
        request.getRequestDispatcher("/home.ftlh").forward(request, response);
    }
}