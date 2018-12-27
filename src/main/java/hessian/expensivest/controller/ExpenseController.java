package hessian.expensivest.controller;

import com.google.common.collect.Lists;
import hessian.expensivest.domain.ExpenseWithMapper;
import hessian.expensivest.repository.ExpenseWithMapperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType.*;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Controller
public class ExpenseController {
    @Autowired
    private ExpenseWithMapperRepository repository;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private String header() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html>\n");
        sb.append("  <head>\n" +
                "      <meta charset=\"utf-8\">\n" +
                "      <title>Expensivest</title>\n" +
                "      <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">" +
                "    </head>");
        sb.append("<body>\n" +
                "<table>" +
                "<td><image src=\"/vest.png\" title=\"Expensivest\" width=\"100\" height=\"100\"></td>" +
                "<td><font size=\"7\">Expensivest</font></td>" +
                "</table>" +
                "<hr>");

        return sb.toString();
    }

    private String forms() {
        StringBuilder sb = new StringBuilder();
        sb.append("<hr>\n<hr>\n");
        sb.append("<div style=\"margin-left:5em;\">\n");
        sb.append("<p>\n" +
                "  <a class=\"btn btn-primary\" data-toggle=\"collapse\" href=\"#forms\" role=\"button\" aria-expanded=\"false\" aria-controls=\"forms\">\n" +
                "    Query Forms\n" +
                "  </a>\n" +
                "</p>");
        sb.append("<div class=\"collapse\" id=\"forms\">\n");
        sb.append("  <h4>Find Some</h4>\n");
        sb.append("  <form action=\"/ui/some\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"some\">How Many:</label>");
        sb.append("      <input type=\"text\" id=\"some\" name=\"some\">");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find Some</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>New Expense</h4>\n");
        sb.append("  <form action=\"/ui/add\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"user\">User:</label>");
        sb.append("      <input type=\"text\" id=\"user\" name=\"user\">");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"trip\">Trip:</label>");
        sb.append("      <input type=\"text\" id=\"trip\" name=\"trip\">");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"expts\">When (<i>YYYY-MM-DD HH:MM:SS</i>):</label>");
        sb.append("      <input type=\"text\" id=\"expts\" name=\"expts\">");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"amount\">Amount:</label>");
        sb.append("      <input type=\"text\" id=\"amount\" name=\"amount\">");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"category\">Category:</label>");
        sb.append("      <input type=\"text\" id=\"category\" name=\"category\">");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"comment\">Comment:</label>");
        sb.append("      <input type=\"text\" id=\"comment\" name=\"comment\">");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Save New Expense</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Find By User</h4>\n");
        sb.append("  <form action=\"/ui/user\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"user\">User:</label>");
        sb.append("      <input type=\"text\" id=\"user\" name=\"user\">");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find By User</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Find By User And Trip</h4>\n");
        sb.append("  <form action=\"/ui/user_trip\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"user\">User:</label>");
        sb.append("      <input type=\"text\" id=\"user\" name=\"user\">");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"trip\">Trip:</label>");
        sb.append("      <input type=\"text\" id=\"trip\" name=\"trip\">");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find By User And Trip</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Find By Category</h4>\n");
        sb.append("  <form action=\"/ui/category\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"category\">Category:</label>");
        sb.append("      <input type=\"text\" id=\"category\" name=\"category\">");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find By Category</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Find Big Expenses</h4>\n");
        sb.append("  <form action=\"/ui/amount_gt\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"amount\">Minimum Amount:</label>");
        sb.append("      <input type=\"text\" id=\"amount\" name=\"amount\">");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find Big Expenses</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Global Aggregates</h4>\n");
        sb.append("  <form action=\"/ui/agg_global\" method=\"post\">\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Global Aggregates</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Aggregates By User</h4>\n");
        sb.append("  <form action=\"/ui/agg_user\" method=\"post\">\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Aggs By User</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Aggregates By User And Trip</h4>\n");
        sb.append("  <form action=\"/ui/agg_user_trip\" method=\"post\">\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Aggs By User And Trip</button></div>\n");
        sb.append("  </form>\n");

        sb.append("</div>\n");
        sb.append("</div>\n");

        return sb.toString();
    };

    private String footer() {
        StringBuilder sb = new StringBuilder();
        sb.append("<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
                "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>");
        sb.append("</body>\n</html>\n");

        return sb.toString();
    }

    private String makeTable(List<ExpenseWithMapper> expenses) {
        expenses.sort(new Comparator<ExpenseWithMapper>() {
            @Override
            public int compare(ExpenseWithMapper e1, ExpenseWithMapper e2) {
                if (e1.getUser() == e2.getUser()) {
                    if (e1.getTrip() == e2.getTrip()) {
                        if (e1.getExpts() == e2.getExpts())
                            return 0;
                        else
                            return e2.getExpts().compareTo(e1.getExpts());
                    }
                    else
                        return e1.getTrip().compareTo(e2.getTrip());
                }
                else
                    return e1.getUser().compareTo(e2.getUser());
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append("<div>\n");
        sb.append("<table border=\"1\" style=\"margin-left:5em;\">\n");
        sb.append("  <tr>\n");
        sb.append("    <th>User</th>\n");
        sb.append("    <th>Trip</th>\n");
        sb.append("    <th>When</th>\n");
        sb.append("    <th>Category</th>\n");
        sb.append("    <th>Amount</th>\n");
        sb.append("    <th>Comment</th>\n");
        sb.append("  </tr>\n");
        for (ExpenseWithMapper e : expenses) {
            sb.append("  <tr>\n");
            sb.append("    <td>" + e.getUser() + "</td>\n");
            sb.append("    <td>" + e.getTrip() + "</td>\n");
            sb.append("    <td>" + dateFormat.format(e.getExpts()) + "</td>\n");
            sb.append("    <td>" + e.getCategory() + "</td>\n");
            sb.append("    <td>" + e.getAmount() + "</td>\n");
            sb.append("    <td>" + e.getComment() + "</td>\n");
            sb.append("  </tr>\n");
        }
        sb.append("</table>\n");
        sb.append("</div>\n");
        sb.append("<p>&nbsp\n");

        return sb.toString();
    }

    private String makeAggTable(List<ExpenseWithMapperRepository.SumCount> aggs) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div>\n");
        sb.append("<table border=\"1\" style=\"margin-left:5em;\">\n");
        sb.append("  <tr>\n");
        sb.append("    <th>User</th>\n");
        sb.append("    <th>Trip</th>\n");
        sb.append("    <th>Count</th>\n");
        sb.append("    <th>Sum</th>\n");
        sb.append("  </tr>\n");
        for (ExpenseWithMapperRepository.SumCount e : aggs) {
            sb.append("  <tr>\n");
            sb.append("    <td>" + e.getUser() + "</td>\n");
            sb.append("    <td>" + e.getTrip() + "</td>\n");
            sb.append("    <td>" + e.getCount_val() + "</td>\n");
            sb.append("    <td>" + e.getSum_val() + "</td>\n");
            sb.append("  </tr>\n");
        }
        sb.append("</table>\n");
        sb.append("</div>\n");
        sb.append("<p>&nbsp\n");

        return sb.toString();
    }

    @RequestMapping("/")
    @ResponseBody
    public String hello() {
        return "<!DOCTYPE html>\n<html>\n  <head>\n    <meta charset=\"utf-8\">\n    <title>My test page</title>\n  </head>\n<body>" +
                "<table>" +
                "<td><image src=\"/vest.png\" title=\"Expensivest\" width=\"100\" height=\"100\"></td>" +
                "<td><font size=\"7\">Expensivest</font></td>" +
                "</table>" +
                "<hr>" +
                "<h4>Hello World</h4></body></html>";
    }

    @RequestMapping("ui/")
    @ResponseBody
    public String index() {
        return header() + makeTable(repository.findSome(10)) + forms() + footer();
    }

    @RequestMapping(value = "ui/some", method = RequestMethod.POST)
    @ResponseBody
    public String some(@RequestParam("some") Integer some) {
        return header() + makeTable(repository.findSome(some)) + forms() + footer();
    }

    @RequestMapping(value = "ui/delete", method = RequestMethod.POST)
    @ResponseBody
    public String some(@RequestParam("user") String user,
                       @RequestParam("trip") String trip,
                       @RequestParam("expts") Date expts) {
        repository.delete(user, trip, expts);
        return header() + makeTable(repository.findSome(10)) + forms() + footer();
    }

    @RequestMapping(value = "ui/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestParam("user") String user,
                      @RequestParam("trip") String trip,
                      @RequestParam("expts") String expts,
                      @RequestParam("amount") String amount,
                      @RequestParam("category") String category,
                      @RequestParam("comment") String comment) {
        Date tdate = null;
        try {
            tdate = dateFormat.parse(expts);
        }
        catch (Exception e){
            return header() + "<p>Couldn't parse date: expts = " + expts + forms() + footer();
        }
        ExpenseWithMapper e = new ExpenseWithMapper(user, trip, tdate, Double.valueOf(amount), category, comment);
        repository.save(user, trip, expts, amount, category, comment);
        return header() + makeTable(Lists.newArrayList(e)) + forms() + footer();
    }

    @RequestMapping(value = "ui/user", method = RequestMethod.POST)
    @ResponseBody
    public String user(@RequestParam("user") String user) {
        return header() + makeTable(repository.findByKeyUser(user)) + forms() + footer();
    }

    @RequestMapping(value = "ui/user_trip", method = RequestMethod.POST)
    @ResponseBody
    public String user_trip(@RequestParam("user") String user, @RequestParam("trip") String trip) {
        return header() + makeTable(repository.findByKeyUserAndKeyTrip(user, trip)) + forms() + footer();
    }

    @RequestMapping(value = "ui/category", method = RequestMethod.POST)
    @ResponseBody
    public String category(@RequestParam("category") String category) {
        return header() + makeTable(repository.findByCategory(category)) + forms() + footer();
    }

    @RequestMapping(value = "ui/amount_gt", method = RequestMethod.POST)
    @ResponseBody
    public String amount_gt(@RequestParam("amount") String amount) {
        return header() + makeTable(repository.findByAmountGreaterThan(Double.valueOf(amount))) + forms() + footer();
    }

    @RequestMapping(value = "ui/agg_global", method = RequestMethod.POST)
    @ResponseBody
    public String agg_global() {
        return header() + makeAggTable(Lists.newArrayList(repository.sumCountGlobal())) + forms() + footer();
    }

    @RequestMapping(value = "ui/agg_user", method = RequestMethod.POST)
    @ResponseBody
    public String agg_user() {
        return header() + makeAggTable(repository.sumCountByUser()) + forms() + footer();
    }

    @RequestMapping(value = "ui/agg_user_trip", method = RequestMethod.POST)
    @ResponseBody
    public String agg_user_trip() {
        return header() + makeAggTable(repository.sumCountByUserAndTrip()) + forms() + footer();
    }

}
