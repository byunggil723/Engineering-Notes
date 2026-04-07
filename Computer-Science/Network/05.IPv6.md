# IPv6

### IPv6 Header

```
IPv6 Header (fixed 40B)

0                   15 16                   31
+---------------------+----------------------+
| Version | Traffic Class | Flow Label       |
+--------------------------------------------+
| Payload Length      | Next Header | HopLim |
+--------------------------------------------+
| Source Address (128 bits)                  |
+--------------------------------------------+
| Destination Address (128 bits)             |
+--------------------------------------------+
| Extension Headers (optional, chained) ...  |
+--------------------------------------------+
| Data (payload)                             |
+--------------------------------------------+

```
