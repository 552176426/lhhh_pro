package com.itheima.dao;

import com.itheima.domain.Member;
import com.itheima.domain.Orders;
import com.itheima.domain.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OrdersDao {

    @Select("select * from orders")
    @Results({
            @Result(property = "product",column= "productId",javaType = Product.class,one = @One(select = "com.itheima.dao.ProductDao.findById")),
    })
    List<Orders> findAll();

    @Select("select * from orders where id=#{id}")
    @Results({
            @Result(property = "product",column = "productId",javaType = Product.class,
                    one=@One(select = "com.itheima.dao.ProductDao.findById")),
            @Result(property = "member",column = "memberId",javaType = Member.class,
                    one=@One(select = "com.itheima.dao.MemberDao.findById")),
            @Result(property = "travellers",column = "id",javaType = java.util.List.class,
                    many = @Many(select = "com.itheima.dao.TravellerDao.findByOrdersId"))
    })
    Orders findById(String id);
}
