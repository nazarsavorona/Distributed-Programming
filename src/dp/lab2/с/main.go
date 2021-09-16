package main

import (
	"fmt"
	"log"
	"math/rand"
	"time"
)

const MonkQuantity = 5e5

type Monk struct {
	temple string
	energy int
}

func (m *Monk) decreaseEnergy(energy int) {
	m.energy -= energy
}

func fight(m1 Monk, m2 Monk) Monk {
	if m1.temple == m2.temple {
		if m1.energy >= m2.energy {
			return m1
		}
		return m2
	}
	if m1.energy >= m2.energy {
		m1.decreaseEnergy(m2.energy)
		return m1
	}

	m2.decreaseEnergy(m1.energy)
	return m2
}

func tournament(temple1 []Monk, temple2 []Monk, out chan<- Monk) {
	if len(temple1) == 0 {
		return
	}

	if len(temple1) == 1 {
		out <- fight(temple1[0], temple2[0])
		return
	}

	middle := len(temple1) / 2
	c1 := make(chan Monk, 1)
	c2 := make(chan Monk, 1)
	go tournament(temple1[:middle], temple2[:middle], c1)
	go tournament(temple1[middle:], temple2[middle:], c2)
	winner := fight(<-c1, <-c2)
	//close(c1)
	//close(c2)
	out <- winner
}

func initTemple(templeName string) []Monk {
	temple := []Monk{}
	for i := 0; i < MonkQuantity; i++ {
		tmp := Monk{temple: templeName, energy: rand.Intn(1e3)}
		temple = append(temple, tmp)
	}
	return temple
}

func startTournament() Monk {
	temple1 := initTemple("first")
	temple2 := initTemple("second")
	winMonk := make(chan Monk, 1)
	tournament(temple1, temple2, winMonk)
	winner := <-winMonk
	close(winMonk)
	return winner
}

func main() {
	startTimer := time.Now()
	winner := startTournament()
	fmt.Println(winner.temple + " wins!")
	log.Printf("\nIt took %s", time.Since(startTimer))
}
