package otusqa;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:Config.properties"})
public interface MyConfig extends Config {
    @Key("browser.name")
    String browserName();
    @Key("url")
    String url();
    @Key("newName")
    String newName();
    @Key("newBirthDay")
    String newBirthDay();
}
