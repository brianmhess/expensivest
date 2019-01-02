package hessian.expensivest.controller;

import hessian.expensivest.domain.ExpenseWithSearch;
import hessian.expensivest.repository.ExpenseSearchRepository;
import hessian.typeparser.AnyParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExpenseSearchRestController {
    @Autowired
    private ExpenseSearchRepository expenseSearchRepository;

    @GetMapping("api/search/hello")
    public String hello() {
        return "<html><body><H1>Hello World</H1></body></html>";
    }

    @RequestMapping("api/search/")
    public List<ExpenseWithSearch> all() {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findAll();
    }

    @RequestMapping("api/search/user/{user}")
    public List<ExpenseWithSearch> user(@PathVariable String user) throws ParseException {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByKeyUser(AnyParser.parse(user, String.class));
    }

    @RequestMapping("api/search/user_trip/{user}/{trip}")
    public List<ExpenseWithSearch> userTrip(@PathVariable String user, @PathVariable String trip) throws ParseException {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByKeyUserAndKeyTrip(AnyParser.parse(user, String.class), AnyParser.parse(trip, String.class));
    }

    @RequestMapping("api/search/category/{cat}")
    public List<ExpenseWithSearch> category(@PathVariable String cat) throws ParseException {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByCategory(AnyParser.parse(cat, String.class));
    }

    @RequestMapping("api/search/category/like/{cat}")
    public List<ExpenseWithSearch> categoryLike(@PathVariable String cat) throws ParseException {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByCategoryLike(AnyParser.parse(cat, String.class));
    }

    @RequestMapping("api/search/category/starting/{cat}")
    public List<ExpenseWithSearch> categoryStarting(@PathVariable String cat) throws ParseException {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByCategoryStartingWith(AnyParser.parse(cat, String.class));
    }

    @RequestMapping("api/search/amount/gt/{amount}")
    public List<ExpenseWithSearch> amount_gt(@PathVariable String amount) throws ParseException {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByAmountGreaterThan(AnyParser.parse(amount, Double.class));
    }

}
