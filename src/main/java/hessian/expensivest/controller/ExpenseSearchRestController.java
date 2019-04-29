package hessian.expensivest.controller;

import hessian.expensivest.domain.ExpenseWithSearch;
import hessian.expensivest.repository.ExpenseSearchRepository;
import hessian.typeparser.AnyParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
public class ExpenseSearchRestController {
    @Autowired
    private ExpenseSearchRepository expenseSearchRepository;
    private AnyParser anyParser = new AnyParser();

    @GetMapping("api/search/hello")
    public String hello() {
        return "<html><body><H1>Hello World</H1></body></html>";
    }

    @RequestMapping("api/search/")
    public List<ExpenseWithSearch> all() {
        return expenseSearchRepository.findAll();
    }

    @RequestMapping("api/search/user/{user}")
    public List<ExpenseWithSearch> user(@PathVariable String user) throws ParseException {
        return expenseSearchRepository.findByKeyUser(anyParser.parse(user, String.class));
    }

    @RequestMapping("api/search/user_trip/{user}/{trip}")
    public List<ExpenseWithSearch> userTrip(@PathVariable String user, @PathVariable String trip) throws ParseException {
        return expenseSearchRepository.findByKeyUserAndKeyTrip(anyParser.parse(user, String.class), anyParser.parse(trip, String.class));
    }

    @RequestMapping("api/search/category/{cat}")
    public List<ExpenseWithSearch> category(@PathVariable String cat) throws ParseException {
        return expenseSearchRepository.findByCategory(anyParser.parse(cat, String.class));
    }

    @RequestMapping("api/search/category/like/{cat}")
    public List<ExpenseWithSearch> categoryLike(@PathVariable String cat) throws ParseException {
        return expenseSearchRepository.findByCategoryLike(anyParser.parse(cat, String.class));
    }

    @RequestMapping("api/search/category/starting/{cat}")
    public List<ExpenseWithSearch> categoryStarting(@PathVariable String cat) throws ParseException {
        return expenseSearchRepository.findByCategoryStartingWith(anyParser.parse(cat, String.class));
    }

    @RequestMapping("api/search/amount/gt/{amount}")
    public List<ExpenseWithSearch> amount_gt(@PathVariable String amount) throws ParseException {
        return expenseSearchRepository.findByAmountGreaterThan(anyParser.parse(amount, Double.class));
    }

}
