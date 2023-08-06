### build graph 
- sarrland 
```
Result "com.routerunner.BenchGraphBuilder.buildSaarlandGraph":
  2.079 ±(99.9%) 1.124 s/op [Average]
  (min, avg, max) = (2.026, 2.079, 2.147), stdev = 0.062
  CI (99.9%): [0.955, 3.204] (assumes normal distribution)
```
- baden-wuerttemberg.osm 
```
Result "com.routerunner.BenchGraphBuilder.buildBadenWuGraph":
  43.067 ±(99.9%) 97.569 s/op [Average]
  (min, avg, max) = (39.073, 43.067, 49.143), stdev = 5.348
  CI (99.9%): [≈ 0, 140.636] (assumes normal distribution)
```

### LLC  
reduction to largest connected component 
```
Benchmark                 (city)  Mode  Cnt  Score   Error  Units
BenchLLC.LLC  baden-wuerttemberg  avgt   10  0.560 ± 0.246   s/op
BenchLLC.LLC            saarland  avgt   10  0.029 ± 0.002   s/op
```

### Dijkstra 

#### Avg query time 

```
Benchmark                           (city)  Mode  Cnt    Score    Error  Units
BenchDijkstra.Dijkstra  baden-wuerttemberg  avgt   20  143.509 ± 10.980  ms/op
BenchDijkstra.Dijkstra            saarland  avgt   20    7.686 ±  0.265  ms/op
```

``` 
avg cost in seconds         : 1280.500000
avg number of settled nodes : 111409
```

```
avg cost in seconds         : 6256.900000
avg number of settled nodes : 1705646
```

### 