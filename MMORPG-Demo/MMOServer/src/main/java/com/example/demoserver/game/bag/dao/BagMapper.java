package com.example.demoserver.game.bag.dao;

import com.example.demoserver.game.bag.model.Bag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Mapper
@Repository
public interface BagMapper {


    public List<Bag> selectBagByPlayerId(Integer playerId);

    public int insertBag(Bag bag);

    public int updateByPrimaryKeySelective(Bag bag);


}