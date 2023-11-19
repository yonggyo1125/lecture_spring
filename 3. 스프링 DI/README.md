# 스프링 DI(Dependency Injection - 의존주입)

## 의존이란?

> <code>DI</code>는 <code>Dependency Injection</code>의 약자로 우리말로는 의존 주입이라고 번역한다. 이 단어의 의미를 이해하려면 먼저 의존(dependency)이 뭔지 알아야 하는데, 의존은 객체 간의 의존을 의미한다. 이해를 위해 회원 가입을 처리하는 기능을 구현한 다음 코드를 보자

- <code>MemberRegisterService</code> 클래스가 DB 처리를 위해 <code>MemberDao</code> 클래스의 메서드를 사용한다는 점이다. 회원 데이터가 존재하는지 확인하기 위해 MemberDao 객체의 selectByEmail() 메서드를 실행하고, 회원 데이터를 DB에 삽입하기 위해 insert() 메서드를 실행한다.
이렇게 한 <b>클래스가 다른 클래스의 메서드를 실행 할 때 이를 의존한다고 표현</b>한다. 앞서 코드에서 <b>MemberRegisterService 클래스가 MemberDao 클래스에 의존한다고 표현</b>할 수 있다.

> 의존은 변경에 의해 영향을 받는 관계를 의미한다. 예를 들어 MemberDao의 insert() 메서드의 이름을 insertMember()로 변경하면 이 메서드를 사용하는 MemberRegisterService 클래스의 소스 코드도 함께 변경된다. 이렇게 변경에 따른 영향이 전파되는 관계를 <code>의존</code> 한다고 표현한다.

