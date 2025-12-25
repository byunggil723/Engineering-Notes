# #include <string\>

문자열을 다룰 때 유용한 &lt;string&gt; 헤더에 대해 알아보자.

> 주요 메서드

### Create

(1) 생성

```cpp
#include <iostream>
#include <string>

using namespace std;

int main() {
  string s1 = "Hello";		// 리터럴 대입
  string s2("World");		// 생성자
  string s3(5, 'x');		// "xxxxx", 마지막 매개변수의 인자는 반드시 단일 문자
  cout << s1 << " " << s2 << " " << s3 << endl;
}
```

(2) 추가 (append, push_back, +)

```cpp
string s = "Hello,";
s.append(" World");  	// "Hello, World"
s.append(2, '!');		// "Hello, World!!", 마지막 매개변수의 인자는 반드시 단일 문자
s.push_back('!');    	// "Hello World!!!", push_back(): 문자열 끝에 문자 1개 추가, 매개변수의 인자는 반드시 단일 문자
s = s + " C++";		// "Hello World!!! C++"
string t = "master";
s.append(t, 1, 3); 	// "Hello World! C++ast", index 1부터 이어지는 3개의 문자 추가
cout << s << endl;
```

### Read

(1) 기본 읽기

```cpp
string s = "apple";
cout << s.at(2);	// 'p'
s.at(2) = 'X';		// "apXle"
s[1] = 'X'; 		// "aXXle"
s.size(); 			// 5
s.length; 			// 5, size()와 length()는 동일한 함수
```

(2) 찾기 (find)

```cpp
string t = "master Type";
cout << boolalpha; 				// bool 값을 "true"/"false"로 출력하도록 설정
cout << t.find('r') << '\n'; 		// 5
cout << t.find("te") << '\n'; 		// 3
cout << t.find('t', 2) << '\n'; 	// 3, index 2부터 탐색
cout << bool(string::npos == t.find('t', 4)) << '\n';
// true, 대소문자를 구별하기 때문에 index 4 이후에는 't'가 존재하지 않으므로 string::npos 반환 즉, 비교 연산 결과 true
cout << t.find("st", 1) << '\n'; 	// 2
cout << bool(string::npos == t.find("sx", 1)) << '\n'; // true
cout << noboolalpha; 				// 다시 기본 모드로 돌림
```

### Update

(1) 삽입 (insert)

```cpp
// index 기반
string s = "Hello World";
s.insert(5, ",");					// "Hello, World"
s.insert(7, "C++", 2);				// "Hello, C+World", index 7에 "C+" 삽입
s.insert(0, 2, '*');				// "**Hello, C+World", 마지막 매개변수의 인자는 반드시 단일 문자
s.insert(16, "12345", 3); 			// "**Hello, C+World123"

// 반복자 기반
string t = "Hello";
t.insert(t.begin() + 1, 'X');		// "HXello", 마지막 매개변수의 인자는 반드시 단일 문자
const char *cstr = "C++";
t.insert(t.begin() + 5, cstr, cstr + 2); // "HXellC+o"
t.insert(t.begin(), 3, '!');		// "!!!HXellC+o", 마지막 매개변수의 인자는 반드시 단일 문자

string p = "12345";
t.insert(t.end(), p.begin(), p.begin() + 3);
// "!!!HXellC+o123", p의 [begin, begin+3) 구간(즉 index 0 ~ 2)까지의 문자를 삽입
```

(2) 대체 (replace)

```cpp
// index 기반
string s = "I like apple";
s.replace(7, 5, "banana"); // "I like banana", index 7부터 5개의 문자를 지우고, "banana" 삽입
s.replace(2, 4, "abcd", 1, 2);
// "I bc banana", index 2부터 4개의 문자를 지우고, "abcd"의 index 1부터 2개의 문자를 삽입
s.replace(2, 2, "XYZ");       // ""I XYZ banana"
s.replace(0, 2, "12345", 3);  // "123XYZ banana"
s.replace(7, 6, 5, '*');  // "123XYZ *****", 마지막 매개변수의 인자는 반드시 단일 문자

// 반복자 기반
string t = "abcdefgh";
t.replace(t.begin()+2, t.begin()+5, "XYZ");    // "abXYZfgh"
t.replace(t.begin(), t.begin()+3, 2, '*');     // "**YZfgh", 마지막 매개변수의 인자는 반드시 단일 문자
```

### Delete

(1) 부분 삭제 (erase)

```cpp
// index 기반
string s = "abcdef";
s.erase(2, 3);      // "abf", index 2부터 3개("cde") 삭제
s.erase(2);         // "ab", index 2부터 끝까지 삭제
s.erase();          // "", 전부 삭제

// 반복자 기반
s = "abcdef";
s.erase(s.begin()); // "bcdef", 첫 글자 삭제
s.erase(s.begin()+1, s.begin()+4); // "bf", 구간 [1,4) 문자열 "cde" 삭제
```

(2) 전체 삭제 (clear)

```cpp
string s = "abcdef";
s.clear() // ""
```
