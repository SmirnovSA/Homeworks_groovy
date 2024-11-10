package org.example

def builder = new XlsxBuilder("test.xlsx")
builder.sheet(0) {
    row (0){
        cell {
            value  'Hello!'
            style {
                font "blue"
                color"brown"
            }
        }
    }
    row (1){
        cell {
            value'Hello, Sergey! How are You?'
            style {
                font "pink"
                color"yellow"
                backgroundColor("green")
            }
        }
    }
}
