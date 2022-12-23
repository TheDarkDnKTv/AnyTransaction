package thedarkdnktv.anytransaction.data.graphql.types;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesDto {

    private String datetime;
    private String sales;
    private long points;

    public SalesDto() {}

    public SalesDto(Date datetime, String sales, long points) {
        this.datetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").format(datetime);
        this.sales = sales;
        this.points = points;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }
}
