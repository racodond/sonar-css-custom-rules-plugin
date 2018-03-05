## Raising a Bug or Asking a Question
While raising a bug or asking a question, please provide:
* Version of the SonarQube platform
* Version of the CSS / SCSS / Less plugin
* Log file of the analysis
* Source code to reproduce the error

## Contributing
Any contribution is more than welcome!
 
You feel like:
* Improving the plugin? Just open a PR.
* Fixing some bugs or improving existing checks? Just open a PR.

## Building / Testing
* Building and running unit tests: `mvn clean install`
* Building and running unit tests and running integration tests: `mvn clean install -Pits -Dsonar.runtimeVersion=$VERSION` ($VERSION = 'LTS' or 'LATEST_RELEASE'). Behind a proxy, add `-Dhttps.proxyHost=localhost -Dhttps.proxyPort=3128 -Dhttp.proxyHost=localhost -Dhttp.proxyPort=3128`.
