package jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/api/v1")
@RestController
public class HelloController  {

    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello World";
    }
}
