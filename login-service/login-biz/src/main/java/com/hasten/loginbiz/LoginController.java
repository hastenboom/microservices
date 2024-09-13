package com.hasten.loginbiz;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Hasten
 */
@RestController
@RequiredArgsConstructor
public class LoginController {


    private static String TEST_USERNAME = "lisi";
    private static String TESET_PWD = "123";
    private static long TEST_ID = 1L;

    private static final String LOGIN_PREFIX = "login:token:";

    private static final String LOGIN_CODE_PREFIX = "login:code:";

    private final StringRedisTemplate redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/login")
    public String login(@Validated(ValidationLoginGroup.WithPwd.class)
                        @RequestBody UserInfo userInfo)
    {

        if (!checkUser(userInfo.getUsername(), userInfo.getPassword())) {
            return "invalid username or password";
        }

        UUID uuid = this.generateToken(userInfo);

        return uuid.toString();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        String token = request.getHeader("auth");
        if (token == null) {
            return "no token in request header";
        }

        redisTemplate.delete(LOGIN_PREFIX + token);
        return "logout success";
    }


    private boolean checkUser(String username, String password) {
        return username.equals(TEST_USERNAME) && password.equals(TESET_PWD);
    }

    /*-----------------------------*/

    @PostMapping("/getCode")
    public String getCode(@RequestBody UserInfo userInfo) {

        //validation may be
        String qName = "simple.queue";
        //@async
        rabbitTemplate.convertAndSend(qName, userInfo.getUsername());
        return "server accept your code request, it's handling....";
    }


    @RabbitListener(queues = "simple.queue")
    private void listenSimpleQueue(String username) {
        System.out.println("I've received the code request!!!!!   " + username);
        String randomNum = RandomUtil.randomNumbers(4);
        System.out.println("The code is: " + randomNum);

        redisTemplate.opsForValue().set(LOGIN_CODE_PREFIX + username, randomNum, 60, TimeUnit.SECONDS);
    }


    @PostMapping("/loginWithCode")
    public String loginWithCode(
            @RequestBody @Validated(ValidationLoginGroup.WithCode.class)
            UserInfo userInfo)
    {
        String username = userInfo.getUsername();
        String code = userInfo.getCode();

        String codeInRedis = redisTemplate.opsForValue().get(LOGIN_CODE_PREFIX + username);
        if (codeInRedis == null || !Objects.equals(code, codeInRedis)) {
            return "invalid code, did you request the code?";
        }

        return generateToken(userInfo).toString();
    }

    private UUID generateToken(UserInfo userInfo) {

        UUID token = UUID.randomUUID();

        /*
         *key-value = token-User, the User depends on the biz logic;
         * */
        redisTemplate.opsForValue().set(LOGIN_PREFIX + token, String.valueOf(TEST_ID), 60, TimeUnit.MINUTES);

        return token;
    }


}

