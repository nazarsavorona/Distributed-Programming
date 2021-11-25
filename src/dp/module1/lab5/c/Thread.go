package main

import (
	"fmt"
	"math/rand"
)

var LIMIT = 15

type Thread struct {
	array      []int
	currentSum chan int
}

func NewThread(size int) *Thread {
	array := make([]int, size)

	for i := 0; i < size; i++ {
		array[i] = rand.Intn(int(LIMIT))
	}

	toReturn := &Thread{array, make(chan int, 1)}
	toReturn.currentSum <- 0
	toReturn.evaluateSum()

	return toReturn
}

func (thread *Thread) getCurrentSum() int {
	sum := <-thread.currentSum
	thread.currentSum <- sum
	return sum
}

func (thread *Thread) evaluateSum() {
	sum := 0
	for _, currentItem := range thread.array {
		sum += currentItem
	}

	if len(thread.currentSum) == 1 {
		<-thread.currentSum
	}
	thread.currentSum <- sum
}

func (thread *Thread) arrayModification() {
	index := rand.Intn(len(thread.array))

	if rand.Intn(2) == 0 {
		thread.array[index] = (thread.array[index] - 1) % int(LIMIT)
	} else {
		thread.array[index] = (thread.array[index] + 1) % int(LIMIT)
	}

	thread.evaluateSum()
}

func (thread *Thread) print() {
	sum := thread.getCurrentSum()
	fmt.Printf("Sum: %d\nElements: %v\n", sum, thread.array)
}
