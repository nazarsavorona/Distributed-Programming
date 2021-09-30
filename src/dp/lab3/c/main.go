package main

import (
	"fmt"
	rand "math/rand"
	"time"
)

type Item int

const (
	Paper    Item = 0
	Nicotine      = 1
	Fire          = 2
)

func (item Item) String() string {
	switch item {
	case 0:
		return fmt.Sprintf("Paper")
	case 1:
		return fmt.Sprintf("Nicotine")
	case 2:
		return fmt.Sprintf("Fire")
	default:
		return ""
	}
}

func getTwoRandomItems() (Item, Item) {
	first := rand.Intn(3)
	second := rand.Intn(3)
	if first == second {
		second = (second + 1) % 3
	}

	return Item(first), Item(second)
}

func determineSmoker(ch1, ch2 chan Item, ch3 chan int) {
	for {
		if len(ch1)+len(ch2) == 0 {
			print("Manager is determining\n")
			item1, item2 := getTwoRandomItems()
			ch1 <- item1
			ch2 <- item2

			fmt.Printf("%s and %s set\n", item1, item2)

			smoker := -1

			switch item1 + item2 {
			case 1:
				smoker = 2
			case 2:
				smoker = 1
			case 3:
				smoker = 0
			}

			fmt.Printf("Expecting #%d\n", smoker)
			ch3 <- smoker

			time.Sleep(2 * time.Second)
		}
	}
}

func tryToSmoke(item Item, ch1, ch2 chan Item, ch3 chan int) {
	for {
		currentSmoker := -1

		select {
		case currentSmoker = <-ch3:
			if currentSmoker == int(item) {
				<-ch1
				<-ch2
				fmt.Printf("#%d smokes\n", currentSmoker)
			} else {
				ch3 <- currentSmoker
			}

		default:
			//println("default")
		}
		//println("smoker end")
		time.Sleep(2 * time.Second)
	}
}

func main() {
	ch1 := make(chan Item, 10)
	ch2 := make(chan Item, 10)
	ch3 := make(chan int)

	go determineSmoker(ch1, ch2, ch3)

	go tryToSmoke(Paper, ch1, ch2, ch3)
	go tryToSmoke(Nicotine, ch1, ch2, ch3)
	go tryToSmoke(Fire, ch1, ch2, ch3)

	for {

	}
}
