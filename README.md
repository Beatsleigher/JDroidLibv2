# JDroidLibv2
A Java class library packed with features for communication between Java applications, be it SE or EE and Android powered devices. 

## About JDroidLib
JDroidLib is a Java class library intended for use in communication between Java applications and Android devices.
Inspired by AndroidLib (by regaw_leinad), a .Net class library for essentially the same thing), I wanted this ability for Java
applications, without developers having thousands of lines of boilerplate code (if done correctly, that is. 
The same results could be achieved with less lines of code, but that would be 99.5% hacks and unreadable mess. Trust me!)
rotting away in their applications.

JDroidLib has always been intended for both novice and experienced developers, so it was designed with ease of use in mind.

JDroidLib is free (GPL-like free) for everyone to use!

## Why v2?

JDroidLib (http://github.com/Beatsleigher/JDroidLib) is the first working version of JDroidLib.
It does its job, it does it's job fairly well, but with time one gathers more experience, and I'm definitely no exception!
I could've attempted to update the library on its own, but I don't know how much software is using JDroidLib in its current form
and I don't want to break anything (unless it's really crappy, then I don't mind breaking it).

In the previous version of JDroidLib, some bad (non-standards-compliant) design choices were made in an effort to
make the library as easy to use as possible.
In previous years I've learned that sometimes a seemingly more complicated library is easier to use, especially when
attempting to make it standards-compliant.
That is why I'm developing a new version of JDroidLib whenever I have some free time. (Which isn't that often, to be perfectly honest).

This version, as it is, has been under active development for several weeks, and as you can tell by the (lack of) code, 
I don't have much time for development, but it'll get there eventually!

## Ok, great! Now why should I care?

If Java isn't your language, or you simply don't want/need your apps to communicate with Android devices, then you absolutely don't
have to!
If, however, you're in that niche market, you should give it a looksee to make your application smaller, faster and your development time
a lot shorter!

## Design choices?

This current version of JDroidLib (the one being actively being developed by me, now) is designed with factories, singletons,
and asynchronous operations in mind.
What this means for you? 

### Factories

Factories allow you to easily create the exact object you need, meaning your code becomes a lot simpler and as a result, a lot easier
to understand in the future.
By defining the different proeprties you need in an object, the library can determine the best way to create the object for you,
thus (hopefully) choosing the most efficient method, which will increase application speed.
For more on factories: https://en.wikipedia.org/wiki/Factory_(object-oriented_programming)

### Singletons

Singletons are objects that are instantiated **once**. This means that you, as an application developer, can't accidentally 
instantiate multiple objects that do the exact same thing, which might lead to collisions behind the scenes.
Singletons elliminate the risk of sudden application failures, due to resource hogging and I/O collisions.
For more on singletons: https://en.wikipedia.org/wiki/Singleton_pattern

### Asynchronous programming

Asynchronous programming means that certain operations are completed in the background, 
so that you can concentrate on more pressing things; such as keeping your UI fluent.
While previous versions relied on threading, this proved to be a poor solution and usually caused more problems than it prevent them.
The current version of JDroidLib relies on Java's FutureTask features to set certain operations in to the background.

However: This doesn't mean that everything JDroidLib can do has to be asynchronous!
The beauty of the implementation in JDroidLib, is that it's entirely up to you!
There are generally two methods for a given, IO- and/or CPU-heavy operations: Either synchronous, or asynchronous.
The choice is yours.
For more on asynchrony: https://en.wikipedia.org/wiki/Asynchrony_(computer_programming)

## Ok, that's cool. When is it ready?

That's a perfectly good question, in all honesty. However it's a question I cannot answer (yet).
It'll be done when it's done.
In the meantime; you're more than welcome to fork a copy, work on it as you see fit and suggest your edits to me!
I'll try to be nice!

# Coding Guidelines for code to be accepted to the main repository and added to the master branch

Methods written by you must follow the coding standard used in the library (I'd like to say I'm following the Java guidelines?)
Any methods written in addition to others must have VALID javadoc comments, with the author and creation date!
NO CRYPTIC NAMES FOR ANYTHING! No variables like "a, b, abc, fos, pos" or anything of the sorts!
Think of me as a psychopathic torturer who will find you and rick roll you until you fix it.

# The End

Wow, you read this far? (How often has that joke been made? I lost count)
Thanks for your interest in JDroidLib, if you like what you see so far and want to help out with development, 
buy me a coffee or something. I work two jobs and a 52hr+ week, so a coffee to keep me going is much appreciated!

