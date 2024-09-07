Table of Contents
=================

1. [Overview](#overview)
2. [Features](#features)
3. [Pre-requisites](#pre-requisites)
4. [External Dependencies](#external-dependencies)
5. [Running Application](#running-application)
6. [Contributors](#contributors)
7. [Test Coverage](#test-coverage)

# Overview

An innovative online auction platform that redefines the auction experience by prioritizing user control. BidWise provides a seamless and enjoyable bidding experience with real-time insights, analytics, and adaptive scaling.

# Features

1. LandingPage
 * Entry point of the application where user decides to either login or to register and gets to know about us page.

2. Authentication
 * User Registration: Enables new users to create account within the application.
 * User Login: Permits existing users to access the application using valid credentials.
 * Forgot password - Inputs an email for user that has forgot the password
 * Reset Password : Provides a mechanism for users to reset forgotten passwords.
 * Sign out: Allows users to securely end their application session.

3. Post Landing page 
  * User can view upcoming auction 
  * They can view recommended auctions according to his bidding history
  * And also can see auctions according to categories

4. My Account 
  * In this section user can view their tradebook where they can check their lost auction and won auctions
  * Seller portal shows all the items that the user has listed and can add more items from there itself
  * Add funds where user can add funds into their account and use those funds to bid

5. Upgrade to premium
  * User can upgrade their subscription and perks of doing that is user gets extra time to bid than the regular user.

6. Live Auction
 * Users can start bidding on auctions that are live
  * If the user enters the amount that's less than the current bid it shows error message
  * If the user tries to enter the bid which exceeds the amount in his funds error message will be displayed 
  * If the user wins the auction that will be showed in their trade book
  * If the user looses bid still it will be showed in their trade-book

# Pre-requisites

For build and running the application locally the project requires:
- Java [17.0.0](https://www.oracle.com/java/technologies/downloads/#java17)
- Apache Maven [3.9.7](https://dlcdn.apache.org/maven/maven-3/3.9.7/binaries/apache-maven-3.9.7-bin.zip)
- MySQL [10.5.23](https://mariadb.com/kb/en/mariadb-10-5-23-release-notes/)
- Node.js [20.16.0](https://nodejs.org/dist/v20.16.0/node-v20.16.0.pkg)
- npm [10.3.0](https://www.npmjs.com/package/npm/v/10.3.0)
- Angular [18.0.4](https://www.npmjs.com/package/@angular/cli/v/18.0.4)

# External Dependencies 

| Dependency Name               | Version | Description                                                                                     |
|-------------------------------|---------|-------------------------------------------------------------------------------------------------|
| spring-boot-starter-data-jpa  | 3.2.5   | Starter for using Spring Data JPA with Hibernate                                                |
| spring-boot-starter-security  | 3.2.5   | Starter for using Spring Security                                                               |
| spring-boot-starter-web       | 3.2.5   | Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container. |
| mysql-connector-j             | 8.0.33  | MySQL Connector/J is a JDBC Type 4 driver that uses pure Java to access MySQL databases         |
| lombok                        | 1.18.34 | Lombok is a Java library that helps to reduce boilerplate code by using annotations              |
| spring-boot-starter-test      | 3.2.5   | Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest, and Mockito |
| spring-security-test          | 3.2.5   | Security related test utilities                                                                 |
| jjwt-api                      | 0.12.5  | JWT (JSON Web Token) API                                                                        |
| jjwt-impl                     | 0.12.5  | JWT (JSON Web Token) implementation for Java                                                    |
| jjwt-jackson                  | 0.12.5  | JWT (JSON Web Token) implementation for Java                                                    |
| spring-boot-starter-mail      | 3.2.5   | Starter for using Spring Framework's MailSender, which is used for sending email                |
| junit                         | 4.13.2  | A simple framework to write repeatable tests. It is an instance of the xUnit architecture for unit testing frameworks. |
| cloudinary-http44             | 1.29.0  | Cloudinary API for Java HTTP client                                                             |
| spring-boot-starter-websocket | 3.2.5   | Starter for building WebSocket applications using Spring Framework                              |
| ---UI                         |
| @stomp/stompjs                | ^7.0.0    | STOMP messaging protocol for JavaScript                                                          |
| @types/sockjs-client          | ^1.5.4    | Type definitions for sockjs-client                                                               |
| html2canvas                   | ^1.4.1    | Renders HTML elements to canvas                                                                  |
| jspdf                         | ^2.5.1    | PDF creation library                                                                             |
| chart.js                      | 3.7.1     | JavaScript charting library                                                                      |
| ng2-charts                    | 4.1.0     | Angular directives for Chart.js                                                                  |
| ngx-toastr                    | ^19.0.0   | Angular Toastr notifications                                                                     |
| rxjs                          | ~7.8.0    | Reactive Extensions for JavaScript                                                               |
| sockjs-client                 | ^1.6.1    | SockJS client                                                                                     |
| sweetalert2                   | ^11.12.1  | Beautiful, responsive, customizable, and accessible (WAI-ARIA) replacement for JavaScript's popup boxes |                                    |


# Running Application
## Remotely
Prerequisite: Connect to dal wifi or use dal vpn. <br>
**URL**: http://csci5308-vm6.research.cs.dal.ca 

## Locally
- Clone Repository to your local machine
```bash
    git clone https://git.cs.dal.ca/courses/2024-summer/csci-5308/group06
```
- Create a Database called “Bidwise” using MySQL workbench
```sql
    create database bidwise;
    use bidwise;
```
- Edit the following variables in backend/src/main/resources/application.yml
```yaml
    stages:
  - build
  - test
  - publish
  - deploy

variables:
  MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/Auction_backend/.m2/repository"

cache:
  paths:
    - $CI_PROJECT_DIR/Auction_backend/.m2/repository

build_frontend:
  stage: build
  image: node:latest
  before_script:
    - cd Auction_frontend/
  script:
    - echo "build frontEnd"
    - npm install --legacy-peer-deps
    - npm install -g @angular/cli
    - ng build
  artifacts:
    paths:
      - Auction_frontend/dist/auction-frontend/
  tags:
    - dalfcs_docker_autoscale 
    
build_backend:
  stage: build
  image: maven:3.9.7-sapmachine-17
  before_script:
    - cd Auction_backend/
  script:
    - echo "Build backend"
    - mvn clean install -DskipTests
  artifacts:
    paths:
      - Auction_backend/target/
  tags:
    - dalfcs_docker_autoscale 

test_backend:
  stage: test
  image: maven:3.9.7-sapmachine-17
  before_script:
    - cd Auction_backend/
  script:
    - echo "Loading the cache directory"
    - echo $CI_PROJECT_DIR/Auction_backend/.m2/repository
    - echo "Running Tests"
    - mvn test
  tags:
    - dalfcs_docker_autoscale 

publish_backend:
  stage: publish
  image: docker:latest
  variables:
    DOCKER_HOST: tcp://docker:2375
    DOCKER_TLS_CERTDIR: ""
  services:
    - docker:dind
  before_script:
    - cd Auction_backend/
  script:
    - echo $DOCKER_HOST
    - docker --version
    - echo "Logging Docker"
    - docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
    - echo "Building the Docker image for the Backend"
    - docker build -t docker.io/$DOCKER_USERNAME/auctionbackend:$CI_COMMIT_SHORT_SHA .
    - echo "Successfully build the docker image for the backend"
    - docker push docker.io/$DOCKER_USERNAME/auctionbackend:$CI_COMMIT_SHORT_SHA
  after_script:
    - echo "Docker Image pushed successfully"
    - docker logout
  when: manual
  tags:
    - dalfcs_docker_autoscale 

publish_frontend:
  stage: publish
  image: docker:latest
  variables:
    DOCKER_HOST: tcp://docker:2375
    DOCKER_TLS_CERTDIR: ""
  services:
    - docker:dind
  before_script:
    - cd Auction_frontend/
  script:
    - echo $DOCKER_HOST
    - docker --version
    - echo "Logging Docker"
    - docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
    - echo "Building the Docker image"
    - docker build -t docker.io/$DOCKER_USERNAME/auctionfrontend:$CI_COMMIT_SHORT_SHA .
    - echo "Successfully build the docker image"
    - docker push docker.io/$DOCKER_USERNAME/auctionfrontend:$CI_COMMIT_SHORT_SHA
  after_script:
    - echo "Docker Image pushed successfully"
    - docker logout
  when: manual
  tags:
    - dalfcs_docker_autoscale

deploy_build:
  image: alpine:latest
  variables:
    DOCKER_HOST: tcp://docker:2375
    DOCKER_TLS_CERTDIR: ""
  stage: deploy
  before_script:
    - 'command -v ssh-agent >/dev/null || ( apk add --update openssh )' 
    - eval $(ssh-agent -s)
    - echo "$SSH_PRIVATE_KEY" | tr -d '\r' | ssh-add -
    - mkdir -p ~/.ssh
    - chmod 700 ~/.ssh
    - ssh-keyscan $VM_IPADDRESS >> ~/.ssh/known_hosts
    - chmod 644 ~/.ssh/known_hosts 
  script:  
    - ssh $SSH_USER@$VM_IPADDRESS "hostname && echo 'Welcome!!!' > welcome.txt"
    - ssh $SSH_USER@$VM_IPADDRESS "docker container rm -f auctionfrontend || true"
    - ssh $SSH_USER@$VM_IPADDRESS "docker container rm -f auctionbackend || true"
    - ssh $SSH_USER@$VM_IPADDRESS "docker run -d -p80:4200 --name auctionfrontend  $DOCKER_USERNAME/auctionfrontend:$CI_COMMIT_SHORT_SHA"
    - ssh $SSH_USER@$VM_IPADDRESS "docker run -d -p8080:8080 --name auctionbackend $DOCKER_USERNAME/auctionbackend:$CI_COMMIT_SHORT_SHA"
  when: manual
  tags:
    - dalfcs_docker_autoscale 
```
- Edit the following variable in frontend/src/environments
```javascript
export const environment  ----changes
//Replace the  hostname by localhost 
// This 'http://172.17.3.242:8080/api/v1'by this http://localhost:8080/api/v1 
```
- Open your terminal and navigate to the project's backend folder
```bash
    cd backend/
```
- Make sure you have the right version of Maven installed by typing
```bash
    mvn -v
```
- You can install it by running 
```bash
    mvn clean install
```
- Use Maven to build the project by running 
```bash
    mvn clean package
```
- Run the backend application using Maven with
```bash
    mvn spring:boot run
```

- Switch to the project's frontend folder by typing 
```bash
    cd /frontend
```
- Verify you have the correct versions of Node.js and npm installed by running respectively
```bash
    node -v
    npm -v
```
- Install any necessary frontend libraries using 
```bash
    npm install
```
-  Launch the Angular application using
```bash
    npm start
```

# Contributors
- [Shrey Patel](https://git.cs.dal.ca/shrey) (B00960433)
- [Harshil Makwana](https://git.cs.dal.ca/hmakwana) (B00985236)
- [Shrey Monka](https://git.cs.dal.ca/monka) (B00971102) 
- [Shifa Mirza](https://git.cs.dal.ca/mirza) (B00981843)

# Test Coverage
- [IntelliJ Code Coverage Screenshot](/assets/AuctionTestCoverage.png)
