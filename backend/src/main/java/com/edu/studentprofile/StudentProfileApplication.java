package com.edu.studentprofile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 学生用户画像系统 - 启动类
 *
 * 技术栈：Spring Boot + MyBatis Plus + MySQL
 *
 * @author Senior Developer Team
 */
@SpringBootApplication
@MapperScan("com.edu.studentprofile.mapper")
@EnableTransactionManagement
public class StudentProfileApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentProfileApplication.class, args);
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     🎓 学生用户画像系统启动成功！                                ║");
        System.out.println("║     技术架构: Spring Boot + MyBatis Plus + MySQL               ║");
        System.out.println("║     文档地址: http://localhost:8080/api/swagger-ui.html         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
}
