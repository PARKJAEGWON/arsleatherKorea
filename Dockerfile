# 빌드 스테이지
FROM gradle:jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

# 실행 스테이지
FROM tomcat:10.1.19-jdk21-temurin

# 기본 웹앱 제거 (충돌 방지)
RUN rm -rf /usr/local/tomcat/webapps/*

# WAR 파일 복사
COPY --from=builder /app/build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# 업로드 디렉토리 생성 및 권한 설정
RUN mkdir -p /usr/local/tomcat/webapps/upload \
    && chmod -R 755 /usr/local/tomcat/webapps/upload \
    && chown -R tomcat:tomcat /usr/local/tomcat/webapps/upload

# 톰캣 포트 설정 (server.xml 수정)
RUN sed -i 's/port="8080"/port="8090"/' /usr/local/tomcat/conf/server.xml

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=prod
ENV CATALINA_OPTS="-Dfile.encoding=UTF-8 -Dspring.profiles.active=prod"
ENV TZ=Asia/Seoul

# 볼륨 설정 - 업로드 디렉토리를 영구 저장소로 설정
VOLUME ["/usr/local/tomcat/webapps/upload"]

EXPOSE 8090
CMD ["catalina.sh", "run"]