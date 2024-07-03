# jar-analyzer

### Download the latest build [here](https://github.com/betacraftuk/jar-analyzer/releases/latest/download/jar-analyzer.jar)

Usage:
```sh
java -Dtz=<tz of jar contents> -jar jar-analyzer.jar "/path/to/dir/or/jar/to/analyze"
```

`-Dtz=<tz>` is optional, and if not used, the program will assume the jar contents are UTC.

For best results, use the `TZ identifier` string from [tz database](https://en.wikipedia.org/wiki/List_of_tz_database_time_zones#List)

Common timezones Minecraft versions have been built in so far:
- `Europe/Stockholm`:
	- `pc-13-2011` to `1.2.4`
	- `1.5.2-pre-25-0703`
	- `1.5.2`
	- `13w16a-18-1812` to `16w04a`
	- `1.9.3-pre1`
	- `16w32b`
	- `16w36a` to `16w41a`
- `UTC`:
	- `1.2.5` to `1.5.1`
	- `16w05a` to `16w15b`
	- `1.9.3-pre2` to `16w32a`
	- `16w33a` to `16w35a`
	- `16w42a` until current latest version

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