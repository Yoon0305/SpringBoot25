# SpringBoot25
스프링부트 학습용


======================== application.properties ========================

spring.datasource.driver-class-name=org.mariadb.jdbc.Driver

spring.datasource.url=jdbc:mariadb://localhost:3306/?????

spring.datasource.username=?????

spring.datasource.password=?????

spring.jpa.hibernate.ddl-auto=update

spring.jpa.properties.hibernate.format_sql=true

spring.jpa.show-sql=true

======================== build.gradle ========================


    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'      /* 데이터 베이스 관련 외부 라이브러리*/

    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'     /* 프론트 관련 외부 라이브러리*/
 
    implementation 'org.springframework.boot:spring-boot-starter-web'           /* string-web*/
    
    compileOnly 'org.projectlombok:lombok'                                      /* lombok*/
    
    annotationProcessor 'org.projectlombok:lombok'                              /* lombok*/
    
    testCompileOnly 'org.projectlombok:lombok'                                  /* lombok*/
    
    testAnnotationProcessor 'org.projectlombok:lombok'                          /* lombok*/
    
    developmentOnly 'org.springframework.boot:spring-boot-devtools'             /* boot dev-tool*/


    /* 1단계, 2단계 설정 -> src/main/resources/application.properties 에서 설정   */

   
    runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'                          /* maria db driver*/

    testImplementation 'org.springframework.boot:spring-boot-starter-test'      /* junit, method test*/

    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'   

