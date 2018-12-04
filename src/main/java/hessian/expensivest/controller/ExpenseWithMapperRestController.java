package hessian.expensivest.controller;

import hessian.expensivest.domain.ExpenseWithMapper;
import hessian.expensivest.repository.ExpenseWithMapperRepository;
import hessian.expensivest.service.ExpenseWithMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpenseWithMapperRestController {
    //@Autowired
    //private ExpenseWithMapperService service;
    @Autowired
    private ExpenseWithMapperRepository service;

    @RequestMapping("api/dse/hello")
    public String hello() {
        return "<html><body><H1>Hello World</H1></body></html>";
    }

    @RequestMapping(value = "api/dse/add", method = RequestMethod.POST)
    public ExpenseWithMapper createExpense(@RequestBody ExpenseWithMapper expense) {
        service.save(expense);
        return expense;
    }

    @RequestMapping("api/dse/all")
    public List<ExpenseWithMapper> all() {
        return service.findAll();
    }

    @RequestMapping("api/dse/some")
    public List<ExpenseWithMapper> some() {
        return service.findSome(10);
    }

    @RequestMapping("api/dse/some/{some}")
    public List<ExpenseWithMapper> some(@PathVariable Integer some) {
        return service.findSome(some);
    }

    @RequestMapping("api/dse/user/{user}")
    public List<ExpenseWithMapper> user(@PathVariable String user) {
        return service.findByKeyUser(user);
    }

    @RequestMapping("api/dse/user_trip/{user}/{trip}")
    public List<ExpenseWithMapper> userTrip(@PathVariable String user, @PathVariable String trip) {
        return service.findByKeyUserAndKeyTrip(user, trip);
    }

    @RequestMapping("api/dse/category/{cat}")
    public List<ExpenseWithMapper> category(@PathVariable String cat) {
        return service.findByCategory(cat);
    }

    @RequestMapping("api/dse/amount/gt/{amount}")
    public List<ExpenseWithMapper> amountGreaterThan(@PathVariable double amount) {
        return service.findByAmountGreaterThan(amount);
    }

    @RequestMapping("api/dse/sum_count/global")
    public ExpenseWithMapperRepository.SumCount sumCountGlobal() {
        return service.sumCountGlobal();
    }
    @RequestMapping("api/dse/sum_count/user")
    public List<ExpenseWithMapperRepository.SumCount> sumCountByUser() {
        return service.sumCountByUser();
    }

    @RequestMapping("api/dse/sum_count/user_and_trip")
    public List<ExpenseWithMapperRepository.SumCount> sumCountByUserAndTrip() {
        return service.sumCountByUserAndTrip();
    }
}
