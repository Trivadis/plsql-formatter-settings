# Git `pre-commit` Hook

## Introduction

Do you have a Git repository with PL/SQL and SQL code? Do you want to automatically format the code in this repository on commit? Then you can configure this [`pre-commit`](pre-commit) hook for your repository to achieve this.

## How Does the Git `pre-commit` Hook Work?

When you run `git commit` Git looks in the hook folder `.git/hooks` if a file named `pre-commit` exists. If this file is executable then Git will call it without passing any parameters. If the `pre-commit` executable ends with status `0` then the commit process updates the local repository accordingly. In all other cases the commit process will be aborted.

See also [documentation](https://git-scm.com/docs/githooks).

## Can I Enforce Formatting Rules With a `pre-commit` Hook?

No. The `.git/hooks` directory only exists in your workspace. It will be initially populated with inactive examples from your Git installation. It's not possible to populate this directory with a Git command from a remote repository such as `git clone` or `git pull`. 

You can store the `pre-commit` script in a Git repository, but you have to copy it to the `.git/hooks` directory to activate it. It's a security measure. This gives you the chance to check for malicious code.

See server-side hooks in the chapters [8.3](https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks) and [8.4](https://git-scm.com/book/en/v2/Customizing-Git-An-Example-Git-Enforced-Policy#_an_example_git_enforced_policy) in the [Pro Git Book](https://git-scm.com/book/en/v2) which could be used to enforce formatting rules. However, these hooks are not meant to change code. They can be used to do some checks and accept or reject a push. Fixing an issue needs a rewrite of the local Git history. This is not very convienient. Using a `pre-commit` hook is the more user-friendly solution.

## How to Install

### Minimal SQLcl & `format.js` Variant

1. Clone this repository.
2. Copy the [`pre-commit`](pre-commit) script into the `.git/hooks` directory of your target Git workspace.
3. Open `.git/hooks/pre-commit` in an editor and change the environment variable `$FORMATTER_GITHUB_DIR` to point to the root of the Git repository you cloned in step 1.

The minimal installation variant has the advantage that you do not have to change anything in your target directory. The downside is that you are dependent on another locally installed Git repository. And you have not documented the formatter settings used for your target repository.

### Optimal SQLcl & `format.js` Variant

1. Create a subdirectory `formatter` in the workspace of your target repository

2. Copy the following files into this subdirectory:
    - [`pre-commit`](pre-commit)
    - [`settings/sql_developer/trivadis_advanced_format.xml`](../settings/sql_developer/trivadis_advanced_format.xml)
    - [`settings/sql_developer/trivadis_custom_format.arbori`](../settings/sql_developer/trivadis_custom_format.arbori)
    - [`sqlcl/format.js`](../sqlcl/format.js)

3. Open the `pre-commit` script in your workspace in an editor and change the following environment variables:

    Enivronment Variable | New Value
    -------------------- | ---------
    `FORMATTER_JS` | `"formatter/format.js"`
    `FORMATTER_SQLDEV_SETTINGS_DIR` | `"formatter"`

4. Create a file named `install-pre-commit-hook.sh` in the `formatter` directory and save it with the following content:

    ```sh
    #!/bin/sh

    FORMATTER_DIR="$(dirname $0)"
    GIT_HOOK_DIR="$FORMATTER_DIR/../.git/hooks"
    cp $FORMATTER_DIR/pre-commit $GIT_HOOK_DIR/pre-commit
    chmod +x $GIT_HOOK_DIR/pre-commit
    echo "pre-commit hook installed in $GIT_HOOK_DIR/pre-commit."
    ```

5. Open a terminal window (on Windows use `"C:\Program Files\Git\bin\bash.exe" --cd-to-home`), change to the root directory of your workspace and run the following commands:
    - `chmod +x formatter/install-pre-commit-hook.sh`
    - `formatter/install-pre-commit-hook.sh`
6. Commit the files in the `formatter` subdirectory and push the changes so that others can install the hook with the same formatter configuration.

This installation variant is independent of the `Trivadis/plsql-formatter-settings` Git repository and offers all team members to automatically format their code on `git commit` using the same settings.

### Optimal `tvdformat.jar` Variant

1. Create a subdirectory `formatter` in the workspace of your target repository

2. Copy the following files into this subdirectory:
    - [`pre-commit`](pre-commit)
    - [`settings/sql_developer/trivadis_advanced_format.xml`](../settings/sql_developer/trivadis_advanced_format.xml)
    - [`settings/sql_developer/trivadis_custom_format.arbori`](../settings/sql_developer/trivadis_custom_format.arbori)

3. Open the `pre-commit` script in your workspace in an editor and change the following environment variables:

    Enivronment Variable | New Value
    -------------------- | ---------
    `FORMATTER_VARIANT` | `"3"`
    `FORMATTER_JAR` | `"$(dirname $0)/tvdformat.jar"`
    `FORMATTER_SQLDEV_SETTINGS_DIR` | `"formatter"`

4. Create a file named `install-pre-commit-hook.sh` in the `formatter` directory and save it with the following content:

    ```sh
    #!/bin/sh

    FORMATTER_DIR="$(dirname $0)"
    GIT_HOOK_DIR="$FORMATTER_DIR/../.git/hooks"
    cp $FORMATTER_DIR/pre-commit $GIT_HOOK_DIR/pre-commit
    chmod +x $GIT_HOOK_DIR/pre-commit
    curl -o $GIT_HOOK_DIR/tvdformat.jar -L https://github.com/Trivadis/plsql-formatter-settings/releases/download/sqldev-21.2.1/tvdformat.jar
    echo "pre-commit hook installed in $GIT_HOOK_DIR/pre-commit."
    ```

5. Open a terminal window (on Windows use `"C:\Program Files\Git\bin\bash.exe" --cd-to-home`), change to the root directory of your workspace and run the following commands:
    - `chmod +x formatter/install-pre-commit-hook.sh`
    - `formatter/install-pre-commit-hook.sh`
6. Commit the files in the `formatter` subdirectory and push the changes so that others can install the hook with the same formatter configuration.

This installation variant is independent of the `Trivadis/plsql-formatter-settings` Git repository and offers all team members to automatically format their code on `git commit` using the same settings. 

## How to Test

### Running `pre-commit` Directly

Run the `pre-commit` script with a parameter to test it. Please note that the `pre-commit` script does not receive any parameters when called during `git commit`.

Open a terminal window (on Windows use `"C:\Program Files\Git\bin\bash.exe" --cd-to-home`) and run the following from your workspace root directory:

```sh
.git/hooks/pre-commit foo.sql
```

The expected output is something like this:

```
sql -nolog -noupdates -S
script formatter/format.js foo.sql ext=sql,prc,fnc,pks,pkb,trg,vw,tps,tpb,tbp,plb,pls,rcv,spc,typ,aqt,aqp,ctx,dbl,tab,dim,snp,con,collt,seq,syn,grt,sp,spb,sps,pck mext=markdown,mdown,mkdn,md xml=formatter/trivadis_advanced_format.xml arbori=formatter/trivadis_custom_format.arbori


file or directory foo.sql does not exist.

usage: script format.js <rootPath> [options]

mandatory argument: (one of the following)
  <rootPath>      file or path to directory containing files to format (content will be replaced!)
  <config.json>   configuration file in JSON format (must end with .json)
  *               use * to format the SQLcl buffer

options:
  --register, -r  register SQLcl command tvdformat, without processing, no <rootPath> required
  ext=<ext>       comma separated list of file extensions to process, e.g. ext=sql,pks,pkb
  mext=<ext>      comma separated list of markdown file extensions to process, e.g. ext=md,mdown
  xml=<file>      path to the file containing the xml file for advanced format settings
                  xml=default uses default advanced settings included in sqlcl
                  xml=embedded uses advanced settings defined in format.js
  arbori=<file>   path to the file containing the Arbori program for custom format settings
                  arbori=default uses default Arbori program included in sqlcl
```

You see the executed command (`sql...` and within SQLcl `script formatter/format.js...`) and the result of the command. In this case an error and help message, because `foo.sql` does not exist in the root of the workspace.

You can also use the `pre-commit` script to format all relevant files with the configured settings. For that just pass `.` as the rootPath.

### Running `pre-commit` Via `git commit` From Command Line

Let's create a SQL file named `bar.sql` with the following content in the root directory of the workspace:

```sql
select
*
from
dual
;
```

Open a terminal window (on Windows use `"C:\Program Files\Git\bin\bash.exe" --cd-to-home`) and run the following from your workspace root directory:

```sh
git add bar.sql
git commit -m "initial commit conforming to formatter settings"
```

The output should be similar to this:

```
sql -nolog -noupdates -S
script formatter/format.js /var/folders/lf/8g3r0ts900gfdfn2xxkn9yz00000gn/T/tmp.6NfQV4EU.json ext=sql,prc,fnc,pks,pkb,trg,vw,tps,tpb,tbp,plb,pls,rcv,spc,typ,aqt,aqp,ctx,dbl,tab,dim,snp,con,collt,seq,syn,grt,sp,spb,sps,pck mext=markdown,mdown,mkdn,md xml=formatter/trivadis_advanced_format.xml arbori=formatter/trivadis_custom_format.arbori


Formatting file 1 of 1: bar.sql... done.
[dummy 385978c] initial commit conforming to formatter settings
 1 file changed, 2 insertions(+)
 create mode 100644 bar.sql
```

The content of `bar.sql` should look like this:

```sql
select *
  from dual;
```

Run the following commands to reset your branch and your workspace to the state before this test:

```sh
git reset --soft HEAD~
rm bar.sql
```

### Running `pre-commit` Via `git commit` From a GUI

The `pre-commit` hook should work in most GUI tools. For example:

Tool | Tested version | Integration Type | Console Output?
---- | -------------- | ---------------- | ---------------
Source Tree | 4.1.3 | via CLI | Yes
Visual Studio Code | 1.60.2 | via CLI | Yes
IntelliJ IDEA | 2021.2.1 | via CLI | Yes
Eclipse IDE | 2021-03 | via Egit & JGit | No ([Bug](https://bugs.eclipse.org/bugs/show_bug.cgi?id=461232))

The console output shows the output of Git operations including the `pre-commit` trigger. Even if the commit works, the formatter might not be called. The console output reveals why.

Setting up the right environment (e.g. path to find SQLcl) is OS and OS version specific. The console output will help you to find out what's missing. Please consult the documentation of your system to determine how to change global settings. For example, on macOS 11.0.6 creating a `$HOME/.zshenv` for `$PATH`, creating a `$HOME/Library/LaunchAgents/global.environment.plist` for `$SQLPATH` and calling `launchctl load $HOME/Library/LaunchAgents/global.environment.plist` worked.   

SQL Developer has also a Git integration via [JGit](https://www.eclipse.org/jgit/). Unfortunatelly, SQL Developer 21.2.1 uses an old version 3.6.2 of JGit which does not support Git hooks. So, when you commit changes via SQL Developer it behaves like when you issue `git commit --no-verify` via command line. It simply does not execute the `pre-commit` script.

## Example

See [this GitHub repository](https://github.com/PhilippSalvisberg/plscope-utils) for an example how to integrate the `pre-commit` hook to format PL/SQL and SQL code. This repository uses the [Optimal `tvdformat.jar` Variant](#optimal-tvdformatjar-variant).

## Why `tvdformat.jar`?

The [standalone](../standalone) executable `tvdformat.jar` combines the formatter components of SQLcl with `format.js`.

### Advantages

- Performance

    - Slightly faster startup times 
    
      Test results calling `pre-commit` script for a non-existing file, fastet of three runs, no login.sql, Java 11:

      Variant | Startup time [s] | Percentage
      ------- | ---------------- | ----------
      SQLcl | 1.733 | 100
      `tvdformat.jar` | 1.399 | 81

      The reason for this difference is that the startup process is reduced to the max for `tvdformat.jar`. 

    - Significant faster formatter times
    
      Test results calling `pre-commit` script for all files in the example repository, fastest of three runs, no login.sql, Java 11, 56 files:

      Variant | Runtime [s] | Percentage
      ------- | ---------------- | ----------
      SQLcl | 72.680 | 100
      `tvdformat.jar` | 25.341 | 35

      The reason for this difference is, that `tvdformat.jar` uses GraalVM's JavaScript library while SQLcl still uses the default JavaScript Nashorn engine which is part of the JDK. However, since Nashorn is deprecated we expect that SQLcl will switch to GraalVM's JavaScript library in one of the coming versions. 

- Independence of SQLcl

    - SQLcl not required

      SQLcl does not need to be installed on the system. 

    - No version conflicts   
    
      It also works when an incompatible SQLcl version is installed. The Arbori program uses grammar constructs which are version specific.
      
    - Consistent formatter results

      If your settings rely on the default Arbori program provided by the SQLDev team then your settings are still depending on a certain SQLcl version. Based on the formatter settings included in the SQLcl distribution the formatter might produce different results.

    - Works with Java > 11

      SQLcl requires Java 8 or Java 11. It currently does not work with new Java versions. The standalone `tvdformat.jar` works with any Java version >= 8. Using Java 17 improves the runtime performance further.

### Disadvantages

- Combination of SQLcl and `format.js`
  
  A change of `format.js` or SQLcl might require to build a new version of `tvdformat.jar`. If the required combination is not provided in [Releases](https://github.com/Trivadis/plsql-formatter-settings/releases) you need to build the standalone executable yourself and make it available for all repository users. When you decide to store it in the Git repository you should consider that the JAR file has a size of around 30M. This is nowadays not that large. However, for small repositories this might lead to a major contribution of the overall size. Especially when the file is updated regularly. 

- Licensing questions
  
  SQLcl is licensed under the [Oracle Free Use Terms and Conditions license](https://www.oracle.com/downloads/licenses/oracle-free-license.html) since May, 4 2021. See [this blog post](https://blogs.oracle.com/database/post/sqlcl-now-under-the-oracle-free-use-terms-and-conditions-license). Redistribution is allowed under certain conditions. The standalone formatter uses SQLcl as a library like GraalVM's js-scriptengine, which is available on [Maven Central](https://search.maven.org/artifact/org.graalvm.js/js-scriptengine/21.2.0/jar). Using `tvdformat.jar` might raise some questions about licensing. If you want to avoid this, you should use SQLcl.
