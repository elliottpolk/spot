package com.rocketmopp.spot.v1;

import static com.rocketmopp.spot.v1.Property.CLIENT_FINGERPRINT;
import static com.rocketmopp.spot.v1.Property.CLIENT_ID;
import static com.rocketmopp.spot.v1.Property.SCREEN_RESOLUTION;
import static com.rocketmopp.spot.v1.Property.TOUCH_SUPPORT;
import static com.rocketmopp.spot.v1.Property.USER_AGENT;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

import org.springframework.web.servlet.support.RequestContextUtils;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FingerPrint {

    String id;

    Long timestamp;

    Map<String, String> headers = new HashMap<>();

    @JsonProperty("client_fingerprint")
    String clientFingerPrint;

    @JsonProperty("user_agent")
    UserAgent userAgent;

    @JsonProperty("touch_support")
    Boolean touchSupport;

    @JsonProperty("screen_resolution")
    String screenResolution;

    @JsonProperty("ip_address")
    String ipAddr;

    @JsonProperty("user_timezone")
    TimeZone userTimezone;

    @JsonProperty("user_language")
    String language;

    FingerPrint(HttpServletRequest req) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.nanoTime();

        this.headers = new HashMap<>();
        Collections.list(req.getHeaderNames()).forEach(name -> {
            if (name.toLowerCase().equals(USER_AGENT.toString()))
                this.userAgent = UserAgent.parseUserAgentString(req.getHeader(name));
            else if(name.toLowerCase().equals(CLIENT_ID.toString()))
                this.id = req.getHeader(name);
            else if(name.toLowerCase().equals(CLIENT_FINGERPRINT.toString()))
                this.clientFingerPrint = req.getHeader(name);
            else if(name.toLowerCase().equals(TOUCH_SUPPORT.toString()))
                this.touchSupport = req.getHeader(name).toLowerCase().equals("true");
            else if(name.toLowerCase().equals(SCREEN_RESOLUTION.toString()))
                this.screenResolution = req.getHeader(name);
            else
                this.headers.put(name, req.getHeader(name));
        });
        
        this.ipAddr = req.getRemoteAddr();
        this.userTimezone = RequestContextUtils.getTimeZone(req);
        this.language = RequestContextUtils.getLocale(req).getLanguage();
    }

    public String encode() {
        return Base64.getEncoder().encodeToString(new Gson().toJson(this).getBytes());
    }

    public static FingerPrint decode(String src) {
        return new Gson().fromJson(Base64.getDecoder().decode(src).toString(), FingerPrint.class);
    }

}