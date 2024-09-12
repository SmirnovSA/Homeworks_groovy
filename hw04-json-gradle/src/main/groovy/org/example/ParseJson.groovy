package org.example

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

def url = new URL('https://raw.githubusercontent.com/Groovy-Developer/groovy-homeworks/main/hw-5/test.json')

def parser = new JsonSlurper()
def builder = new MarkupBuilder()

def json = parser.parse(url)

builder.div{
    div(id:"employee") {
        p json.get('name')
        br()
        p json.get('age')
        br()
        p json.get('secretIdentity')
        br()
        ul(id: "powers") {
            json.get('powers').each {
                li it
            }

        }
    }
}

builder.employee{
    name json.get('name')
    age json.get('age')
    secretIdentity json.get('secretIdentity')
    powers{
        json.get('powers').each {
            power it
        }
    }
}