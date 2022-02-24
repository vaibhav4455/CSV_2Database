package com.moglix.csv.CSV2Database.config;

import com.moglix.csv.CSV2Database.model.User;
import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User,User> {
    @Override
    public User process(User user) throws Exception {


        return user;
    }
}
