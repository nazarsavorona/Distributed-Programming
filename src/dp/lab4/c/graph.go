package main

import (
	"fmt"
	"strings"
)

type Status int

const (
	Visited Status = iota
	Unvisited
	Pending
)

type Vertex struct {
	city     string
	adjacent []*Vertex
	price    []int
}

func (vertex *Vertex) getGraphvizInfo() string {
	info := fmt.Sprintf("\"%s\"\n", vertex.city)
	for index, adjacentVertex := range vertex.adjacent {
		info += fmt.Sprintf("\"%s\" -> \"%s\" [ label = \"%d\" ]\n",
			vertex.city, adjacentVertex.city, vertex.price[index])
	}

	return info
}

func (vertex *Vertex) changeRoutePrice(anotherCity string, newPrice int) {
	for index, adjacentVertex := range vertex.adjacent {
		if adjacentVertex.city == anotherCity {
			vertex.price[index] = newPrice
			return
		}
	}

	fmt.Printf("There's no route between \"%s\" and \"%s\"\n", vertex.city, anotherCity)
}

func (vertex *Vertex) deleteRoute(anotherCity string) {
	for index, adjacentVertex := range vertex.adjacent {
		if adjacentVertex.city == anotherCity {
			vertex.adjacent = append(vertex.adjacent[:index], vertex.adjacent[index+1:]...)
			vertex.price = append(vertex.price[:index], vertex.price[index+1:]...)
			return
		}
	}

	fmt.Printf("There's no route between \"%s\" and \"%s\"\n", vertex.city, anotherCity)
}

func (vertex *Vertex) deleteAllAdjacents() {
	for _, adjacentVertex := range vertex.adjacent {
		vertex.deleteRoute(adjacentVertex.city)
		adjacentVertex.deleteRoute(vertex.city)
	}
}

func (vertex *Vertex) dfs(statuses map[string]Status, previous map[string]string) {
	statuses[vertex.city] = Pending

	for _, adjacentVertex := range vertex.adjacent {
		if statuses[adjacentVertex.city] == Unvisited {
			previous[adjacentVertex.city] = vertex.city
			adjacentVertex.dfs(statuses, previous)
		}
	}

	statuses[vertex.city] = Visited
}

type Graph struct {
	vertices []*Vertex
}

func (graph *Graph) getVertex(city string) *Vertex {
	for _, vertex := range graph.vertices {
		if vertex.city == city {
			return vertex
		}
	}
	return nil
}

func (graph *Graph) addVertex(city string) {
	if graph.exists(city) {
		fmt.Printf("City \"%s\" already exists in a graph\n", city)
		return
	}

	graph.vertices = append(graph.vertices,
		&Vertex{
			city: city, adjacent: []*Vertex{}, price: []int{},
		})
}

func (graph *Graph) deleteVertex(city string) {
	if !graph.exists(city) {
		fmt.Printf("City \"%s\" doesn`t exist in a graph\n", city)
	}

	for index, vertex := range graph.vertices {
		if vertex.city == city {
			vertex.deleteAllAdjacents()
			graph.vertices = append(graph.vertices[:index], graph.vertices[index+1:]...)
		}
	}
}

func (graph *Graph) exists(city string) bool {
	return graph.getVertex(city) != nil
}

func (graph *Graph) deleteEdge(firstCity, secondCity string) {
	if firstCity == secondCity {
		fmt.Printf("There's no need for route firstCity \"%s\" secondCity \"%s\"\n", firstCity, secondCity)
		return
	}

	fromVertex := graph.getVertex(firstCity)
	toVertex := graph.getVertex(secondCity)

	if fromVertex == nil || toVertex == nil {
		fmt.Println("Incorrect city name occurred")
		return
	}

	fromVertex.deleteRoute(secondCity)
	toVertex.deleteRoute(firstCity)
}

func (graph *Graph) addEdge(from, to string, price int) {
	if from == to {
		fmt.Printf("There's no need for route from \"%s\" to \"%s\"\n", from, to)
		return
	}

	fromVertex := graph.getVertex(from)
	toVertex := graph.getVertex(to)

	if fromVertex == nil || toVertex == nil {
		fmt.Println("Incorrect city name occurred")
		return
	}

	fromVertex.adjacent = append(fromVertex.adjacent, toVertex)
	fromVertex.price = append(fromVertex.price, price)

	toVertex.adjacent = append(toVertex.adjacent, fromVertex)
	toVertex.price = append(toVertex.price, price)
}

func (graph *Graph) changePrice(firstCity, secondCity string, newPrice int) {
	if !graph.exists(firstCity) || !graph.exists(secondCity) {
		fmt.Printf("There is no route between \"%s \" and \"%s \"\n", firstCity, secondCity)
		return
	}

	firstVertex := graph.getVertex(firstCity)
	secondVertex := graph.getVertex(secondCity)

	firstVertex.changeRoutePrice(secondCity, newPrice)
	secondVertex.changeRoutePrice(firstCity, newPrice)
}

func (graph *Graph) dfs(firstCity string) map[string]string {
	statuses := make(map[string]Status)
	previous := make(map[string]string)

	for _, vertex := range graph.vertices {
		statuses[vertex.city] = Status(Unvisited)
		previous[vertex.city] = ""
	}

	initialVertex := graph.getVertex(firstCity)

	initialVertex.dfs(statuses, previous)

	for _, vertex := range graph.vertices {
		if statuses[vertex.city] == Unvisited {
			vertex.dfs(statuses, previous)
		}
	}

	return previous
}

func (graph *Graph) getAdjacentPathCost(firstCity, secondCity string) int {
	firstVertex := graph.getVertex(firstCity)

	for index, adjacentVertex := range firstVertex.adjacent {
		if adjacentVertex.city == secondCity {
			return firstVertex.price[index]
		}
	}

	panic("Vertices are not adjacent")
}

func (graph *Graph) getPathCost(firstCity, secondCity string) (bool, int) {
	price := 0
	previous := graph.dfs(firstCity)

	previousCity := secondCity
	currentCity := secondCity

	for {
		previousCity = currentCity
		currentCity = previous[currentCity]

		if currentCity == "" {
			return false, -1
		}

		price += graph.getAdjacentPathCost(previousCity, currentCity)

		if currentCity == firstCity {
			return true, price
		}
	}
}

func (graph *Graph) getGraphvizInfo(name string) string {
	name = strings.ReplaceAll(name, " ", "_")

	graphString := fmt.Sprintf("digraph %s {\n", name)

	for _, vertex := range graph.vertices {
		graphString += vertex.getGraphvizInfo()
	}

	graphString += "}"

	return graphString
}
