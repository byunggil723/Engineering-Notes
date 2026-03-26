# Type System

### Wrapper Class

|  primitive 	|  wrapper  |
|-------------|-----------|
| int	        | Integer   |
| long	      | Long      |
| double	    | Double    |
| char	      | Character |
| boolean	    | Boolean   |

List, Set, Map 등의 자료구조를 통틀어 컬렉션(Collection Framework)이라고 부른다.<br>
컬렉션은 객체(Object)만 저장할 수 있어, 반드시 Wrapper Class(Integer, Long 등)를 사용하여 선언한다.

```Java
ArrayList<int> list;        // 불가능
ArrayList<Integer> list;    // 가능
```

### 오토박싱, 언박싱 (Autoboxing, Unboxing)

primitive 타입과 Wrapper Class 사이에는 자동 변환이 발생한다.

#### primitive → 객체

```Java
int a = 10;
Integer b = a;   // 오토박싱: 내부적으로 Integer b = Integer.valueOf(10); 를 수행
```

#### 객체 → primitive

```Java
Integer a = 10;
int b = a;   // 언박싱: 내부적으로 int b = a.intValue(); 를 수행
```

### 비교 연산자

primitive 타입은 JVM 메모리 상에서 변수에 값 자체가 저장되고,<br>
reference 타입은 변수에 해당 객체를 가리키는 참조(실제 물리 주소가 아닌, JVM이 관리하는 추상적인 주소)가 저장된다.<br>
따라서 primitive 타입은 값 자체를 저장하므로 `==`로 바로 값의 비교가 가능하다.<br>
반면, Wrapper Class와 같은 reference 타입은 변수에 객체의 주소가 저장되므로 `==`는 주소 비교를 수행한다.<br>
따라서 값 자체의 비교를 위해서는 `equals()` 메서드를 사용해야 한다.

```Java
Integer a = new Integer(10);
Integer b = new Integer(10);

a == b        // false (서로 다른 객체)
a.equals(b)   // true  (값 자체 비교)
```

Integer와 같은 일부 Wrapper 클래스는 -128 ~ 127 범위의 값에 대해 캐싱이 적용되어, 동일한 값이 할당될 경우 내부적으로 동일한 객체가 재사용된다.<br>
따라서 해당 범위 내에서는 == 비교 연산이 값 자체를 비교하는 것처럼 동작한다.<br>
위의 예시에서는 비교 대상 값이 해당 범위 내에 있지만, `new`를 통해 각각 새로운 객체로 생성되었기 때문에<br>
`==` 비교 시 서로 다른 객체로 판단되어 `false`가 된다.

```Java
Integer a = 100;
Integer b = 100;

a == b        // true
a.equals(b)   // true
```

하지만, 이러한 별개의 동작에 의존하지 말고, 객체 간 값 비교는 항상 `equals()` 메서드를 사용하는 것이 안전하다.<br>
단, primitive 타입(int, long 등)은 객체가 아니므로 `equals()`를 사용할 수 없으며, 이 경우에는 반드시 `==`를 사용하여 값 비교를 수행한다.

String 리터럴은 String pool에 저장되며, 이 또한 동일한 문자열이 할당될 경우 내부적으로 동일한 객체가 재사용된다.

```Java
String a = "abc";
String b = "abc";

a == b        // true  (같은 객체)
a.equals(b)   // true
```

그러나 `new`를 사용하면 역시 새로운 객체가 생성된다.

```Java
String a = new String("abc");
String b = new String("abc");

a == b        // false
a.equals(b)   // true
```