# Args Kata

## Why this Kata?
The Args [Kata](https://en.wikipedia.org/wiki/Kata_(programming)) offers an hour-long hands-on practice on readable and maintainable programming. When practicing alone, it improves a developer's craft. When practicing as a team, it educates and aligns the team on a standard set of best practices and coding styles.

Robert C. Martin gave the Java version of the code before and after refactoring in Chapter 14 of his book "Clean Code: A Handbook of Agile Software Craftsmanship." This repo simplifies the code slightly for Kata purpose and ports it into other languages.

Users' goal is to improve the modularity and readability of the `Args` class without breaking its functionalities.

## How to use this Kata
- Read the requirements (the next section) and implementation of the requirements in `before-refactor/`
- Rewrite the implementation in a more readable and maintainable way
- Your rewrite should pass all the unit tests provided in `tests/`
- While unit tests are provided, during the refactor, it's recommended to write your unit tests
- Please don't modify the provided unit tests
- Please don't modify the signature of the public methods of `Args`, i.e. # of arguments, their types as well as the return type. Public methods are:
      - The constructor
      - `getString`
      - `getInt`
      - `getBoolean`
      - `getDouble`
- You can add as many classes as you see fit
- Efficiency is NOT the primary concern comparing to modularity and readability

## Args Requirements
A program with command-line interface (CLI) usually takes several arguments. Take `curl -v -H content-type:application/json https://google.com` for example. It sends a HTTP request to `https://google.com`. It takes a key-value-pair-like argument: `-H` (the key, means set a HTTP header) and `content-type:application/json` (its value, means set a header named `content-type` to `application/json`). It also takes a boolean flag: `-v`. That means verbose mode is enabled and `curl` will print the HTTP request and the response in detail. Besides, it takes a plain string argument: `https://google.com`, which is the target URL of the HTTP request.

Most programming languages don't understand what flags and key-value pairs, they just split the arguments by space, like `["curl", "-v". "-H", "content-type:application/json", "https://google.com"]`. CLI tool authors need to write code to parse that list of strig to something meaningful like key-value pairs and flags. It's a common and tedious work, therefore people created libraries like [oclif](https://github.com/oclif/oclif) and [cli](https://github.com/urfave/cli).

`Args` is a simple CLI argument parser library. The constructor of `Args` takes a `schema` string and a list of string arguments `args`. The `schema` defines the parsing rules of the string list. The syntax of `schema` is
- different arguments are separated by `,`
- Argument names must be single letters (a to z, upper or lower cases)
- This library supports 4 types of arguments: boolean, int, double, and string
- Int arguments look like `p#`, where `p` is the argument name and `#` means it's a int argument. `p#` tells `Args` that there can be something like `-p 1234` in the arguments.
- String arguments look like `d*`, where `d` is te argument name and `*` means it's a string argument. `d*` tells `Args` that there can be something like `-d abcd` in the arguments
- The order of arguments doesn't matter. For example, schema `l,p#,d*` and schema `p#,l,d*` are equivalent. They can both parse commands like `command -l -p 1234 -d abcd` and `command -p 1234 -d abcd -l`
- Arguments are optional. For example, the schema `p#,l,d*` can match to `command -l` without any error. If a string argument is not presented, its value is default to an empty string. If an int or double argument is not presented, its value is default to 0. If a boolean argument is not presented, its value is default to false.

Once `Args` parsed the argument list, it provides methods for querying
- `getString`: takes an argument name and returns the value of that argument as a string
- `getInt`: takes an argument name and returns the value of that argument as an integer
- `getBoolean`: takes an argument name and returns the value of that argument as a boolean
- `getDouble`: takes an argument name and returns the value of that argument as a double

When parsing runs into a problem, it throws an exception. The exception should implement an `errorMessage()` method that returns a string message and `getErrorCode()` method that returns an enum.

Here is a list of error scenarios and their corresponding error codes and messages (replace `${key}` with the value of `key`):
|Scenario|Error Code|Message|
|-|-|-|
| Found an argument that is not presented in the schema | UNEXPECTED_ARGUMENT | `Argument -${problematicArgName} unexpected.` |
| Found an argument whose value should be an integer but it's not | INVALID_INTEGER | `Argument -${problematicArgName} expects an integer but was '${problematicArgValue}'.` |
| Found an argument whose value should be a double but it's not | INVALID_DOUBLE | `Argument -${problematicArgName} expects an integer but was '${problematicArgValue}'.` |
| There is a string argument at the end of the argument list with no value, like a single `-s` | MISSING_STRING | `Could not find string parameter for -${problematicArgName}.` |
| There is an integer argument at the end of the argument list with no value, like a single `-i` | MISSING_INTEGER | `Could not find integer parameter for -${problematicArgName}.` |
| There is a double argument at the end of the argument list with no value, like a single `-d` | MISSING_DOUBLE | `Could not find double parameter for -${problematicArgName}.` |
| Found an argument whose name is not a single letter (a-z, upper or lower cases) | INVALID_ARGUMENT_NAME | `'${problematicArgName}' is not a valid argument name.` |
| Found an argument that doesn't start with `-` | INVALID_ARGUMENT_FORMAT | `'${problematicSchemaValue}' is not a valid argument format.` |
| Found an argument with unknown type syntax, like `p#*` | INVALID_ARGUMENT_FORMAT | `'${problematicSchemaValue}' is not a valid argument format.` |
| Not an error, this should never happen | OK | `TILT: Should not get here.` |

## Solution
There is a solution provided in `after-refactor/`. It is one of the many ways to improve the code. It follows the practices mentioned in the Clean Code book, to name a few:
- Single Responsibility Principle (SRP): Keep classes small and cohesive
- DRY (Don't repeat yourself)
- One function should do one thing: statements within a function should all be at the same level of abstraction
- Use descriptive names
- Concepts that are closely related should be kept vertically close to each other
- Explain yourself in readable code instead of comments
