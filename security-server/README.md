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

  