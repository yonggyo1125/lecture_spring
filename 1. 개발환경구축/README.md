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


### 프로젝트가 정상적으로 생성되면 다음과 같은 구조가 됩니다.

![image4](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image4.png)


### File -> Project Stucture 메뉴를 선택 한 후 

![image5](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image5.png)

### SDK 버전이 다음과 같이 17로 설정되어 있는지 확인합니다.

![image6](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image6.png)

![image7](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image7.png)


### 한글 깨짐을 방지하기 위해서 File -> Settings -> Editor -> File Encodings으로 이동

![image8](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image8.png)

1. Project Encoding -> UTF-8,
2. Default encoding for properties files -> UTF-8
3. Path에 src 디렉토리 를 선택
4. Apply 클릭



### Build, Execution, Deploymemnt -> Build Tools -> Gradle 설정

![image9](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image9.png)

1. Build and run using -> IntelliJ IDEA
2. Run tests using -> IntelliJ IDEA
3. Gradle JVM -> JDK 17로 설정되어 있는지 확인
4. Apply 클릭


### build.gradle 파일

![image10](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image10.png)

1. 자바 버전 및 annotationProcessor 설정 추가

```groovy
plugins {
    id 'java'
}

group 'org.project'
version '1.0-SNAPSHOT'

java {
    sourceCompatibility = '17'
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework:spring-context:6.1.0'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
}

test {
    useJUnitPlatform()
}
```

2. Sync 아이콘 클릭 : 변경 사항 반영 


## Gradle 의존 설정 

![image12](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image12.png)

1. [Maven Repository](https://mvnrepository.com) 에서 <code>Spring Context</code> 키워드로 검색합니다.
2. <code>Spring Context</code>를 클릭 합니다.

![image13](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image13.png)

1. 현 시점의 최신 버전은 <code>6.1</code> 입니다. 
2. <code>6.1.0</code>를 클릭합니다. 

![image14](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image14.png)

1. Gradle (Short) 탭을 클릭합니다.
2. <code>implementation 'org.springframework:spring-context:6.1.0'</code>를 복사합니다.

3. build.gradle 파일의 dependencies 하위 요소로 추가합니다.

```groovy

...

dependencies {
    implementation 'org.springframework:spring-context:6.1.0'
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.1'
}

... 

```
4. Sync 아이콘을 클릭하여 반영합니다.


![image15](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image15.png)

> 반영이 완료되면 <code>External Libraries</code>에 위와 같은 spring context 의존성 및 spring context가 의존하고 있는 하위 의존성까지 함께 반영된 것을 확인 할 수 있습니다. 

## 메이븐 레포지토리

- 메이븐의 pom.xml 또는 Gradle의 build.gradle에 의존 설정을 추가하고 <code>maven compile</code> 또는 <code>gradle build</code>,  인텔리제이에서 Sync 아이콘을 클릭하는 경우 
- 기본적으로 [사용자 홈폴더].m2\repository 로컬 리포지토리로 사용하며 [그룹ID]\[아티팩트ID]\[버전] 폴더에 아티팩트 ID-버전.jar 형식의 이름을 갖는 파일이 생성된다.

```
C:\Users\YONGGYO\.m2\repository\org\springframework\spring-context\6.1.0\spring-context-6.1.0.jar
```

## 스프링 컨테이너 사용해 보기


## 스프링은 객체 컨테이너

- 간단한 스프링 프로그램인 Main을 작성하고 실행해봤다. 이 코드에서 핵심은 <code>AnnotationConfigApplicationContext</code> 클래스이다.
- 스프링의 핵심 기능은 객체를 생성하고 초기화 하는 것이다. 이와 관련된 기능은 <code>ApplicationContext</code>라는 인터페이스에 정의되어 있다.
- <code>AnnotationConfigApplicationContext</code> 클래스는 이 인터페이스를 알맞게 구현한 클래스 중 하나이다. <code>AnnotationConfigApplicationContext</code> 클래스는 자바 클래스에서 정보를 읽어와 객체 생성과 초기화를 수행한다.
- XML 파일이나 그루비 설정 코드를 이용해서 객체 생성/초기화를 수행하는 클래스도 존재한다.

### AnnotationConfigApplicationContext 클래스 계층도 일부

![image16](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/1.%20%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD%EA%B5%AC%EC%B6%95/images/image16.png)

- 계층도를 보면 가장 상위 <code>BeanFactory</code> 인터페이스가 위치하고, 위에서 세 번째에 <code>ApplicationContext</code> 인터페이스, 그리고 가장 하단에 <code>AnnotationConfigApplicationContext</code>등의 구현 클래스가 위치한다. 

- <code>BeanFactory</code> 인터페이스는 객체 생성과 검색에 대한 기능을 정의한다. 예를 들어 생성된 객체를 검색하는데 필요한 <code>getBean()</code> 메서드가 BeanFactory에 정의되어 있다. 객체를 검색하는 것 이외에 싱글톤/프로토타입 빈인지 확인하는 기능도 제공한다.

- <code>ApplicationContext</code> 인터페이스는 메시지, 프로필/환경 변수 등을 처리할 수 있는 기능을 추가로 정의한다.

- 앞서 예제에서 사용한 <code>AnnotationConfigApplicationContext</code>를 비롯해 계층도의 가장 하단에 위치한 세개의 클래스는 <code>BeanFactory</code>와 <code>ApplicationContext</code>에 정의된 기능의 구현을 제공한다. 각 클래스의 차이점은 다음과 같다.

   - <code>AnnotationConfigApplicationContext</code> : 자바 애노테이션을 이용한 클래스로부터 객체 설정 정보를 가져온다.
   - <code>GenericXmlApplicationContext</code> : XML로 부터 객체 정보를 가져온다.
   - <code>GenericGroovyApplicationContext</code> : 그루비 코드를 이용해 설정 정보를 가져온다.
- 어떤 구현 클래스를 사용하단, 각 구현 클래스는 설정 정보로부터 <code>빈(Bean)</code>이라고 불리는 객체를 생성하고 그 객체를 내부에 보관한다. 그리고 <code>getBean()</code> 메서드를 실행하면 해당하는 빈 객체를 제공한다. 예를 들어 앞서 작성한 Main.java 코드를 보면 다윽뫄 같이 설정 정보를 이용해서 빈 객체를 생성하고 해당 빈 객체를 제공하는 것을 알 수 있다.

```java
// 1. 설정 정보를 이용해서 빈 객체를 생성한다.
AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

// 2. 빈 객체를 제공한다.
Greeter g = ctx.getBean("greeter", Greeter.class);
```

- <code>ApplicationContext(또는 BeanFactory)</code>는 빈 객체의 생성, 초기화, 보관, 제거 등을 관리하고 있어 <code>ApplicationContext</code>를 <code>컨터이너(Contatiner)</code>라고도 부른다. 강의에서도 ApplicationContext나 BeanFactory 등을 <code>스프링 컨테이너</code>라고 표현할 것이다.

- 스프링 컨테이너는 내부적으로 빈 객체와 빈 이름을 연결하는 정보를 갖는다. 예를 들어 project.Greeter 타입의 객체를 greeter라는 이름의 빈으로 설정했다고 하면 컨터이너는 greeter 이름과 Greeter 객체를 연결한 정보를 관리한다. 
- 이름과 실제 객체의 관계뿐만 아니라 실제 객체의 생성, 초기화, 의존 주입 등 스프링 컨테이너 객체 관리를 위한 다양한 기능을 제공한다.

## 싱글톤(SingleTon)객체

```java
package project;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main2 {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppContext.class);
		Greeter g1 = ctx.getBean("greeter", Greeter.class);
		Greeter g2 = ctx.getBean("greeter", Greeter.class);
		System.out.println("(g1 == g2) = " + (g1 == g2));
		ctx.close();
	}
}
```

> 실행 결과

```
(g1 == g2) = true
```

> (g1 == g2)의 결과가 true라는 것은 g1과 g2가 같은 객체라는 것을 의미한다. 즉 아래 코드에서 getBean() 메서드는 같은 객체를 반환하는 것이다.

```java
Greeter g1 = ctx.getBean("greeter", Greeter.class);
Greeter g2 = ctx.getBean("greeter", Greeter.class);
```

> 별도 설정을 하지 않을 경우 스프링은 한 개의 빈 객체만을 생성하며, 이 떄 빈 객체는 <code>싱글톤(singleton) 범위를 갖는다</code>고 표현한다. 싱글톤은 단일 객체(single object)를 의미하는 단어로 스프링은 기본적으로 한 개의 @Bean 애노테이션에 대해 한 개의 빈 객체를 생성한다. 따라서 다음과 같은 설정을 사용하면 greeter에 해당하는 객체 한 개와 greeter1에 해당하는 객체 한 개, 이렇게 두 개의 빈 객체가 생성된다.

```java
@Bean
public Greeter greeter() {
	Greeter g = new Greeter();
	g.setFormat("%s, 안녕하세요!");
	return g;
}
```

```java
@Bean
public Greeter greeter1() {
	Greeter g = new Greeter();
	g.setFormat("안녕하세요, %s님!");
	return g;
}
```
