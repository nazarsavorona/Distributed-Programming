package main

import (
	rand "math/rand"
	"sync"
	"time"
)

/*
	Банк.
	Имеется банк с кассирами, клиентами и их счетами. Клиент может
	снимать/пополнять/переводить/оплачивать/обменивать денежные
	средства. Кассир последовательно обслуживает клиентов. Поток-наблюдатель
	следит, чтобы в кассах всегда были наличные, при скоплении денег более
	определенной суммы, часть их переводится в хранилище, при истощении
	запасов наличных происходит пополнение из хранилища.
*/

type Currency int

const (
	UAH Currency = iota
	USD
	EUR
)

type Account struct {
	money    int
	currency Currency
}

func NewAccount(money int, currency Currency) *Account {
	return &Account{money, currency}
}

func (account *Account) getMoney(money int, currency Currency) bool {
	if account.currency != currency {
		return false
	}

	if account.money > money {
		account.money -= money
		return true
	}

	return false
}
func (account *Account) addMoney(money int, currency Currency) bool {
	if account.currency != currency {
		return false
	}

	account.money += money
	return true
}

type Client struct {
	account []Account
}

func NewClient() *Client {
	return &Client{}
}

func (client *Client) addAccount(account Account) {
	client.account = append(client.account, account)
}

func (client *Client) getMoney(cashier Cashier, money int, accountIndex int) bool {
	return cashier.getMoney(money, client.account[accountIndex])
}

func (client *Client) addMoney(cashier Cashier, money int, accountIndex int) bool {
	return cashier.addMoney(money, client.account[accountIndex])
}

func (client *Client) operate(cashier *Cashier) {
	for {
		switch rand.Intn(2) {
		case 0:
			cashier.getMoney(rand.Intn(500), client.account[rand.Intn(len(client.account))])
		case 1:
			cashier.addMoney(rand.Intn(500), client.account[rand.Intn(len(client.account))])
		case 2:
			client.addAccount(*NewAccount(rand.Intn(700), Currency(rand.Intn(2))))
		}
	}
}

type Cashier struct {
	cash     int
	currency Currency

	mutex sync.Mutex
}

func (cashier *Cashier) getCash() int {
	return cashier.cash
}

func (cashier *Cashier) addCash(cash int) {
	cashier.mutex.Lock()
	cashier.cash += cash
	cashier.mutex.Unlock()
}

func (cashier *Cashier) getMoney(money int, account Account) bool {
	cashier.mutex.Lock()
	if account.getMoney(money, cashier.currency) {
		cashier.mutex.Unlock()
		return true
	}

	cashier.mutex.Unlock()
	return false
}

func (cashier *Cashier) addMoney(money int, account Account) bool {
	cashier.mutex.Lock()
	if account.addMoney(money, cashier.currency) {
		cashier.mutex.Unlock()
		return true
	}

	cashier.mutex.Unlock()
	return false
}

func NewCashier(money int, currency Currency) *Cashier {
	return &Cashier{money, currency, sync.Mutex{}}
}

type Manager struct {
	cashier *Cashier
}

func NewManager(cashier *Cashier) *Manager {
	return &Manager{cashier}
}

func (manager *Manager) manageCashier() {
	for {
		if manager.cashier.getCash() < 5000 {
			manager.cashier.addCash(15000)
		}
	}
}

func main() {
	clientsCount := 10
	cashier := NewCashier(rand.Intn(10000), UAH)
	manager := NewManager(cashier)

	clients := make([]Client, clientsCount)
	for i := range clients {
		go clients[i].operate(cashier)
	}

	go manager.manageCashier()

	time.Sleep(10 * time.Second)
}
