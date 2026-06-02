# 시맨틱 태그

```html
<!doctype html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
  </head>
  <body>
    <header>
      <h1>여행 정보 사이트</h1>
      <p>국내 여행지와 관련 정보를 소개하는 페이지입니다.</p>
    </header>

    <nav>
      <a href="#intro">소개</a>
      <a href="#places">추천 여행지</a>
      <a href="#tips">여행 팁</a>
    </nav>

    <main>
      <section id="intro">
        <h2>사이트 소개</h2>
        <p>
          이 페이지는 <strong>국내 여행 정보</strong>를 정리한 예시
          페이지입니다. 초보 여행자도 쉽게 이해할 수 있도록 내용을 구성했습니다.
        </p>
      </section>

      <section id="places">
        <h2>추천 여행지</h2>

        <article>
          <h3>부산</h3>
          <p>바다와 도시 풍경을 함께 즐길 수 있는 대표적인 여행지입니다.</p>
          <blockquote>
            "추천 방문 시기:
            <time datetime="2026-05">5월"</time>
          </blockquote>
        </article>

        <article>
          <h3>경주</h3>
          <p>역사 유적과 전통적인 분위기를 느낄 수 있는 도시입니다.</p>
          <p>
            경주는 걸으면서 천천히 둘러볼수록 매력이 잘 드러나는 도시입니다.
          </p>
          <p><cite>* 국내 여행 가이드</cite> 참고</p>
        </article>
      </section>

      <section id="tips">
        <h2>여행 팁</h2>
        <p>
          여행 전에는 교통편과 숙소를 미리 확인하는 것이<br />
          <em>"매우 중요"</em> 합니다.
        </p>

        <aside>
          <h3>참고 정보</h3>
          <p>
            성수기에는 숙박 요금이 크게 오를 수 있으니 미리 예약하는 것이
            좋습니다.
          </p>
        </aside>
      </section>
    </main>

    <footer>
      <address>문의: travel@example.com</address>
      <p>© 2026 여행 정보 사이트</p>
    </footer>
  </body>
</html>
```

시맨틱 태그(semantic tag)란,<br>
**태그 이름만 보고도 해당 요소의 의미나 역할을 알 수 있는 HTML 태그**를 말한다.<br>
즉, 이 영역이 **무엇을 위한 구역인지**를 나타낸다.

단, 그 자체로 **구체적인 레이아웃 구조나 배치 방식이 정해지는 것은 아니다.**

즉,<br>
\- `header`라고 해서 자동으로 페이지 맨 위에 고정되는 것도 아니고,<br>
\- `aside`라고 해서 자동으로 옆에 배치되는 것은 아니며,<br>
\- `section`이라고 해서 눈에 보이는 구획선이 생기는 것도 아니다.

## 1. 주요 시맨틱 태그

#### `header`
페이지 또는 구역의 머리말 영역

#### `nav`
메뉴, 내비게이션 링크 영역

#### `main`
문서의 핵심 본문 영역

#### `section`
하나의 주제를 기준으로 묶은 구역

#### `article`
독립적으로 떼어 내어도 의미가 통하는 하나의 완성된 내용

#### `aside`
본문과 간접적으로 관련된 보조 정보 영역

#### `footer`
페이지 또는 구역의 바닥글 영역

#### `h1 ~ h6`
제목 태그, 숫자가 작을수록 상위 제목

#### `p`
문단을 나타내는 태그

#### `strong`
중요한 내용을 나타내는 태그

#### `em`
강조되는 내용을 나타내는 태그

#### `blockquote`
인용문 블록을 나타내는 태그

#### `cite`
출처, 작품명, 문서명 등을 나타내는 태그

#### `time`
날짜나 시간을 의미 있게 나타내는 태그

#### `address`
연락처, 작성자 정보, 운영자 정보 등을 나타내는 태그

## 2. 시맨틱 태그가 아닌 태그

#### `div`
의미 없는 일반 블록 컨테이너

#### `span`
의미 없는 일반 인라인 컨테이너