# 스프링 프레임워크란

> 스프링(Spring)은 매우 방대한 기능을 제공하고 있어서 스프링을 한마디로 정의하기는 힘들다. 흔히 스프링이라고 하면 스프링 프레임워크를 말하는데, 스프링 프레임워크의 주요 특징은 다음과 같다.

1. 의존 주입(Dependency Inject : DI) 지원
2. AOP(Aspect-Oriented Programming) 지원
3. MVC 웹 프레임워크 제공
4. JDBC, JPA 연동, 선언적 트랜잭션 처리 등 DB 연동 지원

> 이 외에도 스케줄링, 메시지 연동(JMS), 이메일 발송, 테스트 지원 등 자바 기반 어플리케이션을 개발하는데 필요한 다양한 기능을 제공한다.
> 실제로 스프링 프레임워크를 이용해서 웹 어플리케이션을 개발할 때에는 스프링 프레임워크만 단독으로 사용하기 보다는 여러 스프링 관련 프로젝트를 함께 사용한다. 현재 스프링을 주도적으로 개발하고 있는 피보탈(Pivotal)은 스프링 프레임워크뿐만 아니라 어플리케이션 개발에 필요한 다양한 프로젝트를 진행하고 있다. 이들 프로젝트 중 자주 사용되는 것은 다음과 같다.

1. <b>스프링 데이터</b> : 적은 양의 코드로 데이터 연동을 처리할 수 있도록 도와주는 프레임워크이다. JPA, 몽고DB, 레디스 등 다양한 저장소 기술을 지원한다.
2. <b>스프링 시큐리티</b> : 인증/인가와 관련된 프레임워크로서 웹 접근 제어, 객체 접근 제어, DB - 오픈 ID - LDAP 등 다양한 인증 방식, 암호화 기능을 제공한다.
3. <b>스프링 배치</b> : 로깅/추적, 작업 통계, 실패 처리 등 배치 처리에 필요한 기본 기능을 제공한다.

> 이 외에도 스프링 인티그레이션, 스프링 하둡, 스프링 소셜 등 다양한 프로젝트가 있습니다. 
> 각 프로젝트에 대한 내용은 https://spring.io 사이트를 참고

# 개발환경 구축하기

1. JDK 설치 및 JAVA_HOME 환경 변수 설정

> 인텔리제이, 이클립스 사용시 내장된 openjdk를 사용한다면 별도 JDK를 설치할 필요는 없다.
> IDE를 통한 설치가 아닌 maven 또는 gradle 명령어를 통한 직접 프로젝트 생성시에는 JDK 설치 및 JAVA_HOME 환경 변수 설정 필요

- [JDK 다운로드](https://www.oracle.com/kr/java/technologies/downloads/)

> 실습에서 진행하게될 스프링 버전은 6버전을 사용합니다. 6버전의 JDK 컴파일 버전은 17이므로 최소한 17버전 이상을 설치하여야 정상적인 실습이 가능합니다. 

2. 인텔리제이 설치
    - 인텔리제이는 Ultimate 버전과 Community 버전 2가지로 다운받을 수 있다. 무료 버전은 Community 이므로 Community 버전으로 다운받아 설치한다.
    - [IDE 다운로드](https://www.jetbrains.com/ko-kr/idea/download)
   
![image1](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image1.png)

3. [스프링 프레임워크 API 문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/) 즐겨찾기 등록 

![image2](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image2.png)

# 메이븐 및 그레이들 사용방법 알아보기

- [학습 URL](https://github.com/yonggyo1125/lecture_spring/tree/master/2.%20%EB%A9%94%EC%9D%B4%EB%B8%90(Maven)%2C%20%EA%B7%B8%EB%A0%88%EC%9D%B4%EB%93%A4(Gradle)%20%EC%82%AC%EC%9A%A9%EB%B2%95)


# 스프링 프로젝트 생성하기

# 그레이들 프로젝트 생성 

> 그레이들 프로젝트는 명령프롬프트에서 <code>gradle init --type java-application</code>을 통해서도 생성한 후 인텔리제이에서 open 하여 편집할 수도 있고 또는 인텔리제이에 탑재되어 있는 자체 gradle 명령어를 통해서도 생성할 수 있습니다. 여기에서는 인텔리제이 IDE에 탑재된 기능을 통해서 생성합니다

<code>New Project</code> 클릭

![image3](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image3.png)

1. Name: 프로젝트명을 입력합니다. 프로젝트 명은 location 디렉토리의 하위 디렉토리로 생성이 됩니다. 
2. Language: <code>Java</code>를 선택 합니다.
3. Build system: <code>Gradle</code>를 선택합니다.
4. JDK: 스프링 6버전 부터는 기본 17버전으로 컴파일 되어 배포되므로 17버전 이상을 선택합니다. 그러나 현재 최신 버전인 Gradle 8.4버전에서는 17버전 까지만 지원하므로 17로 설정
5. Gradle DSL: <code>Groovy</code>로 선택
6. Advanced Settings:
   - <b>GroupId</b> : org.project
   - <b>ArtifactId</b> : sample
7. <code>Create</code> 버튼을 클릭하여 프로젝트를 생성합니다.


프로젝트가 정상적으로 생성되면 다음과 같은 구조가 됩니다.

![image4](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image4.png)


File -> Project Stucture 메뉴를 선택 한 후 

![image5](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image5.png)

SDK 버전이 다음과 같이 17로 설정되어 있는지 확인합니다.

![image6](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image6.png)


한글 깨짐을 방지하기 위해서 File -> Settings -> Editor -> File Encodings으로 이동한 후

![image7](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image7.png)

1. Project Encoding -> UTF-8,
2. Default encoding for properties files -> UTF-8
3. Path에 src 디렉토리 를 선택
4. Apply 클릭


Build, Execution, Deploymemnt -> Build Tools -> Gradle 설정

![image8](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image8.png)

1. Build and run using -> IntelliJ IDEA
2. Run tests using -> IntelliJ IDEA
3. Gradle JVM -> JDK 17로 설정되어 있는지 확인
4. Apply 클릭 



![image9](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image9.png)

![image10](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image10.png)

![image12](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image12.png)

![image13](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image13.png)

![image14](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image14.png)

![image15](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image15.png)


