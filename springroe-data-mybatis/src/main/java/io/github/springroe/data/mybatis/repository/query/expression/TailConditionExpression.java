package io.github.springroe.data.mybatis.repository.query.expression;

import io.github.springroe.data.mybatis.domain.query.Order;
import io.github.springroe.data.mybatis.domain.query.QueryCondition;

import java.util.List;

/**
 * @description:
 * @author: tl
 * @date: 2020/6/12
 */
public class TailConditionExpression extends QueryConditionExpression {

    private List<Order> orders;

    private List<String> groupBy;

    public TailConditionExpression(List<Order> orders, List<String> groupBy) {
        super(ExpressionType.TailExpression);
        this.orders = orders;
        this.groupBy = groupBy;
    }

    @Override
    public String interpret(MybatisQueryContext context) {
        StringBuilder groupBySb = new StringBuilder();
        StringBuilder orderBySb = new StringBuilder();
        QueryCondition queryCondition = context.getQueryCondition();
        if (null != groupBy && groupBy.size() > 0) {
            groupBySb.append(" GROUP BY ");
            for (int i = 0; i < groupBy.size(); i++) {
                String column = getFullColumn(queryCondition, groupBy.get(i));
                groupBySb.append(column);
                if (i < groupBy.size() - 1) {
                    groupBySb.append(", ");
                }
            }
        }
        if (null != orders && orders.size() > 0) {
            orderBySb.append(" ORDER BY ");
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                String orderBy = order.getOrderBy();
                boolean ascending = order.isAscending();
                String column = getFullColumn(context.getQueryCondition(), orderBy);
                String sql = column + " " + (ascending ? "ASC" : "DESC");
                orderBySb.append(sql);
                if (i < orders.size() - 1) {
                    orderBySb.append(", ");
                }
            }
        }
        String groupBySql = groupBySb.toString();
        String orderBySql = orderBySb.toString();
        context.appendGroupByFragment(groupBySql.trim());
        context.appendOrderByFragment(orderBySql.trim());
        return groupBySql + orderBySql;
    }

}
