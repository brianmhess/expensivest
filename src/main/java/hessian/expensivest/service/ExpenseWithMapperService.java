package hessian.expensivest.service;


import hessian.expensivest.domain.ExpenseWithMapper;

import java.util.List;

public interface ExpenseWithMapperService {
    public void save(ExpenseWithMapper expenseWithMapper);
    public List<ExpenseWithMapper> findAll();
    public List<ExpenseWithMapper> findByKeyUser(String user);
    public List<ExpenseWithMapper> findByKeyUserAndKeyTrip(String user, String trip);
    public List<ExpenseWithMapper> findByCategory(String category);
    public List<ExpenseWithMapper> findByAmountGreaterThan(Double amount);
}
