# IPv4

### IPv4 Header

```
IPv4 Header (min 20B)

0                   15 16                   31
+---------------------+----------------------+
| Version | IHL       | Type of Service      |
+---------------------+----------------------+
| Total Length                               |
+--------------------------------------------+
| Identification      | Flags | Frag Offset  |
+--------------------------------------------+
| TTL                 | Protocol             |
+---------------------+----------------------+
| Header Checksum                            |
+--------------------------------------------+
| Source Address                             |
+--------------------------------------------+
| Destination Address                        |
+--------------------------------------------+
| Options (if any) ... (padding)             |
+--------------------------------------------+
| Data (payload)                             |
+--------------------------------------------+

```

### IP 단편화(IP Fragmentation)

TCP는 송신 측에서 로컬 링크 MTU(Maximum Transmission Unit, 한 링크에서 프레임 payload로 실을 수 있는 최대 크기, 단위: byte)를<br>
기준으로 산출한 MSS(Maximum Segment Size)에 맞춰 세그먼트를 구성해 IP 계층에 인계한다.<br>
그러나 실제 전송 중 Path MTU가 더 작은 구간이 존재하면, 해당 IP datagram 은 다음 홉에서 그대로 전달될 수 없으므로,<br>
IPv4 라우터가 IP 단편화(IP fragmentation)를 수행하여 MTU에 맞는 여러 fragment로 분할 전송한다.

반면, IPv6는 원칙적으로 단편화를 수행하지 않는다.
라우터가 단편화를 수행할 경우 패킷을 분할·관리하는 추가 연산과 상태 처리가 필요해 처리 지연과 구현 복잡도가 증가하고,<br>
fragment 중 하나라도 유실되면 원본 패킷 전체를 재조립할 수 없어 전송 효율과 신뢰성이 급격히 떨어진다.<br>
따라서 IPv6는 라우터에 오버헤드로 부담을 주지 않고, 고속 포워딩(fast forwarding)에 집중할 수 있도록 설계되었다.<br>
IPv6에서 어떤 패킷이 MSS를 넘게 되면, 라우터는 해당 패킷을 드롭한 뒤 "ICMPv6 Packet Too Big" 메시지를 송신자에게 통지하여<br>
송신자가 Path MTU에 맞게 패킷 크기를 재조정하여 전송하도록 설계되어 있다.

\* 로컬 링크: 내 PC(호스트) ↔ 기본 게이트웨이(첫 라우터) 사이의 링크<br> \* $MSS = MTU − (IPv4 Header+TCP Header)$

### 서브넷 마스크(Subnet Mask)
