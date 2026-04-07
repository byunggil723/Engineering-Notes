# OSI 7-Layer

| OSI 계층 | 이름             | 주요 프로토콜 / 예시                | 설명                      |
| -------- | ---------------- | ----------------------------------- | ------------------------- |
| 7        | 응용 계층        | HTTP, HTTPS, SMTP, WebSocket, JWT   | 사용자 요청/응답 처리     |
| 6        | 표현 계층        | JSON, Base64, Base64URL, JWT 인코딩 | 데이터 인코딩/압축/형식화 |
| 5        | 세션 계층        | TLS 세션, SSH                       | 세션 관리, 연결 유지      |
| 4        | 전송 계층        | TCP, UDP                            | 신뢰성·순서 보장          |
| 3        | 네트워크 계층    | IP, ICMP, 라우터                    | 주소 지정, 경로 설정      |
| 2        | 데이터 링크 계층 | Ethernet, Wi-Fi, MAC                | 프레임 전송, 오류 검출    |
| 1        | 물리 계층        | 케이블, 신호, 전파                  | 비트를 실제 신호로 변환   |

#### 브라우저에서 데이터가 전송되는 과정을 통해 OSI 7-Layer에 대해 살펴 보자.

OSI 계층은 통신 과정에서 수행되는 작업의 목적을 기준으로 분류한다.

#### 1. 응용 계층 (L7)

1\) 브라우저(응용 프로그램) 내부에서 fetch API 호출을 통해 HTTP 요청 생성이 시작된다.

```js
fetch("/api/login", {
  method: "POST",
  body: JSON.stringify({ id: "user1" }),
});
```

2\) 브라우저는 위 호출을 바탕으로 HTTP 요청 메세지를 구성한다.

```http
POST /api/login HTTP/1.1
Host: example.com
Content-Type: application/json
Content-Length: 14

{"id":"user1"}
```

3\) 브라우저는 서버와 TCP 연결을 맺기 위해 도메인 이름(example.com)을 IP 주소로 변환한다.
필요한 경우 OS의 Name Resolver를 호출하여 DNS Resolution을 수행한다.

\* TCP(Transmission Control Protocol): 전송 제어 프로토콜

```C
getaddrinfo("example.com", "443", ...);

/*
1. /etc/hosts 확인

127.0.0.1     localhost
192.168.0.10  myserver

2. 없으면 로컬 캐시 확인 (Name Resolver 관여)

3. 로컬 캐시에도 없으면 DNS 서버에 질의 (DNS Resolution)
/*
```

→ 최종적으로 목적지 서버의 IP 주소 및 포트 정보 획득

4\) 소켓 객체 생성

소켓(Socket)은 IP 주소, 포트 번호, 연결 상태, 송·수신 버퍼 등 네트워크 통신에 필요한 상태 정보를 관리하는 커널 객체이다.<br>
브라우저 내부에는 네트워크 요청을 관리하기 위한 네이티브 모듈(클래스)이 존재하며,
이 모듈은 커널이 생성한 소켓 객체를 파일 디스크립터(File Descriptor, fd)를 통해 참조한다.<br>
즉, 브라우저는 소켓을 직접 구현하는 것이 아니라, 커널이 제공하는 소켓을 시스템 콜을 통해 생성·사용한다.

```C
int fd = socket(
  AF_INET,        // 주소 체계: IPv4
  SOCK_STREAM,    // 전송 의미: 바이트 스트림 (연결 지향)
  IPPROTO_TCP     // 전송 프로토콜: TCP or UDP
);
```

네트워크 요청(fetch)이 발생하면, 브라우저는 무조건 새 소켓을 생성하지 않는다.
대신 브라우저(사용자 공간)에서 먼저 기존 소켓의 재사용 가능 여부를 판단한다.

```
요청 발생 (fetch)
↓
[Connection Pool 조회]
↓
[같은 scheme + host + port]
├─ 사용 가능한 연결 있음 → 재사용
└─ 없음 → socket() 호출
```

socket() 시스템 콜이 성공하면 커널은 새로운 소켓 객체를 생성하고,
이를 참조하기 위한 파일 디스크립터(fd)를 브라우저 프로세스에 반환한다.<br>
브라우저는 반환받은 fd를 저장한 뒤,
connect, send, recv 등의 시스템 콜을 호출할 때 해당 fd를 인자로 전달하여
동일한 커널 소켓 객체를 지속적으로 사용한다.<br>

소켓 객체에는 대략 다음과 같은 상태 정보를 저장하게 된다.

- 로컬 IP / 포트 (초기에는 미정일 수 있으며, connect() 또는 bind() 이후 확정)
- 원격 IP / 포트 (연결 수립 후 확정)
- TCP 상태 (예: CLOSED → SYN_SENT → ESTABLISHED)
- 송신 버퍼 / 수신 버퍼
- 재전송 큐
- 시퀀스 번호
- 흐름 제어 및 혼잡 제어 상태 등

이러한 정보는 모두 커널 공간에서 관리되며, 사용자 공간에서는 직접 접근할 수 없다.

\* 파일 디스크립터(File Descriptor): Unix 계열 운영체제에서 파일, 네트워크 소켓, 파이프 등 각종 입출력 리소스에 접근하기 위해 사용되는 정수형 식별자를 말한다.

#### 2. 표현 계층 (L6)

HTTP 메시지를 **전송 가능한 바이트열** 로 만든다 (직렬화 & 인코딩)

```
7B 20 22 69 64 22 3A 20 22 75 73 65 72 31 22 20 7D
```

문자열 → 바이트열 변환은 보통 **브라우저(유저 공간)** 내부에서 수행된다.
커널은 이미 만들어진 바이트열을 소켓 버퍼로 받아 네트워크로 흘려보내는 역할이 핵심이다.

#### 3. 세션 계층 (L5)

**세션(session)** 이란 두 통신 주체가 한 번의 대화(통신)를 지속적으로 이어가기 위해 공유하는 논리적 상태(state)와 그 유지 기간을 말한다.

전송 계층에서 TCP 연결이 수립된 뒤, 보안 세션 설정을 위해 수행되는 TLS 핸드셰이크가 일반적으로 세션 계층(또는 표현 계층)으로 분류된다.<br>
또한 JWT, WebSocket 등의 기술들도 통신을 세션 단위로 유지·관리하는 기능을 포함하므로, 실제 구현에서는 다른 계층의 기능과 결합된 형태로 세션 계층의 역할을 일부 담당한다고 볼 수 있다.

#### 4. 전송 계층 (L4)

1\) TCP 연결 (TCP 3-way handshake)

```C
connect(fd, ip, port);
```

이 호출로 커널은 목적지 IP:PORT(예: 93.184.216.34:443)로 TCP 연결을 수립한다.<br>
성공적으로 연결되면, 커널 내부의 이 fd 소켓의 상태는 ESTABLISHED가 된다.

[TCP 3-way handshake 수행 과정](/Computer-Science/Network/TCP.md)<br>

2\) 보안 세션 생성 (TLS handshake)

TLS 핸드셰이크는 보통 커널이 아니라 브라우저(사용자 공간)의 TLS 스택이 수행한다.
커널은 TLS를 알지 못하며, TLS 메시지도 그냥 TCP 위를 흐르는 바이트로 취급된다.

1. 브라우저 → ClientHello 전송: 브라우저가 서버에게 TLS 핸드셰이크 시작을 요청

   ```C
   // [TLS Record] ClientHello 메세지 (cipher suites, random, SNI 등 포함)
   send(fd, client_hello_bytes, client_hello_len, 0);
   ```

   \* [TLS Record]

   ```C
   [TCP Header | TCP Payload]
                      ↑
                     이 곳에 [TLS Record Header | TLS Payload(Handshake message)] 가 들어감
   ```

2. 서버 → ServerHello + Certificate 수신: 인증서 + 공개키(암호화 전용 키) 전달

   ```C
   // [TLS Record] ServerHello 메세지 수신
   recv(fd, server_hello_bytes, server_hello_len, 0);

   // [TLS Record] Certificate 수신 (서버 인증서 체인 포함)
   recv(fd, cert_bytes, cert_len, 0);
   ```

3. 브라우저 → 인증서(CA) 검증

   ```C
   // 브라우저 자체 작업
   verify_certificate(cert_bytes);
   ```

4. 브라우저 → Pre-Master Secret 생성

   ```C
   // 브라우저 자체 작업
   pre_master_secret = generate_random_bytes();
   ```

5. 브라우저 → Pre-Master Secret을 서버 공개키로 암호화해 서버로 전송

   ```C
   // 브라우저 자체 작업
   encrypted_pms = RSA_encrypt(server_public_key, pre_master_secret);

   // [TLS Record] ClientKeyExchange에 encrypted_pms를 담아 전송
   send(fd, client_key_exchange_bytes(encrypted_pms),
       client_key_exchange_len, 0);
   ```

6. 서버 → 개인키(복화화 전용 키)로 복호화

   ```C
   // 서버 내부 작업
   pre_master_secret = RSA_decrypt(server_private_key, encrypted_pms);
   ```

7. 결과적으로 브라우저와 서버 둘만 알게 되는 유일한 Session Key를 서로 갖게 됨

8. Finished 교환 (이제부터 메시지는 암호화되기 시작)

   브라우저 → 서버

   ```C
   // (유저 공간 내부 처리)
   // 이제 session_key를 알고 있으니 Finished 메시지를 암호화해서 보낼 수 있음
   send(fd, finished_from_client_encrypted, finished_client_len, 0);
   ```

   서버 → 브라우저

   ```C
   recv(fd, finished_from_server_encrypted, finished_server_len, 0);
   ```

   \* 참고

   ```C
   send(fd, buf, len, 0)
   ```

   buf: 커널로 넘길 바이트 배열의 시작 주소(pointer)<br>
   len: 그 바이트 배열의 길이(몇 바이트 보낼지)

→ TLS 세션 구축 완료 이후, 애플리케이션 데이터(HTTP 평문)는 TLS 세션키(대칭키)로 암호화되어 TLS Record의 Payload에 담겨 전송되며, 수신 측은 동일한 키로 TLS Payload를 복호화한다.

```C
[TCP Header | TCP Payload]
                    ↑
            [TLS Record Header | TLS Payload(암호화된 HTTP)]
```

#### 5. 네트워크 계층 (L3)

네트워크 계층에서는 전송 계층에서 생성된 TCP 세그먼트가 IP 헤더로 캡슐화되어 IP 패킷(IP datagram)이 되며, 이후 목적지 IP를 기준으로 라우팅되어 다음 홉으로 전달된다.

```
[IP Header | TCP Header | TCP Payload]
```

IP 헤더의 필드 중 하나인 TTL(Time To Live)은 패킷이 라우터(홉)를 한 번 거칠 때마다 1씩 감소하는 값이며,<br>
TTL이 0이 되면 해당 패킷은 폐기되어 라우팅 루프 상황에서 패킷이 네트워크 상을 무한히 순환하는 것을 방지한다.

#### 6. 데이터 링크 계층 (L2)

인터넷 통신에서 데이터는 최종 목적지까지 한 번에 전달되는 것이 아니라 여러 라우터를 거쳐 전달된다.
이때 데이터가 다음 장치로 넘어가는 전달 단계 단위를 **홉(hop)**이라고 부르며, L2(데이터 링크 계층)의 프레임 전송은 각 구간마다 독립적으로 수행된다.<br>
따라서 송신 측 애플리케이션이 어떤 서버의 IP 주소를 목적지로 설정하면, L3(네트워크 계층)는 최종 목적지를 기준으로 전달 방향을 결정하지만,<br>
실제 전송은 항상 다음 라우터에게 넘겨야 하므로 L2는 현재 라우터에서 다음 라우터 MAC(Media Access Control) 주소를 필요로 한다.<br>
예를 들어 사용자의 PC가 원격 서버로 데이터를 보낼 때, 서버는 같은 LAN에 있지 않으므로 PC는 먼저 기본 게이트웨이(라우터, 보통 공유기)로 프레임을 보내야 하고,<br>
이를 위해 PC는 ARP(Address Resolution Protocol)를 통해 게이트웨이 IP에 대응되는 게이트웨이 MAC 주소를 알아낸 뒤 이 MAC을 목적지로 하여 이더넷/와이파이 프레임을 전송한다.<br>
이후 게이트웨이는 해당 프레임을 수신하면 L2 헤더를 제거하여 그 안의 IP 패킷을 꺼내고, IP 헤더를 확인하여 다음으로 보낼 경로를 결정한 뒤, 다음 라우터(다음 홉)로 전달하기 위해 새로운 L2 프레임을 다시 생성하는데,<br>
이 과정에서 목적지 MAC 주소는 서버의 MAC이 아니라 다음 라우터의 MAC 주소로 설정되고 출발지 MAC 주소 역시 현재 송신하는 라우터 (게이트웨이)자신의 MAC 주소로 바뀐다.<br>
따라서 IP 주소(출발지/목적지)는 종단 간 통신 동안 유지되지만, MAC 주소는 각 링크 구간에서 바로 옆 장치에게 전달하기 위한 주소이므로 라우터를 지날 때마다 새로운 프레임으로 재캡슐화되면서 계속 바뀌게 된다.

\* 게이트웨이(기본 게이트웨이): 현재 LAN에서 바깥 네트워크로 나갈 때 첫 관문 역할을 하는 대상<br> \* 라우터: 네트워크 사이를 연결하고 IP 기반으로 경로를 선택해 전달하는 장비

네트워크 계층에서 만들어진 IP 패킷(IP 데이터그램)은 데이터 링크 계층에서 링크 기술에 맞는 프레임(frame)으로 캡슐화되어 물리 매체로 전송된다.<br>
데이터 링크 계층의 기술은 이더넷/와이파이뿐만 아니라 PPP/PPPoE, LTE/5G, DOCSIS, MPLS 등 각 통신 환경(유선, 무선, 통신사망 등)에 따라 매우 다양하다.<br>
다만 일반적인 LAN 환경에서 가장 대표적으로 사용되는 L2 기술은 유선 이더넷(IEEE 802.3) 과 무선 Wi-Fi(IEEE 802.11) 이다.

유선(이더넷 링크)로 보낼 때:<br>

```
[Ethernet Header | IP Header | TCP/UDP Header | TCP/UDP Payload(= Data) | FCS]
```

무선(와이파이 링크)로 보낼 때:<br>

```
[802.11 Header | IP Header | TCP/UDP Header | TCP/UDP Payload(= Data) | (무선 FCS)]
```

데이터 링크 계층(L2)은 기능적으로 LLC(Logical Link Control) 와 MAC(Media Access Control) 의 두 서브레이어로 나뉜다.

LLC는 현재 프레임의 payload가 어떤 상위 프로토콜 데이터인지를 구분하여 수신 측에서 올바른 상위 처리 모듈로 넘겨주는 논리적 분기(프로토콜 매핑/디멀티플렉싱)를 담당한다.<br>
예를 들어 이더넷 프레임에서는 보통 헤더의 EtherType 필드 값으로 payload의 종류를 식별하며, 0x0800이면 IPv4, 0x86DD이면 IPv6, 0x0806이면 ARP로 판단하여 각각 IP 처리기 또는 ARP 처리기로 전달한다.<br>

MAC은 같은 LAN 구간에서 프레임의 전달 대상을 식별하기 위해 출발지/목적지 MAC 주소를 포함한 프레임 헤더를 구성하고,<br>
유선에서는 CSMA/CD, 무선에서는 CSMA/CA 같은 매체 접근 규칙에 따라 프레임을 실제 물리 매체로 전송한다.<br>
여기서 MAC 주소는 같은 LAN 구간에서 장치를 식별하는 48비트(6바이트) 주소로서, 유선 랜카드(이더넷)뿐 아니라 무선 랜카드(와이파이)에도 MAC 주소가 존재한다.

또한 프레임의 끝에는 FCS(Frame Check Sequence)가 붙는데, 이는 보통 CRC(Cyclic Redundancy Check)로 계산된 오류 검출용 체크값이며, 수신 측은 동일한 CRC를 다시 계산하여 FCS와 비교함으로써 전송 중 비트 오류가 발생한 프레임을 검출하고, 불일치하는 프레임은 폐기한다.

오류 검출 방식에는 패리티 비트, 해밍코드 같은 방법도 존재하지만, 패리티는 검출력이 약하고(특히 다중 비트 오류에 취약),<br>
해밍코드는 오류 수정까지 가능하나 오버헤드와 구현 복잡도가 커서 통신 프레임 수준에서는 비효율적일 수 있다.<br>
반면 CRC는 비교적 적은 오버헤드로 버스트 오류에 대한 검출력이 매우 높고 하드웨어로 빠르게 처리할 수 있어, 현대 네트워크에서 링크 계층 오류 검출 방식의 표준처럼 널리 사용된다.

L2에서는 프레임의 비트 오류를 검출하여 손상된 프레임을 제거한다.<br>
그러나 이는 링크 단위의 정상 전달만 보장하며, 종단 간 정상 전달을 보장하지는 않는다.<br>
L4에서는 종단 간 세그먼트의 유실 여부를 확인하고, 누락된 세그먼트를 재전송함으로써 데이터를 복구하며, 시퀀스 번호를 기반으로 순서 재조립과 중복 제거를 수행하여 신뢰성을 제공한다.<br>
한편, L3, L4에서 오류 제어의 본질적 역할이 비트 오류 제어는 아니지만, 종단 간 무결성을 확보하기 위해 Check Sum으로 데이터그램이나 세그먼트의 비트 오류를 한 번 더 검증하며,<br>
오류가 검출되면 해당 데이터그램 또는 세그먼트를 폐기하고 재전송으로 복구한다.

#### 7. 물리 계층 (L1)

데이터 링크 계층에서 생성된 이더넷 프레임은 물리 계층에서 전기 신호(UTP), 빛 신호(광섬유), 전파(무선) 등의 형태로 변환되어 실제 전송 매체를 통해 전달된다.<br>
물리 계층은 비트(bit) 단위의 전송을 담당하며, 프레임의 의미나 주소(MAC/IP), 신뢰성/재전송과 같은 논리적 처리는 수행하지 않는다.
