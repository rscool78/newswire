package com.newswire;

//import com.newswire.source.FeedProperties;
//import com.newswire.config.NewswireProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;

// Spring will auto-detect all configuration properties. Single appliocation entry point.
// Scans all @ConfigurationProperties under com.newswire
@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.newswire")
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}


/*
//First implementation
@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = FeedProperties.class)
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
//Second implementation 
@SpringBootApplication
@EnableConfigurationProperties(NewswireProperties.class)
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}*/