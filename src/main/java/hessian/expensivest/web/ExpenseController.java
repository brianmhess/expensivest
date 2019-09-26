package hessian.expensivest.web;

import hessian.expensivest.mapper.*;
import hessian.typeparser.AnyParser;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class ExpenseController {
    @Autowired
    private ExpenseDao repository;
    private AnyParser anyParser = AnyParser.defaultParser;

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
        sb.append("  <form action=\"/web/some\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"some\">How Many:</label>");
        sb.append("      <input type=\"text\" id=\"some\" name=\"some\" required pattern=\"[0-9]+\">");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find Some</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>New Expense</h4>\n");
        sb.append("  <form action=\"/web/add\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"user\">User:</label>");
        sb.append("      <input type=\"text\" id=\"user\" name=\"user\" required>");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"trip\">Trip:</label>");
        sb.append("      <input type=\"text\" id=\"trip\" name=\"trip\" required>");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"expts\">When (<i>YYYY-MM-DD HH:MM:SS</i>):</label>");
        sb.append("      <input type=\"text\" id=\"expts\" name=\"expts\" required pattern=\"[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\">");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"amount\">Amount:</label>");
        sb.append("      <input type=\"text\" id=\"amount\" name=\"amount\" required>");
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
        sb.append("  <form action=\"/web/user\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"user\">User:</label>");
        sb.append("      <input type=\"text\" id=\"user\" name=\"user\" required>");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find By User</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Find By User And Trip</h4>\n");
        sb.append("  <form action=\"/web/user_trip\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"user\">User:</label>");
        sb.append("      <input type=\"text\" id=\"user\" name=\"user\" required>");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"trip\">Trip:</label>");
        sb.append("      <input type=\"text\" id=\"trip\" name=\"trip\" required>");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find By User And Trip</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Find By Category</h4>\n");
        sb.append("  <form action=\"/web/category\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"category\">Category:</label>");
        sb.append("      <input type=\"text\" id=\"category\" name=\"category\" required>");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find By Category</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Find By Category Like</h4>\n");
        sb.append("  <form action=\"/web/category/like\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"category\">Category:</label>");
        sb.append("      <input type=\"text\" id=\"category\" name=\"category\" required>");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find By Category Like</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Find By Category Starting With</h4>\n");
        sb.append("  <form action=\"/web/category/starting\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"category\">Category:</label>");
        sb.append("      <input type=\"text\" id=\"category\" name=\"category\" required>");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find By Category Starting With</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Find Big Expenses</h4>\n");
        sb.append("  <form action=\"/web/amount/gt\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"amount\">Minimum Amount:</label>");
        sb.append("      <input type=\"text\" id=\"amount\" name=\"amount\" required>");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Find Big Expenses</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Global Aggregates</h4>\n");
        sb.append("  <form action=\"/web/sum_count/global\" method=\"post\">\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Global Aggregates</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Aggregates By User</h4>\n");
        sb.append("  <form action=\"/web/sum_count/user\" method=\"post\">\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Aggs By User</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Aggregates By User And Trip</h4>\n");
        sb.append("  <form action=\"/web/sum_count/user_and_trip\" method=\"post\">\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Aggs By User And Trip</button></div>\n");
        sb.append("  </form>\n");

        sb.append("  <hr>\n");
        sb.append("  <h4>Delete Expense</h4>\n");
        sb.append("  <form action=\"/web/delete\" method=\"post\">\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"user\">User:</label>");
        sb.append("      <input type=\"text\" id=\"user\" name=\"user\" required>");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"trip\">Trip:</label>");
        sb.append("      <input type=\"text\" id=\"trip\" name=\"trip\" required>");
        sb.append("    <div>\n");
        sb.append("    <div>\n");
        sb.append("      <label for=\"expts\">When (<i>YYYY-MM-DD HH:MM:SS</i>):</label>");
        sb.append("      <input type=\"text\" id=\"expts\" name=\"expts\" required pattern=\"[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\">");
        sb.append("    <div>\n");
        sb.append("    <div class=\"button\"><button type=\"submit\">Delete Expense</button></div>\n");
        sb.append("  </form>\n");

        sb.append("</div>\n");
        sb.append("</div>\n");

        return sb.toString();
    };

    private String footer() {
        return "<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>\n" +
                "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>" +
                "</body>\n</html>\n";
    }

    private String makeTable(Publisher<Expense> expenses) {
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
        sb.append(Flux.from(expenses)
                        .sort(new Comparator<Expense>() {
                            private int compareNulls(Object o1, Object o2) {
                                if (null == o1) {
                                    if (null == o2)
                                        return 0;
                                    else
                                        return -1;
                                }
                                if (null == o2)
                                    return 1;
                                return 2;
                            }
                            @Override
                            public int compare(Expense e1, Expense e2) {
                                int ret = compareNulls(e1, e2);
                                if (2 != ret)
                                    return ret;
                                ret = compareNulls(e1.getUser(), e2.getUser());
                                if (2 != ret)
                                    return ret;
                                if (e1.getUser().equals(e2.getUser())) {
                                    ret = compareNulls(e1.getTrip(), e2.getTrip());
                                    if (2 != ret)
                                        return ret;
                                    if (e1.getTrip().equals(e2.getTrip())) {
                                        ret = compareNulls(e1.getExpts(), e2.getExpts());
                                        if (2 != ret)
                                            return ret;
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
                        })
                        .map(e -> {
                            String s = null;
                            try {
                                s = String.format("  <tr>\n" +
                                                "    <td>%s</td>\n" +
                                                "    <td>%s</td>\n" +
                                                "    <td>%s</td>\n" +
                                                "    <td>%s</td>\n" +
                                                "    <td>%s</td>\n" +
                                                "    <td>%s</td>\n  </tr>\n",
                                        anyParser.format(e.getUser()),
                                        anyParser.format(e.getTrip()),
                                        anyParser.format(e.getExpts()),
                                        anyParser.format(e.getCategory()),
                                        anyParser.format(e.getAmount()),
                                        anyParser.format(e.getComment())
                                );
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            return s;
                        }).collect(Collectors.joining()).block()
        );

        sb.append("</table>\n");
        sb.append("</div>\n");
        sb.append("<p>&nbsp\n");

        return sb.toString();
    }

    private String makeAggTable(Publisher<ExpenseSumCount> aggs) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div>\n");
        sb.append("<table border=\"1\" style=\"margin-left:5em;\">\n");
        sb.append("  <tr>\n");
        sb.append("    <th>User</th>\n");
        sb.append("    <th>Trip</th>\n");
        sb.append("    <th>Count</th>\n");
        sb.append("    <th>Sum</th>\n");
        sb.append("  </tr>\n");
        sb.append(Flux.from(aggs)
                .sort(new Comparator<ExpenseSumCount>() {
                    private int compareNulls(Object o1, Object o2) {
                        if (null == o1) {
                            if (null == o2)
                                return 0;
                            else
                                return -1;
                        }
                        if (null == o2)
                            return 1;
                        return 2;
                    }
                    @Override
                    public int compare(ExpenseSumCount e1, ExpenseSumCount e2) {
                        int ret = compareNulls(e1, e2);
                        if (2 != ret)
                            return ret;
                        ret = compareNulls(e1.getUser(), e2.getUser());
                        if (2 != ret)
                            return ret;
                        if (e1.getUser().equals(e2.getUser())) {
                            ret = compareNulls(e1.getTrip(), e2.getTrip());
                            if (2 != ret)
                                return ret;
                            return e1.getTrip().compareTo(e2.getTrip());
                        }
                        else
                            return e1.getUser().compareTo(e2.getUser());
                    }
                })
                .map(e -> {
                    String s = null;
                    try {
                        s = String.format("  <tr>\n" +
                                        "    <td>%s</td>\n" +
                                        "    <td>%s</td>\n" +
                                        "    <td>%s</td>\n" +
                                        "    <td>%s</td>\n" +
                                        "  </td>\n",
                                anyParser.format(e.getUser()),
                                anyParser.format(e.getTrip()),
                                anyParser.format(e.getCount_val()),
                                anyParser.format(e.getSum_val()));
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return s;
                })
                .collect(Collectors.joining())
                .block()
        );

        sb.append("</table>\n");
        sb.append("</div>\n");
        sb.append("<p>&nbsp\n");

        return sb.toString();
    }

    private String returnString(String str) {
        return header() + str + forms() + footer();
    }

    @RequestMapping("/")
    @ResponseBody
    public String hello() {
        return returnString("<h4>Hello World</h4>");
    }

    @RequestMapping("web")
    @ResponseBody
    public String index() {
        return returnString("<h4>Sum/Count by User and Trip</h4>" + makeAggTable(repository.sumCountByUserAndTrip()));
    }

    @RequestMapping(value = "web/some", method = RequestMethod.POST)
    @ResponseBody
    public String some(@RequestParam("some") String some) throws ParseException{
        return returnString("<h4>" + some + " records</h4>" + makeTable(repository.findSome(anyParser.parse(some, Integer.class))));
    }

    @RequestMapping(value = "web/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam("user") String user,
                       @RequestParam("trip") String trip,
                       @RequestParam("expts") String expts)  throws ParseException {
        Instant tdate = anyParser.parse(expts, Instant.class);
        repository.delete(user, trip, tdate);
        return returnString(makeTable(repository.findByKeyUserAndKeyTrip(user, trip)));
    }

    @RequestMapping(value = "web/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestParam("user") String user,
                      @RequestParam("trip") String trip,
                      @RequestParam("expts") String expts,
                      @RequestParam("amount") String amount,
                      @RequestParam("category") String category,
                      @RequestParam("comment") String comment) throws  ParseException {
        Instant tdate = anyParser.parse(expts, Instant.class);
        Double tdouble = anyParser.parse(amount, Double.class);
        Expense e = new Expense(user, trip, tdate, tdouble, category, comment);
        repository.save(e);
        return returnString("<h4>Created new record</h4>" + makeTable(Mono.just(e)));
    }

    @RequestMapping(value = "web/user", method = RequestMethod.POST)
    @ResponseBody
    public String user(@RequestParam("user") String user) throws ParseException {
        return returnString("<h4>Records for " + user + "</h4>" + makeTable(repository.findByKeyUser(anyParser.parse(user, String.class))));
    }

    @RequestMapping(value = "web/user_trip", method = RequestMethod.POST)
    @ResponseBody
    public String user_trip(@RequestParam("user") String user, @RequestParam("trip") String trip) throws ParseException {
        return returnString("<h4>Records for " + user + " and trip " + trip + "</h4>" + makeTable(repository.findByKeyUserAndKeyTrip(anyParser.parse(user, String.class), anyParser.parse(trip, String.class))));
    }

    @RequestMapping(value = "web/category", method = RequestMethod.POST)
    @ResponseBody
    public String category(@RequestParam("category") String category) throws ParseException {
        return returnString("<h4>Records for category " + category + "</h4>" + makeTable(repository.findByCategory(anyParser.parse(category, String.class))));
    }

    @RequestMapping(value = "web/category/like", method = RequestMethod.POST)
    @ResponseBody
    public String categoryLike(@RequestParam String category) throws ParseException {
        return returnString("<h4>Records for category like " + category + "</h4>" + makeTable(repository.findByCategoryLike(anyParser.parse(category, String.class))));
    }

    @RequestMapping(value = "web/category/starting", method = RequestMethod.POST)
    @ResponseBody
    public String categoryStarts(@RequestParam String category) throws ParseException {
        return returnString("<h4>Records for category starting " + category + "</h4>" + makeTable(repository.findByCategoryStartingWith(anyParser.parse(category, String.class))));
    }

    @RequestMapping(value = "web/amount/gt", method = RequestMethod.POST)
    @ResponseBody
    public String amount_gt(@RequestParam("amount") String amount) throws ParseException {
        return returnString("<h4>Records with amount greater than " + amount + "</h4>" + makeTable(repository.findByAmountGreaterThan(anyParser.parse(amount, Double.class))));
    }

    @RequestMapping(value = "web/sum_count/global", method = RequestMethod.POST)
    @ResponseBody
    public String agg_global() {
        return returnString("<h4>Global statistics</h4>" + makeAggTable(Mono.just(repository.sumCountGlobal())));
    }

    @RequestMapping(value = "web/sum_count/user", method = RequestMethod.POST)
    @ResponseBody
    public String agg_user() {
        return returnString("<h4>Statistics by User</h4>" + makeAggTable(repository.sumCountByUser()));
    }

    @RequestMapping(value = "web/sum_count/user_and_trip", method = RequestMethod.POST)
    @ResponseBody
    public String agg_user_trip() {
        return returnString("<h4>Statistics by User and Trip</h4>" + makeAggTable(repository.sumCountByUserAndTrip()));
    }

}
