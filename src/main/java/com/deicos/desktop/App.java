/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.deicos.desktop;

import com.deicos.desktop.components.MainLayout;
import com.deicos.desktop.config.AbstractJavaFxApplicationSupport;
import com.deicos.desktop.config.MvcConfig;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.application.Preloader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.net.URL;

/**
 * asoto
 */
@Lazy
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
public class App extends AbstractJavaFxApplicationSupport {

    /**
     * Note that this is configured in application.properties
     */
    @Value("${app.ui.title:Example App}")//
    private String windowTitle;

    @Autowired
    private ApplicationContext context;


    private int port = 0;

    @Autowired//
    private MainLayout mainLayout;

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{MvcConfig.class/*, ServletContainerCustomizer.class*/}, args);
        launchApp(App.class, args);
    }


    private String getServerURL() {
        String baseUri = "http://localhost";
        try {
            if (port == 0) {
                port = ((AnnotationConfigEmbeddedWebApplicationContext) context).getEmbeddedServletContainer().getPort();
            }
            return new URL( baseUri + ":" + port ).toString();
        } catch (Exception e) {
            System.out.println("ERR LOCALHOST:" + e.toString());
        }
        return baseUri+ "/";
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins(getServerURL());
            }
        };
    }

    @Override
    public void start(Stage stage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));
        stage.setTitle(windowTitle);
        //stage.setScene(new Scene(mainLayout));

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Browser browser = new Browser(BrowserType.HEAVYWEIGHT);
        BrowserView view = new BrowserView(browser);
        //String url = getClass().getClassLoader().getResource("app-lince.html").toString();
        String url = getServerURL();
        browser.loadURL(url);
        String remoteDebuggingURL = browser.getRemoteDebuggingURL();
        System.out.println("==========================");
        System.out.println("Remote debug:" + remoteDebuggingURL);
        System.out.println("Remote uri: " + url);
        System.out.println("==========================");
        Scene scene = new Scene(new BorderPane(view), screenBounds.getWidth() - 20, screenBounds.getHeight() - 40);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }



}
