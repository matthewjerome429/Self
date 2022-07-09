test folder is for re-runnable unit tests. they will be automatically run by pipeline at build stage.
Please note that no external dependency is allowed in unit tests.
As the build environment cannot guarantee availability of any of your external dependencies
External dependency includes database, redis, other micro-services etc.
If you really want to test run your container, you need to mock all of its dependencies.

If you just need to play around your services programmatically, please use playground. There is a ContainerTest class in
playground demonstrating how to test your code locally only..