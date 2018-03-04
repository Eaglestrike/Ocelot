# Ocelot &nbsp;[![CircleCI](https://circleci.com/gh/Eaglestrike/Ocelot.svg?style=shield&circle-token=bc1154ecbf06f1d35aaa765db1d7eb34b550bb20)](https://circleci.com/gh/Eaglestrike/Ocelot) 

Team 114's 2018 FRC code. 

## Setup Development Environment (MacOS)

Use [this guide](http://wat.sinnpi.com/dl/FRC%20Getting%20Started%20-%20IntelliJ%20IDEA.pdf), it's
more comprehensive. Or refer to this shorter set of instructions, up to you.

### Clone repository
In your shell, run `git clone https://github.com/Eaglestrike/Ocelot/`. `cd` into the directory.

### Bootstrap `Gradle`
In your shell, run `./gradlew`. It will automatically install any missing dependencies.

### Build
Run `./gradlew build` to make sure everything compiles fine. If all is well, you're done!

## Setup and Integrate IntelliJ IDEA

### Download IntelliJ IDEA
Go ahead to the [download page on IntelliJ's website](https://www.jetbrains.com/idea/download)
and select the Community Edition. If you apply for their
[education package](https://www.jetbrains.com/student/), you can download the Ultimate edition
(but you won't really ever use any of the extra features).

### Generate IDEA project
In the project directory, run `./gradlew idea`. It will generate several files...

Go ahead and open `Ocelot.ipr` with IntelliJ IDEA. This will open a blank screen, and hopefully
a popup in the bottom right-hand corner will appear asking to link your Gradle project.

On the popup, `Import Gradle Project`. Select *only* `Use auto-import` and
`Use gradle wrapper task configuration`. Press OK.

In the toolbar, click `View`->`Tool Windows`->`Gradle`. This will open a Gradle tool window on the
right-hand side of the screen. This is where you can run tasks, etc.

Find and run the `build` task. If it runs successfully, everything is set up correctly!
Congratulations.

## Project style

We attempt to follow the
[Google Java Style guide](https://google.github.io/styleguide/javaguide.html),
except that we follow the Intellij defaults in most cases where they disagree.
We do not support any other editor, although programmers are permitted to use one
if they configure it to follow the same conventions.
