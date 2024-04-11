FROM openjdk:21-oraclelinux8
WORKDIR /home/shop
ENV JWT_SECRET=segredoaqui
EXPOSE 8080
COPY . .
ENTRYPOINT ["java", "-jar", "shop-1.0.2.jar"]
