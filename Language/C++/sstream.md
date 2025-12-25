# 문자열 파싱

### **버퍼(buffer)**

데이터가 임시 저장되는 메모리 공간을 말한다.
모든 C++ 스트림은 내부적으로 **stream buffer 객체(std::streambuf)**를 사용한다.

이 버퍼 안에는 두 영역이 있다.
get area (입력 버퍼)
데이터를 입력할 때 사용하는 영역
예: `cin >> a;` → cin의 입력 버퍼(get area)에서 문자를 꺼냄
put area (출력 버퍼)
데이터를 출력할 때 사용하는 영역
예: `cout << "hi";` → cout의 출력 버퍼(put area)에 저장 → flush 시 터미널에 출력

\* flush:
"터미널에 연결된 stdout은 보통 line buffered다."
이 모드에서 출력 버퍼가 flush되는 조건이 3가지다:

1. \n을 만나서 줄바꿈이 발생할 때
2. 새로운 입력을 기다리기 전에 (예: cin >> 호출 직전)
3. 스트림 조작자(std::endl, std::flush)를 통한 명시적 flush 또는 프로그램 종료 시

\* cin.tie(NULL):
cin 실행 시, 기본적으로 cout 버퍼에 있는 값을 먼저 flush하는 것이 디폴트 동작이며, cin.tie(NULL)은 이 자동 flush를 해제하는 것이다.
터미널 환경에서 실행할 때는 터미널의 line buffered 정책 때문에 차이가 잘 드러나지 않지만, 백준 같은 온라인 저지 환경에서는 stdout이 파일/파이프에 연결되어 block buffered 모드로 동작하므로 cin.tie(NULL)을 했는지 여부에 따라 출력 시간 차이가 발생하게 된다.

\* ios::sync_with_stdio(false):
C++ 표준 입출력(cin, cout)은 기본적으로 C 표준 입출력(scanf, printf)과 동기화(sync) 되어 있다.

```cpp
printf("C style\n");
cout << "C++ style\n";
```

즉, 위와 같은 코드가 있을 때,

```bash
C style
C++ style
```

와 같이 출력의 순서를 보장한다.
하지만, ios::sync_with_stdio(false)로 동기화를 끊으면 출력 순서를 보장 받을 수 없게 된다.

입력 버퍼(get area), 출력 버퍼(put area) 라는 말은 논리적 구분(추상적 인터페이스)이다.
실제 구현은 std::stringbuf 내부에서 **하나의 메모리 블록(문자열)**으로 관리된다.

차이는 포인터가 따로 있다는 것이다.
`get pointer (gptr)` → 읽기 위치
`put pointer (pptr)` → 쓰기 위치

즉, 메모리는 하나지만, 읽고 쓰는 “커서”가 별도로 움직인다.

### **스트림(stream)**

실제 데이터 소스(콘솔, 파일, 메모리 등)에 상관없이 동일한 방식으로 입출력을 다룰 수 있도록, 데이터의 흐름을 추상화한 개념이다.

C++ 표준 라이브러리에서는 크게 다음의 세 가지 범주로 나누어 클래스를 제공한다.
`<iostream>` → 콘솔(표준 입출력)
`<sstream>` → 문자열(메모리)
`<fstream>` → 파일(디스크)

이 중, 문자열 파싱이나 포맷팅을 할 때 주로 사용되는 `<sstream>`에 대해 알아보고자 한다.

<br>

> 개요

sstream 헤더 안에는 문자열을 대상으로 입출력을 수행할 수 있는 다음과 같은 대표적인 스트림 클래스들이 정의되어 있다.

1. `istringstream`: 입력 전용 클래스

버퍼에 저장된 문자열 데이터를 `>>` 연산자를 통해 변수에 입력할 수 있다.
`>>` 연산자는 기본적으로 공백을 무시하고, 공백 단위로 끊어서 토큰을 읽는다.
입력된 데이터의 타입은 변수 타입에 맞게 자동 변환(캐스팅 X)된다.

```cpp
  std::istringstream iss("123 45");
  int a, b;
  iss >> a >> b;   // a = 123, b = 45

  std::istringstream iss.str("123asdf");
  int x;
  string s;
  iss >> x >> s // x = 123, s = asdf -> 부분 파싱
```

\* 공백 종류
스페이스: `' '`
탭: `\t`
개행: `\n`
캐리지 리턴: `\r`
수직 탭: `\v`
폼피드: `\f`

\* 캐스팅: 메모리에 있는 값을 그대로 다른 타입으로 해석

```cpp
  char c = 'A';	// 내부 값: 65
  int n = c;		// n = 65 (아스키 코드에 따른 정수 변환)
```

2. `ostringstream`: 출력 전용 클래스
   데이터를 `<<` 연산자를 통해 버퍼에 출력할 수 있다.(공백 포함)

```cpp
	std::ostringstream oss;
	oss << 123 << ", " << 45;
	std::string s = oss.str();	// "123, 45"
```

3. `stringstream`: 입출력 겸용 클래스
   문자열 데이터를 버퍼로부터 입력할 수도, 버퍼에 출력할 수도 있다.

```cpp
	std::stringstream ss;
	ss << "100 200";
	int x, y;
	ss >> x >> y;	// x = 100, y = 200
```

<br>

> 주요 메서드

`ostringstream`, `istringstream`, `stringstream` 중 가장 보편적으로 사용되는 `stringstream`의 주요 메서드들에 대해 정리해 보자. 이 중, 일부 메서드는 `ostringstream`와 `istringstream`에도 공통적으로 적용된다.

1. `str()`:
   getter: ss.str() → 현재 내부 버퍼 내용을 std::string으로 반환
   setter: ss.str("새 문자열") → 내부 버퍼 내용을 교체

2. `seekg(n)`: 읽기 커서를 n번째 인덱스로 이동

3. `tellg()`: 현재 읽기 커서 위치 반환

4. `seekp(n)`: 쓰기 커서를 n번째 인덱스로 이동

5. `tellp()`: 현재 쓰기 커서 위치 반환

6. `cleear()`: 스트림 상태 플래그를 초기화, 버퍼 초기화는 `str("")` 로 한다.

<예시 코드>

```cpp
#include <iostream>
#include <sstream>
using namespace std;

int main() {
  // 입력 전용
  istringstream iss("10 20 30");
  int a, b, c;
  iss >> a >> b >> c;
  cout << "입력: " << a << ", " << b << ", " << c << "\n";

  // 출력 전용
  ostringstream oss;
  oss << "Hello " << 123;
  cout << "출력: " << oss.str() << "\n";

  // 입출력 모두
  stringstream ss("100 200");
  int x, y;
  ss >> x >> y;   // x에 100, y에 200 입력
  ss << " 300";   // ss에 300
  cout << "혼합: " << ss.str() << "\n";
}
```

<br>

> 상태 플래그

스트림 객체(istream, ostream, stringstream, fstream …)에는 다음의 4가지 상태 플래그가 있다.

**goodbit**: 오류 없음 (정상 상태) - 초기 상태 혹은 오류를 clear()로 지운 후
**eofbit**: 입력이 끝에 도달 - 파일 끝까지 읽었거나 stringstream 끝까지 읽음
**failbit**: 연산 실패(형 변환 불가 등) - "abc"를 int로 읽으려 할 때
**badbit**: 심각한 오류 (스트림 자체 손상) - 입출력 장치 문제, 버퍼 자체 에러 등

<예시 코드>

```cpp
#include <iostream>
#include <sstream>

int main() {
  std::stringstream ss("123");
  int n;

  ss >> n;
  std::cout << "첫 읽기 후: "
            << "good=" << ss.good()
            << " eof="  << ss.eof()
            << " fail=" << ss.fail()
            << " bad="  << ss.bad()
            << "\n";

  ss >> n;
  std::cout << "두번째 읽기 후: "
            << "good=" << ss.good()
            << " eof="  << ss.eof()
            << " fail=" << ss.fail()
            << " bad="  << ss.bad()
            << "\n";
}
```

<실행 결과>

```bash
첫 읽기 후: good=1 eof=0 fail=0 bad=0
두번째 읽기 후: good=0 eof=1 fail=1 bad=0
```
