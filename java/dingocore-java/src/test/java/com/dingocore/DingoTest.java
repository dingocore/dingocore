package com.dingocore;

import com.dingocore.endpoint.Endpoint;
import org.junit.Test;

public class DingoTest {

    @Test
    public void test() {
        Dingo dingo = Dingo.connect("nest-bridge", "localhost", 8331);
        Endpoint ep = dingo.addEndpoint(null);
    }

}
