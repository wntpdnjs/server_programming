# 팡팡 커뮤니티

간단한 게시판 기능을 제공하는 웹 애플리케이션입니다.

## 기능

- 회원가입/로그인
- 게시글 작성, 조회, 수정, 삭제
- 이미지 첨부 기능
- 반응형 웹 디자인

## 기술 스택

- Java 8+
- JSP/Servlet
- Oracle Database
- Bootstrap 4.5.2
- HTML5/CSS3/JavaScript

## 개발 환경 설정

### 1. 필수 요구사항

- JDK 8 이상
- Apache Tomcat 8.5 이상
- Oracle Database 11g 이상
- Eclipse IDE (또는 다른 Java IDE)

### 2. 데이터베이스 설정

1. Oracle Database에 접속하여 다음 명령을 실행:

```sql
-- 사용자 생성
CREATE USER pangpang IDENTIFIED BY 1234;
GRANT CONNECT, RESOURCE TO pangpang;
GRANT CREATE SESSION TO pangpang;
GRANT CREATE TABLE TO pangpang;
GRANT CREATE SEQUENCE TO pangpang;

-- 테이블 생성
CREATE TABLE users (
    user_id NUMBER PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    password VARCHAR2(100) NOT NULL,
    name VARCHAR2(50) NOT NULL,
    role VARCHAR2(20) DEFAULT 'USER' NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE posts (
    post_id NUMBER PRIMARY KEY,
    user_id NUMBER REFERENCES users(user_id),
    title VARCHAR2(200) NOT NULL,
    content CLOB,
    image_path VARCHAR2(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 시퀀스 생성
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE post_seq START WITH 1 INCREMENT BY 1;
```

### 3. 프로젝트 설정

1. 프로젝트를 클론합니다:
```bash
git clone [repository-url]
```

2. Eclipse에서 프로젝트를 임포트합니다:
   - File > Import > Existing Projects into Workspace
   - 클론한 프로젝트 디렉토리 선택

3. 프로젝트 빌드 패스에 필요한 라이브러리 추가:
   - ojdbc6.jar (Oracle JDBC 드라이버)
   - servlet-api.jar
   - jstl-1.2.jar
   - javax.servlet.jsp.jstl-api-1.2.1.jar

4. `src/main/java/com/pangpang/util/DatabaseUtil.java` 파일에서 데이터베이스 연결 정보 수정:
```java
private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
private static final String USER = "pangpang";
private static final String PASSWORD = "1234";
```

### 4. 실행 방법

1. Apache Tomcat 서버 설정
2. 프로젝트를 서버에 추가
3. 서버 시작
4. 웹 브라우저에서 다음 주소로 접속:
```
http://localhost:8081/pangpang-community/
```

## 디렉토리 구조

```
src/main/
├── java/
│   └── com/
│       └── pangpang/
│           ├── controller/    # 서블릿 클래스
│           ├── dao/          # 데이터 액세스 객체
│           ├── model/        # 모델 클래스
│           ├── service/      # 서비스 클래스
│           └── util/         # 유틸리티 클래스
├── webapp/
│   ├── WEB-INF/
│   │   └── web.xml          # 웹 애플리케이션 설정
│   ├── uploads/             # 업로드된 이미지 저장 디렉토리
│   └── *.jsp                # JSP 페이지들
```

## 주의사항

- `uploads` 디렉토리의 쓰기 권한 확인
- 데이터베이스 연결 정보 확인
- 서버 포트 번호 확인 (기본값: 8081)
