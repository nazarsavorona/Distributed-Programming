package main

import (
	"time"
)

func changePriceThread(citiesGraph *SafeGraph) {
	citiesGraph.changeRoutePrice("Kyiv", "Lviv", 200)
	citiesGraph.changeRoutePrice("Herson", "Lviv", 500)
	citiesGraph.changeRoutePrice("Kyiv", "Zhytomyr", 300)
	citiesGraph.changeRoutePrice("Bila Tserkva", "Lviv", 385)
}

func addDeleteRoutesThread(citiesGraph *SafeGraph) {
	citiesGraph.addRoute("Herson", "Kharkiv", 1000)
	citiesGraph.addRoute("Herson", "Kyiv", 750)
	citiesGraph.addRoute("Herson", "Bila Tserkva", 1500)
	citiesGraph.deleteRoute("Herson", "Kyiv")
	citiesGraph.deleteRoute("Ukrainka", "Kyiv")
	citiesGraph.addRoute("Ukrainka", "Obukhiv", 750)
	citiesGraph.deleteRoute("Herson", "Bila Tserkva")
	citiesGraph.deleteRoute("Lviv", "Kharkiv")

}

func addDeleteCitiesThread(citiesGraph *SafeGraph) {
	citiesGraph.addCity("IvanoNefrankivsk")
	citiesGraph.addCity("Varash")
	citiesGraph.addCity("Lugansk")
	citiesGraph.deleteCity("Rivne")
	citiesGraph.addCity("Obukhiv")
	citiesGraph.deleteCity("Kramatorsk")
	citiesGraph.addCity("Zhmerynka")
	citiesGraph.addCity("Rivne")
	citiesGraph.deleteCity("Lviv")
	citiesGraph.addCity("Yalta")
	citiesGraph.deleteCity("Boryspil")
	citiesGraph.deleteCity("Zhmerynka")
}

func routeFinderThread(citiesGraph *SafeGraph) {
	citiesGraph.getPathCost("Kyiv", "Ukrainka")
	citiesGraph.getPathCost("Herson", "Lviv")
	citiesGraph.getPathCost("Bila Tserkva", "Lviv")
	citiesGraph.getPathCost("Herson", "Kharkiv")
	citiesGraph.getPathCost("Kyiv", "Obukhiv")
	citiesGraph.getPathCost("Zhytomyr", "Lviv")
}

func main() {
	citiesGraph := &SafeGraph{}

	citiesGraph.addCity("Kyiv")
	citiesGraph.addCity("Ukrainka")
	citiesGraph.addCity("Lviv")
	citiesGraph.addCity("Kharkiv")
	citiesGraph.addCity("Herson")
	citiesGraph.addCity("Bila Tserkva")
	citiesGraph.addCity("Zhytomyr")
	citiesGraph.addCity("IvanoFrankivsk")
	citiesGraph.addCity("Kramatorsk")
	citiesGraph.addCity("Lugansk")
	citiesGraph.addCity("Yalta")
	citiesGraph.addCity("Obukhiv")
	citiesGraph.addCity("Boryspil")

	citiesGraph.addRoute("Kyiv", "Lviv", 150)
	citiesGraph.addRoute("Herson", "Lviv", 550)
	citiesGraph.addRoute("Kyiv", "Zhytomyr", 400)
	citiesGraph.addRoute("Bila Tserkva", "Lviv", 370)
	citiesGraph.addRoute("Ukrainka", "Kyiv", 35)
	citiesGraph.addRoute("Zhytomyr", "Lviv", 600)
	citiesGraph.addRoute("Herson", "Kharkiv", 1000)
	citiesGraph.addRoute("Herson", "Kyiv", 750)
	citiesGraph.addRoute("Kharkiv", "Lviv", 754)
	citiesGraph.addRoute("Kyiv", "Obukhiv", 35)

	citiesGraph.printGraphviz("Initial")

	go changePriceThread(citiesGraph)
	go addDeleteRoutesThread(citiesGraph)
	go addDeleteCitiesThread(citiesGraph)
	go routeFinderThread(citiesGraph)

	time.Sleep(3 * time.Second)
	citiesGraph.printGraphviz("Final")
}
