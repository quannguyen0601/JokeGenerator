package com.quan.service;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class JokeServiceTest {

    private static final JokeService JOKE_SERVICE = spy(JokeService.class);

    private IJokeService.BaseResponse response;
    private IJokeService.BaseResponse filteredResponse;

    @BeforeEach
    void setup() {
        response = new IJokeService.BaseResponse();
        response.setJokes(Arrays.asList("When Chuck Norris kidnaps mobsters, he puts them inside the trunk of his motorcycle.",
                "Chuck Norris one rode a trike. When he got off of it, it was a motorcycle ,measuring to be 16 feet tall and 72 feet long, with mounted grenade launchers and bazookas. He still keeps it in his garage.",
                "The Terminator needs your clothes, boots and your motorcycle, because Chuck Norris took his.",
                "Chuck Norris can destroy any aircraft, ship, or motorized vechicle just by yelling \\\"Bang!\\\" at it.",
                "Chuck Norris once won a D 1 Drifting Championship by driving a smart car with no motor,tranny,wheels or tires...come to think of it he was merely running sidways while making scretching noises",
                "What are you thinking about right now? Chuck Norris says you're thinking about a teddy bear riding a unicycle. If you aren't, your last and shortly upcoming sight will be a GRIZZLY bear riding a MOTORcycle, which is one of the weaker pets of Chuck Norris.",
                "once, while having sex in a tractor trailer, a drop of Chuck Norris's sperm escaped into the motor.....we now know this truck as Optimus Prime!",
                "When Chuck Norris got cut off in traffic, he rammed the offending car off the road, pulled the asshole out by his hair and slapped him to death with his schlong. Then he boned his wife on the hood of the car, in full view of passing motorists. Don't fuck with big Chuck.",
                "Chuck Norris can ride a motor without the cycle"));

        filteredResponse = new IJokeService.BaseResponse();
        filteredResponse.setJokes(Arrays.asList(
                "Chuck Norris once won a D 1 Drifting Championship by driving a smart car with no motor,tranny,wheels or tires...come to think of it he was merely running sidways while making scretching noises",
                "once, while having sex in a tractor trailer, a drop of Chuck Norris's sperm escaped into the motor.....we now know this truck as Optimus Prime!",
                "Chuck Norris can ride a motor without the cycle"
        ));
    }

    @AfterEach
    void tearDown() {
        reset(JOKE_SERVICE);
    }

    @Test
    void whenGetJokesFromQuery_ThenSuccessFilteredJokes() {
        when(JOKE_SERVICE.doGetJokes(any())).thenReturn(response);

        IJokeService.BaseResponse response1 = JOKE_SERVICE.getJokes(new IJokeService.BaseSearch("motor"));

        assertThat(response1.getJokes().size()).isEqualTo(filteredResponse.getJokes().size());
    }

    @Test
    void whenGetJokesFromEmptyQuery_ThenGetAllJokes() {
        when(JOKE_SERVICE.doGetJokes(any())).thenReturn(response);

        IJokeService.BaseResponse response1 = JOKE_SERVICE.getJokes(new IJokeService.BaseSearch(""));

        assertThat(response1.getJokes().size()).isEqualTo(response.getJokes().size());
    }
}
