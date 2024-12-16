# Building babashka

## Prerequisites

- Install [lein](https://leiningen.org/) for producing uberjars
- Download [GraalVM](https://www.graalvm.org/downloads/). Currently we use *Oracle GraalVM 23*.
- For Windows, installing Visual Studio 2019 with the "Desktop development
with C++" workload is recommended.
- Set `$GRAALVM_HOME` to the GraalVM distribution directory. On macOS this can look like:

  ``` shell
  export GRAALVM_HOME=~/Downloads/graalvm-jdk-21.0.0.1/Contents/Home
  ```

  On linux:

  ``` shell
  export GRAALVM_HOME=~/Downloads/graalvm-jdk-21.0.0.1
  ```

  On Windows, from the [Visual Studio 2019 x64 Native Tools Command Prompt](https://github.com/oracle/graal/issues/2116#issuecomment-590470806) or `cmd.exe` (not Powershell):
  ```
  set GRAALVM_HOME=%USERPROFILE%\Downloads\graalvm-ce-jdk-21.0.0.1
  ```
  If you are not running from the x64 Native Tools Command Prompt, you will need to set additional environment variables using:
  ```
  call "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvars64.bat"
  ```

## Clone repository

NOTE: the babashka repository contains submodules. You need to use the
`--recursive` flag to clone these submodules along with the main repo.

``` shellsession
$ git clone https://github.com/babashka/babashka --recursive
```

To update later on:

``` shellsession
$ git submodule update --init --recursive
```

## Build

Run the `uberjar` and `compile` script:

``` shell
$ script/uberjar
$ script/compile
```

To configure max heap size you can use:

```
$ export BABASHKA_XMX="-J-Xmx6500m"
```

Note: setting the max heap size to a low value can cause the build to crash or
take long to complete.

### Alternative: Build inside Docker

To build a Linux version of babashka, you can use `docker build`, enabling the
desired features via `--build-arg` like this:

```shell
docker build --build-arg BABASHKA_FEATURE_JDBC=true --target BASE -t bb-builder .
container_id=$(docker create bb-builder)
docker cp $container_id:/opt/bb bb # copy to ./bb on the host file system
docker rm $container_id
```

NOTE: If you get _Error: Image build request failed with exit status 137_ then
check whether Docker is allowed to use enough memory (e.g. in Docker Desktop
preferences). If it is, then increase the memory GraalVM can use, for example
by adding `--build-arg BABASHKA_XMX="-J-Xmx8g"`
(or whatever Docker has available, bigger than the default).

## Windows

Run `script\uberjar.bat` followed by `script\compile.bat`.

## Static

To compile babashka as a static binary for linux, set the `BABASHKA_STATIC`
environment variable to `true`.

## Feature flags

Babashka supports the following feature flags:

| Name   |  Description                                 | Default  |
|--------|----------------------------------------------|----------|
| `BABASHKA_FEATURE_CSV` | Includes the [clojure.data.csv](https://github.com/clojure/data.csv) library | `true` |
| `BABASHKA_FEATURE_JAVA_NET_HTTP` | Includes commonly used classes from the `java.net.http` package | `true` |
| `BABASHKA_FEATURE_JAVA_NIO` | Includes commonly used classes from the `java.nio` package | `true` |
| `BABASHKA_FEATURE_JAVA_TIME` | Includes commonly used classes from the `java.time` package | `true` |
| `BABASHKA_FEATURE_TRANSIT` | Includes the [transit-clj](https://github.com/cognitect/transit-clj) library | `true` |
| `BABASHKA_FEATURE_XML` | Includes the [clojure.data.xml](https://github.com/clojure/data.xml) library | `true` |
| `BABASHKA_FEATURE_YAML` | Includes the [clj-yaml](https://github.com/clj-commons/clj-yaml) library | `true` |
| `BABASHKA_FEATURE_HTTPKIT_CLIENT` | Includes the [http-kit](https://github.com/http-kit/http-kit) client library | `true` |
| `BABASHKA_FEATURE_HTTPKIT_SERVER` | Includes the [http-kit](https://github.com/http-kit/http-kit) server library | `true` |
| `BABASHKA_FEATURE_CORE_MATCH` | Includes the [clojure.core.match](https://github.com/clojure/core.match) library | `true` |
| `BABASHKA_FEATURE_HICCUP` | Includes the [hiccup](https://github.com/weavejester/hiccup) library | `true` |
| `BABASHKA_FEATURE_TEST_CHECK` | Includes the [clojure.test.check](https://github.com/clojure/test.check) library | `true` |
| `BABASHKA_FEATURE_SPEC_ALPHA` | Includes the [clojure.spec.alpha](https://github.com/clojure/spec.alpha) library (WIP) | `false` |
| `BABASHKA_FEATURE_JDBC` | Includes the [next.jdbc](https://github.com/seancorfield/next-jdbc) library | `false`    |
| `BABASHKA_FEATURE_SQLITE` | Includes the [sqlite-jdbc](https://github.com/xerial/sqlite-jdbc) library | `false`    |
| `BABASHKA_FEATURE_POSTGRESQL` | Includes the [PostgresSQL](https://jdbc.postgresql.org/) JDBC driver |  `false` |
| `BABASHKA_FEATURE_HSQLDB` | Includes the [HSQLDB](http://www.hsqldb.org/) JDBC driver | `false` |
| `BABASHKA_FEATURE_ORACLEDB` | Includes the [Oracle](https://www.oracle.com/database/technologies/appdev/jdbc.html) JDBC driver | `false` |
| `BABASHKA_FEATURE_DATASCRIPT` | Includes [datascript](https://github.com/tonsky/datascript) | `false` |
| `BABASHKA_FEATURE_LANTERNA` | Includes [clojure-lanterna](https://github.com/babashka/clojure-lanterna) | `false` |
| `BABASHKA_FEATURE_LOGGING` | Includes [clojure.tools.logging](https://github.com/clojure/tools.logging) with [taoensso.timbre](https://github.com/ptaoussanis/timbre) as the default implementation| `true` |
| `BABASHKA_FEATURE_PRIORITY_MAP` | Includes [clojure.data.priority-map](https://github.com/clojure/data.priority-map) | `true` |
| `BABASHKA_FEATURE_REBEL_READLINE` | Includes [rebel-readline](https://github.com/bhauman/rebel-readline) | `false` |

Note that httpkit server is currently experimental, the feature flag could be toggled to `false` in a future release.

To disable all of the above features, you can set `BABASHKA_LEAN` to `true`.

Here is an [example
commit](https://github.com/babashka/babashka/commit/13f65f05aeff891678e88965d9fbd146bfa87f4e)
that can be used as a checklist when you want to create a new feature flag.

### HyperSQL

To compile babashka with the `next.jdbc` library and the embedded HyperSQL
database:

``` shell
$ export BABASHKA_FEATURE_JDBC=true
$ export BABASHKA_FEATURE_HSQLDB=true
$ script/uberjar
$ script/compile
```

Note: there is now a [pod](https://github.com/babashka/babashka-sql-pods) for working with HyperSQL.

### PostgresQL

To compile babashka with the `next.jdbc` library and a PostgresQL driver:

``` shell
$ export BABASHKA_FEATURE_JDBC=true
$ export BABASHKA_FEATURE_POSTGRESQL=true
$ script/uberjar
$ script/compile
```

Note: there is now a [pod](https://github.com/babashka/babashka-sql-pods) for working with PostgreSQL.

### Lanterna

To compile babashka with the [babashka/clojure-lanterna](https://github.com/babashka/clojure-lanterna) library:

``` shell
$ export BABASHKA_FEATURE_LANTERNA=true
$ script/uberjar
$ script/compile
```

Example program:

``` clojure
(require '[lanterna.terminal :as terminal])

(def terminal (terminal/get-terminal))

(terminal/start terminal)
(terminal/put-string terminal "Hello TUI Babashka!" 10 5)
(terminal/flush terminal)

(read-line)
```

### rebel-readline

To compile babashka with `rebel-readline`:

``` shell
$ export BABASHKA_FEATURE_REBEL_READLINE=true
$ script/uberjar
$ script/compile
```

Example program:

``` clojure
$ bb -m rebel-readline.main
Dec 15, 2024 6:04:39 PM org.jline.utils.Log logr
WARNING: Unable to retrieve infocmp for type screen-256color
java.lang.NullPointerException
        at java.base@21.0.2/java.io.Reader.<init>(Reader.java:168)
        at org.jline.utils.InputStreamReader.<init>(InputStreamReader.java:135)
        at org.jline.utils.InfoCmp.loadDefaultInfoCmp(InfoCmp.java:609)
        at org.jline.utils.InfoCmp.lambda$static$3(InfoCmp.java:619)
        at org.jline.utils.InfoCmp.getLoadedInfoCmp(InfoCmp.java:558)
        at org.jline.utils.InfoCmp.getInfoCmp(InfoCmp.java:546)
        at org.jline.terminal.impl.AbstractTerminal.parseInfoCmp(AbstractTerminal.java:168)
        at org.jline.terminal.impl.PosixSysTerminal.<init>(PosixSysTerminal.java:43)
        at org.jline.terminal.TerminalBuilder.doBuild(TerminalBuilder.java:334)
        at org.jline.terminal.TerminalBuilder.build(TerminalBuilder.java:219)
        at rebel_readline.jline_api$create_terminal.invokeStatic(jline_api.clj:62)
        at rebel_readline.jline_api$create_terminal.doInvoke(jline_api.clj:59)
        at clojure.lang.RestFn.invoke(RestFn.java:400)
        at rebel_readline.clojure.main$_main.invokeStatic(main.clj:112)
        at rebel_readline.clojure.main$_main.doInvoke(main.clj:111)
        at clojure.lang.RestFn.invoke(RestFn.java:400)
        at clojure.lang.AFn.applyToHelper(AFn.java:152)
        at clojure.lang.RestFn.applyTo(RestFn.java:135)
        at clojure.core$apply.invokeStatic(core.clj:667)
        at rebel_readline.main$_main.invokeStatic(main.clj:5)
        at rebel_readline.main$_main.doInvoke(main.clj:5)
        at clojure.lang.RestFn.invoke(RestFn.java:400)
        at clojure.lang.AFn.applyToHelper(AFn.java:152)
        at clojure.lang.RestFn.applyTo(RestFn.java:135)
        at clojure.core$apply.invokeStatic(core.clj:667)
        at clojure.core$apply.invoke(core.clj:662)
        at sci.lang.Var.invoke(lang.cljc:211)
        at sci.impl.analyzer$return_call$reify__4588.eval(analyzer.cljc:1420)
        at sci.impl.interpreter$eval_form.invokeStatic(interpreter.cljc:40)
        at sci.impl.interpreter$eval_string_STAR_.invokeStatic(interpreter.cljc:66)
        at sci.impl.interpreter$eval_string_STAR_.invoke(interpreter.cljc:57)
        at sci.impl.interpreter$eval_string_STAR_.invokeStatic(interpreter.cljc:59)
        at sci.core$eval_string_STAR_.invokeStatic(core.cljc:272)
        at babashka.main$exec$fn__34343$fn__34381$fn__34382.invoke(main.clj:1054)
        at babashka.main$exec$fn__34343$fn__34381.invoke(main.clj:1054)
        at babashka.main$exec$fn__34343.invoke(main.clj:1044)
        at clojure.lang.AFn.applyToHelper(AFn.java:152)
        at clojure.lang.AFn.applyTo(AFn.java:144)
        at clojure.core$apply.invokeStatic(core.clj:667)
        at clojure.core$with_bindings_STAR_.invokeStatic(core.clj:1990)
        at clojure.core$with_bindings_STAR_.doInvoke(core.clj:1990)
        at clojure.lang.RestFn.invoke(RestFn.java:428)
        at babashka.main$exec.invokeStatic(main.clj:839)
        at babashka.main$main.invokeStatic(main.clj:1231)
        at babashka.main$main.doInvoke(main.clj:1175)
        at clojure.lang.RestFn.applyTo(RestFn.java:140)
        at clojure.core$apply.invokeStatic(core.clj:667)
        at babashka.main$_main.invokeStatic(main.clj:1263)
        at babashka.main$_main.doInvoke(main.clj:1255)
        at clojure.lang.RestFn.applyTo(RestFn.java:140)
        at babashka.main.main(Unknown Source)
        at java.base@21.0.2/java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH)

Dec 15, 2024 6:04:40 PM org.jline.utils.Log logr
WARNING: Unable to create a system terminal, creating a dumb terminal (enable debug logging for more information)
Dec 15, 2024 6:04:40 PM org.jline.utils.Log logr
WARNING: Unable to retrieve infocmp for type screen-256color
java.lang.NullPointerException
        at java.base@21.0.2/java.io.Reader.<init>(Reader.java:168)
        at org.jline.utils.InputStreamReader.<init>(InputStreamReader.java:135)
        at org.jline.utils.InfoCmp.loadDefaultInfoCmp(InfoCmp.java:609)
        at org.jline.utils.InfoCmp.lambda$static$3(InfoCmp.java:619)
        at org.jline.utils.InfoCmp.getLoadedInfoCmp(InfoCmp.java:558)
        at org.jline.utils.InfoCmp.getInfoCmp(InfoCmp.java:546)
        at org.jline.terminal.impl.AbstractTerminal.parseInfoCmp(AbstractTerminal.java:168)
        at org.jline.terminal.impl.DumbTerminal.<init>(DumbTerminal.java:102)
        at org.jline.terminal.TerminalBuilder.doBuild(TerminalBuilder.java:350)
        at org.jline.terminal.TerminalBuilder.build(TerminalBuilder.java:219)
        at rebel_readline.jline_api$create_terminal.invokeStatic(jline_api.clj:62)
        at rebel_readline.jline_api$create_terminal.doInvoke(jline_api.clj:59)
        at clojure.lang.RestFn.invoke(RestFn.java:400)
        at rebel_readline.clojure.main$_main.invokeStatic(main.clj:112)
        at rebel_readline.clojure.main$_main.doInvoke(main.clj:111)
        at clojure.lang.RestFn.invoke(RestFn.java:400)
        at clojure.lang.AFn.applyToHelper(AFn.java:152)
        at clojure.lang.RestFn.applyTo(RestFn.java:135)
        at clojure.core$apply.invokeStatic(core.clj:667)
        at rebel_readline.main$_main.invokeStatic(main.clj:5)
        at rebel_readline.main$_main.doInvoke(main.clj:5)
        at clojure.lang.RestFn.invoke(RestFn.java:400)
        at clojure.lang.AFn.applyToHelper(AFn.java:152)
        at clojure.lang.RestFn.applyTo(RestFn.java:135)
        at clojure.core$apply.invokeStatic(core.clj:667)
        at clojure.core$apply.invoke(core.clj:662)
        at sci.lang.Var.invoke(lang.cljc:211)
        at sci.impl.analyzer$return_call$reify__4588.eval(analyzer.cljc:1420)
        at sci.impl.interpreter$eval_form.invokeStatic(interpreter.cljc:40)
        at sci.impl.interpreter$eval_string_STAR_.invokeStatic(interpreter.cljc:66)
        at sci.impl.interpreter$eval_string_STAR_.invoke(interpreter.cljc:57)
        at sci.impl.interpreter$eval_string_STAR_.invokeStatic(interpreter.cljc:59)
        at sci.core$eval_string_STAR_.invokeStatic(core.cljc:272)
        at babashka.main$exec$fn__34343$fn__34381$fn__34382.invoke(main.clj:1054)
        at babashka.main$exec$fn__34343$fn__34381.invoke(main.clj:1054)
        at babashka.main$exec$fn__34343.invoke(main.clj:1044)
        at clojure.lang.AFn.applyToHelper(AFn.java:152)
        at clojure.lang.AFn.applyTo(AFn.java:144)
        at clojure.core$apply.invokeStatic(core.clj:667)
        at clojure.core$with_bindings_STAR_.invokeStatic(core.clj:1990)
        at clojure.core$with_bindings_STAR_.doInvoke(core.clj:1990)
        at clojure.lang.RestFn.invoke(RestFn.java:428)
        at babashka.main$exec.invokeStatic(main.clj:839)
        at babashka.main$main.invokeStatic(main.clj:1231)
        at babashka.main$main.doInvoke(main.clj:1175)
        at clojure.lang.RestFn.applyTo(RestFn.java:140)
        at clojure.core$apply.invokeStatic(core.clj:667)
        at babashka.main$_main.invokeStatic(main.clj:1263)
        at babashka.main$_main.doInvoke(main.clj:1255)
        at clojure.lang.RestFn.applyTo(RestFn.java:140)
        at babashka.main.main(Unknown Source)
        at java.base@21.0.2/java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH)

----- Error --------------------------------------------------------------------
Type:     java.lang.NullPointerException
Location: <expr>:1:44

----- Context ------------------------------------------------------------------
1: (ns user (:require [rebel-readline.main])) (apply rebel-readline.main/-main *command-line-args*)
                                              ^--- 

----- Stack trace --------------------------------------------------------------
rebel-readline.jline-api/create-terminal - <built-in>
rebel-readline.clojure.main/-main        - <built-in>
clojure.core/apply                       - <built-in>
rebel-readline.main/-main                - <built-in>
clojure.core/apply                       - <built-in>
user                                     - <expr>:1:44


```

