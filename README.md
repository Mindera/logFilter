## LogFilter

An library for logging servlet requests and responses made at Mindera.

## Description

It implements doFilter and uses custom requests and responses 
wrappers based on Spring Boot ContentCachingRequestWrapper and
ContentCachingResponseWrapper without actualy using Spring Boot,
so it can be used without Spring Boot and in different Frameworks.

- [It also has an Spring Boot AutoStarter made as a seperate library.](https://github.com/DiogoNoronha)

## Install

To use the LogFilter add it as an external library on your project.
The .jar file is located in out/artifacts/logfilter_jar

## Authors

- [@diogonoronha](https://github.com/DiogoNoronha)
- [@luisfaria](https://github.com/luisfcfaria)

## License

[APACHE LICENSE Version 2.0](http://www.apache.org/licenses/)

## Notes

Feel free to contact us on any issue or sugestion!