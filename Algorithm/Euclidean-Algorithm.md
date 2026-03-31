# 유클리드 호제법

$$
\text{두 양의 정수 } a \text{, } b(a > b) \text{에 대하여 }
a = bq + r(0≤r<b)
\text{라 하면,}
$$

$$
a \text{, } b \text{의 최대공약수는 } b \text{, } r \text{의 최대공약수와 같다. 즉}
$$

$$
gcd(a, b) = gcd(b, r) \text{이다.}
$$

$$
\text{이때, } r = 0 \text{이면, } a \text{, } b \text{의 최대공약수는 } b \text{가 된다.}
$$

### 구현 예시

자바에는 `int`나 `long`형에 대해 제공되는 `gcd`, `lcm` 내장 함수가 없으므로, 보통 유클리드 호제법을 이용해 최대공약수 함수를 직접 구현한다.<br>
최소공배수는 두 수 $a, b$의 최대공약수를 $G$, 최소공배수를 $L$이라 할 때 $a * b = G * L$이 성립함을 이용하여 구할 수 있다.

```java
// long 범위의 정수를 다룰 경우에는 매개변수와 반환형 역시 long으로 선언해야 함에 주의

static int gcd(int a, int b) {
  while (b != 0) {
    int temp = a % b;
    a = b;
    b = temp;
  }
  return a;
}

static int lcm(int a, int b) {
  return a / gcd(a, b) * b;
  // 오버플로우를 방지하기 위해 a / gcd(a, b) 먼저 연산
}
```
