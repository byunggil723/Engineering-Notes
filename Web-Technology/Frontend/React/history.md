# Frontend evolution to React

### 1. HTML

초기 웹은 HTML만으로 문서의 구조와 내용을 표현하였다.

- HTML로 모든 제목, 문단, 링크, 표 등을 작성
- 페이지는 대부분 정적: 페이지 내용이 미리 고정되어 있어서, 사용자의 행동에 따라 브라우저 안에서 즉시 바뀌지 않는 상태
- 사용자는 링크를 눌러 다른 페이지로 이동하는 방식으로 웹을 이용

이 시기의 웹은 지금의 `앱`이라기보다 문서 뷰어에 가까웠다.

### 2. CSS & JavaScript

하지만 HTML만으로는 디자인을 세밀하게 제어하기 어려웠기 때문에, 스타일을 담당하는 CSS가 본격적으로 사용되기 시작했다.<br>
이후 웹페이지에 사용자와의 상호작용을 추가하고, 화면의 일부를 동적으로 바꾸기 위해 JavaScript가 도입되었다.

JavaScript가 HTML을 조작하려면, 브라우저는 HTML 문서를 JavaScript가 접근 가능한 구조로 바꿔야 했다.<br>
하지만, 당시에는 지금처럼 `document.querySelector()` 같은 통일된 방법이 없었고, 브라우저마다 각자 문서에 접근하는 기능을 제공했다.<br>
대표적으로 옛 Internet Explorer 계열은 document.all, Netscape 4 계열은 document.layers 같은 식의 자체 모델을 썼었다.<br>
따라서 이 시기에는 브라우저별 분기 처리가 흔하였다.

```JS
if (document.all) {
  // IE 방식
} else if (document.layers) {
  // Netscape 방식
}
```

### 3. 표준 DOM

이후 문서를 다루는 방식을 통일하기 위해 표준 DOM이 정리되었다.<br>
DOM이란, 브라우저가 HTML을 읽고, 이를 객체 트리 형태로 메모리에 만든 것이다.

예를 들어 HTML이 다음과 같다면

```html
<!DOCTYPE html>
<html>
  <body>
    <div id="title">Hello</div>
  </body>
</html>
```

브라우저는 이를 대략 다음과 같은 트리 형태로 구조화한다.

```
document
└── html
    └── body
        └── div#title
            └── "Hello"
```

`document`는 브라우저가 제공하는 DOM 접근용 객체다.<br>
이를 통해 JavaScript는 다음과 같이 DOM 안의 요소를 찾아 내용을 바꿀 수 있다.

```JS
document.getElementById("title").textContent = "Hi";
```

### 4. Ajax (Asynchronous JavaScript and XML)

Ajax가 등장하기 이전에는, 사용자가 요청을 보내면 서버가 새로운 HTML 페이지 전체를 반환하고 브라우저는 그 페이지를 다시 렌더링하는 방식이 일반적이었다.<br>

하지만 웹페이지의 기능이 점점 복잡해지면서, 페이지 전체를 새로고침하지 않고도 서버와 데이터를 주고받을 필요가 생겼다.<br>
이때 등장한 개념이 Ajax이다.

Ajax란, JavaScript를 통해 서버와 비동기적으로 통신하고, 페이지 전체를 다시 불러오지 않은 채 필요한 부분만 갱신하는 방식을 말한다.

\* 어플리케이션: 사용자가 어떤 목적을 이루기 위해 사용하는 소프트웨어

예를 들어 서버가 다음과 같은 XML 형식으로 데이터를 보낸다고 하자.

```XML
<user>
  <name>Alice</name>
  <age>25</age>
</user>
```

그러면 JavaScript는 이를 다음과 같이 받아 사용할 수 있다.

```JS
const xhr = new XMLHttpRequest();
xhr.open("GET", "/user.xml", true);

xhr.onload = function () {
  const xml = xhr.responseXML;

  const name = xml.getElementsByTagName("name")[0].textContent;
  const age = xml.getElementsByTagName("age")[0].textContent;

  console.log(name, age); // Alice 25
};

xhr.send();
```

여기서 `responseXML`은 서버가 보낸 XML 문서를 브라우저가 파싱한 결과이며, JavaScript는 각 태그를 찾아 그 안의 값을 꺼내 사용할 수 있다.

초기에는 서버와 데이터를 주고받을 때 XML 형식이 많이 사용되었기 때문에 `Asynchronous JavaScript and XML`이라는 이름이 붙었다.<br>
하지만 시간이 지나면서 XML보다 더 간단하고 JavaScript와 잘 맞는 JSON 형식이 훨씬 더 널리 사용되게 되었다.

\* XML: eXtensible Markup Language, 데이터를 구조적으로 표현하기 위한 언어

Ajax의 등장은 웹을 단순한 문서 열람 도구에서 벗어나, Gmail, 지도 서비스, 실시간 검색 추천과 같은 더 인터랙티브한 웹 애플리케이션으로 발전시키는 데 중요한 역할을 했다.

### 5. jQuery

이론적으로는 DOM 표준이 존재했지만, 실제 브라우저들은 이를 항상 동일하게 구현하지는 않았기 때문에,<br>
JavaScript로 DOM을 조작하거나 서버와 비동기 통신을 수행하는 일은 여전히 불편했다.

예를 들어, 표준 브라우저에서는 다음과 같이 이벤트를 등록했지만,

```JS
element.addEventListener("click", handler);
```

구형 Internet Explorer에서는 다음과 같은 별도 방식을 사용해야 하는 경우가 있었다.

```JS
element.attachEvent("onclick", handler);
```

Ajax 요청 역시 브라우저에 따라 생성 방식이 달랐다.

```JS
var xhr;
if (window.XMLHttpRequest) {
  xhr = new XMLHttpRequest();
} else {
  xhr = new ActiveXObject("Microsoft.XMLHTTP");
}
```

이러한 불편을 크게 줄여 준 대표적인 라이브러리가 `jQuery`이다.

jQuery는 다음과 같은 역할을 했다.

- DOM 선택과 조작을 더 간단한 문법으로 제공
- 이벤트 처리를 더 일관되게 지원
- Ajax 요청을 쉽게 작성 가능하게 함
- 브라우저별 구현 차이를 내부적으로 흡수

예를 들어 순수 JavaScript에서는 다음과 같이 작성하던 것을,

```JS
document.getElementById("title").textContent = "Hi";
```

jQuery에서는 더 짧고 직관적으로 작성할 수 있었다.

```JS
$("#title").text("Hi");
```

즉, jQuery는 브라우저 호환성 문제를 완화하고, DOM 조작과 Ajax를 더 간편하게 만들어 준 도구였다.

### 6. React

jQuery를 사용하더라도 본질적으로는 여전히 개발자가 HTML 구조를 바탕으로 id나 class를 기준으로 DOM 요소를 직접 찾아 수정해야 했다.<br>
즉, 서비스가 복잡해질수록 상태 변화와 DOM 수정 사이의 흐름을 추적하기 어려워지고, HTML 구조 변경에 코드가 쉽게 영향을 받으며,<br>
여러 코드가 동일한 DOM을 건드리며 충돌할 가능성이 높아지는 등 다양한 한계점이 드러났다.

이러한 근본적인 문제를 해결하기 위해 등장한 것이 `React`이다. 

기존 DOM 직접 조작 방식에서는 상태 변화가 반영되어야 하는 지점마다 개발자가 이에 대응하는 DOM 수정 코드를 직접 작성해 주어야 했지만,<br>
React는 페이지를 컴포넌트 단위로 구분하고 각 컴포넌트가 가져야 할 상태와 그 상태에 따른 UI를 미리 선언해 둠으로써,<br>
상태 변화와 화면 반영의 관계를 더욱 깔끔하고 편리하게 관리할 수 있게 되었다.

다음의 기능을 두 방식으로 구현한 예시를 통해 차이점을 살펴 보자.

- 현재 숫자를 보여줌
- +1, -1 버튼으로 값 변경
- 짝수/홀수 메시지 표시

#### DOM 직접 조작 방식

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Vanilla DOM Counter</title>
  <style>
    /* 스타일 */
  </style>
</head>
<body>
  <div class="app">
    <h1>Vanilla DOM Counter</h1>
    <p id="count">현재 값: 0</p>
    <p id="message">짝수입니다.</p>

    <div class="buttons">
      <button id="plusBtn">+1</button>
      <button id="minusBtn">-1</button>
    </div>
  </div>

  <script>
    let count = 0;

    const countEl = document.getElementById("count");
    const messageEl = document.getElementById("message");
    const plusBtn = document.getElementById("plusBtn");
    const minusBtn = document.getElementById("minusBtn");

    function render() {
      countEl.textContent = `현재 값: ${count}`;
      messageEl.textContent = count % 2 === 0 ? "짝수입니다." : "홀수입니다.";
    }

    plusBtn.addEventListener("click", function () {
      count += 1;
      render();
    });

    minusBtn.addEventListener("click", function () {
      count -= 1;
      render();
    });

    render();
  </script>
</body>
</html>
```

#### React

```html
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>React Counter without JSX</title>
  <style>
    /* 스타일 */
  </style>
</head>
<body>
  <div id="root"></div>

  <script src="https://unpkg.com/react@18/umd/react.development.js" crossorigin></script>
  <script src="https://unpkg.com/react-dom@18/umd/react-dom.development.js" crossorigin></script>

  <script>
    const { useState } = React;

    function Counter() {
      const [count, setCount] = useState(0);

      return React.createElement(
        "div",
        { className: "app" },
        React.createElement("h1", null, "React Counter"),
        React.createElement("p", null, `현재 값: ${count}`),
        React.createElement(
          "p",
          { className: "message" },
          count % 2 === 0 ? "짝수입니다." : "홀수입니다."
        ),
        React.createElement(
          "div",
          { className: "buttons" },
          React.createElement(
            "button",
            { onClick: () => setCount(count + 1) },
            "+1"
          ),
          React.createElement(
            "button",
            { onClick: () => setCount(count - 1) },
            "-1"
          )
        )
      );
    }

    const root = ReactDOM.createRoot(document.getElementById("root"));
    root.render(React.createElement(Counter));
  </script>
</body>
</html>
```

그런데 위 React 예시에서 return 문을 보면, UI 구조가 모두 함수 호출 형태로 작성되어 코드가 지나치게 길고 직관성이 떨어진다.

```JS
React.createElement("h1", null, "Hello");
```

이러한 불편함을 줄이고 React 컴포넌트의 화면 구조를 더 직관적으로 표현하기 위해 고안된 문법이 바로 JSX이다.

JSX는 JavaScript XML의 줄임말로, JavaScript 안에서 HTML과 유사한 형태로 UI를 작성할 수 있게 해 준다.

예를 들어 위 코드는 JSX를 사용하면 다음과 같이 훨씬 직관적으로 표현할 수 있다.

```JS
<h1>Hello</h1>
```

또한 JSX 안에서는 중괄호 {}를 사용하여 JavaScript 표현식을 직접 삽입할 수 있다.

```JS
<p>현재 값: {count}</p>
<p>{count % 2 === 0 ? "짝수입니다." : "홀수입니다."}</p>
```

이처럼 JSX를 사용하면 상태에 따라 달라지는 UI를 더 읽기 쉽고 간결하게 표현할 수 있다.

다만 JSX는 브라우저가 직접 이해할 수 있는 문법은 아니다.<br>
브라우저는 JSX를 그대로 실행하지 못하므로, 이를 일반 JavaScript 코드로 변환하는 과정이 필요하다.<br>
이때 사용되는 대표적인 도구가 `Babel`이다.

따라서, 다음의 JSX 코드는

```JS
<h1>Hello</h1>
```

빌드(또는 트랜스파일) 과정에서 일반 JavaScript 코드로 변환된다.

```JS
React.createElement("h1", null, "Hello");
```

다음은 `JSX`를 활용하여 작성된 React 예시이다.

```JS
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>React Counter</title>
  <style>
    /* 스타일 */
  </style>
</head>
<body>
  <div id="root"></div>

  <script src="https://unpkg.com/react@18/umd/react.development.js" crossorigin></script>
  <script src="https://unpkg.com/react-dom@18/umd/react-dom.development.js" crossorigin></script>
  <script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>

  <script type="text/babel">
    const { useState } = React;

    function Counter() {
      const [count, setCount] = useState(0);

      return (
        <div className="app">
          <h1>React Counter</h1>
          <p>현재 값: {count}</p>
          <p className="message">
            {count % 2 === 0 ? "짝수입니다." : "홀수입니다."}
          </p>

          <div className="buttons">
            <button onClick={() => setCount(count + 1)}>+1</button>
            <button onClick={() => setCount(count - 1)}>-1</button>
          </div>
        </div>
      );
    }

    const root = ReactDOM.createRoot(document.getElementById("root"));
    root.render(<Counter />);
  </script>
</body>
</html>
```