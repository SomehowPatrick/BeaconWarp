package com.github.unknownbanana.beaconwarp.exception;

/**
 * Exception thrown while parsing the config in {@link com.github.unknownbanana.beaconwarp.factories.WarpFactory}
 *
 * @author UnknownBanana
 * @version 1.0
 * @since 1.0
 */

public class ConfigParseException extends Exception {

    public ConfigParseException(String message) {
        super(message);
    }
}
