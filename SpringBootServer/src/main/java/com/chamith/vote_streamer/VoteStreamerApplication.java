package com.chamith.vote_streamer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VoteStreamerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoteStreamerApplication.class, args);
		System.out.println("Hello Vote Streamer!");
	}

}
