# Browser

```
	 ┌──────────────────────────────────────┐
	 │        JavaScript Engine (V8)        │
	 │   ┌────────────┐   ┌────────────┐    │
	 │   │ Call Stack │   │    Heap    │    │
	 │   └────────────┘   └────────────┘    │
	 └──────────────────────────────────────┘
     	                │
        	            ▼
      ┌─────────────────────────────────────┐
      │       Event Loop (HTML Spec)        │
      └─────────────────────────────────────┘
                        │
     ┌──────────────────┬──────────────────┐
     ▼                  ▼                  ▼
Macro Task Queue   Microtask Queue   Rendering Queue
(setTimeout, I/O) (Promise, await)     (Repaint)

```

브라우저의 작동 과정을 살펴보자.

### 1. HTML 파싱

서버에 첫 번째 요청은 항상 HTML이다.<br>
브라우저의 HTML 파서는 HTML을 토큰화 → DOM 트리 구성하는데, 이 과정에서<br>

1. HTML 파싱하면서 `<link\>` 발견 → CSS 다운로드 시작
2. 파싱하며 `<script\>` 발견 → JS 다운로드 시작
3. `<img\>` 발견 → 이미지 다운로드 시작
   ...
   과 같은 과정을 거치게 된다.

예를 들어, HTML 파싱 도중

```
<link rel="stylesheet" href="/styles/main.css">
```

가 있다하면,

```
GET /styles/main.css
```

브라우저는 서버에 위와 같은 요청을 다시 보내어 css 파일을 받아 온다.<br>
css 파일을 받아 오는 동안에 HTML 파싱은 계속 진행되며, css 파싱도 마찬가지로 병렬적으로 일어 난다.

```
<script src="/app.js"></script>
```

가 있다하면,

```
HTML 파싱 중단
↓
JS 다운로드
↓
JS 실행 (즉시)
↓
JS 종료 후 HTML 파싱 재개
```

그런데 JS 실행 시점에서 아직 CSSOM이 준비되지 않았다면, JS 실행이 지연될 수 있다.

`<script src="...">` 나 `<script async src="...">` 도 있는데,

```
<script defer src="/main.js"></script>
```

```
HTML 파싱 계속 진행 (중단 없음)
↓
JS 다운로드는 비동기
↓
HTML 파싱 끝나면 DOM 완성
↓
JS 실행
```

```
<script async src="/ads.js"></script>
```

→ hydration 스크립트로 자주 사용됨

```
HTML 파싱 계속 진행 (중단 없음)
↓
JS 다운로드는 병렬
↓
JS 다운로드가 끝나는 즉시 실행 (파싱 잠시 중단)
↓
JS 실행 끝 → HTML 파싱 재개
```

→ 광고/analytics에 적합

참고)

```
<link rel="stylesheet" href="/style.css">
<link rel="icon" href="/favicon.ico">
<link rel="preload" href="/main.js" as="script">
<link rel="manifest" href="/manifest.json">
```

rel 속성은 “이 링크가 무엇을 위한 것인지”를 정의한다.<br>
`rel="stylesheet"` → CSS 파일<br>
`rel="icon"` → 파비콘<br>
`rel="preload"` → 미리 불러오기<br>
`rel="manifest"` → 웹앱 manifest<br>
rel 속성이 없으면 브라우저가 파일을 어떻게 다뤄야 하는지 모른다.<br>
만약<br>

```
<link href="style.css">
```

그냥 위와 같이 쓴다면, css 파일을 받고도 브라우저는 해당 파일에 대한 적절한 조치를 해주지 않는다.

주의!) 브라우저는 오직 최초 로딩 시에만 DOM 트리 전체를 생성할 뿐, 이후 변경 사항은 메모리에 적재된 DOM을 직접 수정한다.

### 2. 렌더 트리(Render Tree) 생성
