package com.github.choonchernlim.calsync.ui.core

import com.google.common.eventbus.EventBus
import com.google.common.io.Resources
import com.google.inject.Injector
import javafx.fxml.FXMLLoader
import javafx.fxml.JavaFXBuilderFactory
import javafx.util.Callback

import javax.inject.Inject

final class FxmlLoaderService {
    private final Injector injector
    private final EventBus eventBus

    @Inject
    FxmlLoaderService(final Injector injector, final EventBus eventBus) {
        this.injector = injector
        this.eventBus = eventBus
    }

    /**
     * When creating the controller, use Guice to inject dependencies into it first and then register it to the event bus
     * before returning it.
     *
     * @param fxmlEnum FXML Enum
     * @return Node
     */
    def <T> T load(final FxmlEnum fxmlEnum) {
        try {
            return FXMLLoader.load(Resources.getResource(fxmlEnum.getPath()),
                                   ResourceBundle.getBundle('app'),
                                   new JavaFXBuilderFactory(),
                                   new Callback<Class<?>, Object>() {
                                       @Override
                                       Object call(final Class<?> clazz) {
                                           final Object controller = injector.getInstance(clazz)
                                           eventBus.register(controller)
                                           return controller
                                       }
                                   })
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to loadParent FXML from path: " + fxmlEnum.getPath(), e)
        }
    }
}