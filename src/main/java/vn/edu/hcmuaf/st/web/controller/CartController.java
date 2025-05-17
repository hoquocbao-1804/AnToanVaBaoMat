package vn.edu.hcmuaf.st.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.st.web.entity.*;
import vn.edu.hcmuaf.st.web.service.ProductService;
import vn.edu.hcmuaf.st.web.service.ProductVariantService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "cartController", urlPatterns = "/cart")
public class CartController extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ProductVariantService productVariantService = new ProductVariantService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("view/view-order/cart.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        String action = req.getParameter("action");

        switch (action) {
            case "add":
                int productId = Integer.parseInt(req.getParameter("idProduct"));
                Product product = productService.getProductById(productId);
                List<ProductVariant> variants = productVariantService.getProductVariantsByIdProduct(productId);
                product.setProductVariants(variants);
                cart.addItem(product);
                resp.sendRedirect(req.getContextPath() + "/cart");
                break;

            case "continue":
                String prevUrl = (String) session.getAttribute("prevUrl");
                if (prevUrl == null) {
                    prevUrl = req.getContextPath() + "/home";
                }
                resp.sendRedirect(prevUrl);
                break;

            case "remove":
                int idVariant = Integer.parseInt(req.getParameter("idVariant"));
                cart.removeItem(idVariant);
                session.setAttribute("cart", cart);
                resp.sendRedirect(req.getContextPath() + "/cart");
                break;


            case "updateQuantity":
                try {
                    String idVariantStr = req.getParameter("idVariant");
                    String quantityStr = req.getParameter("quantity");

                    int idVariantUpdate = Integer.parseInt(idVariantStr);
                    int newQuantity = Integer.parseInt(quantityStr);

                    cart.addQuantity(idVariantUpdate, newQuantity);
                    session.setAttribute("cart", cart);

                    double totalPrice = cart.getTotalPrice();
                    double discountAmount = cart.getDiscountAmount(); // nếu có giảm giá
                    double finalTotal = cart.getFinalTotal();

                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");

                    String json = String.format(
                            "{\"totalPrice\": %.0f, \"discountAmount\": %.0f, \"finalTotal\": %.0f}",
                            totalPrice, discountAmount, finalTotal
                    );

                    resp.getWriter().write(json);
                } catch (NumberFormatException e) {
                    resp.setContentType("text/plain");
                    resp.setCharacterEncoding("UTF-8");
                    resp.getWriter().write("ERROR: Invalid number format");
                }
                break;
                

            default:
                resp.sendRedirect(req.getContextPath() + "/cart");
                break;
        }
    }
}
