[![Release](https://img.shields.io/github/release/racodond/sonar-css-custom-rules-plugin.svg)](https://github.com/racodond/sonar-css-custom-rules-plugin/releases/latest)
[![Build Status](https://api.travis-ci.org/racodond/sonar-css-custom-rules-plugin.svg?branch=master)](https://travis-ci.org/racodond/sonar-css-custom-rules-plugin)
[![AppVeyor Build status](https://ci.appveyor.com/api/projects/status/ua8p229aypr0uf6x/branch/master?svg=true)](https://ci.appveyor.com/project/racodond/sonar-css-custom-rules-plugin/branch/master)


[![Quality Gate status](https://sonarcloud.io/api/project_badges/measure?project=org.sonar.sonar-plugins%3Acss-custom-rules&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.sonar.sonar-plugins%3Acss-custom-rules)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=org.sonar.sonar-plugins%3Acss-custom-rules&metric=ncloc)](https://sonarcloud.io/dashboard?id=org.sonar.sonar-plugins%3Acss-custom-rules)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.sonar.sonar-plugins%3Acss-custom-rules&metric=coverage)](https://sonarcloud.io/dashboard?id=org.sonar.sonar-plugins%3Acss-custom-rules)


# Sample plugin that defines SonarQube custom rules for CSS, SCSS and Less files

## Description
The [SonarQube CSS / SCSS / Less Analyzer](https://github.com/racodond/sonar-css-plugin) can be enhanced by writing custom rules through a plugin using SonarQube CSS / SCSS / Less API.
This sample plugin is designed to help you get started writing your own plugin and custom rules.


## Usage
1. [Download and install](https://docs.sonarqube.org/display/SONAR/Setup+and+Upgrade) SonarQube 6.7 or greater
1. Install the SonarQube CSS / SCSS / Less plugin (4.13 or greater) by a [direct download](https://github.com/racodond/sonar-css-plugin/releases)
1. Install this sample plugin by a [direct download](https://github.com/racodond/sonar-css-custom-rules-plugin/releases)
1. Start SonarQube
1. [Activate some of the custom rules](https://docs.sonarqube.org/display/SONAR/Quality+Profiles) implemented in this sample plugin. "Forbidden properties should not be used" for example.
1. Install your [favorite scanner](https://docs.sonarqube.org/display/SONAR/Analyzing+Source+Code#AnalyzingSourceCode-RunningAnalysis) (SonarQube Scanner, Maven, Ant, etc.)
1. [Analyze your code](https://docs.sonarqube.org/display/SONAR/Analyzing+Source+Code)
1. Browse the issues through the web interface 


## Writing Custom Rules

### Creating a SonarQube Plugin
* Create a [standard SonarQube plugin](https://docs.sonarqube.org/display/DEV/Build+Plugin) from scratch or start from this sample plugin
* Attach this plugin to the SonarQube CSS / SCSS / Less plugin through the [POM](sonar-csscustomrules-plugin/pom.xml):
  * Add the [dependency](sonar-csscustomrules-plugin/pom.xml#L33) to the CSS / SCSS / Less plugin
  * [Base the plugin on the CSS / SCSS / Less plugin](sonar-csscustomrules-plugin/pom.xml#L22)
  * [Require a minimal version of the CSS / SCSS / Less plugin](sonar-csscustomrules-plugin/pom.xml#L23)
* Implement the following extension points:
  * [Plugin](http://javadocs.sonarsource.org/latest/apidocs/index.html?org/sonar/api/Plugin.html) as in [`MyCssCustomRulesPlugin.java`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/MyCssCustomRulesPlugin.java)
  * [RulesDefinition](http://javadocs.sonarsource.org/latest/apidocs/index.html?org/sonar/api/server/rule/RulesDefinition.html) as in [`MyCssCustomRulesDefinition.java`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/MyCssCustomRulesDefinition.java)
* Declare the [`RulesDefinition` implementation as an extension in the `Plugin` extension point](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/MyCssCustomRulesPlugin.java#L31).

### Implementing a CSS Rule
* Create a class to define the implementation of a rule. It should:
  * Either extend [`SubscriptionVisitorCheck`](https://github.com/racodond/sonar-css-plugin/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/visitors/SubscriptionVisitorCheck.java) or [`DoubleDispatchVisitorCheck`](https://github.com/racodond/sonar-css-plugin/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/visitors/DoubleDispatchVisitorCheck.java).
  * Define the [rule's attributes](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/checks/css/ForbiddenPropertiesCheck.java#L32): key, name, priority, etc.
* Declare this class in the [class implementing `RulesDefinition`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/MyCssCustomRulesDefinition.java#L51)

There are two different ways to browse the AST:

#### Using DoubleDispatchVisitorCheck
To explore part of the AST, override a method from [`DoubleDispactchVisitor`](https://github.com/racodond/sonar-css-plugin/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/visitors/DoubleDispatchVisitor.java).
For instance, if you want to explore property nodes, override [`DoubleDispactchVisitor#visitProperty`](https://github.com/racodond/sonar-css-plugin/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/visitors/DoubleDispatchVisitor.java#L112). This method is called each time a key node is encountered in the AST.
Note: When overriding a visit method, you must call the super method in order to allow the visitor to visit the children of the node.
See [`ForbiddenPropertiesCheck`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/checks/css/ForbiddenPropertiesCheck.java) for example.


#### Using SubscriptionVisitorCheck
To explore part of the AST, override [`SubscriptionVisitor#nodesToVisit`](https://github.com/racodond/sonar-css-plugin/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/visitors/SubscriptionVisitor.java#L36) by returning the list of [`Tree#Kind`](https://github.com/racodond/sonar-css-plugin/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/tree/Tree.java#L54) nodes you want to visit.
For instance, if you want to explore URI content nodes the method should return a list containing [`Tree#Kind#URI_CONTENT`](https://github.com/racodond/sonar-css-plugin/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/tree/Tree.java#L86).
See [`ForbiddenUrlCheck`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/checks/css/ForbiddenUrlCheck.java) for example.

#### Creating Issues
Precise issue or file issue or line issue can be created by calling the related method in [Issues](https://github.com/racodond/sonar-css-plugin/blob/master/css-frontend/src/main/java/org/sonar/css/visitors/Issues.java).

#### Testing
Testing is made easy by the [CssCheckVerifier](https://github.com/racodond/sonar-css-plugin/blob/master/css-checks-testkit/src/main/java/org/sonar/css/checks/verifier/CssCheckVerifier.java).
There are two ways to assert that an issue should be raised:
* Through comments directly in the .css test file
* Or using assertions in the check class test

Examples of coding rule implementation and testing can be found in the CSS plugin [`css-checks` module](https://github.com/racodond/sonar-css-plugin/tree/master/css-checks/src/main/java/org/sonar/css/checks).

### Implementing an SCSS Rule

The same as "Implementing a CSS Rule" applies. But, instead, look at:
* [`MyScssCustomRulesDefinition`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/MyScssCustomRulesDefinition.java)
* [`InterpolatedPropertiesCheck`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/checks/scss/InterpolatedPropertiesCheck.java)

### Implementing a Less Rule

The same as "Implementing a CSS Rule" applies. But, instead, look at:
* [`MyLessCustomRulesDefinition`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/MyLessCustomRulesDefinition.java)
* [`InterpolatedPropertiesCheck`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/checks/less/InterpolatedPropertiesCheck.java)

### Make a rule apply to CSS and SCCS and Less
Just add the rule to [`MyCssCustomRulesDefinition`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/MyCssCustomRulesDefinition.java)
and [`MyScssCustomRulesDefinition`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/MyScssCustomRulesDefinition.java)
and [`MyLessCustomRulesDefinition`](sonar-csscustomrules-plugin/src/main/java/org/sonar/css/MyLessCustomRulesDefinition.java)
