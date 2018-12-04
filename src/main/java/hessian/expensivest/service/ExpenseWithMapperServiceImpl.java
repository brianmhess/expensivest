package hessian.expensivest.service;

import hessian.expensivest.domain.ExpenseWithMapper;
import hessian.expensivest.repository.ExpenseWithMapperRepository;
import hessian.expensivest.service.ExpenseWithMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseWithMapperServiceImpl implements ExpenseWithMapperService {
    @Autowired
    ExpenseWithMapperRepository repository;

    public ExpenseWithMapperServiceImpl() { super(); }

    @Override
    public void save(ExpenseWithMapper expense) {
        repository.save(expense);
    }

    @Override
    public List<ExpenseWithMapper> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ExpenseWithMapper> findByKeyUser(String user) {
        return repository.findByKeyUser(user);
    }

    @Override
    public List<ExpenseWithMapper> findByKeyUserAndKeyTrip(String user, String trip) {
        return repository.findByKeyUserAndKeyTrip(user, trip);
    }

    @Override
    public List<ExpenseWithMapper> findByCategory(String category) {
        return repository.findByCategory(category);
    }

    @Override
    public List<ExpenseWithMapper> findByAmountGreaterThan(Double amount) {
        return repository.findByAmountGreaterThan(amount);
    }
}
