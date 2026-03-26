# Queue(FIFO)

```Java
import java.util.*;

Queue<Integer> q = new ArrayDeque<>();
// Queue는 인터페이스로서, ArrayDeque 구현체를 통해 구현된다.
// 일반적으로 LinkedList보다 ArrayDeque가 더 빠르고 메모리 효율이 좋아, Queue 구현에는 ArrayDeque를 주로 사용한다.

q.offer(1);   // 삽입
q.offer(2);

q.poll();     // 가장 앞(가장 먼저 들어온 데이터)에 있는 값을 꺼냄 (없으면 null → 예외 발생 X)
q.peek();     // 가장 앞에 있는 값을 꺼내지 않고 조회 → 1

q.isEmpty();
q.size();
```
