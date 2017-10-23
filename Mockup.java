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
