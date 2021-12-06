# Spring Security

## login設置

### 基本設定

* 單純引入Security，預設url為`/login`、`logout`，username為user，密碼為UUID亂數產生於log中，詳細可參考`UserDetailsServiceAutoConfiguration.java`、`InMemoryUserDetailsManager.java`、`SecurityProperties.java`
* 預設的畫面顯示詳細`DefaultLoginPageGenerationFilter.java`、`DefaultLogoutPageGeneratingFilter.java`

### 頁面設定

* 使用config繼承`WebSecurityConfigurerAdapter.java`，覆寫`configure`方法

  ```java
  @Configuration
  public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .formLogin() // 啟動html form login page
        .loginPage("/login.html") // 登入的畫面url
        .loginProcessingUrl("/doLogin") // 執行登入的url
        .usernameParameter("uname") // 使用者帳號參數定義
        .passwordParameter("passwd") // 使用者密碼參數定義
        .permitAll()
        .and()
        .csrf().disable();
    }
  }
  ```

  

### 不經過Security

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Override
  protected void configure(WebSecurity web) throws Exception {
    web.ignoring()
    	.antMatchers("/login.html", "/css/**", "/js/**", "/images/**"); // 靜態資源或登入頁面不需經過security
  }
}
```



### 使用者訊息

* `SecurityContextHolder.java`可以取得`Authentication`

* UserDetails定義

  ```java
  @Configuration
  public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
  	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      // InMemoryUserDetailsManager
      // JdbcUserDetailsManager
      // 自定義實作UserDetailsService
      // auth.userDetailsService(userValidateService);
    }
  }
  ```

* 可於controller取得登入者資料`Authentication`、`Principal`

  ```java
  public void authentication(Authentication authentication) {
  	logger.info(authentication.toString());
  }
  ```

### 新增驗證

* 將`AuthenticationProvider`新增至`ProviderManager`管理

  ```java
  @Configuration
  public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
      // 新增驗證碼檢核
      ProviderManager manager = new ProviderManager(new KaptchaAuthenticationProvider());
      return manager;
    }
  }
  ```

  

### 加上filter

* 可將帳號密碼放至request body並傳送

  ```java
  // ...
  @Override
  protected void configure(HttpSecurity http) throws Exception {
  	// ...
  	http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
  }
  //...
  ```

  

### session 設定

 ```java
 @Override
 protected void configure(HttpSecurity http) throws Exception {
   http.authorizeRequests()
   .anyRequest().authenticated()
   .and()
   .formLogin()
   .and()
   .csrf().disable()
   .sessionManagement()// 開啟session管理
   .maximumSessions(1) // 限制只能建立一個session
   .maxSessionsPreventsLogin(true); // 當客戶已登入，就無法再由其他地方登入
 }
 ```

* 簡易使用docker建立redis

  ```bash
  docker pull redis
  docker run -p 6379:6379 --name redis -d redis --requirepass "123"
  ################# 測試
  docker exec -it redis redis-cli -a 123
  set msg Hello
  get msg
  ```

* Cookie設定

  ```java
  @Bean
  public CookieSerializer cookieSerializer() {
    DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
    cookieSerializer.setSameSite("strict");
    return cookieSerializer;
  }
  ```

  

## Redis

### RedisTemplate 設定

```java
@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
  StringRedisSerializer keySerializer = new StringRedisSerializer();
  RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer();

  RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
  redisTemplate.setConnectionFactory(redisConnectionFactory);
  redisTemplate.setKeySerializer(keySerializer); // 設定key serializer
  redisTemplate.setHashKeySerializer(keySerializer);// 設定key serializer
  redisTemplate.setValueSerializer(valueSerializer);
  redisTemplate.setHashValueSerializer(valueSerializer);
  return redisTemplate;
}
```

