# domain-collections
Collection of reusable Mixins for domain behavior isolation pattern.

The core module of this library enable you to implement your domain entities behavior using Mixin pattern.

For example, you can allow your domain entity class to access database to get setting value just by adding some Mixin to it.

```
// declaring Mixin interface
public interface ApplicationSettingSupport {

    @DomainBehaviorTarget
    default ApplicationSetting getSettingValue(ApplicationSettingKeys key) {
        throw new DomainBehaviorNotImplementedException();
    }
}
```

```
// implement the Mixin behavior
public class ApplicationSettingBehavior implements ApplicationSettingSupport {

    private ApplicationSettingRepository applicationSettingRepository;

    public ApplicationSettingBehavior(ApplicationSettingRepository applicationSettingRepository) {
        this.applicationSettingRepository = applicationSettingRepository;
    }

    @Override
    public ApplicationSetting getSettingValue(ApplicationSettingKeys key) {
        return applicationSettingRepository.findByKey(key);
    }
}
```

```
// use the Mixin in domain entity

@Entity
public class FooEntity implements DomainBehaviorSupport, ApplicationSettingSupport {
    @Transient
    @DomainBehaviorProxy
    private FooEntity self;

    private BigDecimal price = BigDecimal.ZERO;

    public BigDecimal getPriceIncludeVat() {
        ApplicationSetting vatRateSetting = self.getSettingValue(ApplicationSettingKeys.VAT_RATE);
        BigDecimal vatRate = new BigDecimal(vatRateSetting.getValue());
        return price.multiply(vatRate).divide(BigDecimal.valueOf(100));
    }
}
```

# How to use

Include maven dependency `com.github.asinkxcoswt:domain-collections:V1.0.0-beta` to your project's `pom.xml` along with Spring JPA
```
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.asinkxcoswt</groupId>
            <artifactId>domain-collections</artifactId>
            <version>V1.0.0-beta</version>
        </dependency>
    
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.1.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.1.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-params</artifactId>
            <version>5.1.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

Define `ApplicationSetting` entity and its related repository, no special things here, just a typical Spring JPA and Hibernate application.

```
@Entity
public class ApplicationSetting {
    @Id
    private UUID uuid = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    private ApplicationSettingKeys key;
    private String value;

    /**
     * For Hibernate
     */
    private ApplicationSetting() {}

    public ApplicationSetting(ApplicationSettingKeys key, String value) {
        this.key = key;
        this.value = value;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getValue() {
        return value;
    }

    public ApplicationSettingKeys getKey() {
        return key;
    }

}
```

```
public enum ApplicationSettingKeys {
    VAT_RATE
}
```

```
@Repository
public interface ApplicationSettingRepository extends JpaRepository<ApplicationSetting, UUID> {
    ApplicationSetting findByKey(ApplicationSettingKeys key);
}
```

Here begin the magic, define the mixin interface. The annotation `@DomainBehaviorTarget` tells us which methods you want to use in the Mixin implementation. The method should be declared with `default` keyword so that the target entity (to which the mixin is applied) doesn't have to implement the method. The exception `DomainBehaviorNotImplementedException` is to ensure that the magic works correctly, if anything goes wrong and the mixin behavior doesn't work, this exception will be thrown.
```
public interface ApplicationSettingSupport {

    @DomainBehaviorTarget
    default ApplicationSetting getSettingValue(ApplicationSettingKeys key) {
        throw new DomainBehaviorNotImplementedException();
    }
}
```

Next, define the mixin implementation. Note that this class can be a typical Spring bean, you can inject dependency to it as you like. Here we inject the `ApplicationSettingRepository` via the constructor.
```
public class ApplicationSettingBehavior implements ApplicationSettingSupport {

    private ApplicationSettingRepository applicationSettingRepository;

    public ApplicationSettingBehavior(ApplicationSettingRepository applicationSettingRepository) {
        this.applicationSettingRepository = applicationSettingRepository;
    }

    @Override
    public ApplicationSetting getSettingValue(ApplicationSettingKeys key) {
        return applicationSettingRepository.findByKey(key);
    }
}
```

Next, let's define the entity to use this mixin. A lot of things happen here.

First, the entity have to implement `DomainBehaviorSupport`. It is a marker interface that tell us that the entity is eligible to be wrapped by a proxy that handle the magic.

Next, in the method `getPriceIncludeVat()` we call to `self.getSettingValue()` of the mixin `ApplicationSettingSupport`. The instance field `self` is special, it is annotated with `@DomainBehaviorProxy`. This allow the variable to be injected with the proxy instance of the entity itself. You have to call mixin methods via this proxy, otherwise the `DomainBehaviorNotImplementedException` will be thrown.

```
@Entity
public class FooEntity implements DomainBehaviorSupport, ApplicationSettingSupport {

    @Transient
    @DomainBehaviorProxy
    private FooEntity self;

    @Id
    private UUID uuid = UUID.randomUUID();
    private BigDecimal price = BigDecimal.ZERO;

    public UUID getUuid() {
        return uuid;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceIncludeVat() {
        ApplicationSetting vatRateSetting = self.getSettingValue(ApplicationSettingKeys.VAT_RATE);
        BigDecimal vatRate = new BigDecimal(vatRateSetting.getValue());
        return price.multiply(vatRate).divide(BigDecimal.valueOf(100));
    }
}
```

The repository looks normal, but it also has magic. For every method in the repository interface that extends `JpaRepository`, if its return type implements `DomainBehaviorSupport`, the return value will be wrapped with the behavior proxy. This includes all possible generic type supported by `JpaRepository` such as `List<T>`, `Set<T>`, `Optional<T>`, `Page<T>`, `Example<T>`.

In addition, all arguments passed into the method will be ensure to be unwrapped from the behavior proxy. This is an important point to remember. Your ORM engine will not be affected by the behavior proxy.
```
public interface FooRepository extends JpaRepository<FooEntity, UUID> {
    default FooEntity create() {
        return new FooEntity();
    }
    List<FooEntity> findByPriceGreaterThan(BigDecimal price);
    Optional<FooEntity> findByUuid(UUID uuid);
}
```

In the main class, add `DomainBehaviorSupportJpaRepositoryFactoryBean.class` to the `@EnableJpaRepositories`. This is to enable to mentioned magic around all repository classes that extend `JpaRepository`.
```
@EnableJpaRepositories(repositoryFactoryBeanClass = DomainBehaviorSupportJpaRepositoryFactoryBean.class)
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

Finally, the configuration bean that is the heart of the magic is `DomainBehaviorManager`. You can register mixins via `domainBehaviorManager.registerBehavior(...)` as much as you like. The `SpringApplicationContextHolder` is used in the `DomainBehaviorManager`, please help declare it as a bean.
```
@Configuration
public class ApplicationConfiguration {

    @Bean
    public SpringApplicationContextHolder springApplicationContextHolder() {
        return new SpringApplicationContextHolder();
    }

    @Bean
    public ApplicationSettingBehavior applicationSettingBehavior(ApplicationSettingRepository applicationSettingRepository) {
        return new ApplicationSettingBehavior(applicationSettingRepository);
    }

    @Bean
    public DomainBehaviorManager domainBehaviorManager() {
        return new DomainBehaviorManager();
    }

    @Autowired
    public void configure(DomainBehaviorManager domainBehaviorManager,
                          ApplicationSettingBehavior applicationSettingBehavior) {
        domainBehaviorManager.registerBehavior(ApplicationSettingSupport.class, applicationSettingBehavior);
        domainBehaviorManager.afterPropertiesSet();
    }
}
```

Time to test our magic!
```
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApplicationTest {
    @Autowired
    private FooRepository fooRepository;

    @Autowired
    private ApplicationSettingRepository applicationSettingRepository;

    @Test
    public void canGetPriceIncludeVat_usingVatRateFromSetting() {
        // prepare setting vatRate = 7%
        applicationSettingRepository.save(new ApplicationSetting(ApplicationSettingKeys.VAT_RATE, "7"));

        // prepare foo entity with price = 10
        FooEntity fooEntity = fooRepository.create();
        fooEntity.setPrice(BigDecimal.valueOf(10));

        // check that the domain behavior method 'getSettingValue(...) works
        ApplicationSetting vatRateSetting = fooEntity.getSettingValue(ApplicationSettingKeys.VAT_RATE);
        Assertions.assertEquals("7", vatRateSetting.getValue());

        // price include vat should be 10*7/100 = 0.7
        Assertions.assertEquals(new BigDecimal("0.7"), fooEntity.getPriceIncludeVat());
    }
}
```