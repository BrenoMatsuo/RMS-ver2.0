package Controller;

import Model.Staff2;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/management/*", "/warehouse/*"})
public class AuthFilter2 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Object value = httpRequest.getSession(false) == null
                ? null : httpRequest.getSession(false).getAttribute("staff");
        if (!(value instanceof Staff2)) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/Login2.jsp");
            return;
        }

        Staff2 staff = (Staff2) value;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        boolean managerPath = path.startsWith("/management/");
        String position = staff.getPosition() == null ? "" : staff.getPosition();
        boolean allowed = managerPath ? "manager".equals(position) : "warehouse".equals(position);
        if (!allowed) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập chức năng này.");
            return;
        }
        chain.doFilter(request, response);
    }
}
