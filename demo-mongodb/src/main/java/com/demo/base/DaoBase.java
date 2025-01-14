package com.demo.base;

import com.alibaba.fastjson2.util.DateUtils;
import com.demo.entity.pojo.ParamsQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * <h1>Dao层基类</h1>
 *
 * <p>
 * createDate 2022/01/04 17:17:26
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class DaoBase {

    /**
     * 自定义查询分页排序
     *
     * @param mongoTemplate mongoTemplate
     * @param entityClass   类
     * @param criteria      查询
     * @param pageable      分页器
     * @return Page
     */
    public <T> Page<T> find(MongoTemplate mongoTemplate, Class<T> entityClass, Criteria criteria, Pageable pageable) {
        Query query = Query.query(criteria);
        long count = mongoTemplate.count(query, entityClass);
        return new PageImpl<>( //
                mongoTemplate.find(query.with(pageable), entityClass), // 查询
                pageable, // 分页
                count // 总数
        );
    }

    /**
     * 构建查询参数
     *
     * @param criteria        Criteria
     * @param paramsQueryList List&lt;ParamsQuery>
     */
    public static void buildParamsQuery(Criteria criteria, List<ParamsQuery> paramsQueryList) {
        if (paramsQueryList == null || paramsQueryList.isEmpty()) {
            return;
        }
        for (ParamsQuery query : paramsQueryList) {
            buildCriteria(criteria, query.getOperator(), query.getField(), //
                    conversionType(query.getType(), query.getValue(), query.getValue2()));
        }
    }

    /**
     * 转换类型
     *
     * @param type  类型
     * @param value 值
     * @return 转换后的值对象数组
     */
    private static Object[] conversionType(String type, String value, String value2) {
        Object[] objects = new Object[2];
        if (value == null) {
            return null;
        }
        // switch不能为null
        if (type == null) {
            type = "string";
        }
        switch (type) {
            case "int": {
                objects[0] = Integer.parseInt(value);
                if (value2 != null) {
                    objects[1] = Integer.parseInt(value2);
                }
                break;
            }
            case "long": {
                objects[0] = Long.parseLong(value);
                if (value2 != null) {
                    objects[1] = Long.parseLong(value2);
                }
                break;
            }
            case "boolean": {
                objects[0] = Boolean.parseBoolean(value);
                if (value2 != null) {
                    objects[1] = Boolean.parseBoolean(value2);
                }
                break;
            }
            case "timestamp": {
                objects[0] = new Timestamp(DateUtils.parseMillis(value));
                if (value2 != null) {
                    objects[1] = new Timestamp(DateUtils.parseMillis(value2));
                }
                break;
            }
            case "string":
            default: {
                objects[0] = value;
                if (value2 != null) {
                    objects[1] = value2;
                }
            }
        }
        return objects;
    }

    /**
     * 构建查询
     *
     * @param criteria Criteria
     * @param operator 操作符
     * @param field    字段
     * @param value    值对象数组
     */
    private static void buildCriteria(Criteria criteria, String operator, String field, Object[] value) {
        if (field == null || value == null) {
            return;
        }
        // switch不能为null
        if (operator == null) {
            operator = "eq";
        }
        switch (operator) {
            // 不等
            case "neq": {
                criteria.and(field).ne(value[0]);
                return;
            }
            // 小于
            case "lt": {
                criteria.and(field).lte(value[0]);
                return;
            }
            // 大于
            case "gt": {
                criteria.and(field).gte(value[0]);
                return;
            }
            // 在...与...之间
            case "bt": {
                criteria.and(field).gte(value[0]).lte(value[1]);
                return;
            }
            // 不在...与...之间
            case "nbt": {
                criteria.orOperator(Criteria.where(field).lte(value[0]), Criteria.where(field).gte(value[1]));
                return;
            }
            // 等于
            case "eq":
            default: {
                criteria.and(field).is(value[0]);
            }
        }
    }

    /**
     * 构建范围查询
     *
     * @param criteria Criteria
     * @param field    字段名
     * @param start    开始
     * @param end      结束
     * @param not      否定
     */
    public static <T> void buildRange(Criteria criteria, String field, T start, T end, Boolean not) {
        /* 不查询，SE同时为null */
        if (start == null && end == null) {
            return;
        }
        /* SE不同时为null。SE相等时，根据N是否为null或false决定==和!= */
        if (Objects.equals(start, end)) {
            if (not == null || !not) {
                // ___(S=E)___ 等于
                criteria.and(field).is(start);
            } else {
                // _xxx(S=E)xxx_(N) 不等
                criteria.and(field).ne(start);
            }
            return;
        }
        /* SE有一个为null，N无效。根据SE是否为null<和> */
        if (start == null) {
            // _xxx(E)___ 小于
            criteria.and(field).lte(end);
            return;
        }
        if (end == null) {
            // ___(S)xxx_ 大于
            criteria.and(field).gte(start);
            return;
        }
        /* SE既不同时为null也不相等。根据N是否为null或false决定between和not between */
        if (not == null || !not) {
            // ___(S)xxx(E)___ 在...与...之间
            criteria.and(field).gte(start).lte(end);
        } else {
            // _xxx(S)___(E)xxx_(N) 不在...与...之间
            criteria.orOperator(Criteria.where(field).lte(start), Criteria.where(field).gte(end));
        }
    }

}
