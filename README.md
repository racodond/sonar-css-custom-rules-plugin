Sample plugin that defines SonarQube custom rules for CSS files
====================

[![Build Status](https://api.travis-ci.org/racodond/sonar-css-custom-rules-plugin.svg?branch=master)](https://travis-ci.org/racodond/sonar-css-custom-rules-plugin)
[![AppVeyor Build status](https://ci.appveyor.com/api/projects/status/ua8p229aypr0uf6x/branch/master?svg=true)](https://ci.appveyor.com/project/racodond/sonar-css-custom-rules-plugin/branch/master)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=org.sonar.sonar-plugins:sonar-css-custom-rules-plugin)](https://sonarqube.com/overview?id=org.sonar.sonar-plugins%3Asonar-css-custom-rules-plugin)

## Description
The [SonarQube CSS plugin](https://github.com/SonarQubeCommunity/sonar-css) can be enhanced by writing custom rules through a plugin using SonarQube CSS API.
This sample plugin is designed to help you get started writing your own plugin and custom rules.

## Usage
1. [Download and install](http://docs.sonarqube.org/display/SONAR/Setup+and+Upgrade) SonarQube 5.6 or greater
1. Install the CSS plugin (2.1 or greater) either by a [direct download](https://github.com/SonarQubeCommunity/sonar-css/releases) or through the [Update Center](http://docs.sonarqube.org/display/SONAR/Update+Center).
1. Install this sample plugin by a [direct download](https://github.com/racodond/sonar-css-custom-rules-plugin/releases)
1. Start SonarQube
1. [Activate some of the custom rules](http://docs.sonarqube.org/display/SONAR/Configuring+Rules) implemented in this sample plugin. "Forbidden properties should not be used" for example.
1. [Install your favorite analyzer](http://docs.sonarqube.org/display/SONAR/Analyzing+Source+Code#AnalyzingSourceCode-RunningAnalysis) (SonarQube Scanner, Maven, Ant, etc.) and analyze your code. Note that Java 8 is required to run an analysis.
1. Browse the issues through the web interface 

## Writing Custom Rules

### Creating a SonarQube Plugin
* Create a [standard SonarQube plugin](http://docs.sonarqube.org/display/DEV/Build+Plugin) from scratch or start from this sample plugin
* Attach this plugin to the SonarQube CSS plugin through the [POM](https://github.com/racodond/sonar-css-custom-rules-plugin/blob/master/pom.xml):
  * Add the [dependency](https://github.com/racodond/sonar-css-custom-rules-plugin/blob/master/pom.xml#L71) to the CSS plugin
  * Add the following property to the [`sonar-packaging-maven-plugin` configuration](https://github.com/racodond/sonar-css-custom-rules-plugin/blob/master/pom.xml#L105):
 ```
 <basePlugin>css</basePlugin>
 ```
* Implement the following extension points:
  * [Plugin](http://javadocs.sonarsource.org/latest/apidocs/index.html?org/sonar/api/Plugin.html) as in [`MyCssCustomRulesPlugin.java`](https://github.com/racodond/sonar-css-custom-rules-plugin/blob/master/src/main/java/org/sonar/css/MyCssCustomRulesPlugin.java)
  * [RulesDefinition](http://javadocs.sonarsource.org/latest/apidocs/index.html?org/sonar/api/server/rule/RulesDefinition.html) as in [`MyCssCustomRulesDefinition.java`](https://github.com/racodond/sonar-css-custom-rules-plugin/blob/master/src/main/java/org/sonar/css/MyCssCustomRulesDefinition.java)
* Declare the [`RulesDefinition` implementation as an extension in the `Plugin` extension point](https://github.com/racodond/sonar-css-custom-rules-plugin/blob/master/src/main/java/org/sonar/css/MyCssCustomRulesPlugin.java#L34).

### Implementing a Rule
* Create a class to define the implementation of a rule. It should:
  * Either extend [`SubscriptionVisitorCheck`](https://github.com/SonarQubeCommunity/sonar-css/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/visitors/SubscriptionVisitorCheck.java) or [`DoubleDispatchVisitorCheck`](https://github.com/SonarQubeCommunity/sonar-css/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/visitors/DoubleDispatchVisitorCheck.java).
  * Define the [rule's attributes](https://github.com/racodond/sonar-css-custom-rules-plugin/blob/master/src/main/java/org/sonar/css/checks/ForbiddenPropertiesCheck.java#L32): key, name, priority, etc.
* Declare this class in the [class implementing `RulesDefinition`](https://github.com/racodond/sonar-css-custom-rules-plugin/blob/master/src/main/java/org/sonar/css/MyCssCustomRulesDefinition.java#L51)

There are two different ways to browse the AST:

#### Using DoubleDispatchVisitorCheck
To explore part of the AST, override a method from [`DoubleDispactchVisitor`](https://github.com/SonarQubeCommunity/sonar-css/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/visitors/DoubleDispatchVisitor.java).
For instance, if you want to explore property nodes, override [`DoubleDispactchVisitor#visitProperty`](https://github.com/SonarQubeCommunity/sonar-css/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/visitors/DoubleDispatchVisitor.java#L110). This method is called each time a key node is encountered in the AST.
Note: When overriding a visit method, you must call the super method in order to allow the visitor to visit the children of the node.
See [`ForbiddenPropertiesCheck`](https://github.com/racodond/sonar-css-custom-rules-plugin/blob/master/src/main/java/org/sonar/css/checks/ForbiddenPropertiesCheck.java) for example.


#### Using SubscriptionVisitorCheck
To explore part of the AST, override [`SubscriptionVisitor#nodesToVisit`](https://github.com/SonarQubeCommunity/sonar-css/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/visitors/SubscriptionVisitor.java#L36) by returning the list of [`Tree#Kind`](https://github.com/SonarQubeCommunity/sonar-css/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/tree/Tree.java#L33) nodes you want to visit.
For instance, if you want to explore URI content nodes the method should return a list containing [`Tree#Kind#URI_CONTENT`](https://github.com/SonarQubeCommunity/sonar-css/blob/master/css-frontend/src/main/java/org/sonar/plugins/css/api/tree/Tree.java#L64).
See [`ForbiddenUrlCheck`](https://github.com/racodond/sonar-css-custom-rules-plugin/blob/master/src/main/java/org/sonar/css/checks/ForbiddenUrlCheck.java) for example.

#### Creating Issues
Precise issue or file issue or line issue can be created by calling the related method in [Issues](https://github.com/SonarQubeCommunity/sonar-css/blob/master/css-frontend/src/main/java/org/sonar/css/visitors/Issues.java).

#### Testing
Testing is made easy by the [CssCheckVerifier](https://github.com/SonarQubeCommunity/sonar-css/blob/master/css-checks-testkit/src/main/java/org/sonar/css/checks/verifier/CssCheckVerifier.java).
There are two ways to assert that an issue should be raised:
* Through comments directly in the .css test file
* Or using assertions in the check class test

Examples of coding rule implementation and testing can be found in the CSS plugin [`css-checks` module](https://github.com/SonarQubeCommunity/sonar-css/tree/master/css-checks/src/main/java/org/sonar/css/checks).
