package com.shion1305.lumos.twitter;

import com.shion1305.lumos.general.ConfigManager;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;
import static com.shion1305.lumos.general.ConfigManager.*;

class DMNotifierTest {
    @Test
    void test() {
        DMNotifier.checkDM();
    }
    @Test
    void confTest(){
        assertNotNull(getConfig(Config.TWITTER_API_KEY));
        assertNotNull(getConfig(Config.TWITTER_SECRET_KEY));
        assertNotNull(getConfig(Config.TWITTER_ACCESS_TOKEN));
        assertNotNull(getConfig(Config.TWITTER_ACCESS_TOKEN_SECRET));
    }
}