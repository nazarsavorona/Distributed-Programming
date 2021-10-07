package main

import (
	"fmt"
	"sync"
)

type SafeGraph struct {
	container Graph
	mutex     sync.Mutex
}

func (graph *SafeGraph) changeRoutePrice(firstCity, secondCity string, newPrice int) {
	graph.mutex.Lock()
	graph.container.changePrice(firstCity, secondCity, newPrice)
	graph.mutex.Unlock()
}

func (graph *SafeGraph) addRoute(firstCity, secondCity string, price int) {
	graph.mutex.Lock()
	graph.container.addEdge(firstCity, secondCity, price)
	graph.mutex.Unlock()
}

func (graph *SafeGraph) deleteRoute(firstCity, secondCity string) {
	graph.mutex.Lock()
	graph.container.deleteEdge(firstCity, secondCity)
	graph.mutex.Unlock()
}

func (graph *SafeGraph) addCity(city string) {
	graph.mutex.Lock()
	graph.container.addVertex(city)
	graph.mutex.Unlock()
}

func (graph *SafeGraph) deleteCity(city string) {
	graph.mutex.Lock()
	graph.container.deleteVertex(city)
	graph.mutex.Unlock()
}

func (graph *SafeGraph) getPathCost(firstCity, secondCity string) {
	graph.mutex.Lock()
	exists, cost := graph.container.getPathCost(firstCity, secondCity)
	graph.mutex.Unlock()

	if exists {
		fmt.Printf("\"%s\" -> \"%s\" route cost is %d\n", firstCity, secondCity, cost)
	} else {
		fmt.Printf("There is no route between \"%s\" and \"%s\"\n", firstCity, secondCity)
	}
}

func (graph *SafeGraph) printGraphviz(name string) {
	graph.mutex.Lock()
	fmt.Println(graph.container.getGraphvizInfo(name))
	graph.mutex.Unlock()
}
