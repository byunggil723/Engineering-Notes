#### Browser

```
0ms                                                                                         16.67ms
|---------------------------------------------------------------------------------------------|
[ scanout ...................................................................... ][   VBlank  ]
[ JS Task ][ Microtask ][ rAF ][ Style ][ Layout ][ Paint ][Composite][GPU Submit][buffer Swap]
|--------- Event Loop --------||-------------- Rendering Pipeline ---------------|
                                                                                 ↑
                                                                         (vsync 이벤트 발생)
```

#### Mobile (React Native)

```
0ms                                                                                         16.67ms
|---------------------------------------------------------------------------------------------|
[ scanout ...................................................................... ][   VBlank  ]
(JS Thread)
[ JS Task ][ Microtask ]
|----- Event Loop -----|

                     Commit (Bridge / Fabric)
                        ↓

                        (Native UI Thread)
                        [ Layout (Yoga) ][ View Update ][  Draw  ][  GPU Submit  ][buffer swap]
                                                                                 ↑
                                                                         (vsync 이벤트 발생)
```