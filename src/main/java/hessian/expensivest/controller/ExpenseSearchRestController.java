package hessian.expensivest.controller;

import hessian.expensivest.domain.ExpenseWithSearch;
import hessian.expensivest.repository.ExpenseSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("api/search/{user}")
    public List<ExpenseWithSearch> user(@PathVariable String user) {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByKeyUser(user);
    }

    @RequestMapping("api/search/{user}/{trip}")
    public List<ExpenseWithSearch> userTrip(@PathVariable String user, @PathVariable String trip) {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByKeyUserAndKeyTrip(user, trip);
    }

    @RequestMapping("api/search/category/{cat}")
    public List<ExpenseWithSearch> category(@PathVariable String cat) {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByCategory(cat);
    }

    @RequestMapping("api/search/category/like/{cat}")
    public List<ExpenseWithSearch> categoryLike(@PathVariable String cat) {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByCategoryLike(cat);
    }

    @RequestMapping("api/search/category/starting/{cat}")
    public List<ExpenseWithSearch> categoryStarting(@PathVariable String cat) {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByCategoryStartingWith(cat);
    }

    @RequestMapping("api/search/amount/gt/{amount}")
    public List<ExpenseWithSearch> category(@PathVariable double amount) {
        return (ArrayList<ExpenseWithSearch>)expenseSearchRepository.findByAmountGreaterThan(amount);
    }

}
