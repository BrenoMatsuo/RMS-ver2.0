package Controller.Dish;

import DAO.CategoryDAO2;
import DAO.DishDAO2;
import Model.Category2;
import Model.Dish2;
import Utils.DishImageStorage2;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

@WebServlet("/management/dishes/add")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024)
public class AddDishServlet2 extends HttpServlet {
    private final CategoryDAO2 categoryDAO = new CategoryDAO2();
    private final DishDAO2 dishDAO = new DishDAO2();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showForm(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Dish2 dish = new Dish2();
        dish.setName(value(request.getParameter("name")));
        dish.setPicture(value(request.getParameter("picture")));
        dish.setState(value(request.getParameter("state")));
        dish.setDescription(value(request.getParameter("description")));
        String priceText = value(request.getParameter("price"));
        String categoryText = value(request.getParameter("categoryId"));
        dish.setPrice(parsePrice(priceText));
        request.setAttribute("selectedCategoryId", categoryText);
        try {
            if (dish.getName().isEmpty()) {
                throw new IllegalArgumentException("Tên món không được để trống.");
            }
            if (dish.getPicture().length() > 255) {
                throw new IllegalArgumentException("Đường dẫn ảnh không được vượt quá 255 ký tự.");
            }
            BigDecimal price = new BigDecimal(priceText);
            if (price.signum() <= 0) {
                throw new IllegalArgumentException("Giá món phải lớn hơn 0.");
            }
            int categoryId = Integer.parseInt(categoryText);
            Category2 category = categoryDAO.getCategory(categoryId);
            if (category == null) {
                throw new IllegalArgumentException("Vui lòng chọn danh mục hợp lệ.");
            }
            dish.setPrice(price);
            dish.setCategory(category);
            if (dish.getState().isEmpty()) {
                dish.setState("Available");
            }

            Part imagePart = null;
            String contentType = request.getContentType();
            if (contentType != null && contentType.toLowerCase(Locale.ROOT).startsWith("multipart/")) {
                imagePart = request.getPart("image");
            }
            String uploadedImage = null;
            if (imagePart != null && imagePart.getSize() > 0) {
                try {
                    uploadedImage = DishImageStorage2.save(imagePart);
                    dish.setPicture(uploadedImage);
                } catch (IllegalArgumentException exception) {
                    showForm(request, response, exception.getMessage(), dish);
                    return;
                } catch (IOException exception) {
                    throw new ServletException("Không thể lưu ảnh món ăn.", exception);
                }
            }
            try {
                if (!dishDAO.addDish(dish)) {
                    throw new SQLException("Cơ sở dữ liệu không thêm được món ăn.");
                }
            } catch (SQLException exception) {
                if (uploadedImage != null) {
                    try {
                        DishImageStorage2.delete(uploadedImage);
                    } catch (IOException cleanupException) {
                        exception.addSuppressed(cleanupException);
                    }
                }
                throw exception;
            }
            response.sendRedirect(request.getContextPath() + "/management/dishes?added=1");
        } catch (NumberFormatException exception) {
            showForm(request, response, "Giá hoặc danh mục không hợp lệ.", dish);
        } catch (IllegalArgumentException exception) {
            showForm(request, response, exception.getMessage(), dish);
        } catch (SQLException exception) {
            throw new ServletException("Không thể thêm món ăn.", exception);
        }
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        showForm(request, response, error, (Dish2) request.getAttribute("dish"));
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response, String error, Dish2 dish)
            throws ServletException, IOException {
        try {
            List<Category2> categories = categoryDAO.getAllCategory();
            request.setAttribute("categories", categories);
            request.setAttribute("dish", dish);
            request.setAttribute("error", error);
            request.getRequestDispatcher("/WEB-INF/views/dish/AddDish2.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("Không tải được danh mục món ăn.", exception);
        }
    }

    private BigDecimal parsePrice(String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String value(String input) {
        return input == null ? "" : input.trim();
    }
}
