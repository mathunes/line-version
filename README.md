# Line Version

Line Versioning, also called LVN, is a Git helper tool for line-level versioning. LVN acts on projects already versioned by Git by traversing their snapshots and generating versions for each line of the tracked files.

LVN has a storage external to the standard Git storage, having its structure represented in JSON-formatted files.

With this tool, the user can explore line-level versioning, being able to get the versions that have already existed for a specific line or for all lines of a given file.

The tool was proposed and developed as a partial requirement for concluding the Project Management course taught by professor [Leonardo Murta](https://github.com/leomurta) for the graduate course at the Computation Institute of the Fluminense Federal University.

## Installation

Follow the instructions for Linux operating system

1. Download the [package available](https://github.com/mathunes/line-version/blob/release/1.1.0/dist/lvn.tar.gz)
2. Extract the compressed folder
3. Grant execution permission to the lvn script

```
chmod u+x <path>/lvn/bin/lvn
```

4. Add bin path to variable $PATH

```
PATH=$PATH:<path>/lvn/bin
```

5. Check the tool version with the following command:

```
lvn version
```

## Usage

### Initialize lvn repository

In your Git repository, run the following command to initialize an lvn repository and create its initial storage structure.

```
lvn init
```

### Versioning lines of a file

To version the lines of a file, make sure that the file is already versioned by Git and type the following command.


```
lvn add <path file>
```

### Get line version

#### Get versions of all lines in the file

To get all version of all lines in your versioned file, type the following command.

```
lvn show <path file>
```

The result will look something like this:

```
...
LINE 68:
        VERSION 1:
                CONTENT:           <artifactId>maven-project-info-reports-plugin</artifactId>
                AUTHOR: mathunes <matheusantunes720@gmail.com>
                DATE: Wed Oct 19 00:46:00 2022 -0300
                MESSAGE: add initial project
                HASH COMMIT: cfbf0fbed1d123d15614235f5312052c54344313
        VERSION 2:
                CONTENT:           <artifactId>maven-compiler-plugin</artifactId>
                AUTHOR: mathunes <matheusantunes720@gmail.com>
                DATE: Wed Oct 26 22:17:02 2022 -0300
                MESSAGE: add plugin to generate uber jar
                HASH COMMIT: 99fe6fd4116bad11c0f7974624dea3f4bc54ee45
LINE 69:
        VERSION 1:
                CONTENT:           <version>3.0.0</version>
                AUTHOR: mathunes <matheusantunes720@gmail.com>
                DATE: Wed Oct 19 00:46:00 2022 -0300
                MESSAGE: add initial project
                HASH COMMIT: cfbf0fbed1d123d15614235f5312052c54344313
...
```

#### Get versions of line in the file

To get all versions of a specific line in your versioned file, type the following command.

```
lvn show <path file> <line number>
```

The result will look something like this:

```
LINE 68:
        VERSION 1: 
                CONTENT:           <artifactId>maven-project-info-reports-plugin</artifactId>
                AUTHOR: mathunes <matheusantunes720@gmail.com>
                DATE: Wed Oct 19 00:46:00 2022 -0300
                MESSAGE: add initial project
                HASH COMMIT: cfbf0fbed1d123d15614235f5312052c54344313
        VERSION 2:
                CONTENT:           <artifactId>maven-compiler-plugin</artifactId>
                AUTHOR: mathunes <matheusantunes720@gmail.com>
                DATE: Wed Oct 26 22:17:02 2022 -0300
                MESSAGE: add plugin to generate uber jar
                HASH COMMIT: 99fe6fd4116bad11c0f7974624dea3f4bc54ee45
```

#### Get last version of line in the file

To get the last version of a specific line in your versioned file, type the following command.

```
lvn show <path file> <line number> -last
```

The result will look something like this:

```
LINE 68:
        VERSION 2:
                CONTENT:           <artifactId>maven-compiler-plugin</artifactId>
                AUTHOR: mathunes <matheusantunes720@gmail.com>
                DATE: Wed Oct 26 22:17:02 2022 -0300
                MESSAGE: add plugin to generate uber jar
                HASH COMMIT: 99fe6fd4116bad11c0f7974624dea3f4bc54ee45
```

#### Get versions of all lines in the file by clicking in file line graphically

To get all version of all lines in your versioned file graphically, type the following command.

```
lvn show <path file> -graph
```

After that, click on the desired line.

![lvn graph example](https://raw.githubusercontent.com/mathunes/line-version/release/1.1.0/assets/lvn-graph-example-01.png)

If you are using WLS2, follow these instructions to enable the GUI.

- Export the DISPLAY variable with the command: `export DISPLAY=$(hostname).local:0`
- [Setting up a WSL2 GUI X-Server](https://www.shogan.co.uk/how-tos/wsl2-gui-x-server-using-vcxsrv/).

### Versioning update

After versioning the lines with the LVN, the Git project will probably evolve and receive new commits. In this case, the LVN base will be out of date with the new versions and to correct this issue, just type the following command to update all the LVN objects.

```
lvn update
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

After forking the project, follow the instructions below to work locally on the project:

- This project uses maven as dependency manager
- This project uses gitflow as a branch management technique

Compile the code: mvn clean install

Run the tool: java -cp target/line-version-1.1.0-jar-with-dependencies.jar br.uff.ic.App <commands>

## License

[MIT](https://choosealicense.com/licenses/mit/)
