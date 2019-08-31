# knoweth
Two-column reviewing made easy

## Building

1. Use Maven to install dependencies, including TeaVM and Spring.
2. Run `mvn package` to compile it into a jar that can be run.

## Development

1. Run the `Application` class in the server folder. It is recommended to [turn on Auto-Make in Intellij](https://stackoverflow.com/questions/12744303/intellij-idea-java-classes-not-auto-compiling-on-save). 
2. Run `mvn fizzed-watcher:run` to watch JS files and recompile incrementally.
