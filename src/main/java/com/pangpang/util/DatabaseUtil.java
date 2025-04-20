package com.pangpang.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    // localhost는 현재 컴퓨터, 1521은 기본 포트, XE는 Oracle의 기본 SID
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    // 앞서 CREATE USER로 생성한 사용자명
    private static final String USER = "pangpang";
    // CREATE USER 명령어에서 설정한 비밀번호
    private static final String PASSWORD = "1234";
    
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void close(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
} 