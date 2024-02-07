# PL/SQL & SQL Formatter Settings

## Introduction

This repository provides formatter settings for the [coding style rules](https://trivadis.github.io/plsql-and-sql-coding-guidelines/v4.3/3-coding-style/coding-style/#rules) of the Trivadis PL/SQL & SQL Coding Guidelines.

Settings are primarily provided for

- [Oracle SQLcl, Version 23.4.0](https://www.oracle.com/tools/downloads/sqlcl-downloads.html)
- [Oracle SQL Developer, Version 23.1.1](https://www.oracle.com/tools/downloads/sqldev-downloads.html) (requires `dbtools-common.jar` from SQLcl 23.3.0)

These settings have been defined and tested with the product versions mentioned above. They might not work in other versions.

Please note, that these settings cannot be used in [Oracle SQL Developer for VSCode 23.4.0](https://marketplace.visualstudio.com/items?itemName=Oracle.sql-developer). And it does not work in the embedded version of SQLcl 23.3.1.0, which cannot run JavaScript. 

JDK 11 is required for SQLDev and SQLcl. The standalone tvdformat.jar works with JDK 11 or newer.

See [releases](https://github.com/Trivadis/plsql-formatter-settings/releases) for settings supporting older versions.

## Patching SQL Developer

SQL Developer is slowly reaching the end of its life cycle. The days when SQL Developer and SQLcl were released almost simultaneously every quarter are long gone. We can continue to expect regular SQLcl releases, but with the availability of SQL Developer as a Visual Studio Code Extension, SQL Developer will only be updated sporadically.

A new SQLcl version typically comes with enhancements and bug fixes in the area of PL/SQL and SQL grammar. And this also requires an adaptation of the formatting rule due to symbol name changes etc. And as a result, these formatting rules can actually only be used in SQLcl. 

However, we know that the grammars and the formatter are provided in a JAR called `dbtools-common.jar`. And this JAR file also exists in the SQL Developer distribution. This means that in order to be able to use the current formatting rules in SQL Developer, we have to copy the `dbtools-common.jar` file from SQLcl to SQL Developer as follows:

1. Quit SQL Developer
   
   We are going to patch SQL Developer. This is not possible on Windows if SQL Developer is running. On other OS this might have strange effects. Therefore quit SQL Developer.

2. Rename SQL Developer’s `dbtools-common.jar`

   Find the `dbtools-common.jar` in your SQL Developer installation. In our case it's in `/Applications/SQLDeveloper.app/Contents/Resources/sqldeveloper/sqldeveloper/lib`. Rename this file to `dbtools-common.original.jar`.

3. Copy SQLcl’s `dbtools-common.jar`

   Find the `dbtools-common.jar` in your SQLcl installation. In our case it's in `/usr/local/bin/sqlcl/lib`. Copy the file to the SQL Developer’s directory (where `dbtools-common.original.jar` is located).

4. Start SQL Developer

   Open an editor and test if the formatter is working.

We have successfully tested this procedure with SQL Developer 23.1.1. However, there were cases in the past where this did not work. For example, using the JAR of SQLcl 20.3.0 in SQL Developer 20.2.0. Replacing a JAR requires a certain level of compatiblity. We therefore expect that this procedure will no longer work with an upcoming version of SQLcl. We will update this section as soon as we know more.

## Deviating Settings

Please note that these settings do not comply with rule 5. Line breaks are placed after a comma and not before. All other rules are followed. However, you can easily change this in the preferences.

![Change Line Breaks On Comma](images/change_line_breaks_on_comma.png)

## Installation

### Common

Clone this repository or download the ZIP file and extract it. 

### SQLcl

See [sqlcl/README.md](sqlcl/README.md).

### Standalone Formatter

See [standalone/README.md](standalone/README.md)

### SQL Developer

1. Start SQL Developer
2. Open `Preferences`
3. Select `Code Editor` -> `Format` -> `Advanced Format`
4. Press `Import...`
   ![Advanced Format](images/advanced_format.png)
5. Select [`trivadis_advanced_format.xml`](settings/sql_developer/trivadis_advanced_format.xml)
6. Press `Open`
7. Select `Code Editor` -> `Format` -> `Advanced Format` -> `Custom Format`
8. Press `Import...`
   ![Custom Format](images/custom_format.png)
9. Select [`trivadis_custom_format.arbori`](settings/sql_developer/trivadis_custom_format.arbori)
10. Press `Open`
11. Press `OK` to save the settings

### Git `pre-commit` Hook

See [hook/README.md](hook/README.md).

## Nice to Know

### Lightweight Formatter

The Arbori program implements a [lightweight formatter](https://www.salvis.com/blog/2021/07/18/lightweight-formatter-for-pl-sql-sql/) which accepts different coding styles. The primary scope of the formatter is the following SQL statements:

- `create function`
- `create package`
- `create package body`
- `create procedure`
- `create trigger`
- `create type`
- `create type body`
- `create view` (including the `subquery` part of `create materialized view`)
- `delete`
- `insert`
- `merge`
- `select`
- `update`

Other statements like `create table` or `create tablespace` are basically left as is. There are some exceptions like adding line breaks after reaching the maximum line length and changing the case of keywords and identifiers. But that's it. 

If you need a heavyweight formatter to enforce the conformity of your code, you will need to use the default Arbori program provided by the SQL Developer team.

In any case, you can use our SQLcl JavaScript [`format.js`](sqlcl/README.md), the [standalone formatter](standalone/README.md) or the [Git `pre-commit` hook](hook/README.md) to format your code.

### Disable Formatter

The formatter is enabled by default. You can disable the formatter with a single-line or multi-line comment containing `@formatter:off`. From that point on, whitespace are kept as is. To re-enable the formatter use `@formatter:on`. 

Supported Marker Comment | Action | Origin 
-- | -- | --
`@formatter:off` | Disables formatter | Eclipse IDE
`@formatter:on` | Enables formatter | Eclipse IDE
`noformat start` | Disables formatter | PL/SQL Developer
`noformat end` | Enables formatter | PL/SQL Developer

The marker comments are case sensitive.

Here's an example showing the code after calling the formatter:

```sql
-- @formatter:off
select decode(dummy, 'X', 1 
                   , 'Y', 2
                   , 'Z', 3
                        , 0) /* @formatter:on */
  from dual;
```

### Arbori

SQL Developer uses its own parse tree query language called Arbori for its advanced formatter configuration. Here is some additional information that might be useful if you plan to tweak the behavior of the formatter yourself.

#### Links

- [SQL Developer 23.1 User Guide, Code Editor: Format](https://docs.oracle.com/en/database/oracle/sql-developer/23.1/rptug/sql-developer-concepts-usage.html#GUID-9421DA6E-A48A-427B-88C9-4414D83EC9D1__CODEEDITORFORMAT-C73DB981)
- [Formatting Code With SQL Developer](https://www.salvis.com/blog/2020/04/13/formatting-code-with-sql-developer/)
- [Formatter Callback Functions](https://www.salvis.com/blog/2020/11/12/formatter-callback-functions/)
- [Arbori Starter Manual](https://vadimtropashko.files.wordpress.com/2017/02/arbori-starter-manual.pdf)
- [Semantic Analysis with Arbori](https://vadimtropashko.files.wordpress.com/2019/11/arbori.pdf)
- [Arbori Semantic Actions](https://vadimtropashko.wordpress.com/2019/08/01/arbori-semantic-actions/)
- [Custom Formatting in SQLDev 4.2](https://vadimtropashko.wordpress.com/2017/01/03/custom-formatting-in-sqldev-4-2/)
- [Formula for Formatting](https://vadimtropashko.wordpress.com/2017/09/28/formatting-formulas/) 
- [Custom Syntax Coloring](https://vadimtropashko.wordpress.com/2018/10/10/custom-syntax-coloring/)
- [Arbori 20.2](https://vadimtropashko.wordpress.com/2020/06/19/arbori-20-2/)
- [Java Script Conditions](https://vadimtropashko.wordpress.com/2020/05/29/java-script-conditions/)

Thank you, Vadim Tropashko for providing this valuable information.

#### JavaScript Global Variables

To get the most out of the dynamic JavaScript actions from an Arbori program, you should know the following global variables and their corresponding Java class. 

Variable | Type                                             | JAR File
-------- | ------------------------------------------------ | -----------------------
`struct` | oracle.dbtools.app.Format                        | dbtools-common.jar
`target` | oracle.dbtools.parser.Parsed                     | dbtools-common.jar 
`tuple`  | HashMap<String, oracle.dbtools.parser.ParseNode> | dbtools-common.jar
`logger` | oracle.dbtools.util.Logger                       | dbtools-common.jar

### Settings for other Products

Formatter settings are also provided for the following products:

Product | Version | File
------- | ------- | ---------
[Allround Automations PL/SQL Developer](https://www.allroundautomations.com/products/pl-sql-developer/) | 14.0.6 | [trivadis_beautifier.br](settings/plsql_developer/trivadis_beautifier.br)
[JetBrains DataGrip](https://www.jetbrains.com/datagrip/) | 2021.1 | [trivadis.xml](settings/datagrip/trivadis.xml)
[Quest Toad for Oracle](https://www.quest.com/products/toad-for-oracle/) | 14.1 | [trivadis_fmtplus.opt](settings/toad/trivadis_fmtplus.opt)

Please refer to the vendor's documentation for instructions on how to import these settings.

## License

The Trivadis PL/SQL & SQL Formatter Settings are licensed under the Apache License, Version 2.0. You may obtain a copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>.
