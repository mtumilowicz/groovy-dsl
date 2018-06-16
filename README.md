# groovy-dsl
_Reference_: [core DSL from groovy specification](http://docs.groovy-lang.org/docs/latest/html/documentation/core-domain-specific-languages.html)  
_Reference_: [Learning Groovy - Adam L. Davis](https://www.amazon.com/Learning-Groovy-Adam-L-Davis/dp/1484221168)  
_Reference_: [Groovy in Action](https://www.amazon.com/Groovy-Action-Covers-2-4/dp/1935182447)  
_Reference_: [DSL - Martin Fowler](https://www.amazon.com/Domain-Specific-Languages-Addison-Wesley-Signature-Fowler/dp/0321712943)  

# Introduction
`DSLs` (Domain-Specific Languages) are small languages, focused on a particular 
aspect of a software system. They allow business experts to read or write 
code without having to be  programming experts.  
DSLs come in two main forms: external and internal:
* **external** - language that's parsed independently of the host general purpose 
language: good examples include regular expressions and CSS.
* **internal** - particular form of API in a host general purpose language, often 
referred to as a fluent interface. Spock is a good example.


`Groovy` has many features that make it great for writing `DSLs`:
* [Closures](http://groovy-lang.org/closures.html) with [delegates](http://groovy-lang.org/closures.html#_delegate_of_a_closure).
* Parentheses and dots `(.)` are optional.
* Ability to add methods to standard classes using [Category](http://docs.groovy-lang.org/latest/html/api/groovy/lang/Category.html) 
and [Traits](http://docs.groovy-lang.org/next/html/documentation/core-traits.html).
* The ability to [overload operators](http://docs.groovy-lang.org/docs/latest/html/documentation/core-domain-specific-languages.html#_operator_overloading)
* [Metaprogramming](http://groovy-lang.org/metaprogramming.html): `methodMissing` and 
`propertyMissing` methods.

## Closured with delegates
Within `Groovy` you can take a closure as a parameter and then call it using a 
local variable as a delegate.
```
@ToString
class X {
    String value
}

class Y {
    static def handler(closure) {
        X x = new X()
        closure.delegate = x
        closure()
        x
    }
}
```
and calling:
```
println Y.handler {setValue 'test'} // X(test)
```

## Optional parentheses and dots
```
class XXX {
    @Delegate
    Integer value
    
    Integer take(Integer x) {
        x
    }
    
    static def resolve(Closure closure) {
        closure.delegate = new XXX()
        closure()
    }
}
```
and calling:
```
println XXX.resolve {take 10 plus 30 plus 20} // 60
```
}

## Category, Mixins, Traits

### Category
```
@Category(Integer)
class X {
    def reverse() {
        this.toString().reverse().replaceFirst(/^0+/,'').toInteger()
    }
}
```
and calling:
```
use(X) {
    println 123000020.reverse() // 20000321
}
```

### Mixins
```
class X {
    static def test(String x) {
        println "test ${x}"
    }
}
```
and calling:
```
    String.mixin X
    'print test'.test() // print test
}
```
Note that static mixins (`@Mixin`) have been deprecated in favour of `traits`.

### Traits
`Traits` can be seen as interfaces carrying both default implementations 
and state.

```
class Y implements X {
    @Override
    def name() {
        return name
    }
}

trait X {
    def name = "X"
    
    abstract def name()
    def printName() {
        println "test ${name()}"
    }
}
```
and calling:
```
new Y().printName() // X
```
}

## Overriding Operators
```
class ComplexNumber {
    double x
    double y
	
    ComplexNumber plus(ComplexNumber other) {
        new ComplexNumber(x: this.x + other.x, y: this.y + other.y)
    }
}
```
and calling:
```
    ComplexNumber cn1 = new ComplexNumber(x: 1, y: 1)
    ComplexNumber cn2 = new ComplexNumber(x: 2, y: 2)
    ComplexNumber result = cn1 + cn2 // (3, 3)
}
```

## Metaprogramming (Missing Methods and Properties)
`Groovy` provides a way to implement functionality at runtime via the methods:
* methodMissing
* propertyMissing(String name)
* propertyMissing(String name, Object value)
```
class X {
    def methodMissing(String name, args) {
        println "methodMissing: $name $args"
    }

    def propertyMissing(String name, Object value) {
        println "propertyMissing: $name $value"
    }

    def propertyMissing(String name) {
        println "propertyMissing: $name"
    }
}
```
and calling:
```
def x = new X()
x.nonExsistingMethod "1", "2", "3" // methodMissing: nonExsistingMethod [1, 2, 3]
x.nonExsistingProperty // propertyMissing: nonExsistingProperty
x.settingNonExsistingProperty = 5 // "propertyMissing: settingNonExsistingProperty 5"
```
}