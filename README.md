# groovy-dsl
The main goal of this project is to explore basic features of groovy to produce 
specific DSL.

_Reference_: [Groovy specification - core DSLs](http://docs.groovy-lang.org/docs/latest/html/documentation/core-domain-specific-languages.html)  
_Reference_: [Learning Groovy - Adam L. Davis](https://www.amazon.com/Learning-Groovy-Adam-L-Davis/dp/1484221168)  
_Reference_: [Groovy in Action](https://www.amazon.com/Groovy-Action-Covers-2-4/dp/1935182447)  
_Reference_: [DSL - Martin Fowler](https://www.amazon.com/Domain-Specific-Languages-Addison-Wesley-Signature-Fowler/dp/0321712943)  

# introduction
**Domain-Specific Languages** are small languages, focused on a particular 
aspect of a software system. They allow business experts to read or write 
code without having to be  programming experts.  
DSLs come in two main forms:
* **external** - language that's parsed independently of the host general purpose 
language, examples: `regular expressions` and `CSS`.
* **internal** - particular form of `API` in a host general purpose language, often 
referred to as a fluent interface, examples: `Spock` and `Mockito`.
___
`Groovy` has many features that make it great for writing `DSLs`:
* [Closures](http://groovy-lang.org/closures.html) with [delegates](http://groovy-lang.org/closures.html#_delegate_of_a_closure).
* Parentheses and dots `(.)` are optional.
* Ability to add methods to standard classes using [Category](http://docs.groovy-lang.org/latest/html/api/groovy/lang/Category.html) 
and [Traits](http://docs.groovy-lang.org/next/html/documentation/core-traits.html).
* The ability to [overload operators](http://docs.groovy-lang.org/docs/latest/html/documentation/core-domain-specific-languages.html#_operator_overloading).
* [Metaprogramming](http://groovy-lang.org/metaprogramming.html): `methodMissing` and 
`propertyMissing` features.

## closures with delegates
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
```
println Y.handler {setValue 'test'} // X(test)
```

## optional parentheses and dots
In `Groovy` it's possible to omit parentheses and dots
```
X.resolve {take 10 plus 30 minus 15} // it's same as: new X().take(10).plus(30).minus(15)
```
where:
```
class X {
    @Delegate
    Integer value
    
    Integer take(Integer x) {
        x
    }
    
    static def resolve(Closure closure) {
        closure.delegate = new X()
        closure()
    }
}
```
## category, mixins, traits

### category
`Groovy` categories are the mechanism to augment classes with new methods.
```
@Category(Integer)
class X {
    def reverse() {
        this.toString().reverse().replaceFirst(/^0+/,'').toInteger()
    }
}
```
```
use(X) {
    println 123000020.reverse() // 20000321
}
```
* Remarks:
    * During compilation, all methods are transformed to static ones with 
    an additional self parameter of the type you supply as the annotation 
    parameter (the default type for the self parameters is `Object` which 
    might be more broad reaching than you like so it is usually wise to 
    specify a type). 
    * Properties invoked using `'this'` references are transformed so that 
    they are instead invoked on the additional self parameter and not on 
    the `Category` instance. 
    * Remember that once the category is applied, the reverse will occur and 
    we will be back to conceptually having methods on the `this` references 
    again.

### mixins
`Groovy` mixin is a mechanism to augment classes with new methods **at runtime**.
```
class X {
    static def test(String x) {
        println "test ${x}"
    }
}
```
```
String.mixin X
'mixin'.test() // test mixin
```
* Remarks:
    * Static mixins (`@Mixin`) have been deprecated in favour of `traits`.
    * Methods are only visible at runtime.
### traits
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
```
new Y().printName() // X
```
* Remarks:
    * Methods defined in a `trait` are visible in bytecode.
    * Internally, the `trait` is represented as an interface 
    (without default methods) and several helper classes 
    this means that an object implementing a trait effectively implements 
    an interface.
    * Methods are visible from `Java` and they are compatible with 
    type checking and static compilation.

## overriding operators
```
class ComplexNumber {
    double x
    double y
	
    ComplexNumber plus(ComplexNumber other) {
        new ComplexNumber(x: this.x + other.x, y: this.y + other.y)
    }
}
```
```
ComplexNumber cn1 = new ComplexNumber(x: 1, y: 1)
ComplexNumber cn2 = new ComplexNumber(x: 2, y: 2)
ComplexNumber result = cn1 + cn2 // (3, 3)
}
```

## metaprogramming (missing methods and properties)
`Groovy` provides a way to implement functionality at runtime via the methods:
* `methodMissing(String name, args)` - invoked only in the case of a 
failed method dispatch when no method can be found for the given name and/or 
the given arguments.
* `propertyMissing(String name)` - called only when no getter method for 
the given property can be found at runtime.
* `propertyMissing(String name, Object value)` - called only when no setter
method for the given property can be found at runtime.
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
```
def x = new X()
x.nonExsistingMethod "1", "2", "3" // methodMissing: nonExsistingMethod [1, 2, 3]
x.nonExsistingProperty // propertyMissing: nonExsistingProperty
x.settingNonExsistingProperty = 5 // "propertyMissing: settingNonExsistingProperty 5"
```
# project

## project description
We provide DSL to create memos and print them in specified format.  
Memos have structure:  
* title
* deadline-date  
* any number of arbitrary named sections that have title and body

Supported formats:
* json
* text
* xml
Exemplary memo looks like:
```
shopping list
2018-06-16
food: butter, bread, meat
cleaning supplies: washing powder
```
    
## project structure
* `DeadlineMemo`, `ToDo` - entities
* `json` / `text` / `xml` packages contains appropriate converters `memo -> specified format`

## use cases
To print memo in xml format:
```
println DeadlineMemo.make {
                    title 'any title you like'
                    deadline '2018-06-16'
                    subsection-title1 'any body you like'
                    subsection-title2 'any body you like'
                    xml
                }
```
other formats: `json`, `xml`.

## tests
We provide tests for every format converter:
* DeadlineMemoJsonConverterTest
* DeadlineMemoTextConverterTest
* DeadlineMemoXmlConverterTest

And we test DSL itself as well: 
* DslTest