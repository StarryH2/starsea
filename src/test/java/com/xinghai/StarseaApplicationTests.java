package com.xinghai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class StarseaApplicationTests {

    @Test
    void contextLoads() {
        String admin = new BCryptPasswordEncoder().encode("admin");
        System.out.println(admin);
        //$2a$10$dXMvUEq8VaezcuWMpMQAxO6xz4gZJeEFIib34ZFKaM6gczoaz8V9G
    }

}
