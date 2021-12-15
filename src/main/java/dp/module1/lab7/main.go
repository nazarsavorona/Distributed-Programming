package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

func create(size int) [][]float64 {
	matrix := make([][]float64, size)
	for i := range matrix {
		matrix[i] = make([]float64, size)
	}
	return matrix
}
func generate(size int, val int) [][]float64 {
	matrix := create(size)
	for i := range matrix {
		for j := range matrix[i] {
			matrix[i][j] = float64(val)
		}
	}
	return matrix
}
func randMatrix(size int) [][]float64 {
	matrix := create(size)
	for i := range matrix {
		for j := range matrix[i] {
			matrix[i][j] = rand.ExpFloat64()
		}
	}
	return matrix
}
func multiplyGo(matrix1 [][]float64, matrix2 [][]float64) [][]float64 {
	res := create(len(matrix1))
	var wg sync.WaitGroup
	wg.Add(len(matrix1))
	for i := range res {
		go func(index int) {
			for j := range res[index] {
				res[index][j] = 0
				for k := range matrix1[index] {
					res[index][j] += matrix1[index][k] * matrix2[k][j]
				}
			}
			wg.Done()
		}(i)
	}
	wg.Wait()
	return res
}
func multiply(matrix1 [][]float64, matrix2 [][]float64) [][]float64 {
	res := create(len(matrix1))
	for i := range res {
		for j := range res[i] {
			res[i][j] = 0
			for k := range matrix1[i] {
				res[i][j] += matrix1[i][k] * matrix2[k][j]
			}
		}
	}
	return res
}
func main() {
	size := 3000
	matrix1 := randMatrix(size)
	matrix2 := randMatrix(size)
	b := time.Now()
	multiplyGo(matrix1, matrix2)
	fmt.Printf("%fs\n", time.Since(b).Seconds())
}
