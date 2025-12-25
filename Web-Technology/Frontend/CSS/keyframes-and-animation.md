```TS
import styled from "@emotion/styled";
import { keyframes } from "@emotion/react";

const fadeInUp = keyframes`
  from {
    opacity: 0;
    transform: translateY(2rem);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
`;

const Text = styled.div`
  font-size: 1.2rem;
  font-weight: 500;
  animation: ${fadeInUp} 1s ease-in-out forwards;
`;

export default function App() {
  return <Text>참가자에 대한 의견을 남겨주세요.</Text>;
}
```

### **@keyframes**

시간에 따라 어떻게 변화시킬지 "시나리오" 정의

### **animation**

정의한 keyframes를 **실행**

### **transform**

요소를 이동(`translate`), 회전(`rotate`), 확대/축소(`scale`) 등으로 **변형**하는 속성

### **transition**

특정 속성 값이 바뀔 때, 그 변화를 보여주는 방법을 정의
-> 변화를 일으키는 속성은 아님
