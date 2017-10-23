1.
-------------
        MockUp<Properties> propertiesMockUp = new MockUp<Properties>() {
            @Mock
            public String getProperty(Invocation inv, String value) {
                switch (value){
                    case "notebook.run.configuration.dir":
                        return System.getProperty("java.io.tmpdir");
                    case "notebook.run.configuration.file":
                        return "spark_run_configuration.conf";
                    default:
                        return inv.proceed(value);
                }
            }
        };

2. (https://stackoverflow.com/questions/23162777/how-do-i-mock-an-autowired-value-field-in-spring-with-mockito)
--------------
Given:

@Value("#{myProps[‘default.url']}")
private String defaultUrl;
In test method (qualified name just for completeness here):

org.springframework.test.util.ReflectionTestUtils.setField(
        myClassUnderTest, "defaultUrl", "url123");
// Note: Don't use MyClassUnderTest.class
// Note: Don't use the referenced string "#{myProps[‘default.url']}", 
//       but simply the field name "defaultUrl"


https://stackoverflow.com/questions/17353327/populating-spring-value-during-unit-test
    
3.
--------------
Spring allows you to provide an instance of PropertySourcesPlaceholderConfigurer which will be used to populate the values of the fields annotated with @Value().

/**
 * Creates the mock properties that will be used for this test. 
 * These will be used mainly to populate the {@code @Value("")} fields.
 * 
 * @return the mock properties source place holders configure
 */
@Bean
public static PropertySourcesPlaceholderConfigurer properties() {
  final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
  /* 
   * Instead of loading the properties from the file system, the test makes use of the following 
   * hard-coded properties 
   * pspc.setLocation(new ClassPathResource("project.properties"));
   */
  final Properties properties = new Properties();
  properties.setProperty("property.value1", "value1");
  properties.setProperty("property.value2", "value2");
  properties.setProperty("property.valueN", "valueN");
  pspc.setProperties(properties);
  return pspc;
}
