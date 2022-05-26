package mindera.solverde.mockapi.persistence.controller;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class MockController {

    @GetMapping("/get")
    public ResponseEntity<String> getHello(){

//        WebClient webClient = WebClient.create();
//
//        String response = webClient.get()
//                .uri("https://catfact.ninja/fact")
//                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class)).block();

        String result = "hello get";
        return ResponseEntity.ok(Thread.currentThread().getName() + " " + result);
    }

    @PostMapping("/post")
    public ResponseEntity<String> postMessage(@RequestBody String post){

        return  ResponseEntity.ok(Thread.currentThread().getName() + " " + post);
    }

    @PutMapping("/put")
    public ResponseEntity<String> putMessage(@RequestBody String put){

        return ResponseEntity.ok(put);
    }



}
