package hessian.expensivest.spring;

import hessian.typeparser.AnyParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
public class ExpenseControllerSpring {
    @Autowired
    private ExpenseRepositorySpring expenseRepositorySpring;
    private AnyParser anyParser = new AnyParser();

    @GetMapping(value = "api/hello")
    public String hello() {
        return "<html><body><H1>Hello World</H1></body></html>";
    }

    @RequestMapping(value = "api/add", method = RequestMethod.POST)
    public Expense createExpense(@RequestBody Expense expense) {
        expenseRepositorySpring.save(expense);
        return expense;
    }

    @RequestMapping(value = "api/delete", method = RequestMethod.POST)
    public void delete(@RequestParam String user, @RequestParam String trip, @RequestParam String expts) throws ParseException {
        expenseRepositorySpring.deleteByKeyUserAndKeyTripAndKeyExpts(anyParser.parse(user, java.lang.String.class), anyParser.parse(trip, java.lang.String.class), anyParser.parse(expts, java.util.Date.class));
    }

    @RequestMapping(value = "api/all")
    public List<Expense> all() {
        return expenseRepositorySpring.findAll();
    }

    // Lookups
    @RequestMapping(value = "api/user", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> user(@RequestParam String user) throws ParseException {
        return expenseRepositorySpring.findByKeyUser(anyParser.parse(user, String.class));
    }

    @RequestMapping(value = "api/user_trip", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> userTrip(@RequestParam String user, @RequestParam String trip) throws ParseException {
        return expenseRepositorySpring.findByKeyUserAndKeyTrip(anyParser.parse(user, String.class), anyParser.parse(trip, String.class));
    }

    @RequestMapping(value = "api/category", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> category(@RequestParam String category) throws ParseException {
        return expenseRepositorySpring.findByCategory(anyParser.parse(category, String.class));
    }

    // Inequalities
    @RequestMapping(value = "api/amount/gt", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> amount_gt(@RequestParam String amount) throws ParseException {
        return expenseRepositorySpring.findByAmountGreaterThan(anyParser.parse(amount, Double.class));
    }

    // String search queries
    @RequestMapping(value = "api/category/like", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> categoryLike(@RequestParam String category) throws ParseException {
        return expenseRepositorySpring.findByCategoryLike(anyParser.parse(category, String.class));
    }

    @RequestMapping(value = "api/category/starting", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> categoryStarting(@RequestParam String category) throws ParseException {
        return expenseRepositorySpring.findByCategoryStartingWith(anyParser.parse(category, String.class));
    }

    @RequestMapping(value = "api")
    public String usage() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h1>&#x1F627 Something went wrong...</h1>\n" +
                "\n" +
                "<h2>These are the supported REST endpoints</h2>\n" +
                "<ul>\n" +
                "<li>api/add</li>\n" +
                "<li>api/delete</li>\n" +
                "<li>api/all</li>\n" +
                "<li>api/user?user={user}</li>\n" +
                "<li>api/user_trip?user={user}&trip={trip}</li>\n" +
                "<li>api/category?category={category}</li>\n" +
                "<li>api/category/like?category={category}</li>\n" +
                "<li>api/category/starting?category={category}</li>\n" +
                "<li>api/amount/gt?amount={amount}</li>\n" +
                "</ul>\n" +
                "<p><a href=\"/actuator/\">The Actuator</a></p>\n" +
                "</body>\n" +
                "</html>\n";
    }
}
