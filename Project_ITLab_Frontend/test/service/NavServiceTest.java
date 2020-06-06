package service;

import org.junit.jupiter.api.Test;
import services.NavService;

import static org.junit.jupiter.api.Assertions.*;

class NavServiceTest {

    private NavService instance;

    public NavServiceTest() {
        instance = NavService.get();
    }

    @Test
    void get() {
        assertEquals(instance, NavService.get());
    }
}