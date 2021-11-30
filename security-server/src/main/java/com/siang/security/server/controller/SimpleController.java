package com.siang.security.server.controller;

import com.google.code.kaptcha.Producer;
import com.google.common.collect.Lists;
import com.siang.security.server.database.entity.APUser;
import com.siang.security.server.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/simple")
public class SimpleController {
    @Autowired
    private Producer producer;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/info")
    public String info() {
        return "info";
    }

    @GetMapping("/users")
    public List<APUser> getAll() {
        return Lists.newArrayList(userRepository.findAll());
    }

    @GetMapping("/vc.jpg")
    public void getVerifyCode(HttpServletResponse response, HttpSession httpSession) throws IOException {
        response.setContentType("image/jpeg");
        String text = producer.createText();
        httpSession.setAttribute("kaptcha", text);
        BufferedImage image = producer.createImage(text);
        try (ServletOutputStream out = response.getOutputStream()) {
            ImageIO.write(image, "jpg", out);
        }
    }

}
