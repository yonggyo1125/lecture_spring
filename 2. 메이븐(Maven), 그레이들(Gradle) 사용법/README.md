# 메이븐(Maven) 사용법 

## 메이븐 설치

- http://maven.apache.org/ 사이트에 방문한 뒤 [Download] 메뉴를 클릭하여 메이븐 최신 버전을 다운로드 한다. 메이븐을 다운로드 한 후 원하는 폴더에 압축을 풀면 설치가 끝난다.
- 압축을 풀면 [메이븐 설치폴더]\bin 폴더에 mvn.bat 파일이 존재할 것이다.
- 명령 프롬프트에서 메이븐을 실행 할 수 있도록 PATH 환경변수를 설정한다. <br>예) C:\devtool\apache-maven-3.8.4\bin

![image1](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/2.%20%EB%A9%94%EC%9D%B4%EB%B8%90(Maven)%2C%20%EA%B7%B8%EB%A0%88%EC%9D%B4%EB%93%A4(Gradle)%20%EC%82%AC%EC%9A%A9%EB%B2%95/images/image1.png)


![image2](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/2.%20%EB%A9%94%EC%9D%B4%EB%B8%90(Maven)%2C%20%EA%B7%B8%EB%A0%88%EC%9D%B4%EB%93%A4(Gradle)%20%EC%82%AC%EC%9A%A9%EB%B2%95/images/image2.png)

![image3](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/2.%20%EB%A9%94%EC%9D%B4%EB%B8%90(Maven)%2C%20%EA%B7%B8%EB%A0%88%EC%9D%B4%EB%93%A4(Gradle)%20%EC%82%AC%EC%9A%A9%EB%B2%95/images/image3.png)

- 명령 프롬프트([시작]->[모든 프로그램]->[보조 프로그램]->[명령 프롬프트])를 실행한 뒤 다음과 같이 mvn 명령어를 입력한다.(시작 메뉴의 검색창에 cmd라고 입력하여 명령 프롬프트를 실행해도 된다.)

```
mvn -version
```

![image4](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/2.%20%EB%A9%94%EC%9D%B4%EB%B8%90(Maven)%2C%20%EA%B7%B8%EB%A0%88%EC%9D%B4%EB%93%A4(Gradle)%20%EC%82%AC%EC%9A%A9%EB%B2%95/images/image4.png)

![image5](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/2.%20%EB%A9%94%EC%9D%B4%EB%B8%90(Maven)%2C%20%EA%B7%B8%EB%A0%88%EC%9D%B4%EB%93%A4(Gradle)%20%EC%82%AC%EC%9A%A9%EB%B2%95/images/image5.png)

- 위 과정에서 실제로 입력하는 값은 다음과 같다.
  - <b>groupId</b> : 프로젝트가 속하는 그룹 식별자. 회사, 본부, 또는 단체를 의미하는 값을 입력한다. 패키지 형식으로 계층을 표현한다. 위에서는 kr.codefty를 groupId로 입력했다.
  - <b>artifactId</b> : 프로젝트 결과물의 식별자. 프로젝트나 모듈을 의미하는 값이 온다. 위에서는 sample을 artifactId로 입력했다.
  - <b>version</b> : 결과물의 버전을 입력한다. 위에서는 기본 값인 1.0-SNAPSHOT을 사용했다.
  - <b>package</b> : 생성할 패키지를 입력한다. 별도로 입력하지 않을 경우 groupId와 동일한 구조의 패키지를 생성한다.

## 메이븐 프로젝트의 기본 디렉토리 구조

- archetype:generate이 성공적으로 실행되면 artifactId에 입력한 값과 동일한 이름의 폴더가 생성된다. 위 경우는 sample이라는 하위 폴더가 생성된다. 생성되는 폴더는 다음과 같다

```
sample
    src
        main
            java
                kr
                    codefty
                        App.java
            test
                java
                    kr
                        codefty
                            AppTest.java
pom.xml
```

## 메이븐 프로젝트의 주요 디렉토리 구조 

- <b>src/main/java</b> : 자바 소스 파일이 위치한다.
- <b>src/main/resources</b> : 프로퍼티나 XML 등 리소스 파일이 위치한다. 클래스패스에 포함된다.
- <b>src/main/webapp</b> : 웹 어플리케이션 관련 파일이 위치한다. (WEB-INF 폴더, JSP 파일 등)
- <b>src/test/java</b> : 테스트 자바 소스 파일이 위치한다.
- <b>src/test/resources</b> : 테스트 과정에서 사용되는 리소스 파일이 위치한다. 테스트에 사용되는 클래스패스에 포함된다.

> 자동 생성되지 않은 디렉토리는 직접 생성하면 된다. 예를 들어 src/main 디렉토리에 resources 디렉토리를 생성하면 메이븐은 리소스 디렉토리로 인식한다.

## 자바 버전 수정

> pom.xml 파일을 열어서 maven.compiler.source요소와 maven.compiler.target 요소의 값을 21로 변경하여 자바 버전을 21로 설정한다.

```xml
<properties>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  <maven.compiler.source>21</maven.compiler.source>
  <maven.compiler.target>21</maven.compiler.target>
</properties>
```

## 컴파일 하기

```
mvn compile
```

> 컴파일 된 결과는 target/classes 폴더에 생성된다.

## 테스트 실행

```
mvn test
```

> mvn test 명령어를 실행하면 테스트 코드를 컴파일하고 실행한 뒤 테스트 성공 여부를 출력한다. 컴파일된 테스트 클래스들은 target/test-classes 폴더에 생성되고 테스트 결과 리포트는 target-reports 폴더에 저장된다.

## 배포가능한 파일 만들기(패키징)

다음 명령어를 실행하면 프로젝트를 패키징해서 결과물을 생성한다.

``
mvn package
``

> mvn package가 성공적으로 실행되면 target폴더에 프로젝트 이름과 버전에 따라 알맞은 이름을 갖는 jar 파일이 생성된다.

## POM 파일 기본

> 메이븐 프로젝트를 생성하면 pom.xml 파일이 프로젝트 루트 폴더에 생성된다. 이 pom.xml 파일은 Project Object Model 정보를 담고 있는 파일이다. 이 파일에서 다루는 주요 설정 정보는 다음과 같다.

- <b>프로젝트 정보</b> : 프로젝트 이름, 개발자 목록, 라이센스 등의 정보를 기술
- <b>빌드 설정</b> : 소스, 리소스, 라이프 사이클별 실행할 플러그인 등 빌드와 관련된 설정을 기술
- <b>POM 연관 정보</b> : 의존 프로젝트(모듈), 상위 프로젝트, 포함하고 있는 하위 모듈 등을 기술

> archetype:generate 실행시 maven-archetype-quickstart-Archetype을 선택한 경우 생성되는 pom.xml 파일은 다음과 같다.

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>kr.codefty</groupId>
  <artifactId>sample</artifactId>
  <version>1.0-SNAPSHOT</version>

  <name>sample</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.7</maven.compiler.source>
    <maven.compiler.target>1.7</maven.compiler.target>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    ... 
  </build>
</project>
```

위 POM 파일에서 프로젝트 정보를 기술하는 태그는 다음과 같다.

- \<name\> : 프로젝트 이름
- \<url\> : 프로젝트 사이트 URL

POM 연관 정보는 프로젝트간 연관 정보를 기술하며 관련 태그는 다음과 같다.

- \<groupId\> : 프로젝트와 그룹 ID 설정
- \<artifactId\> : 프로젝트와 Artifact ID 설정
- \<version\> : 버전 설정
- \<packaging\> : 패키징 타입 설정, 위 코드는 프로젝트의 결과가 jar 파일로 생성됨을 의미함. jar뿐만 아니라 웹 어플리케이션을 위한 war 타입이 존재

- \<dependencies\> : 이 프로젝트에서 의존하는 다른 프로젝트 정보를 기술
  - \<dependency\> : 의존하는 프로젝트 POM 정보를 기술
  - \<groupId\> : 의존하는 프로젝트의 그룹 ID
  - \<artifactId\> : 의존하는 프로젝트의 artifact ID
  - \<version\> : 의존하는 프로젝트의 버전
  - \<scope\> : 의존하는 범위를 설정


의존성에 정의된 scope

> pom.xml 파일에서 <dependency> 코드를 보면 <scope>를 포함하고 있는 것과 그렇지 않은 것이 존재한다. <scope>는 의존하는 모듈이 언제 사용되는지 설정한다. 

### \<scope\>에 설정하는 값

- compile : 컴파일할 때 필요. 테스트 및 런타임에도 클래스패스에 포함된다. <scope>를 설정하지 않을 경우 기본 값은 compile이다.
- <b>runtime</b> : 런타임에 필요. JDBC 드라이버 등이 예가 된다. 프로젝트 코드를 컴파일 할 때는 필요하지 않지만 실행할 때 필요하다는 것을 의미한다. 배포시 포함된다.
- <b>provided</b> : 컴파일 할 때 필요하지만 실제 런타임 때에는 컨테이너 같은 것에서 기본으로 제공되는 모듈임을 의미한다. 예를 들어 서블릿이나 JSP API 등이 이에 해당한다. 배포시 제외된다.
- <b>test</b> : 테스트 코드를 컴파일 할 때 필요. Mock 테스트를 위한 모듈이 예이다. 테스트 시에 클래스패스에 포함된다. 배포시 제외된다.


# 그레이들(Gradle) 사용법

> 메이븐, 앤트와 그레이들의 가장 큰 차이점 중 하나는 그레이들 빌드 스크립트가 XML 기반이 아니라는 점이다. 그레이들 빌드 스크립트는 그루비 또는 코틀린 DSL로 작성한다. 메이븐과 마찬가지로 그레이들도 컨벤션을 강조하지만 메이븐보다는 유연합니다.

## 그레이들 설치

-  그레이들은 단독형 CLI 툴로 쓸 수 있기 때문에 다양한 개발 환경에서 활용 가능합니다. 이를테면 자바 애플리케이션 빌드 시 되풀이되는 작업(예 : 파일복사, 컴파일)을 자동화하려면 emacs, vi 등의 텍스트 편집기로 그레이들 같은 빌드 툴을 설정할 수 있어야 합니다.
- 그레이들은 무료로 내려받을 수 있고(http://www.gradle.org/downloads) 예제 소스와 바이너리 버전 모두 구할 수 있습니다.
- [https://gradle.org/releases/](https://gradle.org/releases/)
- [Gradle Build Language Reference](https://docs.gradle.org/8.4/dsl/index.html)

![image6](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/2.%20%EB%A9%94%EC%9D%B4%EB%B8%90(Maven)%2C%20%EA%B7%B8%EB%A0%88%EC%9D%B4%EB%93%A4(Gradle)%20%EC%82%AC%EC%9A%A9%EB%B2%95/images/image6.png)


- 자바 툴은 플랫폼에 자유롭기 때문에 부가적인 컴파일 단계를 건너뛰려면 바이너리 버전을 내려 받는 것이 좋습니다.
- 내려 받은 압푹 파일을 풀고 환경 변수 JAVA_HOME, path 값에 추가 지정하면 설치가 끝납니다. 설치하기 전에 메이븐 런타임 구동에 필요한 자바 SDK가 PC에 제대로 설치되어 있는지 확인

![image7](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/2.%20%EB%A9%94%EC%9D%B4%EB%B8%90(Maven)%2C%20%EA%B7%B8%EB%A0%88%EC%9D%B4%EB%93%A4(Gradle)%20%EC%82%AC%EC%9A%A9%EB%B2%95/images/image7.png)


## 그레이들 명령어

### 버전확인 

```
gradle --version
```

![image8](https://raw.githubusercontent.com/yonggyo1125/lecture_spring/master/2.%20%EB%A9%94%EC%9D%B4%EB%B8%90(Maven)%2C%20%EA%B7%B8%EB%A0%88%EC%9D%B4%EB%93%A4(Gradle)%20%EC%82%AC%EC%9A%A9%EB%B2%95/images/image8.png)

### 프로젝트 생성 

```
gradle init [--type 타입명]
```