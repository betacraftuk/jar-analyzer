# jar-analyzer

### Download the latest build [here](https://github.com/betacraftuk/jar-analyzer/releases/latest/download/jar-analyzer.jar)

Usage:
```sh
java -Dtz=<tz of jar contents> -jar jar-analyzer.jar "/path/to/dir/or/jar/to/analyze"
```

`-Dtz=<tz>` is optional, and if not used, the program will assume the jar contents are UTC.

### Building

1. Clone the repository
	```sh
	git clone 'https://github.com/betacraftuk/jar-analyzer.git'
	```
2. Run `./gradlew build` (or `.\gradlew.bat build` if you're on Windows) in the project directory.

Your build will be at `{project dir}/build/libs/jar-analyzer.jar`

### TODO
- add printing in tree view as an option
- make sorting code cleaner?