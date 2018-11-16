package hessian.expensivest.controller;

import hessian.expensivest.domain.Expense;
import hessian.expensivest.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ExpenseRestController {
    @Autowired
    private ExpenseRepository expenseRepository;

    @RequestMapping("api/hello")
    public String hello() {
        return "<html><body><H1>Hello World</H1></body></html>";
    }

    @RequestMapping("api/")
    public List<Expense> all() {
        return (ArrayList<Expense>)expenseRepository.findAll();
    }

    @RequestMapping("api/{user}")
    public List<Expense> user(@PathVariable String user) {
        return (ArrayList<Expense>)expenseRepository.findByUser(user);
    }

    @RequestMapping("api/{user}/{trip}")
    public List<Expense> userTrip(@PathVariable String user, @PathVariable String trip) {
        return (ArrayList<Expense>)expenseRepository.findByUserAndTrip(user, trip);
    }

    @RequestMapping("api/category/{cat}")
    public List<Expense> category(@PathVariable String cat) {
        return (ArrayList<Expense>)expenseRepository.findByCategory(cat);
    }

    @RequestMapping(value = "api/add", method = RequestMethod.POST)
    public Expense createExpense(@RequestBody Expense expense) {
        expenseRepository.save(expense);
        return expense;
    }
}
