## kivakit-core-kernel data.validation &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/checkmark-40.png)
![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Package &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/box-24.png)

*com.telenav.kivakit.core.kernel.data.validation*

### Index

[**Summary**](#summary)  
[**Class Diagrams**](#class-diagrams)  
[**Validators**](#validators)  
[**Validatable**](#validatable)  
[**Ensure**](#ensure)

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)

### Summary <a name="summary"></a>

The data validation package provides a mini-framework for checking the correctness and consistency  
of objects.

### Class Diagrams <a name="class-diagrams"></a> &nbsp; &nbsp; ![](https://telenav.github.io/telenav-assets/images/icons/diagram-48.png)

[**Data Validation**](diagrams/diagram-data-validation.svg)

### Validators <a name="validators"></a>

As is clear from the UML diagram, the central interface of the '*data.validation*' package is *Validator*:

    public interface Validator
    {
        boolean validate(Listener listener);
    }

The '*validate()*' method performs any necessary checks, reporting *Warning*s and *Problem*s to the   
listener, and returns true if no issues were encountered. Validation issues can easily be captured  
and analyzed with the *ValidationIssues* listener.

### Validatable <a name="validatable"></a>

A *Validatable* object can produce a *Validator* for a given type of *Validation*:

    public interface Validatable
    {
        Validator validator(Validation type);
    }

    public class Validation extends Name
    {
        public Validation(String name)
        public Validation exclude(Class<?> type)
        public Validation include(Class<?> type)
        public boolean shouldValidate(Class<?> type)
    }

The *Validation* class allows for different *kinds* of *Validator*s to be offered by a *Validatable* object.  
For example, the kind of validation done on a *User* object might vary between a use case where  
*User* objects are being sampled from a file by a statistical application, and a use case where they  
are being stored in a database. Objects that are encountered during validation can be included or    
excluded from consideration with *Validation.include(Class)* and *Validation.exclude(Class)*.

### Ensure <a name="ensure"></a>

The *Ensure* class provides a variety of assertion-like checks. Issues that arise in these checks  
are reported by pluggable *ValidationReporter*(s). Different reporters can be installed for different  
classes of problems:

    public static void reporter(Class<? extends Message> type, ValidationReporter reporter)

or, they can be created as-needed by a factory using this method:

    public static void reporterFactory(MapFactory<Class<? extends Message>, ValidationReporter> factory)

The *ValidationReporter* interface is a trivial extension of *Listener* which allows for easy reporting  
of validation issues through *ValidationReporter.report(Message)*:

    public interface ValidationReporter extends Listener
    {
        void report(Message message)
    }

The *BaseValidationReporter* class provides the connection between *Listener.onMessage()*, and the  
*report()* method in *ValidationReporter*, ensuring that any messages the validation reporter hears  
are reported:

    public abstract class BaseValidationReporter implements ValidationReporter
    {
        public void onMessage(Message message)
        {
            report(message)
        }
    }

Several subclasses of *BaseValidationReporter* provide different ways of handling validation problems:

* *AssertingValidationReporter* - Reports issues with Java's assert keyword
* *LogValidationReporter* - Logs validation issues
* *NullValidationReporter* - Ignores validation issues
* *ThrowingValidationReporter* - Throws *ValidationFailure* when validation issues are encountered

<br/>

![](https://telenav.github.io/telenav-assets/images/separators/horizontal-line.png)
