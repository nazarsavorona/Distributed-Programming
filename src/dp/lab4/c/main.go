package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	graph := &SafeGraph{}

	for {
		arguments := []string{}
		scanner := bufio.NewScanner(os.Stdin)
		scanner.Scan()

		for _, word := range strings.Split(strings.Trim(scanner.Text(), " "), " ") {
			arguments = append(arguments, word)
		}

		if err := scanner.Err(); err != nil {
			fmt.Fprintln(os.Stderr, "reading standard input:", err)
		}

		switch arguments[0] {
		case "ADD_CITY":
			go graph.addCity(arguments[1])
		case "DELETE_CITY":
			go graph.deleteCity(arguments[1])
		case "ADD_ROUTE":
			price, _ := strconv.Atoi(arguments[3])
			go graph.addRoute(arguments[1], arguments[2], price)
		case "CHANGE_PRICE":
			price, _ := strconv.Atoi(arguments[3])
			go graph.changeRoutePrice(arguments[1], arguments[2], price)
		case "GET_PATH":
			go graph.getPathCost(arguments[1], arguments[2])
		case "PRINT":
			go graph.printGraphviz("graph")
		default:
			println("Unknown command")
		}
	}
}
