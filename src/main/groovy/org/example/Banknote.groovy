package org.example

class Banknote {
    BigDecimal nominal
    String currency

    Banknote(BigDecimal nominal, String currency) {
        this.nominal = nominal
        this.currency = currency
    }

    @Override
    String toString() {
        return "${nominal} ${currency}"
    }
}
