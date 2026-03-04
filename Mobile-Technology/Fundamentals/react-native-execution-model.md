# React Native 앱 실행 구조

### 1. 실행 전체 구조

모바일 앱은 다음과 같은 계층 구조 위에서 실행된다.

```
Hardware
↓
Kernel
↓
App Process (Native code)
↓
JS Engine (Hermes)
↓
JS Bundle 실행
```

\- OS는 Native executable만 직접 실행할 수 있다.<br>
\- Native code가 JS engine(Hermes)을 실행시키고, JS bundle을 로드한다.<br>
\- JS bundle은 앱의 UI 정의 및 로직을 포함한다.<br>

### 2. JS Bundle

JS bundle은 앱의 모든 JavaScript 코드를 하나로 묶은 실행 파일이다.

```ts
AttendanceButton.tsx
App.tsx
api.ts
  ↓
index.android.bundle
  ↓
Hermes engine이 실행
```

JS bundle은 CPU가 직접 실행하는 native machine code가 아니라, JS engine이 실행하는 코드이다.

### 3. 프로세스와 스레드
프로세스(Process): 실행 중인 앱 (독립된 메모리 공간을 가짐)

\- KakaoTalk Process<br>
\- Chrome Process<br>
\- Instagram Process<br>

각 프로세스는 서로 메모리를 공유하지 않는다.

스레드(Thread): 프로세스 내부에서 실행되는 작업 단위

```
KakaoTalk Process
├ UI Thread
├ JS Thread
├ Native Thread
```

모든 스레드는 같은 메모리를 공유한다.

### 4. 스레드 스케줄링(Thread Scheduling)

CPU는 한 번에 하나의 스레드만 실행할 수 있다.<br>
따라서 커널은 다음과 같이 스레드를 번갈아 실행한다.

```
...

Process: KakaoTalk
→ UI Thread 실행 (2ms)

Process: Instagram
→ JS Thread 실행 (2ms)

Process: Chrome
→ Renderer Thread 실행 (2ms)

Process: Instagram
→ UI Thread 실행 (2ms)

Process: KakaoTalk
→ Network Thread 실행 (2ms)

Process: NightSchoolApp
→ Native Module Thread 실행 (2ms)

...
```

이 과정을 Thread Scheduling이라고 한다.

### 5. React Native 앱 동작 방식 (Bridge 기반)

React Native 앱의 사용자 인터랙션 처리 흐름은 다음과 같다.

```
User touch
↓
UI Thread ← 터치 이벤트 수신
↓
Bridge ← UI Thread와 JS Thread 간 통신 중계
↓
JS Thread ← 애플리케이션 로직 처리(React state, fetch 등)
↓
Bridge
↓
UI Thread ← 최종 렌더링 수행
↓
Render
```

따라서 JS Thread가 block되면,

```ts
while(true) {}

// JS Thread block
```

```
User touch
↓
UI Thread
↓
Bridge
↓
JS Thread (block됨)
↓
응답 없음
↓
UI 업데이트 불가
```

앱이 멈춘 것처럼 보인다.

### 6. Reanimated 동작 방식

Reanimated는 worklet을 UI Thread에 직접 등록한다.

```ts
Gesture.Pan()
  .onUpdate((event) => {
    translateX.value = event.translationX;
  });

// 앱 시작 시, 위 콜백 함수가 worklet로 변환되어 UI Thread에서 실행되도록 등록됨
```

```bash
JS Thread
↓
worklet로 변환
↓
UI Thread에 등록

# gesture 발생 시,

User touch
↓
UI Thread
↓
등록된 worklet 실행
↓
sharedValue 변경
↓
UI render
```

즉, JS Thread를 거치지 않는다.

### 7. runOnJS

runOnJS는 UI Thread에서 JS Thread로 작업을 요청하는 함수이다.

```ts
runOnJS(setOnAttendance)(true);
```

```
UI Thread
↓
runOnJS 호출
↓
JS Thread task queue에 등록
↓
UI Thread는 기다리지 않음 (비동기)
↓
JS Thread가 나중에 실행
```

→ runOnJS는 비동기(async)이다. 즉, UI Thread를 block하지 않는다.

### 8. Thread별 역할

#### UI Thread

\- Touch event 처리<br>
\- Rendering 수행<br>
\- Reanimated worklet 실행<br>

#### JS Thread

\- JS bundle 실행<br>
\- React state 관리<br>
\- API 요청 처리<br>
\- Business logic 수행<br>

#### Native Thread

\- Camera 접근<br>
\- File system 접근<br>
\- Bluetooth 접근<br>
\- OS interaction 수행<br>

### 9. Animated vs Reanimated 비교

#### Animated

```
UI Thread
↓
Bridge
↓
JS Thread
↓
Bridge
↓
UI Thread
↓
Render
```

→ JS Thread block 시 UI 멈춤

#### Reanimated

```
UI Thread
↓
Worklet 실행
↓
Render
```

→ JS Thread 무관, Animation이 부드럽게 동작

\* Animated API 또한 `useNativeDriver: true` 옵션을 통해 animation을 UI Thread에서 직접 실행하도록 위임할 수 있다.

### 10. 전체 구조 요약

```
Kernel
↓
App Process
├ UI Thread
├ JS Thread
├ Native Thread
↓
JS Engine (Hermes)
↓
JS Bundle 실행
```

\- 프로세스는 실행 중인 앱이다.<br>
\- 스레드는 프로세스 내부의 실행 단위이다.<br>
\- React Native는 UI Thread와 JS Thread를 분리하여 동작한다.<br>
\- 기본 React Native는 Bridge를 통해 UI Thread와 JS Thread가 통신한다.<br>
\- JS Thread가 block되면 UI 업데이트가 멈춘다.<br>
\- Reanimated는 worklet을 UI Thread에서 직접 실행하여 Bridge를 bypass한다.<br>
\- runOnJS는 UI Thread에서 JS Thread로 비동기 작업 요청을 수행한다.<br>