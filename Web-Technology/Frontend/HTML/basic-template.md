# Basic HTML Template

```html
<!doctype html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
  </head>
  <body></body>
</html>
```

#### 1. `<!doctype html>`
이 문서는 HTML5 문서라는 형식 선언,
브라우저가 표준 방식으로 HTML을 해석하도록 알려 줌

#### 2. `<html lang="ko"> ... </html>`
HTML 문서 전체를 감싸는 최상위 태그<br>
`lang="ko"`는 이 문서의 주 언어가 한국어라는 뜻, 영어면 `"en"`, 일본어면 `"ja"`<br>
검색 엔진, 브라우저, 스크린리더가 문서 언어를 이해할 때 도움 됨

#### 3. `<head> ... </head>`
웹페이지의 설정 정보를 넣는 부분<br>
**화면 본문에 직접 보이는 내용이 아니라**,
문서 정보, 문자 인코딩, 탭 제목, 반응형 설정 등을 작성함

\- `charset="UTF-8"`: 문자 인코딩 방식 지정, UTF-8<br>
\- `name="viewport"`: viewport(브라우저가 현재 웹페이지를 보여 주는 화면 영역) 관련 설정임을 지정<br>
\- `content="width=device-width, initial-scale=1.0"`: 페이지 너비를 기기 화면 너비에 맞추고, 초기 확대 배율을 1로 설정함 (주로 모바일 브라우저 대상 설정)
\- `<title>Document</title>`: 브라우저 탭에 표시되는 페이지 제목, 검색 결과 제목이나 북마크 이름에도 활용될 수 있음

#### 4. `<body> ... </body>`
실제 웹페이지 화면에 표시되는 내용을 넣는 부분<br>
텍스트, 이미지, 버튼, 제목, 목록 등 대부분의 요소가 여기 들어감
