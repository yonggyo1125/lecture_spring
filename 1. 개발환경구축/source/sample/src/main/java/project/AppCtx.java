package project;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
public class AppCtx {

    @Bean
    public Greeter greeter() {
        Greeter g = new Greeter();
        g.setFormat("%s, 안녕하세요.");
        return g;
    }
}
