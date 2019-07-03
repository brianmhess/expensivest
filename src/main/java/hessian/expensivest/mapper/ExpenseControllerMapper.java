package hessian.expensivest.mapper;

import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.oss.driver.api.core.CqlSession;
import hessian.typeparser.AnyParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Date;

@RestController
public class ExpenseControllerMapper {
    @Autowired
    private ExpenseDao repository;
    private AnyParser anyParser = new AnyParser();

    @RequestMapping(value = "dse/hello")
    public String hello() {
        return "<html><body><H1>Hello World</H1></body></html>";
    }

    @RequestMapping(value = "dse/add", method = {RequestMethod.POST, RequestMethod.GET})
    public Expense createExpense(@RequestBody Expense expense) {
        repository.save(expense);
        return expense;
    }

    @RequestMapping(value = "dse/delete", method = {RequestMethod.POST, RequestMethod.GET})
    public void delete(@RequestParam String user, @RequestParam String trip, @RequestParam String expts) throws ParseException {
        repository.delete(anyParser.parse(user, String.class), anyParser.parse(trip, String.class), anyParser.parse(expts, Date.class));
    }

    @RequestMapping(value = "dse/all")
    public List<Expense> all() {
        return repository.findAll().all();
    }

    @RequestMapping(value = "dse/ten")
    public List<Expense> some() {
        return repository.findSome(10).all();
    }

    @RequestMapping(value = "dse/some", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> some(@RequestParam String some) throws ParseException {
        return repository.findSome(anyParser.parse(some, Integer.class)).all();
    }

    // Lookups
    @RequestMapping(value = "dse/user", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> user(@RequestParam String user) throws ParseException {
        return repository.findByKeyUser(anyParser.parse(user, String.class)).all();
    }

    @RequestMapping(value = "dse/user_trip", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> userTrip(@RequestParam String user, @RequestParam String trip) throws ParseException {
        return repository.findByKeyUserAndKeyTrip(anyParser.parse(user, String.class), anyParser.parse(trip, String.class)).all();
    }

    @RequestMapping(value = "dse/category", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> category(@RequestParam String category) throws ParseException {
        return repository.findByCategory(anyParser.parse(category, String.class)).all();
    }

    // Inequalities
    @RequestMapping(value = "dse/amount/gt", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> amountGreaterThan(@RequestParam String amount) throws ParseException {
        return repository.findByAmountGreaterThan(anyParser.parse(amount, Double.class)).all();
    }

    // String search queries
    @RequestMapping(value = "dse/category/like", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> categoryLike(@RequestParam String category) throws ParseException {
        return repository.findByCategoryLike(anyParser.parse(category, String.class)).all();
    }

    @RequestMapping(value = "dse/category/starting", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Expense> categoryStarts(@RequestParam String category) throws ParseException {
        return repository.findByCategoryStartingWith(anyParser.parse(category, String.class)).all();
    }

    // Aggregates
    @RequestMapping(value = "dse/sum_count/global")
    public ExpenseSumCount sumCountGlobal() {
        return repository.sumCountGlobal();
    }
    @RequestMapping(value = "dse/sum_count/user")
    public List<ExpenseSumCount> sumCountByUser() {
        return repository.sumCountByUser().all();
    }

    @RequestMapping(value = "dse/sum_count/user_and_trip")
    public List<ExpenseSumCount> sumCountByUserAndTrip() {
        return repository.sumCountByUserAndTrip().all();
    }

    @RequestMapping(value = "dse")
    public String usage() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<h1>&#x1F627 Something went wrong...</h1>\n" +
                "\n" +
                "<h2>These are the supported REST endpoints</h2>\n" +
                "<ul>\n" +
                "<li>dse/add</li>\n" +
                "<li>dse/delete</li>\n" +
                "<li>dse/all</li>\n" +
                "<li>dse/ten</li>\n" +
                "<li>dse/some?some={some}</li>\n" +
                "<li>dse/user?user={user}</li>\n" +
                "<li>dse/user_trip?user={user}&trip={trip}</li>\n" +
                "<li>dse/category?category={category}</li>\n" +
                "<li>dse/category/like?category={category}</li>\n" +
                "<li>dse/category/starting?category={category}</li>\n" +
                "<li>dse/amount/gt?amount={amount}</li>\n" +
                "<li>dse/sum_count/global</li>\n" +
                "<li>dse/sum_count/user</li>\n" +
                "<li>dse/sum_count/user_and_trip</li>\n" +
                "</ul>\n" +
                "<p><a href=\"/actuator/\">The Actuator</a></p>\n" +
                "</body>\n" +
                "</html>\n";
    }
}
