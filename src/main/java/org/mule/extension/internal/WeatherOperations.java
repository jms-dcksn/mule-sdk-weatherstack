package org.mule.extension.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;

import java.io.InputStream;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class WeatherOperations {

  @Parameter
  @Example("Dallas")
  @DisplayName("City")
  private String city;

  @Parameter
  @Example("f")
  @DisplayName("Units - 'c' or 'f'")
  private String units;

  /**
   * Example of an operation that uses the configuration and a connection instance to perform some action.
   */
  @MediaType(value = ANY, strict = false)
  @DisplayName(WeatherConnection.CITY)
  public InputStream getWeatherByCity(@Config WeatherConfiguration configuration, @Connection WeatherConnection connection){
    return connection.callHttpCITY(city, units);
  }

  /**
   * Example of a simple operation that receives a string parameter and returns a new string message that will be set on the payload.
   */
//  @MediaType(value = ANY, strict = false)
//  public String sayHi(String person) {
//    return buildHelloMessage(person);
//  }
//
//  /**
//   * Private Methods are not exposed as operations
//   */
//  private String buildHelloMessage(String person) {
//    return "Hello " + person + "!!!";
//  }
}
