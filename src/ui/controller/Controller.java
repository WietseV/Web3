package ui.controller;

import domain.db.DbException;
import domain.db.PersonDbInMemory;
import domain.db.ProductDbInMemory;
import domain.model.DomainException;
import domain.model.Person;
import domain.model.Product;
import domain.service.ShopService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Controller")
public class Controller extends HttpServlet {

    private ShopService shopservice = new ShopService();
    private final PersonDbInMemory PersonDbInMemory = new PersonDbInMemory();
    private final ProductDbInMemory ProductDbInMemory = new ProductDbInMemory();

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<String> errors = new ArrayList<>();
        String action = "index";
        String destination = "";
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }
        switch (action) {
            case "updateproduct":
                request.setAttribute("name", request.getParameter("name"));
                request.setAttribute("description", request.getParameter("description"));
                request.setAttribute("price", request.getParameter("price"));
                request.setAttribute("id", request.getParameter("id"));
                request.setAttribute("Errors", new ArrayList<String>());
                destination = "updateproduct.jsp";
                break;
            case "updateproductform":
                request.setAttribute("id", request.getParameter("id"));
                destination = updateProduct(request, response);
                break;
            case "addproductform":
                destination = addProduct(request, response);
                break;
            case "addproduct":
                request.setAttribute("Errors", errors);
                destination = "addproduct.jsp";
                break;
            case "add":
                destination = addPerson(request, response);
                break;
            case "signUp":
                request.setAttribute("Errors", errors);
                destination = "signUp.jsp";
                break;
            case "personoverview":
                request.setAttribute("PersonDB", shopservice.getPersons());
                destination = "personoverview.jsp";
                break;
            case "productoverview":
                request.setAttribute("ProductDB", ProductDbInMemory.getAll());
                destination = "productoverview.jsp";
                break;
            case "index":
                destination = "index.jsp";
                break;
            case "deleteperson":
                PersonDbInMemory.delete(request.getParameter("id"));
                request.setAttribute("PersonDB", shopservice.getPersons());
                destination = "personoverview.jsp";
                break;
            case "deleteproduct":
                ProductDbInMemory.delete(Integer.parseInt(request.getParameter("id")));
                request.setAttribute("ProductDB", ProductDbInMemory.getAll());
                destination = "productoverview.jsp";
                break;
            default:
                destination = "index.jsp";
                break;
        }

        RequestDispatcher view = request.getRequestDispatcher(destination);
        view.forward(request, response);
    }

    private String updateProduct(HttpServletRequest request, HttpServletResponse response) {
        ArrayList<String> errors = new ArrayList<>();
        Product product = new Product();
        try {
            product = (Product) ProductDbInMemory.get(Integer.parseInt(request.getParameter("id")));
        }catch (NullPointerException n){
            errors.add((String) request.getAttribute("id"));
        }
        getName(product, request, errors);
        getDescription(product, request, errors);
        getPrice(product, request, errors);


        if (errors.size() == 0) {

            try {
                ProductDbInMemory.update(product);

                request.setAttribute("ProductDB", ProductDbInMemory.getAll());
                return "productoverview.jsp";
            } catch (DbException e) {
                errors.add(e.getMessage());
                request.setAttribute("Errors", errors);
                return "updateproduct.jsp";
            }

        } else {
            request.setAttribute("Errors", errors);
            return "updateproduct.jsp";
        }
    }

    private String addProduct(HttpServletRequest request, HttpServletResponse response) {
        ArrayList<String> errors = new ArrayList<String>();

        Product product = new Product();
        getName(product, request, errors);
        getDescription(product, request, errors);
        getPrice(product, request, errors);

        if (errors.size() == 0) {

            try {
                ProductDbInMemory.add(product);
                request.setAttribute("ProductDB", ProductDbInMemory.getAll());
                return "productoverview.jsp";
            } catch (DbException e) {
                errors.add(e.getMessage());
                request.setAttribute("Errors", errors);
                return "addproduct.jsp";
            }

        } else {
            request.setAttribute("Errors", errors);
            return "addproduct.jsp";
        }
    }

    private void getName(Product product, HttpServletRequest request, ArrayList<String> errors) {
        String nameFromForm = request.getParameter("name");
        try {
            product.setName(nameFromForm);
            request.setAttribute("name", nameFromForm);

        } catch (IllegalArgumentException | NullPointerException | DomainException n) {
            errors.add(n.getMessage());
        }
    }

    private void getDescription(Product product, HttpServletRequest request, ArrayList<String> errors) {
        String descriptionFromForm = request.getParameter("description");
        try {
            product.setDescription(descriptionFromForm);
            request.setAttribute("description", descriptionFromForm);
        } catch (IllegalArgumentException | NullPointerException | DomainException e) {
            errors.add(e.getMessage());
        }

    }

    private void getPrice(Product product, HttpServletRequest request, ArrayList<String> errors) {
        String priceFromForm = request.getParameter("price");
        try {
            product.setPrice(priceFromForm);
            request.setAttribute("price", priceFromForm);
        } catch (IllegalArgumentException | NullPointerException | DomainException e) {
            errors.add(e.getMessage());
        }

    }

    private String addPerson(HttpServletRequest request, HttpServletResponse response) {
        ArrayList<String> errors = new ArrayList<String>();

        Person person = new Person();
        getUserId(person, request, errors);
        getFirstName(person, request, errors);
        getLastName(person, request, errors);
        getEmail(person, request, errors);
        getPassword(person, request, errors);

        if (errors.size() == 0) {

            try {
                PersonDbInMemory.add(person);

                request.setAttribute("PersonDB", PersonDbInMemory.getAll());
                return "personoverview.jsp";
            } catch (DbException e) {
                errors.add(e.getMessage());
                request.setAttribute("Errors", errors);
                return "signUp.jsp";
            }

        } else {
            request.setAttribute("Errors", errors);
            return "signUp.jsp";
        }
    }

    private void getUserId(Person person, HttpServletRequest request, ArrayList<String> errors) {
        String userIdFromForm = request.getParameter("userid");
        try {
            person.setUserid(userIdFromForm);
            request.setAttribute("userid", userIdFromForm);

        } catch (IllegalArgumentException | NullPointerException n) {
            errors.add("No userid given");
        }
    }

    private void getEmail(Person person, HttpServletRequest request, ArrayList<String> errors) {
        String emailFromForm = request.getParameter("email");
        try {
            person.setEmail(emailFromForm);
            request.setAttribute("email", emailFromForm);
        } catch (IllegalArgumentException | NullPointerException e) {
            errors.add(e.getMessage());
        }

    }

    private void getLastName(Person person, HttpServletRequest request, ArrayList<String> errors) {
        String lastNameFromForm = request.getParameter("lastName");
        try {
            person.setLastName(lastNameFromForm);
            request.setAttribute("lastName", lastNameFromForm);
        } catch (IllegalArgumentException | NullPointerException e) {
            errors.add("No last name given");
        }

    }

    private void getFirstName(Person person, HttpServletRequest request, ArrayList<String> errors) {
        String firstNameFromForm = request.getParameter("firstName");
        try {
            person.setFirstName(firstNameFromForm);
            request.setAttribute("firstName", firstNameFromForm);
        } catch (IllegalArgumentException | NullPointerException e) {
            errors.add("No firstname given");
        }

    }

    private void getPassword(Person person, HttpServletRequest request, ArrayList<String> errors) {
        String passwordFromForm = request.getParameter("password");
        try {
            person.setPassword(passwordFromForm);
            request.setAttribute("password", passwordFromForm);
        } catch (IllegalArgumentException  | NullPointerException e) {
            errors.add("No password given");
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);

    }
}
