package ru.freelib.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.freelib.model.User;
import ru.freelib.service.UserService;

import java.io.IOException;


@WebFilter({"/*", "/"})
public class AutoLoginFilter implements Filter {
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = (UserService) filterConfig.getServletContext().getAttribute("userService");
        ServletContext context = filterConfig.getServletContext();
        FilterRegistration adminfilter = context.getFilterRegistration("AdminFilter");
        adminfilter.addMappingForUrlPatterns(null, true, "/admin", "/admin/*");

        FilterRegistration authfilter = context.getFilterRegistration("AuthFilter");
        authfilter.addMappingForUrlPatterns(null, true, "/profile", "/profile/*", "/comment/*", "/profile-edit", "/profile-edit/*");

        FilterRegistration uploadfilter = context.getFilterRegistration("UploadFilter");
        uploadfilter.addMappingForUrlPatterns(null, true, "/upload", "/upload/*");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

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
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }
}
