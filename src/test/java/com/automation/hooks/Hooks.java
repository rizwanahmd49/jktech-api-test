package com.automation.hooks;

import com.automation.config.ConfigManager;
import org.junit.Before;

public class Hooks {
private static ConfigManager configManager;
Hooks(){
    configManager=ConfigManager.getInstance();
}

@Before
public void setUp(){

}



}
