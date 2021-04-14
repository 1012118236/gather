package com.example.gather;

import com.example.gather.socket.TCPServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatherApplication {

    public static void main(String[] args) {
      new TCPServer().startServer();
    }

}
