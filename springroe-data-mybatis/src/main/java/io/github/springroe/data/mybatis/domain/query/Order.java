package io.github.springroe.data.mybatis.domain.query;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class Order {

    private String orderBy;

    private boolean isAscending;

    public static Order asc(String orderBy) {
        return new Order(orderBy, true);
    }

    public static Order desc(String orderBy) {
        return new Order(orderBy, false);
    }

    public Order() {}

    public Order(String orderBy, boolean isAscending) {
        this.orderBy = orderBy;
        this.isAscending = isAscending;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(boolean ascending) {
        isAscending = ascending;
    }
}
