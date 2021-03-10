package com.xdxiaoran;
import com.xdxiaoran.mapper.AdminMapper;
import com.xdxiaoran.service.WXServerListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.SQLException;

    @SpringBootTest
    class VoteSystemApplicationTests {
        @Autowired
        DataSource dataSource;
        @Autowired
        AdminMapper adminMapper;
//        @Autowired
//        WXServerListener wxServerListener;
        @Test
        void contextLoads() throws SQLException {
//        System.out.println(dataSource.getConnection());
//            System.out.println(adminMapper.getAllAdmin());
//            wxServerListener.sendTextMsg("fileHelper","adasdas");
//            wxServerListener.getContactList();
        }
}
