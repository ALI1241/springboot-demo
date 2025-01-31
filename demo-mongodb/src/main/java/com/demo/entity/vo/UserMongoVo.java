package com.demo.entity.vo;

import com.demo.entity.mongo.UserMongo;
import com.demo.entity.pojo.ParamsQuery;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

/**
 * <h1>UserMongoVo</h1>
 *
 * <p>
 * createDate 2022/06/02 15:32:09
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
@Setter
public class UserMongoVo extends UserMongo {

    /**
     * 时间-结束
     */
    private Timestamp dateEnd;
    /**
     * 时间-否定
     */
    private Boolean dateNot;
    /**
     * 查询参数列表
     */
    private List<ParamsQuery> paramsQueryList;

    public UserMongoVo() {

    }

    public UserMongoVo(UserMongoVo vo) {
        super(vo);
    }

}
