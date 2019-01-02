package hessian.expensivest.controller;

import hessian.expensivest.domain.ExpenseWithMapper;
import hessian.expensivest.repository.ExpenseWithMapperRepository;
import hessian.typeparser.AnyParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
public class ExpenseWithMapperRestController {
    @Autowired
    private ExpenseWithMapperRepository repository;

    @RequestMapping("api/dse/hello")
    public String hello() {
        return "<html><body><H1>Hello World</H1></body></html>";
    }

    @RequestMapping(value = "api/dse/add", method = RequestMethod.POST)
    public ExpenseWithMapper createExpense(@RequestBody ExpenseWithMapper expense) {
        repository.save(expense);
        return expense;
    }

    @RequestMapping("api/dse/all")
    public List<ExpenseWithMapper> all() {
        return repository.findAll();
    }

    @RequestMapping("api/dse/some")
    public List<ExpenseWithMapper> some() {
        return repository.findSome(10);
    }

    @RequestMapping("api/dse/some/{some}")
    public List<ExpenseWithMapper> some(@PathVariable String some) throws ParseException {
        return repository.findSome(AnyParser.parse(some, Integer.class));
    }

    @RequestMapping("api/dse/user/{user}")
    public List<ExpenseWithMapper> user(@PathVariable String user) throws ParseException {
        return repository.findByKeyUser(AnyParser.parse(user, String.class));
    }

    @RequestMapping("api/dse/user_trip/{user}/{trip}")
    public List<ExpenseWithMapper> userTrip(@PathVariable String user, @PathVariable String trip) throws ParseException {
        return repository.findByKeyUserAndKeyTrip(AnyParser.parse(user, String.class), AnyParser.parse(trip, String.class));
    }

    @RequestMapping("api/dse/category/{cat}")
    public List<ExpenseWithMapper> category(@PathVariable String cat) throws ParseException {
        return repository.findByCategory(AnyParser.parse(cat, String.class));
    }

    @RequestMapping("api/dse/amount/gt/{amount}")
    public List<ExpenseWithMapper> amountGreaterThan(@PathVariable String amount) throws ParseException {
        return repository.findByAmountGreaterThan(AnyParser.parse(amount, Double.class));
    }

    @RequestMapping("api/dse/sum_count/global")
    public ExpenseWithMapperRepository.SumCount sumCountGlobal() {
        return repository.sumCountGlobal();
    }
    @RequestMapping("api/dse/sum_count/user")
    public List<ExpenseWithMapperRepository.SumCount> sumCountByUser() {
        return repository.sumCountByUser();
    }

    @RequestMapping("api/dse/sum_count/user_and_trip")
    public List<ExpenseWithMapperRepository.SumCount> sumCountByUserAndTrip() {
        return repository.sumCountByUserAndTrip();
    }
}
