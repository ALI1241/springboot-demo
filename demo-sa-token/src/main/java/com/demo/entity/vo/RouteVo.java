package com.demo.entity.vo;

import com.demo.entity.po.Route;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>RouteVo</h1>
 *
 * <p>
 * createDate 2021/11/26 10:14:02
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
@Setter
public class RouteVo extends Route {

    /**
     * 所有子节点
     */
    private List<RouteVo> children;

    /**
     * 是可以匹配的路径
     */
    private List<RouteVo> matcher;

    /**
     * 是不可匹配的路径
     */
    private List<RouteVo> direct;

    /**
     * 删除该节点时，是否删除子节点，不删除将移动到该节点的父节点
     */
    private Boolean deleteChildren;

    /**
     * 移动到哪个id下方
     */
    private Long moveId;

    public RouteVo() {

    }

    public RouteVo(Long id, String path, Integer seq, Long parentId) {
        setId(id);
        setPath(path);
        setSeq(seq);
        setParentId(parentId);
    }

}