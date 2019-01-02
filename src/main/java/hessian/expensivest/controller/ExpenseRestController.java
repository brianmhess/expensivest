package hessian.expensivest.controller;

import hessian.expensivest.domain.Expense;
import hessian.expensivest.repository.ExpenseRepository;
import hessian.expensivest.repository.ExpenseSearchRepository;
import hessian.typeparser.AnyParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExpenseRestController {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseSearchRepository expenseSearchRepository;

    @RequestMapping("api/hello")
    public String hello() {
        return "<html><body><H1>Hello World</H1></body></html>";
    }

    @RequestMapping(value = "api/add", method = RequestMethod.POST)
    public Expense createExpense(@RequestBody Expense expense) {
        expenseRepository.save(expense);
        expenseSearchRepository.save(expense);
        return expense;
    }

    @RequestMapping("api/")
    public List<Expense> all() {
        return (ArrayList<Expense>)expenseRepository.findAll();
    }

    @RequestMapping("api/user/{user}")
    public List<Expense> user(@PathVariable String user) throws ParseException {
        return (ArrayList<Expense>)expenseRepository.findByKeyUser(AnyParser.parse(user, String.class));
    }

    @RequestMapping("api/user_trip/{user}/{trip}")
    public List<Expense> userTrip(@PathVariable String user, @PathVariable String trip) throws ParseException {
        return (ArrayList<Expense>)expenseRepository.findByKeyUserAndKeyTrip(AnyParser.parse(user, String.class), AnyParser.parse(trip, String.class));
    }

    @RequestMapping("api/category/{cat}")
    public List<Expense> category(@PathVariable String cat) throws ParseException {
        return (ArrayList<Expense>)expenseRepository.findByCategory(AnyParser.parse(cat, String.class));
    }

    @RequestMapping("api/amount/gt/{amount}")
    public List<Expense> amount_gt(@PathVariable String amount) throws ParseException {
        return (ArrayList<Expense>)expenseRepository.findByAmountGreaterThan(AnyParser.parse(amount, Double.class));
    }

}
