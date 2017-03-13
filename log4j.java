


1.You have to remove all the existing appenders on the loger object (loggerOBject.removeALLApenders() ; )

2.Define a new pattern for the logger use PatternLayout API https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html

Create a new file appender and pass the logpath you want to CREATE/LOG into to the FileAppender constructor

FileAppender appender = new FileAppender(layout,logpath,false);
Add appender object to the Logger object

log.add(appender);
Set the level of the logger

logger.setLevel((Level) Level.INFO);


===============================================
2)
===============================================

You can easily invoke log4j's API programmatically, e.g.

FileAppender appender = new FileAppender();
// configure the appender here, with file location, etc
appender.activateOptions();

Logger logger = getRootLogger();
logger.addAppender(appender);
The logger can be the root logger as in this example, or any logger down the tree. Your unit test can add its custom appender during steup, and remove the appender (using removeAppender()) during teardown.

