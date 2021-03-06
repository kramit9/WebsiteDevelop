package com.example;

import com.example.dao.LoginTicketDAO;
import com.example.dao.UserDAO;
import com.example.model.LoginTicket;
import com.example.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class DatabaseTests {
    @Autowired
    UserDAO userDao;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Test
    public void contextLoads() {
        Random random = new Random();
        for(int i = 0 ; i<11; i++){
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png ",random.nextInt(1000)));
            user.setName(String.format("bonbon%d",i));
            user.setPassword("");
            user.setSalt("");
            userDao.addUser(user);

            user.setPassword("133002008");
            userDao.updateById(user);
            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setExpired(new Date());
            ticket.setTicket(String.format("TICKET%d",i));
            ticket.setUserId(i+1);
            loginTicketDAO.addTicket(ticket);

            loginTicketDAO.updateStatus(2,ticket.getTicket());
        }

        Assert.assertEquals("133002008",userDao.selectById(3).getPassword());
        userDao.delectById(1);
        Assert.assertNull(  userDao.selectById(1));
        Assert.assertEquals(2,loginTicketDAO.selectByTicket("TICKET1").getUserId() );
    }

}
