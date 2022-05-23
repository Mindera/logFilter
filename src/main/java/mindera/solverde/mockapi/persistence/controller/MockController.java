package mindera.solverde.mockapi.persistence.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MockController {


    @GetMapping("/get")
    public ResponseEntity<String> getHello() {
        String result = "hello get";

        return ResponseEntity.ok(result);
    }


    @PostMapping("/post")
    public ResponseEntity<String> postMessage(@RequestBody String post){

        return  ResponseEntity.ok(post);
    }

    @PutMapping("/put")
    public ResponseEntity<String> putMessage(@RequestBody String put){

        return ResponseEntity.ok(put);
    }
}
