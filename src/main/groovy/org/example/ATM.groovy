package org.example

class ATM {
    // ячейки для банкнот разных номиналов
    Map<Banknote, Integer> banknotes = [:]

    // метод для добавления банкнот в ячейки
    // если указали сумму, а банкнот не внесли - ошибка
    void addMoney(Banknote banknote, int count) {
        if(count <= 0){
            throw new IllegalArgumentException("Банкноты отсутствуют")
        }
        banknotes[banknote] = banknotes.getOrDefault(banknote,0) + count
    }

    //метод выдачи банкнот
    void withdraw(BigDecimal amount,String currency){

        Map<Banknote, Integer> withdrawal = [:]
        BigDecimal remainingAmount = amount

        if(amount <= 0){
            throw new IllegalArgumentException("Сумма не внесена")
        }
        if (!isAmountAvailable(amount,currency)){
            def out = new Banknote(amount,currency).toString()
            println "Недосточно средств для выдачи $out"
        }

        banknotes.keySet().findAll {it.currency == currency}.sort{-it.nominal}.each {
            int availableQuantity = banknotes[it]
            int needQuantity = (remainingAmount / it.nominal).intValue()
            int withdrawQuantity = Math.min(needQuantity,availableQuantity)

            if (withdrawQuantity > 0){
                withdrawal[it] = withdrawQuantity
                remainingAmount -= it.nominal * withdrawQuantity
            }
        }
        if (remainingAmount > 0) {
            def availableSum = banknotes.findAll { it.key.currency == currency }
                    .collect { it.key.nominal * it.value }
                    .sum() as BigDecimal
            def outCurrency = new Banknote(availableSum,currency).toString()
            println "Не удалось выдать запрошенную сумму. Баланс счета: $outCurrency."
        } else {
            withdrawal.each {nominal,quantity ->
                banknotes[nominal] -= quantity
            }
            println "Выдано:"
            withdrawal.each { banknote, quantity ->
                println "${quantity} x ${banknote}"
            }
        }

    }

    // метод проверки доступности нужной валюты в банкомате
    boolean isAmountAvailable(BigDecimal amount, String currency) {
        BigDecimal totalAvailable = banknotes.findAll { it.key.currency == currency }
                .collect { it.key.nominal * it.value }
                .sum() as BigDecimal
        return amount <= totalAvailable
    }

    // проверка баланса и вывод доступных средств
    void getBalance(){
        println "Доступные банкноты"
        banknotes.collect {banknote,quantity ->
            println "$banknote : $quantity"
        }
    }
}
